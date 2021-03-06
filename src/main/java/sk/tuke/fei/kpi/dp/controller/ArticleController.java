package sk.tuke.fei.kpi.dp.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import java.util.List;
import javax.validation.Valid;
import sk.tuke.fei.kpi.dp.common.QueryArticleStatus;
import sk.tuke.fei.kpi.dp.common.QueryArticleType;
import sk.tuke.fei.kpi.dp.dto.ArchivedArticleDto;
import sk.tuke.fei.kpi.dp.dto.ArticleEditDto;
import sk.tuke.fei.kpi.dp.dto.update.UpdateArticleDto;
import sk.tuke.fei.kpi.dp.dto.view.ArticleViewDto;
import sk.tuke.fei.kpi.dp.service.ArticleService;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("api/article")
public class ArticleController {

  private final ArticleService articleService;

  public ArticleController(ArticleService articleService) {
    this.articleService = articleService;
  }

  @Get(uri = "/list", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<List<ArticleViewDto>> getAllArticles(Authentication authentication,
      @QueryValue QueryArticleType queryArticleType,
      @QueryValue QueryArticleStatus queryArticleStatus) {
    return HttpResponse.ok(articleService.getAllArticles(authentication, queryArticleType, queryArticleStatus));
  }

  @Post(produces = MediaType.APPLICATION_JSON)
  public HttpResponse<Long> createArticle(Authentication authentication) {
    return HttpResponse.created(articleService.createArticle(authentication));
  }

  @Put(uri = "/{id}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<ArticleEditDto> updateArticle(Authentication authentication, @PathVariable Long id,
      @Valid UpdateArticleDto updateArticleDto) {
    return HttpResponse.ok(articleService.updateArticle(authentication, id, updateArticleDto, true));
  }

  @Put(uri = "/approved/{id}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<ArticleEditDto> approveArticle(Authentication authentication, @PathVariable Long id,
      @Valid UpdateArticleDto updateArticleDto) {
    return HttpResponse.ok(articleService.approveArticle(authentication, id, updateArticleDto));
  }

  @Put(uri = "/archived/{id}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<ArticleEditDto> archiveArticle(Authentication authentication, @PathVariable Long id) {
    return HttpResponse.ok(articleService.archiveArticle(authentication, id));
  }

  @Put(uri = "/sent-to-review/{id}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<ArticleEditDto> sendArticleToReview(Authentication authentication, @PathVariable Long id,
      @Valid UpdateArticleDto updateArticleDto) {
    return HttpResponse.ok(articleService.sendArticleToReview(authentication, id, updateArticleDto));
  }

  @Put(uri = "/sent-review/{id}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<ArticleEditDto> sendArticleReview(Authentication authentication, @PathVariable Long id,
      @Valid UpdateArticleDto updateArticleDto) {
    return HttpResponse.ok(articleService.sendArticleReview(authentication, id, updateArticleDto));
  }

  @Put(uri = "/published/{id}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<ArticleEditDto> publishArticle(Authentication authentication, @PathVariable Long id,
      @Valid UpdateArticleDto updateArticleDto) {
    return HttpResponse.ok(articleService.publishArticle(authentication, id, updateArticleDto));
  }

  @Put(uri = "/denied/{id}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<ArticleEditDto> denyArticle(Authentication authentication, @PathVariable Long id) {
    return HttpResponse.ok(articleService.denyArticle(authentication, id));
  }

  @Delete(uri = "/deleted/{id}")
  public HttpResponse<Void> removeArticle(Authentication authentication, @PathVariable Long id) {
    articleService.removeArticle(authentication, id);
    return HttpResponse.ok();
  }

  @Get(uri = "/archived/{id}")
  public HttpResponse<ArchivedArticleDto> getArchivedArticle(Authentication authentication, @PathVariable Long id) {
    return HttpResponse.ok(articleService.getArchivedArticle(authentication, id));
  }

  @Put(uri = "/restore/{id}")
  public HttpResponse<Void> restoreArticle(Authentication authentication, @PathVariable Long id) {
    articleService.restoreArticle(authentication, id);
    return HttpResponse.ok();
  }
}
