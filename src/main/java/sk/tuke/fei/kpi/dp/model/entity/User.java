package sk.tuke.fei.kpi.dp.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import sk.tuke.fei.kpi.dp.common.AuthProvider;

@Entity
@Table(name = "SYSTEM_USER")
public class User {

  @Id
  @GeneratedValue
  @Column(name = "ID")
  private Long id;

  @Column(name = "USERNAME")
  private String username;

  @Column(name = "NAME")
  private String name;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "AUTH_PROVIDER")
  @Enumerated(EnumType.STRING)
  private AuthProvider authProvider;

  @Column(name = "ROLE")
  private String role;

  public User() {
  }

  public User(String username, String name, String email, AuthProvider authProvider, String role) {
    this.username = username;
    this.name = name;
    this.email = email;
    this.authProvider = authProvider;
    this.role = role;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public AuthProvider getAuthProvider() {
    return authProvider;
  }

  public void setAuthProvider(AuthProvider authProvider) {
    this.authProvider = authProvider;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
