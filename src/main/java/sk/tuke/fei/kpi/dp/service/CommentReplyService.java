package sk.tuke.fei.kpi.dp.service;

import io.micronaut.security.authentication.Authentication;
import sk.tuke.fei.kpi.dp.dto.CommentReplyCreateDto;

public interface CommentReplyService {

  void createCommentReply(Authentication authentication, CommentReplyCreateDto commentReplyCreateDto, Long commentId);

  void deleteCommentReply(Authentication authentication, Long commentReplyId);

}
