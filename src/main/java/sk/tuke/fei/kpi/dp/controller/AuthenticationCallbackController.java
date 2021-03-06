package sk.tuke.fei.kpi.dp.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import java.net.URI;
import sk.tuke.fei.kpi.dp.service.AuthenticationCallbackService;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("api/login-callback")
public class AuthenticationCallbackController {

  private final AuthenticationCallbackService authCallbackService;

  public AuthenticationCallbackController(AuthenticationCallbackService authenticationCallbackService) {
    this.authCallbackService = authenticationCallbackService;
  }

  /**
   * Rest service for catching success login from auth (gitlab/github) provider
   * Generate URI location with frontEndUrl route and access token
   * @param authentication contains logged user info which was saved in authentication service
   * @return http response of redirect URI
   * */
  @Get(produces = MediaType.APPLICATION_JSON)
  public HttpResponse<MutableHttpResponse<URI>> redirectToFrontEndUrlCallbackRouteWithAccessToken(
      Authentication authentication) {
    URI redirectURILocation = authCallbackService.generateFrontEndCallbackURIWithAccessToken(authentication);
    return HttpResponse.seeOther(redirectURILocation);
  }
}
