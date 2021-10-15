package sk.tuke.fei.kpi.dp.service.impl;

import static sk.tuke.fei.kpi.dp.exception.FaultType.INVALID_PARAMS;
import static sk.tuke.fei.kpi.dp.exception.FaultType.RECORD_NOT_FOUND;
import static sk.tuke.fei.kpi.dp.model.entity.ArticleStatus.APPROVED;
import static sk.tuke.fei.kpi.dp.model.entity.ArticleStatus.ARCHIVED;
import static sk.tuke.fei.kpi.dp.model.entity.ArticleStatus.IN_REVIEW;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import sk.tuke.fei.kpi.dp.common.ArticleType;
import sk.tuke.fei.kpi.dp.dto.ArticleDto;
import sk.tuke.fei.kpi.dp.dto.CreateArticleDto;
import sk.tuke.fei.kpi.dp.dto.UpdateArticleDto;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.mapper.ArticleMapper;
import sk.tuke.fei.kpi.dp.model.entity.Article;
import sk.tuke.fei.kpi.dp.model.entity.ArticleStatus;
import sk.tuke.fei.kpi.dp.model.repository.ArticleRepository;
import sk.tuke.fei.kpi.dp.service.ArticleService;

@Singleton
public class ArticleServiceImpl implements ArticleService {

  private final ArticleRepository articleRepository;
  private final ArticleMapper articleMapper;

  public ArticleServiceImpl(ArticleRepository articleRepository, ArticleMapper articleMapper) {
    this.articleRepository = articleRepository;
    this.articleMapper = articleMapper;
  }

  @Override
  public ArticleDto getArticle(Long id) {
    Optional<Article> optionalArticle = articleRepository.findById(id);
    return optionalArticle.map(articleMapper::articleToArticleDto).orElse(null);
  }

  @Override
  public List<ArticleDto> getAllArticles(ArticleType articleType) {
    List<Article> articles;
    if (ArticleType.APPROVED.equals(articleType)) {
      articles = articleRepository.getArticlesByStatus(List.of(APPROVED));
    } else if (ArticleType.ARCHIVED.equals(articleType)) {
      articles = articleRepository.getArticlesByStatus(List.of(ARCHIVED));
    } else {
      // TODO implement when auth will be ready
      articles = articleRepository.getAllArticles();
    }
    return articles.stream().map(articleMapper::articleToArticleDto).collect(Collectors.toList());
  }

  @Override
  public ArticleDto createArticle(CreateArticleDto createArticleDto) {
    Article article = articleMapper.createArticleDtoToArticle(createArticleDto);
    article.setArticleStatus(ArticleStatus.WRITING);
    article.setReviewNumber(0);
    Article savedArticle = articleRepository.save(article);
    return articleMapper.articleToArticleDto(savedArticle);
  }

  @Override
  public ArticleDto updateArticle(Long id, UpdateArticleDto updateArticleDto) {
    if (!id.equals(updateArticleDto.getId())) {
      throw new ApiException(INVALID_PARAMS, "Id is not equal with update article dto id");
    }
    Article article = findArticleById(id);
    articleMapper.updateArticleFromArticleUpdateDto(updateArticleDto, article);
    Article updatedArticle = articleRepository.update(article);
    return articleMapper.articleToArticleDto(updatedArticle);
  }

  @Override
  public ArticleDto approveArticle(Long id) {
    Article article = findArticleById(id);
    if (!IN_REVIEW.equals(article.getArticleStatus())) {
      throw new ApiException(INVALID_PARAMS, "Article must be first reviewed");
    }
    article.setArticleStatus(APPROVED);
    Article updatedArticle = articleRepository.update(article);
    return articleMapper.articleToArticleDto(updatedArticle);
  }

  @Override
  public ArticleDto archiveArticle(Long id) {
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
  public ArticleDto sendArticleToReview(Long id) {
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
  public ArticleDto sendArticleReview(Long id) {
    Article article = findArticleById(id);
    if (!IN_REVIEW.equals(article.getArticleStatus())) {
      throw new ApiException(INVALID_PARAMS, "Article must be in the review");
    }
    article.setArticleStatus(ArticleStatus.WRITING);
    Article updatedArticle = articleRepository.update(article);
    return articleMapper.articleToArticleDto(updatedArticle);
  }

  @Override
  public ArticleDto publishArticle(Long id) {
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
  public ArticleDto denyArticle(Long id) {
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
  public void removeArticle(Long id) {
    Article article = findArticleById(id);
    if (!ArticleStatus.WRITING.equals(article.getArticleStatus())
        || article.getReviewNumber() > 0) {
      throw new ApiException(INVALID_PARAMS,
          "Article must be in writing state and cannot be after any review");
    }
    articleRepository.delete(article);
  }

  private Article findArticleById(Long id) {
    return articleRepository.findById(id).orElseThrow(
        () -> new ApiException(RECORD_NOT_FOUND, "Article was not found"));
  }
}
