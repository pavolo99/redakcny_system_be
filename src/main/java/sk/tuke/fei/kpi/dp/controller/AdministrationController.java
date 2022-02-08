package sk.tuke.fei.kpi.dp.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import sk.tuke.fei.kpi.dp.dto.GitlabProjectRepoDto;
import sk.tuke.fei.kpi.dp.model.entity.PublicationConfiguration;
import sk.tuke.fei.kpi.dp.service.AdministrationService;
import sk.tuke.fei.kpi.dp.service.PublicationConfigurationService;

@Controller("administration")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class AdministrationController {

  private final PublicationConfigurationService configurationService;
  private final AdministrationService administrationService;

  public AdministrationController(PublicationConfigurationService administrationService,
      AdministrationService administrationService1) {
    this.configurationService = administrationService;
    this.administrationService = administrationService1;
  }


  @Get(uri = "/publication-config", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<PublicationConfiguration> getPublicationConfigForAdmin(Authentication authentication) {
    return HttpResponse.ok(configurationService.getPublicationConfigurationForAdmin(authentication));
  }

  @Put(uri = "/publication-config/test", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<GitlabProjectRepoDto> testProjectRepositoryConnectionByPublicationConfig(Authentication authentication,
      PublicationConfiguration publicationConfiguration) {
    return HttpResponse.ok(administrationService.getGitlabProjectByPublicationConfig(authentication, publicationConfiguration));
  }

  @Put(uri = "/publication-config/{id}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<Void> updatePublicationConfig(Authentication authentication, @PathVariable Long id,
      PublicationConfiguration publicationConfiguration) {
    configurationService.updatePublicationConfig(id, authentication, publicationConfiguration);
    return HttpResponse.ok();
  }
}
