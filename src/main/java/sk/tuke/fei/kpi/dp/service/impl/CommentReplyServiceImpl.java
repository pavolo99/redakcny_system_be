package sk.tuke.fei.kpi.dp.service.impl;

import io.micronaut.security.authentication.Authentication;
import java.util.Date;
import javax.inject.Singleton;
import sk.tuke.fei.kpi.dp.dto.create.CommentReplyCreateDto;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.exception.FaultType;
import sk.tuke.fei.kpi.dp.model.entity.Comment;
import sk.tuke.fei.kpi.dp.model.entity.CommentReply;
import sk.tuke.fei.kpi.dp.model.entity.User;
import sk.tuke.fei.kpi.dp.model.repository.CommentReplyRepository;
import sk.tuke.fei.kpi.dp.service.CommentReplyService;
import sk.tuke.fei.kpi.dp.service.CommentService;
import sk.tuke.fei.kpi.dp.service.UserService;

@Singleton
public class CommentReplyServiceImpl implements CommentReplyService {

  private final UserService userService;
  private final CommentReplyRepository commentReplyRepository;
  private final CommentService commentService;

  public CommentReplyServiceImpl(UserService userService,
      CommentReplyRepository commentReplyRepository,
      CommentService commentService) {
    this.userService = userService;
    this.commentReplyRepository = commentReplyRepository;
    this.commentService = commentService;
  }


  @Override
  public void createCommentReply(Authentication authentication,
      CommentReplyCreateDto commentReplyCreateDto, Long commentId) {
    Comment comment = commentService.findByCommentId(commentId);
    User loggedUser = userService.findUserById(Long.parseLong(authentication.getName()));
    CommentReply commentReply = new CommentReply(commentReplyCreateDto.getText(), new Date(), loggedUser, comment);
    commentReplyRepository.save(commentReply);
  }

  @Override
  public void deleteCommentReply(Authentication authentication, Long commentReplyId) {
    CommentReply commentReply = findByCommentReplyId(commentReplyId);
    if (!commentReply.getCreatedBy().getId()
        .equals(userService.findUserById(Long.parseLong(authentication.getName())).getId())) {
      throw new ApiException(FaultType.FORBIDDEN, "Comment reply cannot be deleted by other then creator himself");
    }

    try {
      commentReplyRepository.delete(commentReply);
    } catch (Exception e) {
      throw new ApiException(FaultType.INVALID_PARAMS, "Article cannot be removed");
    }
  }

  private CommentReply findByCommentReplyId(Long id) {
    return commentReplyRepository.findById(id)
        .orElseThrow(() -> new ApiException(FaultType.RECORD_NOT_FOUND, "Comment reply was not found"));
  }
}
