package sk.tuke.fei.kpi.dp.dto;

import io.micronaut.core.annotation.Introspected;
import sk.tuke.fei.kpi.dp.common.Provider;

@Introspected
public class LoggedUserDto {

  private Long id;
  private String username;
  private String fullName;
  private String administrator;
  private String role;
  private String email;
  private Provider authProvider;

  public LoggedUserDto() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getAdministrator() {
    return administrator;
  }

  public void setAdministrator(String administrator) {
    this.administrator = administrator;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Provider getAuthProvider() {
    return authProvider;
  }

  public void setAuthProvider(Provider authProvider) {
    this.authProvider = authProvider;
  }
}
