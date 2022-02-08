package sk.tuke.fei.kpi.dp.service;

import io.micronaut.security.authentication.AuthenticationResponse;
import sk.tuke.fei.kpi.dp.common.AuthProvider;
import sk.tuke.fei.kpi.dp.dto.provider.ProviderUser;

public interface AuthenticationService {

  AuthenticationResponse handleAuthenticationResponse(ProviderUser loggedProviderUserDto, AuthProvider authProvider);
}
