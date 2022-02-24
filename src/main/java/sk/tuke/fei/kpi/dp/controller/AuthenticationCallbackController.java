package sk.tuke.fei.kpi.dp.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import sk.tuke.fei.kpi.dp.service.AuthenticationCallbackService;

//@Controller("login-callback")
@Controller("api/login-callback")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class AuthenticationCallbackController {

  private final AuthenticationCallbackService authenticationCallbackService;

  public AuthenticationCallbackController(AuthenticationCallbackService authenticationCallbackService) {
    this.authenticationCallbackService = authenticationCallbackService;
  }

  /**
   * Rest service for catching success login from auth (gitlab/github) provider
   * Generate URI location with frontEndUrl route and access token
   * @param authentication contains logged user info which was saved in authentication service
   * @return http response of redirect URI
   * */
  @Get(produces = MediaType.APPLICATION_JSON)
  public HttpResponse<Object> redirectToFrontEndUrlCallbackRouteWithAccessToken(Authentication authentication) {
    return HttpResponse.redirect(authenticationCallbackService.generateFrontEndCallbackURIWithAccessToken(authentication));
  }
}
