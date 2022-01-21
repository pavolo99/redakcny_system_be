package sk.tuke.fei.kpi.dp.model.entity;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "COMMENT")
public class Comment {

  @Id
  @GeneratedValue
  @Column(name = "ID")
  private Long id;

  @Column(name = "TEXT")
  private String text;

  @Column(name = "IS_RESOLVED")
  private Boolean isResolved = false;

  @Column(name = "UPDATED_AT")
  private Date updatedAt;

  @Column(name = "RANGE_FROM")
  private Integer rangeFrom;

  @Column(name = "RANGE_TO")
  private Integer rangeTo;

  @Column(name = "COMMENTED_TEXT")
  private String commentedText;

  @ManyToOne(fetch = FetchType.LAZY)
  private User createdBy;

  @ManyToOne(fetch = FetchType.LAZY)
  private Article article;

  public Comment() {}

  public Comment(String text, Article article, User loggedUser) {
    this.text = text;
    this.article = article;
    this.createdBy = loggedUser;
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

  public Boolean getResolved() {
    return isResolved;
  }

  public void setResolved(Boolean resolved) {
    isResolved = resolved;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Integer getRangeFrom() {
    return rangeFrom;
  }

  public void setRangeFrom(Integer rangeFrom) {
    this.rangeFrom = rangeFrom;
  }

  public Integer getRangeTo() {
    return rangeTo;
  }

  public void setRangeTo(Integer rangeTo) {
    this.rangeTo = rangeTo;
  }

  public String getCommentedText() {
    return commentedText;
  }

  public void setCommentedText(String commentedText) {
    this.commentedText = commentedText;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
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
    if (!(o instanceof Comment)) {
      return false;
    }
    Comment comment = (Comment) o;
    return id.equals(comment.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
