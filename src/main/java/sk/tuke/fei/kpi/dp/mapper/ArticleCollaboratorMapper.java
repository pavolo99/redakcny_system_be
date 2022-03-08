package sk.tuke.fei.kpi.dp.mapper;

import javax.inject.Singleton;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import sk.tuke.fei.kpi.dp.dto.ArticleCollaboratorDto;
import sk.tuke.fei.kpi.dp.dto.ArticleConnectedCollaboratorDto;
import sk.tuke.fei.kpi.dp.dto.update.UpdateArticleCollaboratorDto;
import sk.tuke.fei.kpi.dp.model.entity.ArticleCollaborationSession;
import sk.tuke.fei.kpi.dp.model.entity.ArticleCollaborator;

@Mapper(componentModel = "jsr330", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Singleton
public interface ArticleCollaboratorMapper {

  @Mapping(source = "user", target = "userDto")
  ArticleCollaboratorDto articleCollaboratorToArticleCollaboratorDto(ArticleCollaborator articleCollaborator);

  @Mapping(source = "user", target = "userDto")
  ArticleConnectedCollaboratorDto articleCollaboratorToArticleConnectedCollaboratorDto(ArticleCollaborationSession articleCollaborator);

  void updateCollaboratorFromUpdateCollaboratorDto(UpdateArticleCollaboratorDto updateArticleCollaboratorDto, @MappingTarget ArticleCollaborator articleCollaborator);
}
