package sk.tuke.fei.kpi.dp.auth.github;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.oauth2.endpoint.authorization.state.State;
import io.micronaut.security.oauth2.endpoint.token.response.OauthAuthenticationMapper;
import io.micronaut.security.oauth2.endpoint.token.response.TokenResponse;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import sk.tuke.fei.kpi.dp.common.AuthProvider;
import sk.tuke.fei.kpi.dp.dto.provider.github.GithubUserDto;
import sk.tuke.fei.kpi.dp.provider.GithubApiClient;
import sk.tuke.fei.kpi.dp.service.AuthenticationService;

@Named("github")
@Singleton
public class GithubAuthenticationMapper implements OauthAuthenticationMapper {

  private static final String GITHUB_TOKEN_PREFIX = "token ";
  private final GithubApiClient githubApiClient;
  private final AuthenticationService authenticationService;

  public GithubAuthenticationMapper(GithubApiClient githubApiClient,
      AuthenticationService authenticationService) {
    this.githubApiClient = githubApiClient;
    this.authenticationService = authenticationService;
  }

  /**
   * Github auth response from github.authorization.url and github.token.url properties
   * @param tokenResponse data about provided github access and refresh token
   * @param state nullable state information
   * @return publisher of auth response
   * */
  @Override
  public Publisher<AuthenticationResponse> createAuthenticationResponse(TokenResponse tokenResponse,
      @Nullable State state) {

    String githubAccessToken = GITHUB_TOKEN_PREFIX + tokenResponse.getAccessToken();
    Publisher<GithubUserDto> githubUserDtoPublisher = githubApiClient.getLoggedGithubUser(githubAccessToken);

    return Flux.from(githubUserDtoPublisher).map(
        githubUserDto -> authenticationService.handleAuthenticationResponse(githubUserDto,
            AuthProvider.GITHUB));
  }
}
