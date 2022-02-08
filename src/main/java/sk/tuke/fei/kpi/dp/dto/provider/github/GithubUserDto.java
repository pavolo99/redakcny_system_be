package sk.tuke.fei.kpi.dp.dto.provider.github;

import io.micronaut.core.annotation.Introspected;
import sk.tuke.fei.kpi.dp.dto.provider.ProviderUser;

@Introspected
public class GithubUserDto implements ProviderUser {

  private final String login;
  private final String name;
  private final String email;

  public GithubUserDto(String login, String name, String email) {
    this.login = login;
    this.name = name;
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public String getUsername() {
    return login;
  }
}
