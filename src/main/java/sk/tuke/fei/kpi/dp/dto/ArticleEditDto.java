package sk.tuke.fei.kpi.dp.dto;

import io.micronaut.core.annotation.Introspected;
import java.util.ArrayList;
import java.util.List;
import sk.tuke.fei.kpi.dp.model.entity.ArticleStatus;

@Introspected
public class ArticleEditDto {

  private Long id;
  private String name;
  private String text;
  private String keyWords;
  private String articleAbstract;
  private String publicFileName;
  private String publicationDecision;
  private Integer reviewNumber;
  private ArticleStatus articleStatus;
  private boolean canLoggedUserEdit;
  private List<UserDto> allConnectedUsers = new ArrayList<>();
  private List<UserDto> allCollaborators = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getKeyWords() {
    return keyWords;
  }

  public void setKeyWords(String keyWords) {
    this.keyWords = keyWords;
  }

  public String getArticleAbstract() {
    return articleAbstract;
  }

  public void setArticleAbstract(String articleAbstract) {
    this.articleAbstract = articleAbstract;
  }

  public String getPublicFileName() {
    return publicFileName;
  }

  public void setPublicFileName(String publicFileName) {
    this.publicFileName = publicFileName;
  }

  public String getPublicationDecision() {
    return publicationDecision;
  }

  public void setPublicationDecision(String publicationDecision) {
    this.publicationDecision = publicationDecision;
  }

  public Integer getReviewNumber() {
    return reviewNumber;
  }

  public void setReviewNumber(Integer reviewNumber) {
    this.reviewNumber = reviewNumber;
  }

  public ArticleStatus getArticleStatus() {
    return articleStatus;
  }

  public void setArticleStatus(ArticleStatus articleStatus) {
    this.articleStatus = articleStatus;
  }

  public boolean isCanLoggedUserEdit() {
    return canLoggedUserEdit;
  }

  public void setCanLoggedUserEdit(boolean canLoggedUserEdit) {
    this.canLoggedUserEdit = canLoggedUserEdit;
  }

  public List<UserDto> getAllConnectedUsers() {
    return allConnectedUsers;
  }

  public void setAllConnectedUsers(List<UserDto> allConnectedUsers) {
    this.allConnectedUsers = allConnectedUsers;
  }

  public List<UserDto> getAllCollaborators() {
    return allCollaborators;
  }

  public void setAllCollaborators(List<UserDto> allCollaborators) {
    this.allCollaborators = allCollaborators;
  }
}
