package sk.tuke.fei.kpi.dp.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import sk.tuke.fei.kpi.dp.dto.LoggedUserDto;
import sk.tuke.fei.kpi.dp.service.LoggedUserService;

@Controller("loggedUser")
@Secured(SecurityRule.IS_ANONYMOUS)
public class LoggedUserController {

  private final LoggedUserService loggedUserService;

  public LoggedUserController(LoggedUserService loggedUserService) {
    this.loggedUserService = loggedUserService;
  }

  @Get
  public HttpResponse<LoggedUserDto> getLoggedUser() {
    LoggedUserDto savedLoggedUser = loggedUserService.getLoggedUser();
    if (savedLoggedUser == null) {
      return HttpResponse.notFound();
    }
    LoggedUserDto loggedUser = new LoggedUserDto();
    loggedUser.setId(savedLoggedUser.getId());
    loggedUser.setUsername(savedLoggedUser.getUsername());
    loggedUser.setRole(savedLoggedUser.getRole());
    loggedUser.setAccessToken(savedLoggedUser.getAccessToken());
    loggedUser.setAuthProvider(savedLoggedUser.getAuthProvider());
    loggedUserService.setLoggedUser(null);
    return HttpResponse.ok(loggedUser);
  }

}
