package sk.tuke.fei.kpi.dp.service.impl;

import static sk.tuke.fei.kpi.dp.exception.FaultType.INVALID_PARAMS;
import static sk.tuke.fei.kpi.dp.exception.FaultType.RECORD_NOT_FOUND;

import io.micronaut.security.authentication.Authentication;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.tuke.fei.kpi.dp.common.Provider;
import sk.tuke.fei.kpi.dp.dto.LoggedUserDto;
import sk.tuke.fei.kpi.dp.dto.UserDto;
import sk.tuke.fei.kpi.dp.dto.UserForAdminDto;
import sk.tuke.fei.kpi.dp.dto.update.UpdateUserPrivilegesDto;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.exception.FaultType;
import sk.tuke.fei.kpi.dp.mapper.UserMapper;
import sk.tuke.fei.kpi.dp.model.entity.User;
import sk.tuke.fei.kpi.dp.model.repository.UserRepository;
import sk.tuke.fei.kpi.dp.service.UserService;

@Singleton
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  public UserServiceImpl(UserRepository userRepository,
      UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  @Override
  public Optional<User> findByUsernameAndAuthProvider(String username, Provider authProvider) {
    logger.info("About to find by username " + username + " and auth provider " + authProvider.toString());
    return userRepository.findByUsernameAndAuthProvider(username, authProvider);
  }

  @Override
  public User saveUser(User loggedUser) {
    logger.info("About to save user");
    return userRepository.save(loggedUser);
  }

  @Override
  public List<UserDto> getPotentialCollaborators(Authentication authentication, String searchValue) {
    logger.info("About to get potential collaborators for value " + searchValue);
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
  public LoggedUserDto getLoggedUser(Authentication authentication) {
    logger.info("About to get logged user " + authentication.getName());
    String role = authentication
        .getRoles()
        .stream()
        .findAny()
        .orElseThrow(() -> {
          logger.error("User " + authentication.getName() + " does not have any role assigned");
          return new ApiException(FaultType.FORBIDDEN, "User does not have any role assigned");
        });
    Map<String, Object> attributes = authentication.getAttributes();
    LoggedUserDto loggedUserDto = new LoggedUserDto();
    loggedUserDto.setId(Long.valueOf(authentication.getName()));
    loggedUserDto.setRole(role);
    loggedUserDto.setUsername((String) attributes.get("username"));
    loggedUserDto.setFirstName((String) attributes.get("firstName"));
    loggedUserDto.setLastName((String) attributes.get("lastName"));
    loggedUserDto.setAdministrator(String.valueOf(attributes.get("administrator")));
    loggedUserDto.setAuthProvider(Provider.valueOf((String) attributes.get("authProvider")));
    loggedUserDto.setEmail((String) attributes.get("email"));

    return loggedUserDto;
  }

  @Override
  public User findUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> {
          logger.error("User " + userId + " was not found");
          return new ApiException(RECORD_NOT_FOUND, "User was not found");
        });

  }

  @Override
  public List<UserForAdminDto> getAllUsersForAdmin(Authentication authentication) {
    logger.info("About to get all users for admin " + authentication.getName());
    return userRepository.getAllUsers()
        .stream()
        // sets true or false in string format to enable boolean parsing in user mapper
        .peek(user -> user.setRole(String.valueOf("EDITOR".equals(user.getRole()))))
        .map(userMapper::userToUserForAdminDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<UserForAdminDto> updateUserPrivileges(Authentication authentication, Long userId,
      UpdateUserPrivilegesDto updateUserPrivilegesDto) {
    logger.info("About to update user privileges " + userId);
    if (!updateUserPrivilegesDto.getId().equals(userId)) {
      logger.error("User " + userId + " in path variable is not the same as in dto");
      throw new ApiException(INVALID_PARAMS, "Id in path variable is not the same as in dto");
    }
    User user = findUserById(userId);
    user.setRole(updateUserPrivilegesDto.isEditor() ? "EDITOR" : "AUTHOR");
    user.setAdministrator(updateUserPrivilegesDto.isAdministrator());
    userRepository.update(user);
    return getAllUsersForAdmin(authentication);
  }
}
