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
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import java.util.List;
import sk.tuke.fei.kpi.dp.dto.ImageInfoDto;
import sk.tuke.fei.kpi.dp.service.ImageService;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("api/image")
public class ImageController {

  private final ImageService imageService;

  public ImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  @Post(uri = "/uploaded/{articleId}", consumes = MediaType.MULTIPART_FORM_DATA)
  public HttpResponse<String> uploadImage(Authentication authentication,
      @PathVariable Long articleId, @Part CompletedFileUpload file) {
    return HttpResponse.created(imageService.uploadImage(authentication, articleId, file));
  }

  @Get(uri = "/content/{imageName}", produces = MediaType.MULTIPART_FORM_DATA)
  @Secured(SecurityRule.IS_ANONYMOUS)
  public HttpResponse<byte[]> getImageContent(@PathVariable String imageName) {
    return HttpResponse.ok(imageService.getImageContent(imageName));
  }

  @Get(uri = "/info/{articleId}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<List<ImageInfoDto>> getArticleImagesInfo(Authentication authentication,
      @PathVariable Long articleId) {
    return HttpResponse.ok(imageService.getArticleImagesInfo(authentication, articleId));
  }

  @Delete(uri = "/{imageId}")
  public HttpResponse<Void> removeImage(Authentication authentication, @PathVariable Long imageId) {
    imageService.removeImage(authentication, imageId);
    return HttpResponse.ok();
  }
}
