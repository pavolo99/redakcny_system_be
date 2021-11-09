package sk.tuke.fei.kpi.dp.dto;

import io.micronaut.core.annotation.Introspected;
import sk.tuke.fei.kpi.dp.common.AuthProvider;

@Introspected
public class LoggedUserDto {

  private Long id;
  private String username;
  private String fullName;
  private String role;
  private String email;
  private String accessToken;
  private AuthProvider authProvider;

  public LoggedUserDto() {
  }

  public LoggedUserDto(Long id, String username, String fullName, String role, String email,
      String accessToken, AuthProvider authProvider) {
    this.id = id;
    this.username = username;
    this.fullName = fullName;
    this.role = role;
    this.email = email;
    this.accessToken = accessToken;
    this.authProvider = authProvider;
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

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public AuthProvider getAuthProvider() {
    return authProvider;
  }

  public void setAuthProvider(AuthProvider authProvider) {
    this.authProvider = authProvider;
  }
}
