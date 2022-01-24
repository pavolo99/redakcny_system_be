package sk.tuke.fei.kpi.dp.service.impl;

import static sk.tuke.fei.kpi.dp.exception.FaultType.FORBIDDEN;
import static sk.tuke.fei.kpi.dp.exception.FaultType.INVALID_PARAMS;
import static sk.tuke.fei.kpi.dp.exception.FaultType.RECORD_NOT_FOUND;
import static sk.tuke.fei.kpi.dp.model.entity.ArticleStatus.APPROVED;
import static sk.tuke.fei.kpi.dp.model.entity.ArticleStatus.ARCHIVED;
import static sk.tuke.fei.kpi.dp.model.entity.ArticleStatus.IN_REVIEW;
import static sk.tuke.fei.kpi.dp.model.entity.ArticleStatus.WRITING;

import io.micronaut.security.authentication.Authentication;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import sk.tuke.fei.kpi.dp.common.QueryArticleStatus;
import sk.tuke.fei.kpi.dp.common.QueryArticleType;
import sk.tuke.fei.kpi.dp.dto.ArticleEditDto;
import sk.tuke.fei.kpi.dp.dto.ArticleViewDto;
import sk.tuke.fei.kpi.dp.dto.UpdateArticleDto;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.mapper.ArticleMapper;
import sk.tuke.fei.kpi.dp.model.entity.Article;
import sk.tuke.fei.kpi.dp.model.entity.ArticleCollaborator;
import sk.tuke.fei.kpi.dp.model.entity.ArticleStatus;
import sk.tuke.fei.kpi.dp.model.entity.User;
import sk.tuke.fei.kpi.dp.model.entity.Version;
import sk.tuke.fei.kpi.dp.model.repository.ArticleRepository;
import sk.tuke.fei.kpi.dp.model.repository.VersionRepository;
import sk.tuke.fei.kpi.dp.service.ArticleService;
import sk.tuke.fei.kpi.dp.service.UserService;

@Singleton
public class ArticleServiceImpl implements ArticleService {

  private final ArticleRepository articleRepository;
  private final ArticleMapper articleMapper;
  private final UserService userService;
  private final VersionRepository versionRepository;

  public ArticleServiceImpl(ArticleRepository articleRepository, ArticleMapper articleMapper,
      UserService userService, VersionRepository versionRepository) {
    this.articleRepository = articleRepository;
    this.articleMapper = articleMapper;
    this.userService = userService;
    this.versionRepository = versionRepository;
  }

  @Override
  @Transactional
  public ArticleEditDto getArticle(Authentication authentication, Long id) {
    Article article = findArticleById(id);
    boolean canLoggedUserEdit = article.getArticleCollaborators()
        .stream()
        .anyMatch(collaborator -> collaborator.getCanEdit()
            && collaborator.getUser().getId().equals(Long.parseLong(authentication.getName())));
    ArticleEditDto articleEditDto = articleMapper.articleToArticleDto(article);
    articleEditDto.setCanLoggedUserEdit(canLoggedUserEdit);
    return articleEditDto;
  }

  @Override
  public List<ArticleViewDto> getAllArticles(Authentication authentication,
      QueryArticleType queryArticleType, QueryArticleStatus queryArticleStatus) {
    List<Article> articles = new ArrayList<>();
    long loggedUserId = Long.parseLong(authentication.getName());
    if (QueryArticleType.MINE.equals(queryArticleType)) {
      articles = articleRepository.getAllArticlesCreatedByLoggedUser(loggedUserId);
    } else if (QueryArticleType.APPROVED.equals(queryArticleType)) {
      articles = articleRepository.getArticlesByStatus(List.of(APPROVED));
    } else if (QueryArticleType.ARCHIVED.equals(queryArticleType)) {
      articles = articleRepository.getArticlesByStatus(List.of(ARCHIVED));
    } else if (QueryArticleType.SHARED_WITH_ME.equals(queryArticleType)) {
      articles = articleRepository.getSharedArticlesOfLoggedUser(loggedUserId);
    }
    if (QueryArticleStatus.WRITING.equals(queryArticleStatus)) {
      articles = articles.stream().filter(article -> article.getArticleStatus().equals(WRITING)).collect(Collectors.toList());
    } else if (QueryArticleStatus.IN_REVIEW.equals(queryArticleStatus)) {
      articles = articles.stream().filter(article -> article.getArticleStatus().equals(IN_REVIEW)).collect(Collectors.toList());
    } else if (QueryArticleStatus.AFTER_REVIEW.equals(queryArticleStatus)) {
      articles = articles.stream().filter(article -> article.getArticleStatus().equals(WRITING) && article.getReviewNumber() > 0).collect(Collectors.toList());
    }
    return articles
        .stream()
        .map(articleMapper::articleToArticleViewDto)
        .collect(Collectors.toList());
  }

  @Override
  public Long createArticle(Authentication authentication) {
    User createdBy = userService.findUserById(Long.parseLong(authentication.getName()));
    Article article = new Article("Nazov članku", "Text članku", 0, WRITING, createdBy);
    ArticleCollaborator articleCollaborator = new ArticleCollaborator(true, true, true, article, createdBy);
    article.getArticleCollaborators().add(articleCollaborator);
    Version firstVersion = new Version(article.getText(), createdBy, article);
    Article savedArticle = articleRepository.save(article);
    versionRepository.save(firstVersion);
    return savedArticle.getId();
  }

  @Override
  public ArticleEditDto updateArticle(Authentication authentication, Long id, UpdateArticleDto updateArticleDto) {
    if (!id.equals(updateArticleDto.getId())) {
      throw new ApiException(INVALID_PARAMS, "Id is not equal with update article dto id");
    }
    Article article = findArticleById(id);
    // TODO resolve users which are allowed to update article
//    if (loggedUserId != article.getCreatedBy().getId() && !authentication.getRoles().contains("Editor")) {
//      throw new ApiException(FORBIDDEN, "You are not allowed to update this article");
//    }
    articleMapper.updateArticleFromArticleUpdateDto(updateArticleDto, article);
    article.setUpdatedAt(new Date());
    User updatedBy = userService.findUserById(Long.parseLong(authentication.getName()));
    article.setUpdatedBy(updatedBy);
    Article updatedArticle = articleRepository.update(article);
    Version newVersion = new Version(article.getText(), updatedBy, article);
    versionRepository.save(newVersion);
    return articleMapper.articleToArticleDto(updatedArticle);
  }

  @Override
  public ArticleEditDto approveArticle(Authentication authentication, Long id) {
    Article article = findArticleById(id);
    if (!IN_REVIEW.equals(article.getArticleStatus())) {
      throw new ApiException(INVALID_PARAMS, "Article must be first reviewed");
    }
    if (!authentication.getRoles().contains("EDITOR")) {
      throw new ApiException(FORBIDDEN, "Article can be approved only by editor");
    }
    long loggedUserId = Long.parseLong(authentication.getName());
    article.setUpdatedAt(new Date());
    article.setUpdatedBy(new User(loggedUserId));
    article.setArticleStatus(APPROVED);
    Article updatedArticle = articleRepository.update(article);
    return articleMapper.articleToArticleDto(updatedArticle);
  }

  @Override
  public ArticleEditDto archiveArticle(Authentication authentication, Long id) {
    Article article = findArticleById(id);
    // TODO append condition for denied and published articles
    if (!IN_REVIEW.equals(article.getArticleStatus()) && !APPROVED.equals(
        article.getArticleStatus())) {
      throw new ApiException(INVALID_PARAMS, "Article must be first reviewed or approved");
    }
    article.setArticleStatus(ArticleStatus.ARCHIVED);
    Article updatedArticle = articleRepository.update(article);
    return articleMapper.articleToArticleDto(updatedArticle);
  }

  @Override
  public ArticleEditDto sendArticleToReview(Authentication authentication, Long id) {
    Article article = findArticleById(id);
    if (!ArticleStatus.WRITING.equals(article.getArticleStatus())) {
      throw new ApiException(INVALID_PARAMS, "Article must be in the writing process");
    }
    article.setArticleStatus(IN_REVIEW);
    article.setReviewNumber(article.getReviewNumber() + 1);
    Article updatedArticle = articleRepository.update(article);
    return articleMapper.articleToArticleDto(updatedArticle);
  }

  @Override
  public ArticleEditDto sendArticleReview(Authentication authentication, Long id) {
    Article article = findArticleById(id);
    if (!IN_REVIEW.equals(article.getArticleStatus())) {
      throw new ApiException(INVALID_PARAMS, "Article must be in the review");
    }
    article.setArticleStatus(ArticleStatus.WRITING);
    Article updatedArticle = articleRepository.update(article);
    return articleMapper.articleToArticleDto(updatedArticle);
  }

  @Override
  public ArticleEditDto publishArticle(Authentication authentication, Long id) {
    Article article = findArticleById(id);
    if (!APPROVED.equals(article.getArticleStatus())) {
      throw new ApiException(INVALID_PARAMS, "Article must be approved");
    }
    article.setArticleStatus(ARCHIVED);
    // TODO implement article publishing business logic
    Article updatedArticle = articleRepository.update(article);
    return articleMapper.articleToArticleDto(updatedArticle);
  }

  @Override
  public ArticleEditDto denyArticle(Authentication authentication, Long id) {
    Article article = findArticleById(id);
    if (!IN_REVIEW.equals(article.getArticleStatus())) {
      throw new ApiException(INVALID_PARAMS, "Article must be after review");
    }
    article.setArticleStatus(ARCHIVED);
    // TODO implement article denying business logic
    Article updatedArticle = articleRepository.update(article);
    return articleMapper.articleToArticleDto(updatedArticle);
  }

  @Override
  public void removeArticle(Authentication authentication, Long id) {
    Article article = findArticleById(id);
    if (!ArticleStatus.WRITING.equals(article.getArticleStatus())
        || article.getReviewNumber() > 0) {
      throw new ApiException(INVALID_PARAMS,
          "Article must be in writing state and cannot be after any review");
    }
    try {
      articleRepository.delete(article);
    } catch (Exception e) {
      System.out.println("Error while saving article " + article.getId() + " " + e.getMessage());
    }
  }

  @Override
  public Article findArticleById(Long id) {
    return articleRepository.findById(id).orElseThrow(
        () -> new ApiException(RECORD_NOT_FOUND, "Article was not found"));
  }
}
