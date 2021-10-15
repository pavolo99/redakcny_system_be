package sk.tuke.fei.kpi.dp.mapper;

import javax.inject.Singleton;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import sk.tuke.fei.kpi.dp.dto.ArticleDto;
import sk.tuke.fei.kpi.dp.dto.CreateArticleDto;
import sk.tuke.fei.kpi.dp.dto.UpdateArticleDto;
import sk.tuke.fei.kpi.dp.model.entity.Article;

@Mapper(componentModel = "jsr330")
@Singleton
public interface ArticleMapper {

  ArticleDto articleToArticleDto(Article article);

  Article createArticleDtoToArticle(CreateArticleDto createArticleDto);

  void updateArticleFromArticleUpdateDto(UpdateArticleDto updateArticleDto, @MappingTarget Article article);
}
