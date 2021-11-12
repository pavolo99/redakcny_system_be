package sk.tuke.fei.kpi.dp.auth.gitlab;

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
import sk.tuke.fei.kpi.dp.dto.GitlabUserDto;
import sk.tuke.fei.kpi.dp.service.AuthenticationService;

@Named("gitlab")
@Singleton
public class GitlabAuthenticationMapper implements OauthAuthenticationMapper {


  private static final String GITLAB_TOKEN_PREFIX = "Bearer ";
  private final GitlabApiClient gitlabApiClient;
  private final AuthenticationService authenticationService;

  public GitlabAuthenticationMapper(GitlabApiClient gitlabApiClient,
      AuthenticationService authenticationService) {
    this.gitlabApiClient = gitlabApiClient;
    this.authenticationService = authenticationService;
  }

  /**
   * Gitlab auth response from gitlab.authorization.url and gitlab.token.url properties
   * @param tokenResponse contains data about provided gitlab access and refresh token
   * @param state
   * @return publisher of auth response
   * */
  @Override
  public Publisher<AuthenticationResponse> createAuthenticationResponse(TokenResponse tokenResponse,
      @Nullable State state) {
    String gitlabAccessToken = GITLAB_TOKEN_PREFIX + tokenResponse.getAccessToken();
    Publisher<GitlabUserDto> gitlabUserDtoPublisher = gitlabApiClient.getLoggedGitlabUser(gitlabAccessToken);

    return Flux.from(gitlabUserDtoPublisher).map(
        gitlabUserDto -> authenticationService.handleAuthenticationResponse(gitlabUserDto,
            AuthProvider.GITLAB));
  }
}
