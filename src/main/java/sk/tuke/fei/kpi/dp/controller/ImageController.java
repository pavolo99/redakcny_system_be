package sk.tuke.fei.kpi.dp.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Part;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.CompletedFileUpload;
import java.util.List;
import sk.tuke.fei.kpi.dp.dto.ImageInfoDto;
import sk.tuke.fei.kpi.dp.service.ImageService;

@Controller("image")
public class ImageController {

  private final ImageService imageService;

  public ImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  @Post(uri = "/uploaded/{articleId}", consumes = MediaType.MULTIPART_FORM_DATA)
  public HttpResponse<Long> uploadImage(@PathVariable Long articleId, @Part CompletedFileUpload file) {
    return HttpResponse.created(imageService.uploadImage(articleId, file));
  }

  @Get(uri = "/content/{imageId}", produces = MediaType.MULTIPART_FORM_DATA)
  public HttpResponse<byte[]> getImageContent(@PathVariable Long imageId) {
    return HttpResponse.ok(imageService.getImageContent(imageId));
  }

  @Get(uri = "/info/{articleId}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<List<ImageInfoDto>> getArticleImagesInfo(@PathVariable Long articleId) {
    return HttpResponse.ok(imageService.getArticleImagesInfo(articleId));
  }

  @Delete(uri = "/{imageId}")
  public HttpResponse<Void> removeImage(@PathVariable Long imageId) {
    imageService.removeImage(imageId);
    return HttpResponse.ok();
  }
}