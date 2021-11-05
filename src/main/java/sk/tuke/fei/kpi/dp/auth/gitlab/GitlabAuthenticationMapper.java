package sk.tuke.fei.kpi.dp.auth.gitlab;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.oauth2.endpoint.authorization.state.State;
import io.micronaut.security.oauth2.endpoint.token.response.OauthAuthenticationMapper;
import io.micronaut.security.oauth2.endpoint.token.response.TokenResponse;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import sk.tuke.fei.kpi.dp.common.AuthProvider;
import sk.tuke.fei.kpi.dp.dto.GitlabUserDto;
import sk.tuke.fei.kpi.dp.model.entity.User;
import sk.tuke.fei.kpi.dp.service.UserService;

@Named("gitlab") // <1>
@Singleton
public class GitlabAuthenticationMapper implements OauthAuthenticationMapper {

  public static final String TOKEN_PREFIX = "Bearer ";

  private final GitlabApiClient apiClient;
  private final UserService userService;

  public GitlabAuthenticationMapper(GitlabApiClient apiClient, UserService userService) {
    this.apiClient = apiClient;
    this.userService = userService;
  }

  @Override
  public Publisher<AuthenticationResponse> createAuthenticationResponse(TokenResponse tokenResponse,
      @Nullable State state) {
    String gitlabAccessToken = TOKEN_PREFIX + tokenResponse.getAccessToken();
    System.out.println(gitlabAccessToken);
    Publisher<GitlabUserDto> gitlabUser = apiClient.getUser(gitlabAccessToken);

    return Flux.from(gitlabUser)
        .map(user -> {
          User loggedUser = userService.findByUsernameAndAuthProvider(user.getUsername(),
              AuthProvider.GITLAB).orElse(new User(user.getUsername(), user.getName(),
              user.getEmail(), AuthProvider.GITLAB, "AUTHOR"));

          if (loggedUser.getId() == null) {
            userService.saveUser(loggedUser);
          }

          Map<String, Object> attributesMap = new HashMap<>();
          attributesMap.put(ACCESS_TOKEN_KEY, gitlabAccessToken);
          attributesMap.put(REFRESH_TOKEN_KEY, tokenResponse.getRefreshToken());
          attributesMap.put(PROVIDER_KEY, AuthProvider.GITLAB);
          return AuthenticationResponse.success(user.getUsername(),
              Collections.singletonList("AUTHOR"), attributesMap);
        });
  }
}
