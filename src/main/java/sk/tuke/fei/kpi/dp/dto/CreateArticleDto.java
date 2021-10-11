package sk.tuke.fei.kpi.dp.dto;

import io.micronaut.core.annotation.Introspected;
import javax.validation.constraints.NotBlank;

@Introspected
public class CreateArticleDto {

  @NotBlank
  private String name;

  @NotBlank
  private String text;

  @NotBlank
  private String keyWords;

  @NotBlank
  private String articleAbstract;

  @NotBlank
  private String publicFileName;

  @NotBlank
  private String publicationDecision;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getKeyWords() {
    return keyWords;
  }

  public void setKeyWords(String keyWords) {
    this.keyWords = keyWords;
  }

  public String getArticleAbstract() {
    return articleAbstract;
  }

  public void setArticleAbstract(String articleAbstract) {
    this.articleAbstract = articleAbstract;
  }

  public String getPublicFileName() {
    return publicFileName;
  }

  public void setPublicFileName(String publicFileName) {
    this.publicFileName = publicFileName;
  }

  public String getPublicationDecision() {
    return publicationDecision;
  }

  public void setPublicationDecision(String publicationDecision) {
    this.publicationDecision = publicationDecision;
  }
}
