package sk.tuke.fei.kpi.dp.model.entity;

public enum ArticleStatus {
  WRITING(1),
  IN_REVIEW(2),
  AFTER_REVIEW(3),
  APPROVED(4),
  ARCHIVED(5);

  private final Integer id;

  ArticleStatus(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }
}
