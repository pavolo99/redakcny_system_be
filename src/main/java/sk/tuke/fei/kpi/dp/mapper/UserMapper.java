package sk.tuke.fei.kpi.dp.mapper;

import javax.inject.Singleton;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import sk.tuke.fei.kpi.dp.dto.UserDto;
import sk.tuke.fei.kpi.dp.dto.UserForAdminDto;
import sk.tuke.fei.kpi.dp.model.entity.ArticleCollaborator;
import sk.tuke.fei.kpi.dp.model.entity.User;

@Mapper(componentModel = "jsr330", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Singleton
public interface UserMapper {

  UserDto userToUserDto(User user);

  @Mapping(source = "role", target = "editor")
  UserForAdminDto userToUserForAdminDto(User user);

  @Mapping(source = "user.id", target = "id")
  @Mapping(source = "user.username", target = "username")
  @Mapping(source = "user.fullName", target = "fullName")
  @Mapping(source = "user.email", target = "email")
  @Mapping(source = "user.authProvider", target = "authProvider")
  UserDto collaboratorToUserDto(ArticleCollaborator articleCollaborator);

}
