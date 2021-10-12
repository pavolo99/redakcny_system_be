package sk.tuke.fei.kpi.dp.service;

import java.util.List;
import sk.tuke.fei.kpi.dp.common.ArticleType;
import sk.tuke.fei.kpi.dp.dto.ArticleDto;
import sk.tuke.fei.kpi.dp.dto.CreateArticleDto;
import sk.tuke.fei.kpi.dp.dto.UpdateArticleDto;

public interface ArticleService {

  ArticleDto getArticle(Long id);

  List<ArticleDto> getAllArticles(ArticleType articleType);

  ArticleDto createArticle(CreateArticleDto createArticleDto);

  ArticleDto updateArticle(Long id, UpdateArticleDto updateArticleDto);

  ArticleDto approveArticle(Long id);
}
