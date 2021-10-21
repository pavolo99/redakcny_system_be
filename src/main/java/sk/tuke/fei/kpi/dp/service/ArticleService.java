package sk.tuke.fei.kpi.dp.service;

import java.util.List;
import sk.tuke.fei.kpi.dp.common.QueryArticleStatus;
import sk.tuke.fei.kpi.dp.common.QueryArticleType;
import sk.tuke.fei.kpi.dp.dto.ArticleEditDto;
import sk.tuke.fei.kpi.dp.dto.ArticleViewDto;
import sk.tuke.fei.kpi.dp.dto.UpdateArticleDto;

public interface ArticleService {

  ArticleEditDto getArticle(Long id);

  List<ArticleViewDto> getAllArticles(QueryArticleType queryArticleType, QueryArticleStatus queryArticleStatus);

  ArticleEditDto createArticle();

  ArticleEditDto updateArticle(Long id, UpdateArticleDto updateArticleDto);

  ArticleEditDto approveArticle(Long id);

  ArticleEditDto archiveArticle(Long id);

  ArticleEditDto sendArticleToReview(Long id);

  ArticleEditDto sendArticleReview(Long id);

  ArticleEditDto publishArticle(Long id);

  ArticleEditDto denyArticle(Long id);

  void removeArticle(Long id);
}
