package sk.tuke.fei.kpi.dp.dto;

import io.micronaut.core.annotation.Introspected;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Introspected
public class CommentDto {

  Long id;
  Boolean isResolved;
  String text;
  Date updatedAt;
  Integer rangeFrom;
  Integer rangeTo;
  String commentedText;
  UserDto createdBy;
  List<CommentReplyDto> commentReplyDtoList = new ArrayList<>();

  public CommentDto() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getResolved() {
    return isResolved;
  }

  public void setResolved(Boolean resolved) {
    isResolved = resolved;
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

  public UserDto getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(UserDto createdBy) {
    this.createdBy = createdBy;
  }

  public List<CommentReplyDto> getCommentReplyDtoList() {
    return commentReplyDtoList;
  }

  public void setCommentReplyDtoList(List<CommentReplyDto> commentReplyDtoList) {
    this.commentReplyDtoList = commentReplyDtoList;
  }

  public String getCommentedText() {
    return commentedText;
  }

  public void setCommentedText(String commentedText) {
    this.commentedText = commentedText;
  }
}
