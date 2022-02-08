package sk.tuke.fei.kpi.dp.service.impl;

import io.micronaut.security.authentication.Authentication;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import sk.tuke.fei.kpi.dp.dto.ArticleCollaboratorDto;
import sk.tuke.fei.kpi.dp.dto.update.UpdateArticleCollaboratorDto;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.exception.FaultType;
import sk.tuke.fei.kpi.dp.mapper.ArticleCollaboratorMapper;
import sk.tuke.fei.kpi.dp.model.entity.Article;
import sk.tuke.fei.kpi.dp.model.entity.ArticleCollaborator;
import sk.tuke.fei.kpi.dp.model.entity.User;
import sk.tuke.fei.kpi.dp.model.repository.ArticleCollaboratorRepository;
import sk.tuke.fei.kpi.dp.service.ArticleCollaboratorService;
import sk.tuke.fei.kpi.dp.service.ArticleService;
import sk.tuke.fei.kpi.dp.service.UserService;

@Singleton
public class ArticleCollaboratorServiceImpl implements ArticleCollaboratorService {

  private final ArticleCollaboratorRepository collaboratorRepository;
  private final ArticleCollaboratorMapper collaboratorMapper;
  private final ArticleService articleService;
  private final UserService userService;

  public ArticleCollaboratorServiceImpl(
      ArticleCollaboratorRepository collaboratorRepository,
      ArticleCollaboratorMapper collaboratorMapper,
      ArticleService articleService, UserService userService) {
    this.collaboratorRepository = collaboratorRepository;
    this.collaboratorMapper = collaboratorMapper;
    this.articleService = articleService;
    this.userService = userService;
  }

  @Override
  public List<ArticleCollaboratorDto> getArticleCollaborators(Authentication authentication, Long articleId) {
    List<ArticleCollaborator> articleCollaborators = collaboratorRepository.getArticleCollaboratorsByArticle(articleId);
    return articleCollaborators
        .stream()
        .map(collaboratorMapper::articleCollaboratorToArticleCollaboratorDto)
        .collect(Collectors.toList());
  }

  @Override
  public void addArticleCollaborator(Authentication authentication, Long articleId, Long userId) {
    Article article = articleService.findArticleById(articleId);
    User collaborator = userService.findUserById(userId);
    ArticleCollaborator articleCollaborator = new ArticleCollaborator(false, false, false, article, collaborator);
    try {
      collaboratorRepository.save(articleCollaborator);
    } catch (Exception e) {
      throw new ApiException(FaultType.INVALID_PARAMS, "User is already collaborator for the article");
    }
  }

  @Override
  public void updateArticleCollaborator(Authentication authentication,
      Long collaboratorId, UpdateArticleCollaboratorDto updateArticleCollaboratorDto) {
    if (!collaboratorId.equals(updateArticleCollaboratorDto.getId())) {
      throw new ApiException(FaultType.INVALID_PARAMS, "Collaborator id is not the same as in dto");
    }
    ArticleCollaborator articleCollaborator = getCollaboratorById(collaboratorId);
    collaboratorMapper.updateCollaboratorFromUpdateCollaboratorDto(updateArticleCollaboratorDto, articleCollaborator);
    collaboratorRepository.update(articleCollaborator);
  }

  @Override
  public void deleteArticleCollaborator(Authentication authentication, Long collaboratorId) {
    ArticleCollaborator articleCollaborator = getCollaboratorById(collaboratorId);
    collaboratorRepository.delete(articleCollaborator);
  }

  private ArticleCollaborator getCollaboratorById(Long collaboratorId) {
    return collaboratorRepository.findById(collaboratorId)
        .orElseThrow(
            () -> new ApiException(FaultType.RECORD_NOT_FOUND, "Collaborator was not found"));
  }
}
