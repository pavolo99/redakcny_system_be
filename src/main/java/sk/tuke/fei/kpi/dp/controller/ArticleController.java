package sk.tuke.fei.kpi.dp.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import javax.validation.Valid;
import sk.tuke.fei.kpi.dp.dto.ArticleDto;
import sk.tuke.fei.kpi.dp.dto.CreateArticleDto;
import sk.tuke.fei.kpi.dp.dto.UpdateArticleDto;
import sk.tuke.fei.kpi.dp.service.ArticleService;

@Controller("/")
public class ArticleController {

  private final ArticleService articleService;

  public ArticleController(ArticleService articleService) {
    this.articleService = articleService;
  }

  /**
   * @param id The article's id
   * @return The article
   */
  @Get(uri = "/article/{id}", produces = MediaType.APPLICATION_JSON)
  @Operation(summary = "Gets an article",
      description = "Retrieving an article by its id"
  )
  @ApiResponse(
      content = @Content(mediaType = MediaType.APPLICATION_JSON),
      description = "Ok"
  )
  @ApiResponse(responseCode = "404", description = "An article was not found")
  public HttpResponse<ArticleDto> getArticle(
      @Parameter(description = "The id of the article") @PathVariable Long id) {
    ArticleDto articleDto = articleService.getArticle(id);
    if (articleDto != null) {
      return HttpResponse.created(articleDto);
    }
    return HttpResponse.notFound();
  }

  /**
   * @return The list of all articles
   */
  @Get(uri = "/article", produces = MediaType.APPLICATION_JSON)
  @Operation(summary = "Gets all articles",
      description = "Retrieving list of all articles"
  )
  @ApiResponse(
      content = @Content(mediaType = MediaType.APPLICATION_JSON),
      description = "Ok"
  )
  public HttpResponse<List<ArticleDto>> getAllArticles() {
    return HttpResponse.created(articleService.getAllArticles());
  }

  /**
   * @param createArticleDto The article request body
   * @return The created article
   */
  @Post(uri = "/article", produces = MediaType.APPLICATION_JSON)
  @Operation(summary = "Create an article",
      description = "Creates a new article"
  )
  @ApiResponse(
      content = @Content(mediaType = MediaType.APPLICATION_JSON),
      description = "Article created"
  )
  public HttpResponse<ArticleDto> createArticle(@Parameter(description = "Create article dto")
  @Valid CreateArticleDto createArticleDto) {
    return HttpResponse.created(articleService.createArticle(createArticleDto));
  }

  /**
   * @param id               The updating article id
   * @param updateArticleDto The article request body
   * @return The created article
   */
  @Put(uri = "/article/{id}", produces = MediaType.APPLICATION_JSON)
  @Operation(summary = "Updates an article",
      description = "Updates an existing article"
  )
  @ApiResponse(
      content = @Content(mediaType = MediaType.APPLICATION_JSON),
      description = "Article created"
  )
  public HttpResponse<ArticleDto> updateArticle(
      @Parameter(description = "id of the article") @PathVariable Long id,
      @Parameter(description = "Update article dto") @Valid UpdateArticleDto updateArticleDto) {
    return HttpResponse.created(articleService.updateArticle(id, updateArticleDto));
  }
}
