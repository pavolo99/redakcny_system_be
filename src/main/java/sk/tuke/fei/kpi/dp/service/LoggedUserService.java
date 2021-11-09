package sk.tuke.fei.kpi.dp.service;

import sk.tuke.fei.kpi.dp.dto.LoggedUserDto;

public interface LoggedUserService {

  LoggedUserDto getLoggedUser();

  void setLoggedUser(LoggedUserDto loggedUserDto);
}
