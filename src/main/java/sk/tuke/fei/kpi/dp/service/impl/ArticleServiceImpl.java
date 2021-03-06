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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.tuke.fei.kpi.dp.common.QueryArticleStatus;
import sk.tuke.fei.kpi.dp.common.QueryArticleType;
import sk.tuke.fei.kpi.dp.dto.ArchivedArticleDto;
import sk.tuke.fei.kpi.dp.dto.ArticleEditDto;
import sk.tuke.fei.kpi.dp.dto.update.UpdateArticleDto;
import sk.tuke.fei.kpi.dp.dto.view.ArticleViewDto;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.mapper.ArticleMapper;
import sk.tuke.fei.kpi.dp.model.entity.Article;
import sk.tuke.fei.kpi.dp.model.entity.ArticleCollaborator;
import sk.tuke.fei.kpi.dp.model.entity.ArticleStatus;
import sk.tuke.fei.kpi.dp.model.entity.User;
import sk.tuke.fei.kpi.dp.model.entity.Version;
import sk.tuke.fei.kpi.dp.model.repository.ArticleCollaboratorRepository;
import sk.tuke.fei.kpi.dp.model.repository.ArticleRepository;
import sk.tuke.fei.kpi.dp.model.repository.VersionRepository;
import sk.tuke.fei.kpi.dp.service.ArticleService;
import sk.tuke.fei.kpi.dp.service.PublicationService;
import sk.tuke.fei.kpi.dp.service.UserService;

@Singleton
public class ArticleServiceImpl implements ArticleService {

  private final ArticleRepository articleRepository;
  private final ArticleMapper articleMapper;
  private final UserService userService;
  private final VersionRepository versionRepository;
  private final PublicationService publicationService;
  private final ArticleCollaboratorRepository collaboratorRepository;
  private final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

  public ArticleServiceImpl(ArticleRepository articleRepository, ArticleMapper articleMapper,
      UserService userService, VersionRepository versionRepository,
      PublicationService publicationService,
      ArticleCollaboratorRepository collaboratorRepository) {
    this.articleRepository = articleRepository;
    this.articleMapper = articleMapper;
    this.userService = userService;
    this.versionRepository = versionRepository;
    this.publicationService = publicationService;
    this.collaboratorRepository = collaboratorRepository;
  }

  @Override
  public List<ArticleViewDto> getAllArticles(Authentication authentication,
      QueryArticleType queryArticleType, QueryArticleStatus queryArticleStatus) {
    logger.info("About to get all articles");
    List<Article> articles = new ArrayList<>();
    long loggedUserId = Long.parseLong(authentication.getName());
    if (QueryArticleType.MINE.equals(queryArticleType)) {
      articles = articleRepository.getAllArticlesCreatedByLoggedUser(loggedUserId);
    } else if (QueryArticleType.APPROVED.equals(queryArticleType)) {
      articles = articleRepository.getArticlesByStatusForLoggedUser(List.of(APPROVED), loggedUserId);
    } else if (QueryArticleType.ARCHIVED.equals(queryArticleType)) {
      articles = articleRepository.getArticlesByStatusForLoggedUser(List.of(ARCHIVED), loggedUserId);
    } else if (QueryArticleType.SHARED_WITH_ME.equals(queryArticleType)) {
      articles = articleRepository.getSharedArticlesOfLoggedUser(loggedUserId);
    } else if (QueryArticleType.REVIEWED_BY_ME.equals(queryArticleType)) {
      if (!authentication.getRoles().contains("EDITOR")) {
        logger.error("Articles are determined only for editor");
        throw new ApiException(FORBIDDEN, "Articles are determined only for editor");
      }
      articles = articleRepository.getReviewedArticlesForEditor(List.of(IN_REVIEW), loggedUserId);
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
    logger.info("About to create article");
    User createdBy = userService.findUserById(Long.parseLong(authentication.getName()));
    Article article = new Article("N??zov ??l??nku", "Text ??l??nku", 0, WRITING, createdBy);
    ArticleCollaborator articleCollaborator = new ArticleCollaborator(true, true, true, article, createdBy);
    article.getArticleCollaborators().add(articleCollaborator);
    Version firstVersion = new Version(article.getText(), createdBy, article);
    Article savedArticle = articleRepository.save(article);
    versionRepository.save(firstVersion);
    return savedArticle.getId();
  }

  @Override
  @Transactional
  public ArticleEditDto updateArticle(Authentication authentication, Long articleId,
      UpdateArticleDto updateArticleDto, boolean createNewVersion) {
    logger.info("About to update article " + articleId);
    if (!articleId.equals(updateArticleDto.getId())) {
      logger.error("Article id " + articleId + " is not equal with update article dto");
      throw new ApiException(INVALID_PARAMS, "Id is not equal with update article dto articleId");
    }
    Article article = findArticleById(articleId);
    ArticleCollaborator loggedArticleCollaborator = collaboratorRepository
        .findByArticleAndLoggedUser(article.getId(), Long.parseLong(authentication.getName()));

    if (loggedArticleCollaborator == null || !loggedArticleCollaborator.getCanEdit()) {
      logger.error("Logged user collaborator is not allowed to update this article" + articleId);
      throw new ApiException(FORBIDDEN, "You are not allowed to update this article");
    }
    return performArticleUpdate(updateArticleDto, article, loggedArticleCollaborator.getUser(), createNewVersion);
  }

  @Override
  public ArticleEditDto approveArticle(Authentication authentication, Long articleId, UpdateArticleDto updateArticleDto) {
    logger.info("About to approve article " + articleId);
    Article article = findArticleById(articleId);
    if (!IN_REVIEW.equals(article.getArticleStatus())) {
      logger.error("Article " + articleId + " must be first reviewed");
      throw new ApiException(INVALID_PARAMS, "Article must be first reviewed");
    }
    if (!authentication.getRoles().contains("EDITOR")) {
      logger.error("Article " + articleId + " can be approved only by editor");
      throw new ApiException(FORBIDDEN, "Article can be approved only by editor");
    }
    article.setArticleStatus(APPROVED);
    User loggedUser = new User(Long.parseLong(authentication.getName()));
    return performArticleUpdate(updateArticleDto, article, loggedUser, true);
  }

  @Override
  public ArticleEditDto archiveArticle(Authentication authentication, Long articleId) {
    logger.info("About to archive article " + articleId);
    Article article = findArticleById(articleId);
    if (WRITING.equals(article.getArticleStatus()) || APPROVED.equals(article.getArticleStatus())) {
      logger.error("Article " + articleId + " must be first reviewed or approved");
      throw new ApiException(INVALID_PARAMS, "Article must be first reviewed or approved");
    }
    article.setUpdatedAt(new Date());
    article.setUpdatedBy(new User(Long.parseLong(authentication.getName())));
    article.setArticleStatus(ArticleStatus.ARCHIVED);
    Article updatedArticle = articleRepository.update(article);
    return articleMapper.articleToArticleDto(updatedArticle);
  }

  @Override
  public ArticleEditDto sendArticleToReview(Authentication authentication, Long articleId, UpdateArticleDto updateArticleDto) {
    logger.info("About to send article to review " + articleId);
    Article article = findArticleById(articleId);
    if (!ArticleStatus.WRITING.equals(article.getArticleStatus())) {
      logger.error("Article " + articleId + " must be in the writing process");
      throw new ApiException(INVALID_PARAMS, "Article must be in the writing process");
    }
    User loggedUser = new User(Long.parseLong(authentication.getName()));
    article.setArticleStatus(IN_REVIEW);
    article.setReviewNumber(article.getReviewNumber() + 1);
    return performArticleUpdate(updateArticleDto, article, loggedUser, true);
  }

  @Override
  public ArticleEditDto sendArticleReview(Authentication authentication, Long articleId, UpdateArticleDto updateArticleDto) {
    logger.info("About to send article review " + articleId);
    Article article = findArticleById(articleId);
    if (!IN_REVIEW.equals(article.getArticleStatus())) {
      logger.error("Article " + articleId + " must be in the review");
      throw new ApiException(INVALID_PARAMS, "Article must be in the review");
    }
    if (!authentication.getRoles().contains("EDITOR")) {
      logger.error("Review must be send by editor");
      throw new ApiException(INVALID_PARAMS, "Review must be send by editor");
    }
    User loggedUser = new User(Long.parseLong(authentication.getName()));
    article.setUpdatedBy(loggedUser);
    article.setArticleStatus(ArticleStatus.WRITING);
    return performArticleUpdate(updateArticleDto, article, loggedUser, true);
  }

  @Override
  @Transactional
  public ArticleEditDto publishArticle(Authentication authentication, Long articleId, UpdateArticleDto updateArticleDto) {
    logger.info("About to publish article " + articleId);
    Article article = findArticleById(articleId);
    if (!APPROVED.equals(article.getArticleStatus())) {
      logger.error("Article " + articleId + " must be approved");
      throw new ApiException(INVALID_PARAMS, "Article must be approved");
    }

    User loggedUser = new User(Long.parseLong(authentication.getName()));
    article.setArticleStatus(ARCHIVED);
    ArticleEditDto articleEditDto = performArticleUpdate(updateArticleDto, article, loggedUser, true);

    publicationService.publishArticleToProjectRepository(authentication, article);
    return articleEditDto;
  }

  @Override
  public ArticleEditDto denyArticle(Authentication authentication, Long articleId) {
    logger.info("About to deny article " + articleId);
    Article article = findArticleById(articleId);
    if (!IN_REVIEW.equals(article.getArticleStatus())) {
      logger.error("Article " + articleId + " must be after review");
      throw new ApiException(INVALID_PARAMS, "Article must be after review");
    }
    if (!authentication.getRoles().contains("EDITOR")) {
      logger.error("Article " + articleId + " can be denied only by the editor");
      throw new ApiException(INVALID_PARAMS, "Article can be denied only by the editor");
    }
    article.setUpdatedAt(new Date());
    article.setUpdatedBy(new User(Long.parseLong(authentication.getName())));
    article.setArticleStatus(ARCHIVED);
    Article updatedArticle = articleRepository.update(article);
    return articleMapper.articleToArticleDto(updatedArticle);
  }

  @Override
  public void removeArticle(Authentication authentication, Long articleId) {
    logger.info("About to remove article " + articleId);
    Article article = findArticleById(articleId);
    if (!ArticleStatus.WRITING.equals(article.getArticleStatus())
        || article.getReviewNumber() > 0) {
      logger.error("Article " + articleId + " cannot be deleted");
      throw new ApiException(INVALID_PARAMS, "Article cannot be deleted");
    }
    try {
      articleRepository.delete(article);
    } catch (Exception e) {
      logger.error("Article " + articleId + " cannot be deleted");
      throw new ApiException(INVALID_PARAMS, "Article cannot be deleted");
    }
  }

  @Override
  public Article findArticleById(Long articleId) {
    return articleRepository.findById(articleId)
        .orElseThrow(() -> {
          logger.error("Article " + articleId + " was not found");
          return new ApiException(RECORD_NOT_FOUND, "Article was not found");
        });
  }

  @Override
  public ArchivedArticleDto getArchivedArticle(Authentication authentication, Long articleId) {
    logger.info("About to get archive article " + articleId);
    Article archivedArticle = getArchivedArticle(articleId);
    return articleMapper.articleToArchivedArticleDto(archivedArticle);
  }

  @Override
  public void restoreArticle(Authentication authentication, Long articleId) {
    logger.info("About to restore article " + articleId);
    Article archivedArticle = getArchivedArticle(articleId);
    archivedArticle.setArticleStatus(WRITING);
    archivedArticle.setUpdatedAt(new Date());
    archivedArticle.setUpdatedBy(new User(Long.parseLong(authentication.getName())));
    articleRepository.update(archivedArticle);
  }

  private Article getArchivedArticle(Long articleId) {
    Article article = findArticleById(articleId);
    if (!article.getArticleStatus().equals(ARCHIVED)) {
      logger.error("Article " + articleId + " must be archived");
      throw new ApiException(INVALID_PARAMS, "Article must be archived");
    }
    return article;
  }
  private ArticleEditDto performArticleUpdate(UpdateArticleDto updateArticleDto, Article article,
      User loggedUser, boolean createNewVersion) {
    articleMapper.updateArticleFromArticleUpdateDto(updateArticleDto, article);
    article.setUpdatedAt(new Date());
    article.setUpdatedBy(loggedUser);
    if (createNewVersion) {
      Version newVersion = new Version(article.getText(), loggedUser, article);
      versionRepository.save(newVersion);
    }
    Article updatedArticle = articleRepository.update(article);
    return articleMapper.articleToArticleDto(updatedArticle);
  }
}
