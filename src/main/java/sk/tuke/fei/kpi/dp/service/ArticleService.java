package sk.tuke.fei.kpi.dp.service;

import io.micronaut.security.authentication.Authentication;
import java.util.List;
import sk.tuke.fei.kpi.dp.common.QueryArticleStatus;
import sk.tuke.fei.kpi.dp.common.QueryArticleType;
import sk.tuke.fei.kpi.dp.dto.ArticleEditDto;
import sk.tuke.fei.kpi.dp.dto.ArticleViewDto;
import sk.tuke.fei.kpi.dp.dto.UpdateArticleDto;
import sk.tuke.fei.kpi.dp.model.entity.Article;

public interface ArticleService {

  ArticleEditDto getArticle(Authentication authentication, Long id);

  List<ArticleViewDto> getAllArticles(Authentication authentication,
      QueryArticleType queryArticleType, QueryArticleStatus queryArticleStatus);

  ArticleEditDto createArticle(Authentication authentication);

  ArticleEditDto updateArticle(Authentication authentication, Long id, UpdateArticleDto updateArticleDto);

  ArticleEditDto approveArticle(Authentication authentication, Long id);

  ArticleEditDto archiveArticle(Authentication authentication, Long id);

  ArticleEditDto sendArticleToReview(Authentication authentication, Long id);

  ArticleEditDto sendArticleReview(Authentication authentication, Long id);

  ArticleEditDto publishArticle(Authentication authentication, Long id);

  ArticleEditDto denyArticle(Authentication authentication, Long id);

  void removeArticle(Authentication authentication, Long id);

  Article findArticleById(Long id);
}
