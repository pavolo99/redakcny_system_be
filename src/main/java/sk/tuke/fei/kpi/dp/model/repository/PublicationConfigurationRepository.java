package sk.tuke.fei.kpi.dp.model.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import sk.tuke.fei.kpi.dp.model.entity.PublicationConfiguration;

@Repository
public interface PublicationConfigurationRepository extends CrudRepository<PublicationConfiguration, Long> {
}
