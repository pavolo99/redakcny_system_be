package sk.tuke.fei.kpi.dp.model.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import java.util.Optional;
import sk.tuke.fei.kpi.dp.common.AuthProvider;
import sk.tuke.fei.kpi.dp.model.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByUsernameAndAuthProvider(String username, AuthProvider authProvider);
}
