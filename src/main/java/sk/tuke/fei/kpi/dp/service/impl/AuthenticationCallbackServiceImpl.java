package sk.tuke.fei.kpi.dp.service.impl;

import io.micronaut.context.annotation.Value;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import java.net.URI;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.exception.FaultType;
import sk.tuke.fei.kpi.dp.service.AuthenticationCallbackService;

@Singleton
public class AuthenticationCallbackServiceImpl implements AuthenticationCallbackService {

  private final Logger logger = LoggerFactory.getLogger(AuthenticationCallbackServiceImpl.class);

  @Value("${micronaut.application.front-end-url}")
  String frontEndUrl;

  @Override
  public URI generateFrontEndCallbackURIWithAccessToken(Authentication authentication) {
    logger.info("About to generate front end callback uri with access token " + authentication.getName());
    String frontEndCallbackRoute = "/login-callback";
    String accessToken = String.format("?accessToken=%s", authentication.getAttributes().get("accessToken"));
    try {
      return new URI(frontEndUrl + frontEndCallbackRoute + accessToken);
    } catch (URISyntaxException e) {
      logger.error("Cannot generate URI location");
      throw new ApiException(FaultType.GENERAL_ERROR, "Cannot generate URI location");
    }
  }
}
