package sk.tuke.fei.kpi.dp.service;

import io.micronaut.security.authentication.Authentication;
import java.util.List;
import java.util.Optional;
import sk.tuke.fei.kpi.dp.common.AuthProvider;
import sk.tuke.fei.kpi.dp.dto.LoggedUserDto;
import sk.tuke.fei.kpi.dp.dto.UserDto;
import sk.tuke.fei.kpi.dp.model.entity.User;

public interface UserService {

  Optional<User> findByUsernameAndAuthProvider(String username, AuthProvider authProvider);

  User saveUser(User loggedUser);

  List<UserDto> getPotentialCollaborators(Authentication authentication, String searchValue);

  LoggedUserDto getLoggedUser(Authentication authentication);

  User findUserById(Long userId);
}
