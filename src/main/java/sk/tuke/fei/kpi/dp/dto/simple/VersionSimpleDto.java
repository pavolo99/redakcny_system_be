package sk.tuke.fei.kpi.dp.dto.simple;

import io.micronaut.core.annotation.Introspected;
import java.util.Date;
import sk.tuke.fei.kpi.dp.dto.UserDto;

@Introspected
public class VersionSimpleDto {

  private Long id;
  private UserDto createdBy;
  private Date createdAt;
  private Integer order;

  public VersionSimpleDto() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UserDto getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(UserDto createdBy) {
    this.createdBy = createdBy;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Integer getOrder() {
    return order;
  }

  public void setOrder(Integer order) {
    this.order = order;
  }
}
