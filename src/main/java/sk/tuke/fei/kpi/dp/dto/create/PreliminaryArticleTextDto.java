package sk.tuke.fei.kpi.dp.dto.create;

import io.micronaut.core.annotation.Introspected;
import javax.validation.constraints.Size;

@Introspected
public class PreliminaryArticleTextDto {

  @Size(max = 100000)
  String text;

  public PreliminaryArticleTextDto() {
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

}
