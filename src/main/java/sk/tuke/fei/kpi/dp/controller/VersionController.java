package sk.tuke.fei.kpi.dp.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import sk.tuke.fei.kpi.dp.dto.VersionDto;
import sk.tuke.fei.kpi.dp.dto.view.VersionViewDto;
import sk.tuke.fei.kpi.dp.service.VersionService;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("api/version")
public class VersionController {

  private final VersionService versionService;

  public VersionController(VersionService versionService) {
    this.versionService = versionService;
  }

  @Get(uri = "/{articleId}/all", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<VersionViewDto> getVersions(Authentication authentication, @PathVariable Long articleId) {
    return HttpResponse.ok(versionService.getVersions(authentication, articleId));
  }

  @Get(uri = "/{versionId}/detail", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<VersionDto> getVersionDetail(Authentication authentication, @PathVariable Long versionId) {
    return HttpResponse.ok(versionService.getVersionDetail(authentication, versionId));
  }

  @Post(uri = "/{versionId}/current", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<VersionViewDto> createCurrentVersionFromExisting(Authentication authentication, @PathVariable Long versionId) {
    return HttpResponse.ok(versionService.createCurrentVersionFromExisting(authentication, versionId));
  }
}
