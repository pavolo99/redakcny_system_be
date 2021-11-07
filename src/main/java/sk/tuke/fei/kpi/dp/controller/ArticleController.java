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
import io.micronaut.security.rules.SecurityRule;
import java.util.List;
import javax.validation.Valid;
import sk.tuke.fei.kpi.dp.common.QueryArticleStatus;
import sk.tuke.fei.kpi.dp.common.QueryArticleType;
import sk.tuke.fei.kpi.dp.dto.ArticleEditDto;
import sk.tuke.fei.kpi.dp.dto.ArticleViewDto;
import sk.tuke.fei.kpi.dp.dto.UpdateArticleDto;
import sk.tuke.fei.kpi.dp.service.ArticleService;

@Controller("article")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class ArticleController {

  private final ArticleService articleService;

  public ArticleController(ArticleService articleService) {
    this.articleService = articleService;
  }

  @Get(uri = "/{articleId}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<ArticleEditDto> getArticle(@PathVariable Long articleId) {
    return HttpResponse.ok(articleService.getArticle(articleId));
  }

  @Get(uri = "/list", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<List<ArticleViewDto>> getAllArticles(@QueryValue QueryArticleType queryArticleType,
      @QueryValue QueryArticleStatus queryArticleStatus) {
    return HttpResponse.ok(articleService.getAllArticles(queryArticleType, queryArticleStatus));
  }

  @Post(produces = MediaType.APPLICATION_JSON)
  public HttpResponse<ArticleEditDto> createArticle() {
    return HttpResponse.created(articleService.createArticle());
  }

  @Put(uri = "/{id}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<ArticleEditDto> updateArticle(@PathVariable Long id,
      @Valid UpdateArticleDto updateArticleDto) {
    return HttpResponse.ok(articleService.updateArticle(id, updateArticleDto));
  }

  @Put(uri = "/approved/{id}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<ArticleEditDto> approveArticle(@PathVariable Long id) {
    return HttpResponse.ok(articleService.approveArticle(id));
  }

  @Put(uri = "/archived/{id}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<ArticleEditDto> archiveArticle(@PathVariable Long id) {
    return HttpResponse.ok(articleService.archiveArticle(id));
  }

  @Put(uri = "/sent-to-review/{id}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<ArticleEditDto> sendArticleToReview(@PathVariable Long id) {
    return HttpResponse.ok(articleService.sendArticleToReview(id));
  }

  @Put(uri = "/sent-review/{id}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<ArticleEditDto> sendArticleReview(@PathVariable Long id) {
    return HttpResponse.ok(articleService.sendArticleReview(id));
  }

  @Put(uri = "/published/{id}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<ArticleEditDto> publishArticle(@PathVariable Long id) {
    return HttpResponse.ok(articleService.publishArticle(id));
  }

  @Put(uri = "/denied/{id}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<ArticleEditDto> denyArticle(@PathVariable Long id) {
    return HttpResponse.ok(articleService.denyArticle(id));
  }

  @Delete(uri = "/deleted/{id}")
  public HttpResponse<Void> removeArticle(@PathVariable Long id) {
    articleService.removeArticle(id);
    return HttpResponse.ok();
  }
}
