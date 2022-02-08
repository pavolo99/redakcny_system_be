package sk.tuke.fei.kpi.dp.model.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;
import sk.tuke.fei.kpi.dp.model.entity.Image;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {

  @Query("select i from Image i left join fetch i.article a where a.id = :articleId")
  List<Image> findAllByArticle(Long articleId);

  Optional<Image> findByName(String imageName);
}
