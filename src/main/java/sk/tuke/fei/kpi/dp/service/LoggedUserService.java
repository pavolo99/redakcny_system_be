package sk.tuke.fei.kpi.dp.service;

import io.micronaut.security.authentication.Authentication;
import sk.tuke.fei.kpi.dp.dto.LoggedUserDto;

public interface LoggedUserService {

  LoggedUserDto getLoggedUser(Authentication authentication);
}
