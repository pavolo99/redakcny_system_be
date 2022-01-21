package sk.tuke.fei.kpi.dp.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import javax.validation.Valid;
import sk.tuke.fei.kpi.dp.dto.CommentReplyCreateDto;
import sk.tuke.fei.kpi.dp.service.CommentReplyService;

@Controller("comment-reply")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class CommentReplyController {

  private final CommentReplyService commentReplyService;

  public CommentReplyController(CommentReplyService commentReplyService) {
    this.commentReplyService = commentReplyService;
  }

  @Post(uri = "/{commentId}")
  public HttpResponse<Void> createCommentReply(Authentication authentication,
      @Valid CommentReplyCreateDto commentReplyCreateDto, @PathVariable Long commentId) {
    commentReplyService.createCommentReply(authentication, commentReplyCreateDto, commentId);
    return HttpResponse.ok();
  }

  @Delete(uri = "/{commentReplyId}")
  public HttpResponse<Void> deleteCommentReply(Authentication authentication, @PathVariable Long commentReplyId) {
    commentReplyService.deleteCommentReply(authentication, commentReplyId);
    return HttpResponse.ok();
  }

}
