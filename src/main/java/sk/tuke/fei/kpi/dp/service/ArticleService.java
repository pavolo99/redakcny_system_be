package sk.tuke.fei.kpi.dp.service;

import java.util.List;
import sk.tuke.fei.kpi.dp.common.QueryArticleStatus;
import sk.tuke.fei.kpi.dp.common.QueryArticleType;
import sk.tuke.fei.kpi.dp.dto.ArticleDto;
import sk.tuke.fei.kpi.dp.dto.ArticleViewDto;
import sk.tuke.fei.kpi.dp.dto.UpdateArticleDto;

public interface ArticleService {

  ArticleDto getArticle(Long id);

  List<ArticleViewDto> getAllArticles(QueryArticleType queryArticleType, QueryArticleStatus queryArticleStatus);

  ArticleDto createArticle();

  ArticleDto updateArticle(Long id, UpdateArticleDto updateArticleDto);

  ArticleDto approveArticle(Long id);

  ArticleDto archiveArticle(Long id);

  ArticleDto sendArticleToReview(Long id);

  ArticleDto sendArticleReview(Long id);

  ArticleDto publishArticle(Long id);

  ArticleDto denyArticle(Long id);

  void removeArticle(Long id);
}
