package sk.tuke.fei.kpi.dp.model.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "IMAGE")
public class Image {

  @Id
  @GeneratedValue
  @Column(name = "ID")
  private Long id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "IMAGE_CONTENT")
  private byte[] imageContent;

  @ManyToOne
  private Article article;

  public Image() {}

  public Image(String name, byte[] imageContent, Article article) {
    this.name = name;
    this.imageContent = imageContent;
    this.article = article;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public byte[] getImageContent() {
    return imageContent;
  }

  public void setImageContent(byte[] imageContent) {
    this.imageContent = imageContent;
  }

  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Image)) {
      return false;
    }
    Image image = (Image) o;
    return id.equals(image.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
