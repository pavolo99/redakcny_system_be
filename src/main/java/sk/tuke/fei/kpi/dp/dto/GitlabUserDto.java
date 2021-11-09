package sk.tuke.fei.kpi.dp.dto;

import io.micronaut.core.annotation.Introspected;
import sk.tuke.fei.kpi.dp.auth.ProviderUser;

@Introspected
public class GitlabUserDto implements ProviderUser {

  private final String username;
  private final String name;
  private final String email;

  public GitlabUserDto(String username, String name, String email) {
    this.username = username;
    this.name = name;
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

}
