package sk.tuke.fei.kpi.dp.mapper;

import javax.inject.Singleton;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sk.tuke.fei.kpi.dp.dto.UserDto;
import sk.tuke.fei.kpi.dp.dto.UserForAdminDto;
import sk.tuke.fei.kpi.dp.model.entity.User;

@Mapper(componentModel = "jsr330")
@Singleton
public interface UserMapper {

  UserDto userToUserDto(User user);

  @Mapping(source = "role", target = "editor")
  UserForAdminDto userToUserForAdminDto(User user);
}
