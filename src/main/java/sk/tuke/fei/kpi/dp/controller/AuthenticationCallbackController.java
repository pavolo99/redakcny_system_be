package sk.tuke.fei.kpi.dp.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import sk.tuke.fei.kpi.dp.service.AuthenticationCallbackService;

@Controller("login-callback")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class AuthenticationCallbackController {

  private final AuthenticationCallbackService authenticationCallbackService;

  public AuthenticationCallbackController(AuthenticationCallbackService authenticationCallbackService) {
    this.authenticationCallbackService = authenticationCallbackService;
  }

  @Get(produces = MediaType.APPLICATION_JSON)
  public HttpResponse<Object> redirectToFrontEndUrlCallbackRouteWithAccessToken(Authentication authentication) {
    return HttpResponse.redirect(authenticationCallbackService.generateFrontEndCallbackURIWithAccessToken(authentication));
  }
}
