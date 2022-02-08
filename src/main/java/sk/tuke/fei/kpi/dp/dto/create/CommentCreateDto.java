package sk.tuke.fei.kpi.dp.dto.create;

import io.micronaut.core.annotation.Introspected;
import javax.validation.constraints.NotBlank;

@Introspected
public class CommentCreateDto {

  @NotBlank
  String text;

  @NotBlank
  Integer rangeFrom;

  @NotBlank
  String rangeTo;

  String commentedText;

  public CommentCreateDto() {
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Integer getRangeFrom() {
    return rangeFrom;
  }

  public void setRangeFrom(Integer rangeFrom) {
    this.rangeFrom = rangeFrom;
  }

  public String getRangeTo() {
    return rangeTo;
  }

  public void setRangeTo(String rangeTo) {
    this.rangeTo = rangeTo;
  }

  public String getCommentedText() {
    return commentedText;
  }

  public void setCommentedText(String commentedText) {
    this.commentedText = commentedText;
  }
}
