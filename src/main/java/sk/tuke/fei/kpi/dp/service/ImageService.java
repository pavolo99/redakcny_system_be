package sk.tuke.fei.kpi.dp.service;

import io.micronaut.http.multipart.CompletedFileUpload;
import java.util.List;
import sk.tuke.fei.kpi.dp.dto.ImageInfoDto;

public interface ImageService {

  String uploadImage(Long articleId, CompletedFileUpload file);

  byte[] getImageContent(String imageName);

  List<ImageInfoDto> getArticleImagesInfo(Long articleId);

  void removeImage(Long imageId);
}
