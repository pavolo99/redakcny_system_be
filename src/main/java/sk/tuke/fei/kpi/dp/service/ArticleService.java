package sk.tuke.fei.kpi.dp.service;

import io.micronaut.security.authentication.Authentication;
import java.util.List;
import sk.tuke.fei.kpi.dp.common.QueryArticleStatus;
import sk.tuke.fei.kpi.dp.common.QueryArticleType;
import sk.tuke.fei.kpi.dp.dto.ArchivedArticleDto;
import sk.tuke.fei.kpi.dp.dto.ArticleEditDto;
import sk.tuke.fei.kpi.dp.dto.update.UpdateArticleDto;
import sk.tuke.fei.kpi.dp.dto.view.ArticleViewDto;
import sk.tuke.fei.kpi.dp.model.entity.Article;

public interface ArticleService {

  List<ArticleViewDto> getAllArticles(Authentication authentication,
      QueryArticleType queryArticleType, QueryArticleStatus queryArticleStatus);

  Long createArticle(Authentication authentication);

  ArticleEditDto updateArticle(Authentication authentication, Long id,
      UpdateArticleDto updateArticleDto, boolean createNewVersion);

  ArticleEditDto approveArticle(Authentication authentication, Long id, UpdateArticleDto updateArticleDto);

  ArticleEditDto archiveArticle(Authentication authentication, Long id);

  ArticleEditDto sendArticleToReview(Authentication authentication, Long id, UpdateArticleDto updateArticleDto);

  ArticleEditDto sendArticleReview(Authentication authentication, Long id, UpdateArticleDto updateArticleDto);

  ArticleEditDto publishArticle(Authentication authentication, Long id, UpdateArticleDto updateArticleDto);

  ArticleEditDto denyArticle(Authentication authentication, Long id);

  void removeArticle(Authentication authentication, Long id);

  Article findArticleById(Long id);

  ArchivedArticleDto getArchivedArticle(Authentication authentication, Long id);

  void restoreArticle(Authentication authentication, Long id);
}
