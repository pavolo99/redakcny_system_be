package sk.tuke.fei.kpi.dp.model.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;
import sk.tuke.fei.kpi.dp.model.entity.ArticleCollaborationSession;

@Repository
public interface ArticleCollaborationSessionRepository extends CrudRepository<ArticleCollaborationSession, Long> {

  @Query(value = "SELECT s FROM ArticleCollaborationSession s WHERE s.article.id = :articleId")
  List<ArticleCollaborationSession> findByArticleId(Long articleId);

}
