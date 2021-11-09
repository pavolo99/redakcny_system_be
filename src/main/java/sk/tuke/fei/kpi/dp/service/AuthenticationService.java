package sk.tuke.fei.kpi.dp.service;

import io.micronaut.security.authentication.AuthenticationResponse;
import sk.tuke.fei.kpi.dp.auth.ProviderUser;
import sk.tuke.fei.kpi.dp.common.AuthProvider;

public interface AuthenticationService {

  AuthenticationResponse handleAuthenticationResponse(ProviderUser loggedProviderUserDto, AuthProvider authProvider);
}
