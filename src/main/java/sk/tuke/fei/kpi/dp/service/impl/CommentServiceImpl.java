package sk.tuke.fei.kpi.dp.service.impl;

import io.micronaut.security.authentication.Authentication;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.tuke.fei.kpi.dp.dto.CommentDto;
import sk.tuke.fei.kpi.dp.dto.CommentReplyDto;
import sk.tuke.fei.kpi.dp.dto.create.CommentCreateDto;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.exception.FaultType;
import sk.tuke.fei.kpi.dp.mapper.CommentMapper;
import sk.tuke.fei.kpi.dp.model.entity.Article;
import sk.tuke.fei.kpi.dp.model.entity.Comment;
import sk.tuke.fei.kpi.dp.model.entity.CommentReply;
import sk.tuke.fei.kpi.dp.model.entity.User;
import sk.tuke.fei.kpi.dp.model.repository.CommentReplyRepository;
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
  private final CommentReplyRepository commentReplyRepository;
  private final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

  public CommentServiceImpl(ArticleService articleService,
      UserService userService, CommentRepository commentRepository,
      CommentMapper commentMapper,
      CommentReplyRepository commentReplyRepository) {
    this.articleService = articleService;
    this.userService = userService;
    this.commentRepository = commentRepository;
    this.commentMapper = commentMapper;
    this.commentReplyRepository = commentReplyRepository;
  }

  @Override
  public void createComment(Authentication authentication,
      CommentCreateDto commentCreateDto, Long articleId) {
    logger.info("About to create comment for article " + articleId);
    Article article = articleService.findArticleById(articleId);
    User loggedUser = userService.findUserById(Long.parseLong(authentication.getName()));
    Comment comment = commentMapper.commentCreateDtoToComment(commentCreateDto);
    comment.setArticle(article);
    comment.setCreatedBy(loggedUser);
    comment.setUpdatedAt(new Date());
    commentRepository.save(comment);
  }

  @Override
  public void toggleCommentResolved(Authentication authentication, Long commentId) {
    logger.info("About to toggle comment resolved " + commentId);
    Comment comment = findByCommentId(commentId);
    comment.setResolved(!comment.getResolved());
    comment.setUpdatedAt(new Date());
    commentRepository.update(comment);
  }

  @Override
  public void deleteComment(Authentication authentication, Long commentId) {
    logger.info("About to delete comment " + commentId);
    Comment comment = findByCommentId(commentId);
    if (!comment.getCreatedBy().getId()
        .equals(userService.findUserById(Long.parseLong(authentication.getName())).getId())) {
      logger.error("Comment " + commentId + " cannot be deleted by other then creator himself");
      throw new ApiException(FaultType.FORBIDDEN, "Comment cannot be deleted by other then creator himself");
    }

    try {
      commentRepository.delete(comment);
    } catch (Exception e) {
      logger.error("Comment " + commentId + " cannot be deleted");
    }
  }

  @Override
  public List<CommentDto> getComments(Authentication authentication, Long articleId,
      boolean allComments) {
    logger.info("About to get all comments for article " + articleId);
    List<Comment> comments = allComments ? commentRepository.getAllCommentsByArticleId(articleId)
        : commentRepository.getUnResolvedCommentsByArticleId(articleId);

    List<CommentReply> commentReplies =
        allComments ? commentReplyRepository.getAllCommentsByArticleId(articleId)
            : commentReplyRepository.getUnResolvedCommentsByArticleId(articleId);

    return comments.stream()
        .map(comment -> {
          List<CommentReply> commentRepliesForComment = commentReplies
              .stream()
              .filter(commentReply -> commentReply.getComment().equals(comment))
              .collect(Collectors.toList());

          List<CommentReplyDto> commentRepliesDto = commentMapper
              .commentRepliesToCommentRepliesDtoList(commentRepliesForComment);
          CommentDto commentDto = commentMapper.commentToCommentDto(comment);
          commentDto.setCommentReplyDtoList(commentRepliesDto);
          return commentDto;
        })
        .collect(Collectors.toList());
  }

  @Override
  public Comment findByCommentId(Long commentId) {
    return commentRepository.findById(commentId)
        .orElseThrow(() -> {
          logger.error("Comment " + commentId + " was not found");
          return new ApiException(FaultType.RECORD_NOT_FOUND, "Comment was not found");
        });
  }
}
