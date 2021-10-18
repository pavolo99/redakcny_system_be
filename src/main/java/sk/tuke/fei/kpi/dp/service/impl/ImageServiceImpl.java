package sk.tuke.fei.kpi.dp.service.impl;

import io.micronaut.http.multipart.CompletedFileUpload;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import sk.tuke.fei.kpi.dp.dto.ImageInfoDto;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.exception.FaultType;
import sk.tuke.fei.kpi.dp.mapper.ImageMapper;
import sk.tuke.fei.kpi.dp.model.entity.Article;
import sk.tuke.fei.kpi.dp.model.entity.Image;
import sk.tuke.fei.kpi.dp.model.repository.ImageRepository;
import sk.tuke.fei.kpi.dp.service.ImageService;

@Singleton
public class ImageServiceImpl implements ImageService {

  private final ImageRepository imageRepository;
  private final ImageMapper imageMapper;

  public ImageServiceImpl(ImageRepository imageRepository, ImageMapper imageMapper) {
    this.imageRepository = imageRepository;
    this.imageMapper = imageMapper;
  }

  @Override
  public Long uploadImage(Long articleId, CompletedFileUpload file) {
    try {
      Article article = new Article();
      article.setId(articleId);
      Image image = new Image(file.getFilename(), file.getBytes(), article);
      Image savedImage = imageRepository.save(image);
      return savedImage.getId();
    } catch (Exception e) {
      throw new ApiException(FaultType.GENERAL_ERROR, e.getMessage());
    }
  }

  @Override
  public byte[] getImageContent(Long imageId) {
    return getImageById(imageId).getImageContent();
  }

  @Override
  public List<ImageInfoDto> getArticleImagesInfo(Long articleId) {
    return imageRepository.findAllByArticle(articleId)
        .stream()
        .map(imageMapper::imageToImageInfoDto)
        .collect(Collectors.toList());
  }

  @Override
  public void removeImage(Long imageId) {
    imageRepository.delete(getImageById(imageId));
  }

  private Image getImageById(Long imageId) {
    return imageRepository.findById(imageId)
        .orElseThrow(() -> new ApiException(FaultType.RECORD_NOT_FOUND, "Image not found"));
  }
}
