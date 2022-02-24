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
import sk.tuke.fei.kpi.dp.dto.CommentDto;
import sk.tuke.fei.kpi.dp.dto.create.CommentCreateDto;
import sk.tuke.fei.kpi.dp.service.CommentService;

//@Controller("comment")
@Controller("api/comment")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class CommentController {

  private final CommentService commentService;

  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @Post(uri = "/{articleId}")
  public HttpResponse<Void> createComment(Authentication authentication,
      @Valid CommentCreateDto commentCreateUpdateDto, @PathVariable Long articleId) {
    commentService.createComment(authentication, commentCreateUpdateDto, articleId);
    return HttpResponse.ok();
  }

  @Put(uri = "/resolved/{commentId}/toggle")
  public HttpResponse<Void> toggleCommentResolved(Authentication authentication, @PathVariable Long commentId) {
    commentService.toggleCommentResolved(authentication, commentId);
    return HttpResponse.ok();
  }

  @Delete(uri = "/{commentId}")
  public HttpResponse<Void> deleteComment(Authentication authentication, @PathVariable Long commentId) {
    commentService.deleteComment(authentication, commentId);
    return HttpResponse.ok();
  }

  @Get(uri = "/{articleId}/{allComments}")
  public HttpResponse<List<CommentDto>> getComments(Authentication authentication,
      @PathVariable Long articleId, @PathVariable boolean allComments) {
    return HttpResponse.ok(commentService.getComments(authentication, articleId, allComments));
  }

}
