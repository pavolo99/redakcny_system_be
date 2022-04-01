package sk.tuke.fei.kpi.dp.dto;

import io.micronaut.core.annotation.Introspected;
import java.util.Date;

@Introspected
public class CommentReplyDto {

  Long id;
  String text;
  Date updatedAt;
  UserDto createdBy;

  public CommentReplyDto() {
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

  public UserDto getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(UserDto createdBy) {
    this.createdBy = createdBy;
  }
}
