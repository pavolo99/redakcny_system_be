package sk.tuke.fei.kpi.dp.dto.create;

import io.micronaut.core.annotation.Introspected;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Introspected
public class CommentReplyCreateDto {

  @NotBlank
  @Size(max = 1000)
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
