package sk.tuke.fei.kpi.dp.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ARTICLE_COLLABORATOR")
public class ArticleCollaborator {

  @Id
  @GeneratedValue
  @Column(name = "ID")
  private Long id;

  @Column(name = "IS_OWNER")
  private Boolean isOwner = false;

  @Column(name = "CAN_EDIT")
  private Boolean canEdit = false;

  @Column(name = "IS_AUTHOR")
  private Boolean isAuthor = false;

  @ManyToOne
  private Article article;

  @ManyToOne
  private User user;

  public ArticleCollaborator() {
  }

  public ArticleCollaborator(Boolean isOwner, Boolean canEdit, Boolean isAuthor,
      Article article, User user) {
    this.isOwner = isOwner;
    this.canEdit = canEdit;
    this.isAuthor = isAuthor;
    this.article = article;
    this.user = user;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getOwner() {
    return isOwner;
  }

  public void setOwner(Boolean owner) {
    isOwner = owner;
  }

  public Boolean getCanEdit() {
    return canEdit;
  }

  public void setCanEdit(Boolean canEdit) {
    this.canEdit = canEdit;
  }

  public Boolean getAuthor() {
    return isAuthor;
  }

  public void setAuthor(Boolean author) {
    isAuthor = author;
  }

  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
