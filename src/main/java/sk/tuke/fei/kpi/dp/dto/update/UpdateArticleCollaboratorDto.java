package sk.tuke.fei.kpi.dp.dto.update;

import io.micronaut.core.annotation.Introspected;
import javax.validation.constraints.NotBlank;

@Introspected
public class UpdateArticleCollaboratorDto {

  @NotBlank
  private Long id;
  @NotBlank
  private boolean canEdit;
  @NotBlank
  private boolean isAuthor;

  public UpdateArticleCollaboratorDto() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public boolean isCanEdit() {
    return canEdit;
  }

  public void setCanEdit(boolean canEdit) {
    this.canEdit = canEdit;
  }

  public boolean isAuthor() {
    return isAuthor;
  }

  public void setAuthor(boolean author) {
    isAuthor = author;
  }
}
