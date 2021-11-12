package sk.tuke.fei.kpi.dp.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import sk.tuke.fei.kpi.dp.dto.LoggedUserDto;
import sk.tuke.fei.kpi.dp.service.LoggedUserService;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("loggedUser")
public class LoggedUserController {

  private final LoggedUserService loggedUserService;

  public LoggedUserController(LoggedUserService loggedUserService) {
    this.loggedUserService = loggedUserService;
  }

  @Get
  public HttpResponse<LoggedUserDto> getLoggedUser(Authentication authentication) {
    return HttpResponse.ok(loggedUserService.getLoggedUser(authentication));
  }
}
