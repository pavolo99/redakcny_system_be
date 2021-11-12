package sk.tuke.fei.kpi.dp.service.impl;

import io.micronaut.security.authentication.Authentication;
import java.util.Map;
import javax.inject.Singleton;
import sk.tuke.fei.kpi.dp.common.AuthProvider;
import sk.tuke.fei.kpi.dp.dto.LoggedUserDto;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.exception.FaultType;
import sk.tuke.fei.kpi.dp.service.LoggedUserService;

@Singleton
public class LoggedUserServiceImpl implements LoggedUserService {

  @Override
  public LoggedUserDto getLoggedUser(Authentication authentication) {
    String role = authentication
        .getRoles()
        .stream()
        .findAny()
        .orElseThrow(
            () -> new ApiException(FaultType.FORBIDDEN, "User does not have any role assigned"));
    Map<String, Object> attributes = authentication.getAttributes();
    LoggedUserDto loggedUserDto = new LoggedUserDto();
    loggedUserDto.setId(Long.valueOf(authentication.getName()));
    loggedUserDto.setRole(role);
    loggedUserDto.setUsername((String) attributes.get("username"));
    loggedUserDto.setFirstName((String) attributes.get("firstName"));
    loggedUserDto.setLastName((String) attributes.get("lastName"));
    loggedUserDto.setAuthProvider(AuthProvider.valueOf((String) attributes.get("authProvider")));
    loggedUserDto.setEmail((String) attributes.get("email"));

    return loggedUserDto;
  }

}
