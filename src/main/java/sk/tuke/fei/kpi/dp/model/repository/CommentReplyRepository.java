package sk.tuke.fei.kpi.dp.model.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;
import sk.tuke.fei.kpi.dp.model.entity.CommentReply;

@Repository
public interface CommentReplyRepository extends CrudRepository<CommentReply, Long> {

  @Query(value = "select r from CommentReply r "
      + "left join fetch r.createdBy u "
      + "left join fetch r.comment c "
      + "where c.article.id = :articleId "
      + "order by r.updatedAt asc")
  List<CommentReply> getAllCommentsByArticleId(Long articleId);

  @Query(value = "select r from CommentReply r "
      + "left join fetch r.createdBy u "
      + "left join fetch r.comment c "
      + "where c.article.id = :articleId and c.isResolved = false "
      + "order by r.updatedAt asc")
  List<CommentReply> getUnResolvedCommentsByArticleId(Long articleId);
}
