package sk.tuke.fei.kpi.dp.dto;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class GitlabCommitActionDto {

  private String action;
  private String file_path;
  private String content;
  private String encoding;

  public GitlabCommitActionDto() {
    this.action = "create";
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getFile_path() {
    return file_path;
  }

  public void setFile_path(String file_path) {
    this.file_path = file_path;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getEncoding() {
    return encoding;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }
}
