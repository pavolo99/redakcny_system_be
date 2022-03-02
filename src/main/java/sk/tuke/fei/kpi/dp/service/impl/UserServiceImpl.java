package sk.tuke.fei.kpi.dp.service.impl;

import static sk.tuke.fei.kpi.dp.exception.FaultType.INVALID_PARAMS;
import static sk.tuke.fei.kpi.dp.exception.FaultType.RECORD_NOT_FOUND;

import io.micronaut.security.authentication.Authentication;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Singleton;
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

  public UserServiceImpl(UserRepository userRepository,
      UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  @Override
  public Optional<User> findByUsernameAndAuthProvider(String username, Provider authProvider) {
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
  public LoggedUserDto getLoggedUser(Authentication authentication) {
    String role = authentication
        .getRoles()
        .stream()
        .findAny()
        .orElseThrow(
            () -> new ApiException(FaultType.FORBIDDEN, "User does not have any role assigned"));
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
    return userRepository.findById(userId).orElseThrow(
        () -> new ApiException(RECORD_NOT_FOUND, "User was not found"));

  }

  @Override
  public List<UserForAdminDto> getAllUsersForAdmin(Authentication authentication) {
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
    if (!updateUserPrivilegesDto.getId().equals(userId)) {
      throw new ApiException(INVALID_PARAMS, "Id in path variable is not the same as in dto");
    }
    User user = findUserById(userId);
    user.setRole(updateUserPrivilegesDto.isEditor() ? "EDITOR" : "AUTHOR");
    user.setAdministrator(updateUserPrivilegesDto.isAdministrator());
    userRepository.update(user);
    return getAllUsersForAdmin(authentication);
  }
}
