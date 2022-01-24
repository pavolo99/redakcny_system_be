package sk.tuke.fei.kpi.dp.dto;

import io.micronaut.core.annotation.Introspected;
import java.util.Date;

@Introspected
public class VersionDto {

  private Long id;
  private SimpleUserDto createdBy;
  private Date createdAt;
  private String text;

  public VersionDto() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public SimpleUserDto getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(SimpleUserDto createdBy) {
    this.createdBy = createdBy;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
