package sk.tuke.fei.kpi.dp.mapper;

import javax.inject.Singleton;
import org.mapstruct.Mapper;
import sk.tuke.fei.kpi.dp.dto.LoggedUserDto;

@Mapper(componentModel = "jsr330")
@Singleton
public interface UserMapper {

  LoggedUserDto loggedUserDtoToLoggedUserDto(LoggedUserDto loggedUserDto);
}
