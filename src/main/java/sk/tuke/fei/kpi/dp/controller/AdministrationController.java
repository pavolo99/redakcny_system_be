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
import java.util.List;
import javax.validation.Valid;
import sk.tuke.fei.kpi.dp.dto.UserForAdminDto;
import sk.tuke.fei.kpi.dp.dto.provider.gitlab.GitlabProjectRepoDto;
import sk.tuke.fei.kpi.dp.dto.update.UpdateUserPrivilegesDto;
import sk.tuke.fei.kpi.dp.model.entity.PublicationConfiguration;
import sk.tuke.fei.kpi.dp.service.AdministrationService;

//@Controller("administration")
@Controller("api/administration")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class AdministrationController {

  private final AdministrationService administrationService;

  public AdministrationController(AdministrationService administrationService1) {
    this.administrationService = administrationService1;
  }


  @Get(uri = "/publication-config", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<PublicationConfiguration> getPublicationConfigForAdmin(Authentication authentication) {
    return HttpResponse.ok(administrationService.getPublicationConfigurationForAdmin(authentication));
  }

  @Put(uri = "/publication-config/test", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<GitlabProjectRepoDto> testProjectRepositoryConnectionByPublicationConfig(Authentication authentication,
      PublicationConfiguration publicationConfiguration) {
    return HttpResponse.ok(administrationService.testProjectRepositoryConnectionByPublicationConfig(
        authentication, publicationConfiguration));
  }

  @Put(uri = "/publication-config/{id}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<Void> updatePublicationConfig(Authentication authentication, @PathVariable Long id,
      PublicationConfiguration publicationConfiguration) {
    administrationService.updatePublicationConfig(id, authentication, publicationConfiguration);
    return HttpResponse.ok();
  }

  @Get(uri = "/users", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<List<UserForAdminDto>> getAllUsersForAdmin(Authentication authentication) {
    return HttpResponse.ok(administrationService.getAllUsersForAdmin(authentication));
  }

  @Put(uri = "/users/{userId}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<List<UserForAdminDto>> updateUsersPrivileges(Authentication authentication,
      @PathVariable Long userId, @Valid UpdateUserPrivilegesDto updateUserPrivilegesDto) {
    return HttpResponse.ok(administrationService.updateUsersPrivileges(authentication, userId, updateUserPrivilegesDto));
  }
}
