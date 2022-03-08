package sk.tuke.fei.kpi.dp.dto;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class ArticleConnectedCollaboratorDto {

  private Long id;
  private UserDto userDto;
  private boolean canUserEdit;

  public ArticleConnectedCollaboratorDto() {

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UserDto getUserDto() {
    return userDto;
  }

  public void setUserDto(UserDto userDto) {
    this.userDto = userDto;
  }

  public boolean isCanUserEdit() {
    return canUserEdit;
  }

  public void setCanUserEdit(boolean canUserEdit) {
    this.canUserEdit = canUserEdit;
  }
}
