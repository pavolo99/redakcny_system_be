package sk.tuke.fei.kpi.dp.auth.github;

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
import sk.tuke.fei.kpi.dp.dto.GithubUserDto;
import sk.tuke.fei.kpi.dp.dto.LoggedUserDto;
import sk.tuke.fei.kpi.dp.model.entity.User;
import sk.tuke.fei.kpi.dp.service.LoggedUserService;
import sk.tuke.fei.kpi.dp.service.UserService;

@Named("github") // <1>
@Singleton
public class GithubAuthenticationMapper implements OauthAuthenticationMapper {

  public static final String TOKEN_PREFIX = "token ";

  private final GithubApiClient githubApiClient;
  private final UserService userService;
  private final JwtTokenGenerator jwtTokenGenerator;

  @Inject
  private LoggedUserService loggedUserService;

  public GithubAuthenticationMapper(GithubApiClient githubApiClient, UserService userService,
      JwtTokenGenerator jwtTokenGenerator) {
    this.githubApiClient = githubApiClient;
    this.userService = userService;
    this.jwtTokenGenerator = jwtTokenGenerator;
  }

  @Override
  public Publisher<AuthenticationResponse> createAuthenticationResponse(TokenResponse tokenResponse,
      @Nullable State state) {

    String githubAccessToken = TOKEN_PREFIX + tokenResponse.getAccessToken();
    System.out.println(githubAccessToken);
    Publisher<GithubUserDto> githubUser = githubApiClient.getLoggedGithubUser(githubAccessToken);

    return Flux.from(githubUser)
        .map(user -> {
          User loggedUser = userService.findByUsernameAndAuthProvider(user.getLogin(),
              AuthProvider.GITHUB).orElse(new User(user.getLogin(), user.getName(),
              user.getEmail(), AuthProvider.GITHUB, "AUTHOR"));

          if (loggedUser.getId() == null) {
            userService.saveUser(loggedUser);
          }
          int currentTimeMillis = (int) (System.currentTimeMillis() / 1000L);

          Map<String, Object> claims = new HashMap<>();
          claims.put("sub", user.getLogin());
          claims.put("nbf", currentTimeMillis + (60 * 10));
          claims.put("roles", List.of("AUTHOR"));
          claims.put("iss", "redakcny-system-be");
          claims.put("exp", currentTimeMillis + (60 * 180));
          claims.put("iat", currentTimeMillis);
          String jwtAccessToken = jwtTokenGenerator.generateToken(claims).orElse(null);

          LoggedUserDto loggedUserDto = new LoggedUserDto(loggedUser.getId(),
              loggedUser.getUsername(), loggedUser.getRole(), jwtAccessToken, AuthProvider.GITHUB);
          loggedUserService.setLoggedUser(loggedUserDto);

          return AuthenticationResponse.success(user.getLogin());

        });
  }
}
