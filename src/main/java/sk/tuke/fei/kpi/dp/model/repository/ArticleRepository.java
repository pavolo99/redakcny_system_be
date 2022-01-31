package sk.tuke.fei.kpi.dp.model.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;
import sk.tuke.fei.kpi.dp.model.entity.Article;
import sk.tuke.fei.kpi.dp.model.entity.ArticleStatus;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {

  @Query("select a from Article a left join fetch a.updatedBy u "
      + "where a.articleStatus in :articleStatuses order by a.updatedAt desc")
  List<Article> getArticlesByStatus(List<ArticleStatus> articleStatuses);

  @Query("select a from Article a left join fetch a.updatedBy "
      + "where a.createdBy.id = :loggedUserId order by a.updatedAt desc")
  List<Article> getAllArticlesCreatedByLoggedUser(long loggedUserId);

  @Query(value = "select a from Article a left join fetch a.updatedBy u where a.id in "
      + "(select c.article.id from ArticleCollaborator c where c.user.id = :loggedUserId and c.isOwner = false) "
      + "order by a.updatedAt desc")
  List<Article> getSharedArticlesOfLoggedUser(long loggedUserId);

  @Query(value = "select a from Article a left join fetch a.updatedBy u where a.id in "
      + "(select c.article.id from ArticleCollaborator c where c.user.id = :loggedUserId) "
      + "order by a.updatedAt desc")
  List<Article> getReviewedArticlesForEditor(long loggedUserId);
}
