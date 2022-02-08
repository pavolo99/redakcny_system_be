package sk.tuke.fei.kpi.dp.dto.simple;

import io.micronaut.core.annotation.Introspected;
import java.util.Date;

@Introspected
public class VersionSimpleDto {

  private Long id;
  private SimpleUserDto createdBy;
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

  public Integer getOrder() {
    return order;
  }

  public void setOrder(Integer order) {
    this.order = order;
  }
}
