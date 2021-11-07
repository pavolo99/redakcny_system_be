package sk.tuke.fei.kpi.dp.service.impl;

import javax.inject.Singleton;
import sk.tuke.fei.kpi.dp.dto.LoggedUserDto;
import sk.tuke.fei.kpi.dp.service.LoggedUserService;

@Singleton
public class LoggedUserServiceImpl implements LoggedUserService {

  private LoggedUserDto loggedUserDto;

  public LoggedUserServiceImpl() {}

  @Override
  public LoggedUserDto getLoggedUser() {
    return loggedUserDto;
  }

  @Override
  public void setLoggedUser(LoggedUserDto loggedUserDto) {
    this.loggedUserDto = loggedUserDto;
  }

}
