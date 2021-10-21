package sk.tuke.fei.kpi.dp.dto;

import sk.tuke.fei.kpi.dp.model.entity.ArticleStatus;

public class ArticleViewDto {

  private Long id;
  private String name;
  private ArticleStatus articleStatus;
  private Integer reviewNumber;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ArticleStatus getArticleStatus() {
    return articleStatus;
  }

  public void setArticleStatus(ArticleStatus articleStatus) {
    this.articleStatus = articleStatus;
  }

  public Integer getReviewNumber() {
    return reviewNumber;
  }

  public void setReviewNumber(Integer reviewNumber) {
    this.reviewNumber = reviewNumber;
  }
}