package sk.tuke.fei.kpi.dp.model.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import sk.tuke.fei.kpi.dp.common.ArticleStatusConverter;

@Entity
@Table(name = "ARTICLE", schema = "redakcny_system")
public class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "TEXT")
  private String text;

  @Column(name = "KEY_WORDS")
  private String keyWords;

  @Column(name = "ABSTRACT")
  private String articleAbstract;

  @Column(name = "PUBLIC_FILE_NAME")
  private String publicFileName;

  @Column(name = "PUBLICATION_DECISION")
  private String publicationDecision;

  @Column(name = "REVIEW_NUMBER")
  private Integer reviewNumber;

  @Column(name = "CREATED_AT")
  private Date createdAt;

  @Column(name = "UPDATED_AT")
  private Date updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  private User createdBy;

  @ManyToOne(fetch = FetchType.LAZY)
  private User updatedBy;

  @Convert(converter = ArticleStatusConverter.class)
  @Column(name = "ARTICLE_STATUS_ID")
  private ArticleStatus articleStatus;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "article")
  List<ArticleCollaborator> articleCollaborators = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "article")
  List<Image> images = new ArrayList<>();

  public Article() {}

  public Article(String name, String text, Integer reviewNumber, ArticleStatus articleStatus, User createdBy) {
    this.name = name;
    this.text = text;
    this.reviewNumber = reviewNumber;
    this.articleStatus = articleStatus;

    Date now = new Date();
    this.createdAt = now;
    this.updatedAt = now;
    this.createdBy = createdBy;
    this.updatedBy = createdBy;
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

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getKeyWords() {
    return keyWords;
  }

  public void setKeyWords(String keyWords) {
    this.keyWords = keyWords;
  }

  public String getArticleAbstract() {
    return articleAbstract;
  }

  public void setArticleAbstract(String articleAbstract) {
    this.articleAbstract = articleAbstract;
  }

  public String getPublicFileName() {
    return publicFileName;
  }

  public void setPublicFileName(String publicFileName) {
    this.publicFileName = publicFileName;
  }

  public String getPublicationDecision() {
    return publicationDecision;
  }

  public void setPublicationDecision(String publicationDecision) {
    this.publicationDecision = publicationDecision;
  }

  public Integer getReviewNumber() {
    return reviewNumber;
  }

  public void setReviewNumber(Integer reviewNumber) {
    this.reviewNumber = reviewNumber;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public User getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(User updatedBy) {
    this.updatedBy = updatedBy;
  }

  public ArticleStatus getArticleStatus() {
    return articleStatus;
  }

  public void setArticleStatus(ArticleStatus articleStatus) {
    this.articleStatus = articleStatus;
  }

  public List<ArticleCollaborator> getArticleCollaborators() {
    return articleCollaborators;
  }

  public void setArticleCollaborators(
      List<ArticleCollaborator> articleCollaborators) {
    this.articleCollaborators = articleCollaborators;
  }

  public List<Image> getImages() {
    return images;
  }

  public void setImages(List<Image> images) {
    this.images = images;
  }
}
