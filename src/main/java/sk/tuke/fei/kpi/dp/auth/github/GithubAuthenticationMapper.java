package sk.tuke.fei.kpi.dp.auth.github;

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
import reactor.core.publisher.Mono;
import sk.tuke.fei.kpi.dp.common.AuthProvider;
import sk.tuke.fei.kpi.dp.dto.GithubUserDto;
import sk.tuke.fei.kpi.dp.model.entity.User;
import sk.tuke.fei.kpi.dp.service.UserService;

@Named("github") // <1>
@Singleton
public class GithubAuthenticationMapper implements OauthAuthenticationMapper {

  public static final String TOKEN_PREFIX = "token ";

  private final GithubApiClient apiClient;
  private final UserService userService;

  public GithubAuthenticationMapper(GithubApiClient apiClient, UserService userService) {
    this.apiClient = apiClient;
    this.userService = userService;
  }

  @Override
  public Publisher<AuthenticationResponse> createAuthenticationResponse(TokenResponse tokenResponse,
      @Nullable State state) {

    String githubAccessToken = TOKEN_PREFIX + tokenResponse.getAccessToken();
    System.out.println("githubAccessToken = " + githubAccessToken);
    Publisher<GithubUserDto> githubUser = apiClient.getUser(githubAccessToken);
    return Mono.from(githubUser) // <2>
        .map(user -> {

          User loggedUser = userService.findByUsernameAndAuthProvider(user.getLogin(),
              AuthProvider.GITHUB).orElse(new User(user.getLogin(), user.getName(),
              user.getEmail(), AuthProvider.GITHUB, "AUTHOR"));

          if (loggedUser.getId() == null) {
            userService.saveUser(loggedUser);
          }

          System.out.println(user.getLogin());
          Map<String, Object> attributesMap = new HashMap<>();
          attributesMap.put(ACCESS_TOKEN_KEY, githubAccessToken);
          attributesMap.put(REFRESH_TOKEN_KEY, tokenResponse.getRefreshToken());
          attributesMap.put(PROVIDER_KEY, AuthProvider.GITHUB);

          return AuthenticationResponse.success(user.getLogin(),
              Collections.singletonList("AUTHOR"), attributesMap);
        });
  }
}
