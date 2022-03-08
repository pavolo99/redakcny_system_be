package sk.tuke.fei.kpi.dp.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import java.util.List;
import javax.validation.Valid;
import sk.tuke.fei.kpi.dp.dto.ArticleConnectedCollaboratorDto;
import sk.tuke.fei.kpi.dp.dto.ArticleEditDto;
import sk.tuke.fei.kpi.dp.dto.create.PreliminaryArticleTextDto;
import sk.tuke.fei.kpi.dp.service.ArticleCollaborationSessionService;

@Controller("collab-session")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class ArticleCollaborationSessionController {

  private final ArticleCollaborationSessionService sessionService;


  public ArticleCollaborationSessionController(
      ArticleCollaborationSessionService sessionService) {
    this.sessionService = sessionService;
  }

  @Get(uri = "/{articleId}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<ArticleEditDto> getArticleAndCreateSession(Authentication authentication, @PathVariable Long articleId) {
    return HttpResponse.ok(sessionService.getArticleAndCreateSession(authentication, articleId));
  }


  @Delete(uri = "/{articleId}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<Void> disconnectFromArticleSession(Authentication authentication, @PathVariable Long articleId) {
    sessionService.disconnectFromArticleSession(authentication, articleId);
    return HttpResponse.ok();
  }

  @Post(uri = "/{articleId}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<ArticleEditDto> savePreliminaryArticleText(Authentication authentication, @PathVariable Long articleId,
      @Valid PreliminaryArticleTextDto preliminaryArticleTextDto) {
    return HttpResponse.ok(sessionService.savePreliminaryArticleText(authentication, articleId, preliminaryArticleTextDto));
  }

  @Put(uri = "/{articleId}/leave/{userId}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<Void> leaveArticleEditingToOtherCollaborator(Authentication authentication, @PathVariable Long articleId,
      @PathVariable Long userId) {
    sessionService.leaveArticleEditingToOtherCollaborator(authentication, articleId, userId);
    return HttpResponse.ok();
  }

  @Get(uri = "/{articleId}/connected", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<List<ArticleConnectedCollaboratorDto>> getAllConnectedCollaboratorsToArticle(
      Authentication authentication, @PathVariable Long articleId) {
    return HttpResponse.ok(sessionService.getAllConnectedCollaboratorsToArticle(authentication, articleId));
  }
}
