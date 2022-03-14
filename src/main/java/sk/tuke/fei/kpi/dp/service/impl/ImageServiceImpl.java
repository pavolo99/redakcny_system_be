package sk.tuke.fei.kpi.dp.service.impl;

import static sk.tuke.fei.kpi.dp.exception.FaultType.GENERAL_ERROR;
import static sk.tuke.fei.kpi.dp.exception.FaultType.INVALID_PARAMS;
import static sk.tuke.fei.kpi.dp.exception.FaultType.RECORD_NOT_FOUND;

import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.security.authentication.Authentication;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

  public ImageServiceImpl(ImageRepository imageRepository, ImageMapper imageMapper,
      ArticleService articleService) {
    this.imageRepository = imageRepository;
    this.imageMapper = imageMapper;
    this.articleService = articleService;
  }

  @Override
  public String uploadImage(Authentication authentication, Long articleId, CompletedFileUpload uploadedFile) {
    logger.info("About to upload file");

    try {
      if (uploadedFile == null || uploadedFile.getFilename() == null) {
        logger.error("Uploaded file and its filename cannot be empty");
        throw new ApiException(INVALID_PARAMS, "Uploaded file and its filename cannot be empty");
      }
      Article article = articleService.findArticleById(articleId);
      String fileNameWithoutWhiteSpaces = uploadedFile.getFilename().replace(" ", "");
      Image image = new Image(fileNameWithoutWhiteSpaces, uploadedFile.getBytes(), article);
      Image savedImage = imageRepository.save(image);
      return savedImage.getName();
    } catch (Exception e) {
      if (e.getMessage().contains("org.hibernate.exception.ConstraintViolationException")) {
        logger.error("Image with the same name already exists");
        throw new ApiException(INVALID_PARAMS, "Image with the same name already exists");
      }
      logger.error("Unexpected error occurred while uploading image");
      throw new ApiException(GENERAL_ERROR, "Unexpected error occurred while uploading image");
    }
  }

  @Override
  public byte[] getImageContent(String imageName) {
    return imageRepository
        .findByName(imageName)
        .orElseThrow(() -> {
          logger.error("Image " + imageName + " was not found");
          return new ApiException(RECORD_NOT_FOUND, "Image was not found");
        }).getImageContent();
  }

  @Override
  public List<ImageInfoDto> getArticleImagesInfo(Authentication authentication, Long articleId) {
    logger.info("About to get article images info for article " + articleId);
    return imageRepository.findAllByArticle(articleId)
        .stream()
        .map(imageMapper::imageToImageInfoDto)
        .collect(Collectors.toList());
  }

  @Override
  public void removeImage(Authentication authentication, Long imageId) {
    logger.info("About to remove image " + imageId);
    Image imageToDelete = imageRepository.findById(imageId)
        .orElseThrow(() -> {
          logger.error("Image " + imageId + " was not found");
          return new ApiException(RECORD_NOT_FOUND, "Image was not found");
        });
    imageRepository.delete(imageToDelete);
  }
}
