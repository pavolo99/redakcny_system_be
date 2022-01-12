package sk.tuke.fei.kpi.dp.mapper;

import javax.inject.Singleton;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import sk.tuke.fei.kpi.dp.dto.ArticleCollaboratorDto;
import sk.tuke.fei.kpi.dp.dto.UpdateArticleCollaboratorDto;
import sk.tuke.fei.kpi.dp.model.entity.ArticleCollaborator;

@Mapper(componentModel = "jsr330")
@Singleton
public interface ArticleCollaboratorMapper {

  @Mapping(source = "user", target = "userDto")
  ArticleCollaboratorDto articleCollaboratorToArticleCollaboratorDto(ArticleCollaborator articleCollaborator);

  void updateCollaboratorFromUpdateCollaboratorDto(UpdateArticleCollaboratorDto updateArticleCollaboratorDto, @MappingTarget ArticleCollaborator articleCollaborator);
}
