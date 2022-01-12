package sk.tuke.fei.kpi.dp.service;

import io.micronaut.security.authentication.Authentication;
import java.util.List;
import sk.tuke.fei.kpi.dp.dto.ArticleCollaboratorDto;
import sk.tuke.fei.kpi.dp.dto.UpdateArticleCollaboratorDto;

public interface ArticleCollaboratorService {

  List<ArticleCollaboratorDto> getArticleCollaborators(Authentication authentication, Long articleId);

  void addArticleCollaborator(Authentication authentication, Long articleId, Long userId);

  void updateArticleCollaborator(Authentication authentication, Long collaboratorId, UpdateArticleCollaboratorDto updateArticleCollaboratorDto);

  void deleteArticleCollaborator(Authentication authentication, Long collaboratorId);
}
