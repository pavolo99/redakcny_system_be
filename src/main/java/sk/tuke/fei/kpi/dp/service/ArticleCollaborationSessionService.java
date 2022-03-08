package sk.tuke.fei.kpi.dp.service;

import io.micronaut.security.authentication.Authentication;
import java.util.List;
import sk.tuke.fei.kpi.dp.dto.ArticleConnectedCollaboratorDto;
import sk.tuke.fei.kpi.dp.dto.ArticleEditDto;
import sk.tuke.fei.kpi.dp.dto.create.PreliminaryArticleTextDto;

public interface ArticleCollaborationSessionService {

  ArticleEditDto getArticleAndCreateSession(Authentication authentication, Long articleId);

  void disconnectFromArticleSession(Authentication authentication, Long articleId);

  ArticleEditDto savePreliminaryArticleText(Authentication authentication, Long articleId, PreliminaryArticleTextDto preliminaryArticleTextDto);

  void leaveArticleEditingToOtherCollaborator(Authentication authentication, Long articleId, Long userId);

  List<ArticleConnectedCollaboratorDto> getAllConnectedCollaboratorsToArticle(Authentication authentication, Long articleId);
}
