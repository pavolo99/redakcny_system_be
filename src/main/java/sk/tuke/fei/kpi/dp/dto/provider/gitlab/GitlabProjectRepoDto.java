package sk.tuke.fei.kpi.dp.dto.provider.gitlab;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class GitlabProjectRepoDto {

  private String default_branch;

  public String getDefault_branch() {
    return default_branch;
  }

  public void setDefault_branch(String default_branch) {
    this.default_branch = default_branch;
  }
}
