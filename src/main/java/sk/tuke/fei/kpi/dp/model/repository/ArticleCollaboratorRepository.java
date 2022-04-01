package sk.tuke.fei.kpi.dp.model.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;
import sk.tuke.fei.kpi.dp.model.entity.ArticleCollaborator;

@Repository
public interface ArticleCollaboratorRepository extends CrudRepository<ArticleCollaborator, Long> {

  @Query("select o from ArticleCollaborator o left join fetch o.user u "
      + "where o.article.id = :articleId "
      + "order by o.isOwner desc, u.fullName asc, u.email asc, u.username asc, u.authProvider asc")
  List<ArticleCollaborator> getArticleCollaboratorsByArticle(long articleId);

  @Query("select o from ArticleCollaborator o left join fetch o.user u "
      + "where o.article.id = :articleId and u.id = :userId")
  ArticleCollaborator findByArticleAndLoggedUser(Long articleId, Long userId);
}
