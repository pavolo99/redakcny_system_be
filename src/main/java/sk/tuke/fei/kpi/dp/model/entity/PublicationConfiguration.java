package sk.tuke.fei.kpi.dp.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import sk.tuke.fei.kpi.dp.common.Provider;

@Entity
@Table(name = "PUBLICATION_CONFIGURATION")
public class PublicationConfiguration {

  @Id
  @GeneratedValue
  @Column(name = "ID")
  private Long id;

  @Column(name = "PROVIDER")
  @Enumerated(EnumType.STRING)
  private Provider provider;

  @Column(name = "REPOSITORY_PATH")
  private String repositoryPath;

  @Column(name = "BRANCH")
  private String branch;

  @Column(name = "COMMIT_MESSAGE")
  private String commitMessage;

  @Column(name = "PATH_TO_ARTICLE")
  private String pathToArticle;

  @Column(name = "PRIVATE_TOKEN")
  private String privateToken;

  public PublicationConfiguration() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Provider getProvider() {
    return provider;
  }

  public void setProvider(Provider provider) {
    this.provider = provider;
  }

  public String getRepositoryPath() {
    return repositoryPath;
  }

  public void setRepositoryPath(String repositoryPath) {
    this.repositoryPath = repositoryPath;
  }

  public String getBranch() {
    return branch;
  }

  public void setBranch(String branch) {
    this.branch = branch;
  }

  public String getCommitMessage() {
    return commitMessage;
  }

  public void setCommitMessage(String commitMessage) {
    this.commitMessage = commitMessage;
  }

  public String getPathToArticle() {
    return pathToArticle;
  }

  public void setPathToArticle(String pathToArticle) {
    this.pathToArticle = pathToArticle;
  }

  public String getPrivateToken() {
    return privateToken;
  }

  public void setPrivateToken(String privateToken) {
    this.privateToken = privateToken;
  }
}