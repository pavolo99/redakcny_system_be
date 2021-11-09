package sk.tuke.fei.kpi.dp.service.impl;

import java.util.Optional;
import javax.inject.Singleton;
import sk.tuke.fei.kpi.dp.common.AuthProvider;
import sk.tuke.fei.kpi.dp.model.entity.User;
import sk.tuke.fei.kpi.dp.model.repository.UserRepository;
import sk.tuke.fei.kpi.dp.service.UserService;

@Singleton
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Optional<User> findByUsernameAndAuthProvider(String username, AuthProvider authProvider) {
    return userRepository.findByUsernameAndAuthProvider(username, authProvider);
  }

  @Override
  public User saveUser(User loggedUser) {
    return userRepository.save(loggedUser);
  }
}
