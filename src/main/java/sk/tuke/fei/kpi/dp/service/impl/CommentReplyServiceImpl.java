package sk.tuke.fei.kpi.dp.service.impl;

import io.micronaut.security.authentication.Authentication;
import java.util.Date;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private final Logger logger = LoggerFactory.getLogger(CommentReplyServiceImpl.class);

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
    logger.info("About to create comment reply for comment " + commentId);
    Comment comment = commentService.findByCommentId(commentId);
    User loggedUser = userService.findUserById(Long.parseLong(authentication.getName()));
    CommentReply commentReply = new CommentReply(commentReplyCreateDto.getText(), new Date(), loggedUser, comment);
    commentReplyRepository.save(commentReply);
  }

  @Override
  public void deleteCommentReply(Authentication authentication, Long commentReplyId) {
    logger.info("About to delete comment reply " + commentReplyId);
    CommentReply commentReply = findByCommentReplyId(commentReplyId);
    if (!commentReply.getCreatedBy().getId()
        .equals(userService.findUserById(Long.parseLong(authentication.getName())).getId())) {
      logger.error("Comment reply " + commentReplyId + " cannot be deleted by other then creator himself");
      throw new ApiException(FaultType.FORBIDDEN, "Comment reply cannot be deleted by other then creator himself");
    }

    try {
      commentReplyRepository.delete(commentReply);
    } catch (Exception e) {
      logger.error("Comment reply " + commentReplyId + " cannot be removed");
      throw new ApiException(FaultType.INVALID_PARAMS, "Comment reply cannot be removed");
    }
  }

  private CommentReply findByCommentReplyId(Long commentReplyId) {
    return commentReplyRepository.findById(commentReplyId)
        .orElseThrow(() -> {
          logger.error("Comment reply " + commentReplyId + " was not found");
          return new ApiException(FaultType.RECORD_NOT_FOUND, "Comment reply was not found");
        });
  }
}
