package sk.tuke.fei.kpi.dp.model.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;
import sk.tuke.fei.kpi.dp.model.entity.Version;

@Repository
public interface VersionRepository extends CrudRepository<Version, Long> {

  @Query(value = "select v from Version v "
      + "left join fetch v.createdBy u "
      + "where v.article.id = :articleId "
      + "order by v.createdAt asc")
  List<Version> getVersionsByArticle(Long articleId);

  @Query(value = "select v from Version v "
      + "left join fetch v.createdBy u "
      + "where v.id = :versionId")
  Version getVersionByIdFetchCreatedBy(Long versionId);

  @Query(value = "select v from Version v "
      + "left join fetch v.article a "
      + "where v.id = :versionId")
  Version getVersionByIdFetchArticle(Long versionId);
}
