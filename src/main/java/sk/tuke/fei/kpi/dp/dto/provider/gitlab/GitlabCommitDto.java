package sk.tuke.fei.kpi.dp.dto.provider.gitlab;

import io.micronaut.core.annotation.Introspected;
import java.util.List;


@Introspected
public class GitlabCommitDto {

  private String branch;
  private String commit_message;
  private List<GitlabCommitActionDto> actions;
  private String author_name;
  private String author_email;

  public GitlabCommitDto() {}

  public String getBranch() {
    return branch;
  }

  public void setBranch(String branch) {
    this.branch = branch;
  }

  public String getCommit_message() {
    return commit_message;
  }

  public void setCommit_message(String commit_message) {
    this.commit_message = commit_message;
  }

  public List<GitlabCommitActionDto> getActions() {
    return actions;
  }

  public void setActions(List<GitlabCommitActionDto> actions) {
    this.actions = actions;
  }

  public String getAuthor_name() {
    return author_name;
  }

  public void setAuthor_name(String author_name) {
    this.author_name = author_name;
  }

  public String getAuthor_email() {
    return author_email;
  }

  public void setAuthor_email(String author_email) {
    this.author_email = author_email;
  }
}
