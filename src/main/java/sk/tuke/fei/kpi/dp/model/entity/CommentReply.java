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
@Table(name = "COMMENT_REPLY")
public class CommentReply {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "TEXT")
  private String text;

  @Column(name = "UPDATED_AT")
  private Date updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  private User createdBy;

  @ManyToOne(fetch = FetchType.LAZY)
  private Comment comment;

  public CommentReply() {
  }

  public CommentReply(String text, Date updatedAt, User createdBy,
      Comment comment) {
    this.text = text;
    this.updatedAt = updatedAt;
    this.createdBy = createdBy;
    this.comment = comment;
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

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public Comment getComment() {
    return comment;
  }

  public void setComment(Comment comment) {
    this.comment = comment;
  }
}
