package sk.tuke.fei.kpi.dp.mapper;

import javax.inject.Singleton;
import org.mapstruct.Mapper;
import sk.tuke.fei.kpi.dp.dto.UserDto;
import sk.tuke.fei.kpi.dp.model.entity.User;

@Mapper(componentModel = "jsr330")
@Singleton
public interface UserMapper {

  UserDto userToUserDto(User user);
}
