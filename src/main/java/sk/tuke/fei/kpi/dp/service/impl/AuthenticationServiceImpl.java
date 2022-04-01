package sk.tuke.fei.kpi.dp.service.impl;

import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
import jakarta.inject.Singleton;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.tuke.fei.kpi.dp.common.Provider;
import sk.tuke.fei.kpi.dp.dto.provider.ProviderUser;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.exception.FaultType;
import sk.tuke.fei.kpi.dp.model.entity.User;
import sk.tuke.fei.kpi.dp.service.AuthenticationService;
import sk.tuke.fei.kpi.dp.service.UserService;

@Singleton
public class AuthenticationServiceImpl implements AuthenticationService {

  private static final String ISSUER_APP_NAME = "redakcny-system-be";

  private final UserService userService;
  private final JwtTokenGenerator jwtTokenGenerator;
  private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

  public AuthenticationServiceImpl(UserService userService, JwtTokenGenerator jwtTokenGenerator) {
    this.userService = userService;
    this.jwtTokenGenerator = jwtTokenGenerator;
  }

  /**
   * Handler for mapping provider user to system user in the database
   * Generates jwt token which is sent to frontEndCallbackUrl later on
   * If logged user´s username and auth provider does not match record in database, then is saved
   * @param loggedProviderUserDto interface for github and gitlab entity
   * @param authProvider gitlab/github
   * @return auth response with logged user id, roles and other props
   * */
  @Override
  public AuthenticationResponse handleAuthenticationResponse(ProviderUser loggedProviderUserDto,
      Provider authProvider) {
    logger.info("About to handle authentication response " + loggedProviderUserDto.getUsername() + " " + authProvider.toString());

    User loggedSystemUser = userService.findByUsernameAndAuthProvider(loggedProviderUserDto.getUsername(), authProvider)
        .orElse(new User(loggedProviderUserDto.getUsername(), loggedProviderUserDto.getName(),
        loggedProviderUserDto.getEmail(), authProvider));
    // update saved system user for potential updated data in provider interface
    if (loggedSystemUser.getId() != null) {
      loggedSystemUser.setFullName(loggedProviderUserDto.getName());
      loggedSystemUser.setEmail(loggedProviderUserDto.getEmail());
    }
    loggedSystemUser = userService.saveLoggedUser(loggedSystemUser);

    String jwtAccessToken = generateJwtToken(loggedSystemUser);

    Map<String, Object> loggedUserDataDtoMap = new HashMap<>();
    saveBasicUserInfoToMap(loggedSystemUser, loggedUserDataDtoMap);
    loggedUserDataDtoMap.put("accessToken", jwtAccessToken);
    // unique username is generated ID - name field in authentication class
    return AuthenticationResponse.success(String.valueOf(loggedSystemUser.getId()),
        Collections.singletonList(loggedSystemUser.getRole()), loggedUserDataDtoMap);
  }

  private void saveBasicUserInfoToMap(User loggedSystemUser, Map<String, Object> loggedUserDataDtoMap) {
    loggedUserDataDtoMap.put("username", loggedSystemUser.getUsername());
    loggedUserDataDtoMap.put("email", loggedSystemUser.getEmail());
    loggedUserDataDtoMap.put("fullName", loggedSystemUser.getFullName());
    loggedUserDataDtoMap.put("administrator", loggedSystemUser.isAdministrator());
    loggedUserDataDtoMap.put("authProvider", loggedSystemUser.getAuthProvider().toString());
  }

  private String generateJwtToken(User loggedSystemUser) {
    long currentUnixTime = System.currentTimeMillis() / 1000L;

    Map<String, Object> tokenPayloadData = new HashMap<>();
    saveBasicUserInfoToMap(loggedSystemUser, tokenPayloadData);
    tokenPayloadData.put("sub", String.valueOf(loggedSystemUser.getId())); // subject - user unique ID
    tokenPayloadData.put("nbf", currentUnixTime + (60 * 10)); // not valid before - 10 minutes
    tokenPayloadData.put("roles", Collections.singletonList(loggedSystemUser.getRole())); // user role
    tokenPayloadData.put("iss", ISSUER_APP_NAME); // issuer
    tokenPayloadData.put("exp", currentUnixTime + (60 * 360)); // token expiration - 6 hours
    tokenPayloadData.put("iat", currentUnixTime); // issued at
    return jwtTokenGenerator
        .generateToken(tokenPayloadData)
        .orElseThrow(() -> {
          logger.error("JWT cannot be generated");
          return new ApiException(FaultType.GENERAL_ERROR, "JWT cannot be generated");
        });
  }
}
