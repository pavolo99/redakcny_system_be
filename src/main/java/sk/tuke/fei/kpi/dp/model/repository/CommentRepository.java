package sk.tuke.fei.kpi.dp.model.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;
import sk.tuke.fei.kpi.dp.model.entity.Comment;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {


  @Query(value = "select c from Comment c "
      + "left join fetch c.createdBy u "
      + "where c.article.id = :articleId "
      + "order by c.isResolved asc, c.updatedAt desc")
  List<Comment> getAllCommentsByArticleId(Long articleId);

  @Query(value = "select c from Comment c "
      + "left join fetch c.createdBy u "
      + "where c.article.id = :articleId and c.isResolved = false "
      + "order by c.updatedAt desc")
  List<Comment> getUnResolvedCommentsByArticleId(Long articleId);
}
