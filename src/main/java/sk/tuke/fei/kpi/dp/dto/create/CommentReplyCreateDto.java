package sk.tuke.fei.kpi.dp.dto.create;

import io.micronaut.core.annotation.Introspected;
import javax.validation.constraints.NotBlank;

@Introspected
public class CommentReplyCreateDto {

  @NotBlank
  String text;

  public CommentReplyCreateDto() {
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

}
