package sk.tuke.fei.kpi.dp.service;

import java.util.Optional;
import sk.tuke.fei.kpi.dp.common.AuthProvider;
import sk.tuke.fei.kpi.dp.model.entity.User;

public interface UserService {

  Optional<User> findByUsernameAndAuthProvider(String username, AuthProvider authProvider);

  User saveUser(User loggedUser);
}
