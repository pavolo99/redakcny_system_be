package sk.tuke.fei.kpi.dp.service.impl;

import javax.inject.Singleton;
import sk.tuke.fei.kpi.dp.dto.LoggedUserDto;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.exception.FaultType;
import sk.tuke.fei.kpi.dp.mapper.UserMapper;
import sk.tuke.fei.kpi.dp.service.LoggedUserService;

@Singleton
public class LoggedUserServiceImpl implements LoggedUserService {

  private LoggedUserDto loggedUserDto;
  private final UserMapper userMapper;


  public LoggedUserServiceImpl(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public LoggedUserDto getLoggedUser() {
    LoggedUserDto savedLoggedUser = userMapper.loggedUserDtoToLoggedUserDto(loggedUserDto);
    if (savedLoggedUser == null) {
      throw new ApiException(FaultType.RECORD_NOT_FOUND, "Logged user is not available");
    }
    loggedUserDto = null;
    return savedLoggedUser;
  }

  @Override
  public void setLoggedUser(LoggedUserDto loggedUserDto) {
    this.loggedUserDto = loggedUserDto;
  }

}
