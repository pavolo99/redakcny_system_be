package sk.tuke.fei.kpi.dp.controller;

import static io.micronaut.security.oauth2.endpoint.token.response.OauthAuthenticationMapper.ACCESS_TOKEN_KEY;
import static io.micronaut.security.oauth2.endpoint.token.response.OauthAuthenticationMapper.PROVIDER_KEY;
import static io.micronaut.security.oauth2.endpoint.token.response.OauthAuthenticationMapper.REFRESH_TOKEN_KEY;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;
import java.util.HashMap;
import java.util.Map;

@Controller("/repos")
public class ReposController {

  public ReposController() {
  }

  @Secured(SecurityRule.IS_AUTHENTICATED)
  @View("repos")
  @Get
  Map<String, Object> index(Authentication authentication) {
    String accessToken = getAttribute(authentication, ACCESS_TOKEN_KEY);
    String refreshToken = getAttribute(authentication, REFRESH_TOKEN_KEY);
    String provider = getAttribute(authentication, PROVIDER_KEY);
    System.out.println("accessToken from repos = " + accessToken);
    System.out.println("refreshToken from repos = " + refreshToken);
    System.out.println("provider from repos = " + provider);
    authentication.getRoles().forEach(System.out::println);
    System.out.println("username = " + authentication.getName());
    return new HashMap<>();
  }

  private String getAttribute(Authentication authentication, String attribute) {
    Object claim = authentication.getAttributes().get(attribute);  // <6>
    if (claim instanceof String) {
      return (String) claim;
    }
    return "";
  }
}
