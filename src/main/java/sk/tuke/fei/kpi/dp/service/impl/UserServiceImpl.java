package sk.tuke.fei.kpi.dp.service.impl;

import static sk.tuke.fei.kpi.dp.exception.FaultType.RECORD_NOT_FOUND;

import io.micronaut.security.authentication.Authentication;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import sk.tuke.fei.kpi.dp.common.AuthProvider;
import sk.tuke.fei.kpi.dp.dto.UserDto;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.mapper.UserMapper;
import sk.tuke.fei.kpi.dp.model.entity.User;
import sk.tuke.fei.kpi.dp.model.repository.UserRepository;
import sk.tuke.fei.kpi.dp.service.UserService;

@Singleton
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserServiceImpl(UserRepository userRepository,
      UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  @Override
  public Optional<User> findByUsernameAndAuthProvider(String username, AuthProvider authProvider) {
    return userRepository.findByUsernameAndAuthProvider(username, authProvider);
  }

  @Override
  public User saveUser(User loggedUser) {
    return userRepository.save(loggedUser);
  }

  @Override
  public List<UserDto> getPotentialCollaborators(Authentication authentication, String searchValue) {
    StringBuilder generalSearchValue = new StringBuilder().append("%");
    if (searchValue != null) {
      generalSearchValue.append(searchValue.trim().toLowerCase());
    }
    generalSearchValue.append("%");

    List<User> potentialCollaboratorsForArticle = userRepository.getPotentialCollaboratorsForArticle(
        generalSearchValue.toString(), Long.parseLong(authentication.getName()));
    return potentialCollaboratorsForArticle
        .stream()
        .map(userMapper::userToUserDto)
        .collect(Collectors.toList());
  }

  @Override
  public User findUserById(Long userId) {
    return userRepository.findById(userId).orElseThrow(
        () -> new ApiException(RECORD_NOT_FOUND, "User was not found"));

  }
}
