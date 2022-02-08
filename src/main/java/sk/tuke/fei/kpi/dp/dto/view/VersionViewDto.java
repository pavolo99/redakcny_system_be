package sk.tuke.fei.kpi.dp.dto.view;

import io.micronaut.core.annotation.Introspected;
import java.util.List;
import sk.tuke.fei.kpi.dp.dto.simple.VersionSimpleDto;

@Introspected
public class VersionViewDto {

  private List<VersionSimpleDto> versionSimpleDtoList;
  private String currentVersionText;
  private Integer totalVersionCount;

  public VersionViewDto() {
  }

  public String getCurrentVersionText() {
    return currentVersionText;
  }

  public void setCurrentVersionText(String currentVersionText) {
    this.currentVersionText = currentVersionText;
  }

  public Integer getTotalVersionCount() {
    return totalVersionCount;
  }

  public void setTotalVersionCount(Integer totalVersionCount) {
    this.totalVersionCount = totalVersionCount;
  }

  public List<VersionSimpleDto> getVersionSimpleDtoList() {
    return versionSimpleDtoList;
  }

  public void setVersionSimpleDtoList(List<VersionSimpleDto> versionSimpleDtoList) {
    this.versionSimpleDtoList = versionSimpleDtoList;
  }
}
