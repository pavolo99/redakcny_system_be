package sk.tuke.fei.kpi.dp.dto;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class ArticleCollaboratorDto {

  private Long id;
  private UserDto userDto;
  private boolean isOwner;
  private boolean isAuthor;
  private boolean canEdit;

  public ArticleCollaboratorDto() {
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

  public boolean isOwner() {
    return isOwner;
  }

  public void setOwner(boolean owner) {
    isOwner = owner;
  }

  public boolean isAuthor() {
    return isAuthor;
  }

  public void setAuthor(boolean author) {
    isAuthor = author;
  }

  public boolean isCanEdit() {
    return canEdit;
  }

  public void setCanEdit(boolean canEdit) {
    this.canEdit = canEdit;
  }
}
