package sk.tuke.fei.kpi.dp.dto.update;

import io.micronaut.core.annotation.Introspected;
import javax.validation.constraints.NotNull;

@Introspected
public class UpdateUserPrivilegesDto {

  @NotNull
  private Long id;

  @NotNull
  private boolean editor;

  @NotNull
  private boolean administrator;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public boolean isEditor() {
    return editor;
  }

  public void setEditor(boolean editor) {
    this.editor = editor;
  }

  public boolean isAdministrator() {
    return administrator;
  }

  public void setAdministrator(boolean administrator) {
    this.administrator = administrator;
  }
}
