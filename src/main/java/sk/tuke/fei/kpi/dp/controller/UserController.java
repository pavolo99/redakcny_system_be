package sk.tuke.fei.kpi.dp.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import java.util.List;
import sk.tuke.fei.kpi.dp.dto.LoggedUserDto;
import sk.tuke.fei.kpi.dp.dto.UserDto;
import sk.tuke.fei.kpi.dp.service.UserService;

@Secured(SecurityRule.IS_AUTHENTICATED)
//@Controller("user")
@Controller("api/user")
public class UserController {

  private final UserService userService;

  public UserController(UserService loggedUserService) {
    this.userService = loggedUserService;
  }

  @Get(uri = "/collaborator/{searchValue}")
  public HttpResponse<List<UserDto>> getPotentialCollaborators(Authentication authentication,
      @PathVariable String searchValue) {
    return HttpResponse.ok(userService.getPotentialCollaborators(authentication, searchValue));
  }

  @Get(uri = "/logged")
  public HttpResponse<LoggedUserDto> getLoggedUser(Authentication authentication) {
    return HttpResponse.ok(userService.getLoggedUser(authentication));
  }
}
