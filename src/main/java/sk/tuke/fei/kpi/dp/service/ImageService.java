package sk.tuke.fei.kpi.dp.service;

import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.security.authentication.Authentication;
import java.util.List;
import sk.tuke.fei.kpi.dp.dto.ImageInfoDto;

public interface ImageService {

  String uploadImage(Authentication authentication, Long articleId, CompletedFileUpload file);

  byte[] getImageContent(String imageName);

  List<ImageInfoDto> getArticleImagesInfo(Authentication authentication, Long articleId);

  void removeImage(Authentication authentication, Long imageId);
}
