package sk.tuke.fei.kpi.dp.model.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "VERSION", schema = "redakcny_system")
public class Version {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "TEXT")
  private String text;

  @Column(name = "CREATED_AT")
  private Date createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  private Article article;

  @ManyToOne(fetch = FetchType.LAZY)
  private User createdBy;

  public Version() {}

  public Version(String text, User user, Article article) {
    this.text = text;
    this.article = article;

    this.createdAt = new Date();
    this.createdBy = user;
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

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }
}
