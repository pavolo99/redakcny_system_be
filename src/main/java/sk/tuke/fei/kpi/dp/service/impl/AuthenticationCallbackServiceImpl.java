package sk.tuke.fei.kpi.dp.service.impl;

import io.micronaut.context.annotation.Value;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import java.net.URI;
import java.net.URISyntaxException;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.exception.FaultType;
import sk.tuke.fei.kpi.dp.service.AuthenticationCallbackService;

@Singleton
public class AuthenticationCallbackServiceImpl implements AuthenticationCallbackService {

  @Value("${micronaut.application.front-end-url}")
  String frontEndUrl;

  @Override
  public URI generateFrontEndCallbackURIWithAccessToken(Authentication authentication) {
    String frontEndCallbackRoute = "/login-callback";
    String accessToken = String.format("?accessToken=%s", authentication.getAttributes().get("accessToken"));
    try {
      return new URI(frontEndUrl + frontEndCallbackRoute + accessToken);
    } catch (URISyntaxException e) {
      throw new ApiException(FaultType.GENERAL_ERROR, "Cannot generate URI location");
    }
  }
}
