package sk.tuke.fei.kpi.dp.service.impl;

import static sk.tuke.fei.kpi.dp.exception.FaultType.GENERAL_ERROR;
import static sk.tuke.fei.kpi.dp.exception.FaultType.INVALID_PARAMS;
import static sk.tuke.fei.kpi.dp.exception.FaultType.RECORD_NOT_FOUND;

import io.micronaut.http.multipart.CompletedFileUpload;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import sk.tuke.fei.kpi.dp.dto.ImageInfoDto;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.mapper.ImageMapper;
import sk.tuke.fei.kpi.dp.model.entity.Article;
import sk.tuke.fei.kpi.dp.model.entity.Image;
import sk.tuke.fei.kpi.dp.model.repository.ImageRepository;
import sk.tuke.fei.kpi.dp.service.ArticleService;
import sk.tuke.fei.kpi.dp.service.ImageService;

@Singleton
public class ImageServiceImpl implements ImageService {

  private final ImageRepository imageRepository;
  private final ImageMapper imageMapper;
  private final ArticleService articleService;

  public ImageServiceImpl(ImageRepository imageRepository, ImageMapper imageMapper,
      ArticleService articleService) {
    this.imageRepository = imageRepository;
    this.imageMapper = imageMapper;
    this.articleService = articleService;
  }

  @Override
  public String uploadImage(Long articleId, CompletedFileUpload uploadedFile) {
    try {
      if (uploadedFile == null || uploadedFile.getFilename() == null) {
        throw new ApiException(INVALID_PARAMS, "Uploaded file and its filename cannot be empty");
      }
      Article article = articleService.findArticleById(articleId);
      String fileNameWithoutWhiteSpaces = uploadedFile.getFilename().replace(" ", "");
      Image image = new Image(fileNameWithoutWhiteSpaces, uploadedFile.getBytes(), article);
      Image savedImage = imageRepository.save(image);
      return savedImage.getName();
    } catch (Exception e) {
      if (e.getMessage().contains("org.hibernate.exception.ConstraintViolationException")) {
        throw new ApiException(INVALID_PARAMS, "Image with the same name already exists");
      }
      System.out.println("e.getMessage() = " + e.getMessage());
      e.printStackTrace();
      throw new ApiException(GENERAL_ERROR, "Unexpected error occurred while uploading image");
    }
  }

  @Override
  public byte[] getImageContent(String imageName) {
    return imageRepository.findByName(imageName)
        .orElseThrow(() -> new ApiException(RECORD_NOT_FOUND, "Image not found")).getImageContent();
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
    Image imageToDelete = imageRepository.findById(imageId)
        .orElseThrow(() -> new ApiException(RECORD_NOT_FOUND, "Image not found"));
    imageRepository.delete(imageToDelete);
  }
}
