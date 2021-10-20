package sk.tuke.fei.kpi.dp.model.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;
import sk.tuke.fei.kpi.dp.model.entity.Article;
import sk.tuke.fei.kpi.dp.model.entity.ArticleStatus;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {

  @Query("select o from Article o")
  List<Article> getAllArticles();

  @Query("select o from Article o where o.articleStatus in :articleStatuses")
  List<Article> getArticlesByStatus(List<ArticleStatus> articleStatuses);

  @Query("select o from Article o where o.articleStatus = :writingArticleStatus and o.reviewNumber > 0")
  List<Article> getArticlesAfterReview(ArticleStatus writingArticleStatus);
}
