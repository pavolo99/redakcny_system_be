package sk.tuke.fei.kpi.dp.common;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import sk.tuke.fei.kpi.dp.model.entity.ArticleStatus;

@Converter
public class ArticleStatusConverter implements AttributeConverter<ArticleStatus, Integer> {

  @Override
  public Integer convertToDatabaseColumn(ArticleStatus orderStatus) {
    return orderStatus == null ? null : orderStatus.getId();
  }

  @Override
  public ArticleStatus convertToEntityAttribute(Integer orderStatusId) {
    if (orderStatusId == null) {
      return null;
    }
    switch (orderStatusId) {
      case 1:
        return ArticleStatus.WRITING;
      case 2:
        return ArticleStatus.IN_REVIEW;
      case 3:
        return ArticleStatus.AFTER_REVIEW;
      case 4:
        return ArticleStatus.APPROVED;
      case 5:
        return ArticleStatus.ARCHIVED;
      default:
        throw new IllegalArgumentException(orderStatusId + " does not exist");
    }
  }

}
