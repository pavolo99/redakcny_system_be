package sk.tuke.fei.kpi.dp.dto;

import io.micronaut.core.annotation.Introspected;
import sk.tuke.fei.kpi.dp.common.Provider;

@Introspected
public class UserForAdminDto {

  private Long id;
  private String username;
  private String fullName;
  private boolean administrator;
  private boolean editor;
  private String email;
  private Provider authProvider;

  public UserForAdminDto() {
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

  public boolean isAdministrator() {
    return administrator;
  }

  public void setAdministrator(boolean administrator) {
    this.administrator = administrator;
  }

  public boolean isEditor() {
    return editor;
  }

  public void setEditor(boolean editor) {
    this.editor = editor;
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
