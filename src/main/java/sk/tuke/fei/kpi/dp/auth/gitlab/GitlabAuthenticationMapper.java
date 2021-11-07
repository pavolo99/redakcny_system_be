package sk.tuke.fei.kpi.dp.auth.gitlab;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.oauth2.endpoint.authorization.state.State;
import io.micronaut.security.oauth2.endpoint.token.response.OauthAuthenticationMapper;
import io.micronaut.security.oauth2.endpoint.token.response.TokenResponse;
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import sk.tuke.fei.kpi.dp.common.AuthProvider;
import sk.tuke.fei.kpi.dp.dto.GitlabUserDto;
import sk.tuke.fei.kpi.dp.dto.LoggedUserDto;
import sk.tuke.fei.kpi.dp.model.entity.User;
import sk.tuke.fei.kpi.dp.service.LoggedUserService;
import sk.tuke.fei.kpi.dp.service.UserService;

@Named("gitlab") // <1>
@Singleton
public class GitlabAuthenticationMapper implements OauthAuthenticationMapper {

  public static final String TOKEN_PREFIX = "Bearer ";

  private final GitlabApiClient gitlabApiClient;
  private final UserService userService;
  private final JwtTokenGenerator jwtTokenGenerator;

  @Inject
  private LoggedUserService loggedUserService;

  public GitlabAuthenticationMapper(GitlabApiClient gitlabApiClient, UserService userService,
      JwtTokenGenerator jwtTokenGenerator) {
    this.jwtTokenGenerator = jwtTokenGenerator;
    this.gitlabApiClient = gitlabApiClient;
    this.userService = userService;
  }

  @Override
  public Publisher<AuthenticationResponse> createAuthenticationResponse(TokenResponse tokenResponse,
      @Nullable State state) {
    String gitlabAccessToken = TOKEN_PREFIX + tokenResponse.getAccessToken();
    System.out.println(gitlabAccessToken);
    Publisher<GitlabUserDto> gitlabUser = gitlabApiClient.getLoggedGitlabUser(gitlabAccessToken);

    return Flux.from(gitlabUser)
        .map(user -> {
          User loggedUser = userService.findByUsernameAndAuthProvider(user.getUsername(),
              AuthProvider.GITLAB).orElse(new User(user.getUsername(), user.getName(),
              user.getEmail(), AuthProvider.GITLAB, "AUTHOR"));

          if (loggedUser.getId() == null) {
            userService.saveUser(loggedUser);
          }
          int currentTimeMillis = (int) (System.currentTimeMillis() / 1000L);

          Map<String, Object> claims = new HashMap<>();
          claims.put("sub", user.getUsername());
          claims.put("nbf", currentTimeMillis + (60 * 10));
          claims.put("roles", List.of("AUTHOR"));
          claims.put("iss", "redakcny-system-be");
          claims.put("exp", currentTimeMillis + (60 * 180));
          claims.put("iat", currentTimeMillis);
          String jwtAccessToken = jwtTokenGenerator.generateToken(claims).orElse(null);

          LoggedUserDto loggedUserDto = new LoggedUserDto(loggedUser.getId(),
              loggedUser.getUsername(), loggedUser.getRole(), jwtAccessToken, AuthProvider.GITLAB);
          loggedUserService.setLoggedUser(loggedUserDto);

          return AuthenticationResponse.success(user.getUsername());

        });
  }
}
