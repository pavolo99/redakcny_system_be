package sk.tuke.fei.kpi.dp.service;

import io.micronaut.security.authentication.Authentication;
import java.net.URI;

public interface AuthenticationCallbackService {

  URI generateFrontEndCallbackURIWithAccessToken(Authentication authentication);
}
