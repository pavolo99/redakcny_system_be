package sk.tuke.fei.kpi.dp.service.impl;

import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
import jakarta.inject.Singleton;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import sk.tuke.fei.kpi.dp.auth.ProviderUser;
import sk.tuke.fei.kpi.dp.common.AuthProvider;
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
      AuthProvider authProvider) {
    String[] loggedUserFullName = loggedProviderUserDto.getName().split(" ");
    String firstName = loggedUserFullName[0];
    String lastName = loggedUserFullName[1];

    User loggedSystemUser = userService.findByUsernameAndAuthProvider(loggedProviderUserDto.getUsername(), authProvider)
        .orElse(new User(loggedProviderUserDto.getUsername(), firstName, lastName,
        loggedProviderUserDto.getEmail(), authProvider, "AUTHOR"));

    if (loggedSystemUser.getId() == null) {
      loggedSystemUser = userService.saveUser(loggedSystemUser);
    }

    String jwtAccessToken = generateJwtToken(loggedSystemUser);

    HashMap<String, Object> loggedUserDataDtoMap = new HashMap<>();
    loggedUserDataDtoMap.put("username", loggedSystemUser.getUsername());
    loggedUserDataDtoMap.put("email", loggedSystemUser.getEmail());
    loggedUserDataDtoMap.put("firstName", loggedSystemUser.getFirstName());
    loggedUserDataDtoMap.put("lastName", loggedSystemUser.getLastName());
    loggedUserDataDtoMap.put("administrator", loggedSystemUser.isAdministrator());
    loggedUserDataDtoMap.put("authProvider", loggedSystemUser.getAuthProvider().toString());
    loggedUserDataDtoMap.put("accessToken", jwtAccessToken);
    // unique username is generated ID - name field in authentication class
    return AuthenticationResponse.success(String.valueOf(loggedSystemUser.getId()),
        Collections.singletonList(loggedSystemUser.getRole()), loggedUserDataDtoMap);
  }

  private String generateJwtToken(User loggedSystemUser) {
    long currentUnixTime = System.currentTimeMillis() / 1000L;

    Map<String, Object> tokenPayloadData = new HashMap<>();
    tokenPayloadData.put("username", loggedSystemUser.getUsername());
    tokenPayloadData.put("email", loggedSystemUser.getEmail());
    tokenPayloadData.put("firstName", loggedSystemUser.getFirstName());
    tokenPayloadData.put("lastName", loggedSystemUser.getLastName());
    tokenPayloadData.put("administrator", loggedSystemUser.isAdministrator());
    tokenPayloadData.put("authProvider", loggedSystemUser.getAuthProvider().toString());
    tokenPayloadData.put("sub", String.valueOf(loggedSystemUser.getId())); // subject - user unique ID
    tokenPayloadData.put("nbf", currentUnixTime + (60 * 10)); // not valid before - 10 minutes
    tokenPayloadData.put("roles", Collections.singletonList(loggedSystemUser.getRole())); // user role
    tokenPayloadData.put("iss", ISSUER_APP_NAME); // issuer
    tokenPayloadData.put("exp", currentUnixTime + (60 * 360)); // token expiration - 6 hours
    tokenPayloadData.put("iat", currentUnixTime); // issued at
    return jwtTokenGenerator
        .generateToken(tokenPayloadData)
        .orElseThrow(() -> new ApiException(FaultType.GENERAL_ERROR, "JWT cannot be generated"));
  }
}
