package sk.tuke.fei.kpi.dp.controller;

import io.micronaut.http.HttpResponse;
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
import sk.tuke.fei.kpi.dp.dto.ArticleCollaboratorDto;
import sk.tuke.fei.kpi.dp.dto.UpdateArticleCollaboratorDto;
import sk.tuke.fei.kpi.dp.service.ArticleCollaboratorService;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("collaborator")
public class ArticleCollaboratorController {

  private final ArticleCollaboratorService collaboratorService;

  public ArticleCollaboratorController(ArticleCollaboratorService collaboratorService) {
    this.collaboratorService = collaboratorService;
  }

  @Get(uri = "/{articleId}")
  public HttpResponse<List<ArticleCollaboratorDto>> getArticleCollaborators(Authentication authentication,
      @PathVariable Long articleId) {
    return HttpResponse.ok(collaboratorService.getArticleCollaborators(authentication, articleId));
  }

  @Post(uri = "/added/{articleId}/{userId}")
  public HttpResponse<Void> addArticleCollaborator(Authentication authentication,
      @PathVariable Long articleId, @PathVariable Long userId) {
    collaboratorService.addArticleCollaborator(authentication, articleId, userId);
    return HttpResponse.ok();
  }

  @Put(uri = "/updated/{collaboratorId}")
  public HttpResponse<Void> updateArticleCollaborator(Authentication authentication,
      @PathVariable Long collaboratorId, @Valid UpdateArticleCollaboratorDto updateArticleCollaboratorDto) {
    collaboratorService.updateArticleCollaborator(authentication, collaboratorId, updateArticleCollaboratorDto);
    return HttpResponse.ok();
  }

  @Delete(uri = "/deleted/{collaboratorId}")
  public HttpResponse<Void> deleteArticleCollaborator(Authentication authentication, @PathVariable Long collaboratorId) {
    collaboratorService.deleteArticleCollaborator(authentication, collaboratorId);
    return HttpResponse.ok();
  }
}
