package sk.tuke.fei.kpi.dp.model.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ARTICLE_COLLABORATION_SESSION")
public class ArticleCollaborationSession {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "TEXT")
  private String text;

  @Column(name = "CAN_USER_EDIT")
  private Boolean canUserEdit = false;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  private Article article;

  public ArticleCollaborationSession() {}

  public ArticleCollaborationSession(Boolean canUserEdit, User user, Article article) {
    this.canUserEdit = canUserEdit;
    this.user = user;
    this.article = article;
  }

  public ArticleCollaborationSession(String text, Boolean canUserEdit,
      User user, Article article) {
    this.text = text;
    this.canUserEdit = canUserEdit;
    this.user = user;
    this.article = article;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Boolean getCanUserEdit() {
    return canUserEdit;
  }

  public void setCanUserEdit(Boolean canUserEdit) {
    this.canUserEdit = canUserEdit;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ArticleCollaborationSession)) {
      return false;
    }
    ArticleCollaborationSession session = (ArticleCollaborationSession) o;
    return id.equals(session.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
