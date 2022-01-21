package sk.tuke.fei.kpi.dp.service.impl;

import io.micronaut.security.authentication.Authentication;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import sk.tuke.fei.kpi.dp.dto.CommentCreateDto;
import sk.tuke.fei.kpi.dp.dto.CommentDto;
import sk.tuke.fei.kpi.dp.dto.CommentReplyDto;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.exception.FaultType;
import sk.tuke.fei.kpi.dp.mapper.CommentMapper;
import sk.tuke.fei.kpi.dp.model.entity.Article;
import sk.tuke.fei.kpi.dp.model.entity.Comment;
import sk.tuke.fei.kpi.dp.model.entity.User;
import sk.tuke.fei.kpi.dp.model.repository.CommentRepository;
import sk.tuke.fei.kpi.dp.service.ArticleService;
import sk.tuke.fei.kpi.dp.service.CommentService;
import sk.tuke.fei.kpi.dp.service.UserService;

@Singleton
public class CommentServiceImpl implements CommentService {

  private final ArticleService articleService;
  private final UserService userService;
  private final CommentRepository commentRepository;
  private final CommentMapper commentMapper;

  public CommentServiceImpl(ArticleService articleService,
      UserService userService, CommentRepository commentRepository,
      CommentMapper commentMapper) {
    this.articleService = articleService;
    this.userService = userService;
    this.commentRepository = commentRepository;
    this.commentMapper = commentMapper;
  }

  @Override
  public void createComment(Authentication authentication,
      CommentCreateDto commentCreateDto, Long articleId) {
    Article article = articleService.findArticleById(articleId);
    User loggedUser = userService.findUserById(Long.parseLong(authentication.getName()));
    Comment comment = commentMapper.commentCreateDtoToComment(commentCreateDto);
    comment.setArticle(article);
    comment.setCreatedBy(loggedUser);
    comment.setUpdatedAt(new Date());
    commentRepository.save(comment);
  }

  @Override
  public void resolveComment(Authentication authentication, Long commentId) {
    Comment comment = findByCommentId(commentId);
    if (comment.getResolved()) {
      throw new ApiException(FaultType.INVALID_PARAMS, "Comment is already resolved");
    }
    comment.setResolved(true);
    comment.setUpdatedAt(new Date());
    commentRepository.update(comment);
  }

  @Override
  public void deleteComment(Authentication authentication, Long commentId) {
    Comment comment = findByCommentId(commentId);
    if (!comment.getCreatedBy().getId()
        .equals(userService.findUserById(Long.parseLong(authentication.getName())).getId())) {
      throw new ApiException(FaultType.FORBIDDEN, "Comment cannot be deleted by other then creator himself");
    }

    try {
      commentRepository.delete(comment);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  @Transactional
  public List<CommentDto> getComments(Authentication authentication, Long articleId,
      boolean allComments) {

    List<Comment> comments = allComments ? commentRepository.getAllCommentsByArticleId(articleId)
        : commentRepository.getUnResolvedCommentsByArticleId(articleId);
    return mapCommentsToCommentsDtoList(comments);
  }

  @Override
  public Comment findByCommentId(Long id) {
    return commentRepository.findById(id)
        .orElseThrow(() -> new ApiException(FaultType.RECORD_NOT_FOUND, "Comment was not found"));
  }

  private List<CommentDto> mapCommentsToCommentsDtoList(List<Comment> comments) {
    return comments
        .stream()
        .map(comment -> {
          List<CommentReplyDto> commentRepliesDto = commentMapper
              .commentRepliesToCommentRepliesDtoList(comment.getCommentReplies());
          CommentDto commentDto = commentMapper.commentToCommentDto(comment);
          commentDto.setCommentReplyDtoList(commentRepliesDto);

          return commentDto;
        })
        .collect(Collectors.toList());
  }
}
