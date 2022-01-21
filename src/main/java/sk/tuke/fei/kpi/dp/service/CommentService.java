package sk.tuke.fei.kpi.dp.service;

import io.micronaut.security.authentication.Authentication;
import java.util.List;
import sk.tuke.fei.kpi.dp.dto.CommentCreateDto;
import sk.tuke.fei.kpi.dp.dto.CommentDto;
import sk.tuke.fei.kpi.dp.model.entity.Comment;

public interface CommentService {

  void createComment(Authentication authentication, CommentCreateDto commentCreateUpdateDto, Long articleId);

  void resolveComment(Authentication authentication, Long commentId);

  void deleteComment(Authentication authentication, Long commentId);

  List<CommentDto> getComments(Authentication authentication, Long articleId, boolean allComments);

  Comment findByCommentId(Long id);
}
