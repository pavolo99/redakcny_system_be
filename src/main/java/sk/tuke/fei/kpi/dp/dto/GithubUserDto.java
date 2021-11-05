package sk.tuke.fei.kpi.dp.dto;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class GithubUserDto {

  private final String login;
  private final String name;
  private final String email;

  public GithubUserDto(String login, String name, String email) {
    this.login = login;
    this.name = name;
    this.email = email;
  }

  public String getLogin() {
    return login;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }
}
