package sk.tuke.fei.kpi.dp.mapper;

import javax.inject.Singleton;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import sk.tuke.fei.kpi.dp.dto.ArchivedArticleDto;
import sk.tuke.fei.kpi.dp.dto.ArticleEditDto;
import sk.tuke.fei.kpi.dp.dto.update.UpdateArticleDto;
import sk.tuke.fei.kpi.dp.dto.view.ArticleViewDto;
import sk.tuke.fei.kpi.dp.model.entity.Article;

@Mapper(componentModel = "jsr330", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Singleton
public interface ArticleMapper {

  ArticleViewDto articleToArticleViewDto(Article article);

  ArticleEditDto articleToArticleDto(Article article);

  void updateArticleFromArticleUpdateDto(UpdateArticleDto updateArticleDto, @MappingTarget Article article);

  ArchivedArticleDto articleToArchivedArticleDto(Article article);
}
