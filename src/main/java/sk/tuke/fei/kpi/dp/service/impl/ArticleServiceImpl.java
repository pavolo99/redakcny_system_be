package sk.tuke.fei.kpi.dp.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import sk.tuke.fei.kpi.dp.dto.ArticleDto;
import sk.tuke.fei.kpi.dp.dto.CreateArticleDto;
import sk.tuke.fei.kpi.dp.dto.UpdateArticleDto;
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
  public List<ArticleDto> getAllArticles() {
    return articleRepository.getAllArticles()
        .stream()
        .map(articleMapper::articleToArticleDto)
        .collect(Collectors.toList());
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
      // todo handle exceptions
    }
    Article article = articleMapper.updateArticleDtoToArticle(updateArticleDto);
    article.setArticleStatus(ArticleStatus.WRITING);
    article.setReviewNumber(0);
    Article updatedArticle = articleRepository.update(article);
    return articleMapper.articleToArticleDto(updatedArticle);
  }
}
