package sk.tuke.fei.kpi.dp.model.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import sk.tuke.fei.kpi.dp.common.Provider;

@Entity
@Table(name = "SYSTEM_USER")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "USERNAME")
  private String username;

  @Column(name = "FULL_NAME")
  private String fullName;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "AUTH_PROVIDER")
  @Enumerated(EnumType.STRING)
  private Provider authProvider;

  @Column(name = "IS_ADMINISTRATOR")
  private boolean isAdministrator = false;

  @Column(name = "ROLE")
  private String role = "AUTHOR";

  public User() {}

  public User(Long id) {
    this.id = id;
  }

  public User(String username, String fullName,  String email, Provider authProvider) {
    this.username = username;
    this.fullName = fullName;
    this.email = email;
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

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public boolean isAdministrator() {
    return isAdministrator;
  }

  public void setAdministrator(boolean administrator) {
    isAdministrator = administrator;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof User)) {
      return false;
    }
    User user = (User) o;
    return id.equals(user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
