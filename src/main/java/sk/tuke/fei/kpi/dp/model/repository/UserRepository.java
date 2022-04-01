package sk.tuke.fei.kpi.dp.model.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;
import sk.tuke.fei.kpi.dp.common.Provider;
import sk.tuke.fei.kpi.dp.model.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByUsernameAndAuthProvider(String username, Provider authProvider);

  @Query("select u "
      + "from User u "
      + "where (lower(u.fullName) like :searchValue "
      + "or lower(u.email) like :searchValue) "
      + "and u.id != :loggedUserId "
      + "order by u.fullName asc, u.email asc, u.username asc, u.authProvider")
  List<User> getPotentialCollaboratorsForArticle(String searchValue, Long loggedUserId);

  @Query("select u from User u order by u.fullName asc, u.email asc, u.username, u.authProvider")
  List<User> getAllUsers();

}
