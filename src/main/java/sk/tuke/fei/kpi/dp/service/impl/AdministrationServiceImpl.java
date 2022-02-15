package sk.tuke.fei.kpi.dp.service.impl;

import static sk.tuke.fei.kpi.dp.exception.FaultType.FORBIDDEN;

import io.micronaut.security.authentication.Authentication;
import java.util.List;
import javax.inject.Singleton;
import sk.tuke.fei.kpi.dp.common.Utils;
import sk.tuke.fei.kpi.dp.dto.UserForAdminDto;
import sk.tuke.fei.kpi.dp.dto.provider.gitlab.GitlabProjectRepoDto;
import sk.tuke.fei.kpi.dp.dto.update.UpdateUserPrivilegesDto;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.exception.FaultType;
import sk.tuke.fei.kpi.dp.model.entity.PublicationConfiguration;
import sk.tuke.fei.kpi.dp.provider.GitlabApiClient;
import sk.tuke.fei.kpi.dp.service.AdministrationService;
import sk.tuke.fei.kpi.dp.service.PublicationConfigurationService;
import sk.tuke.fei.kpi.dp.service.UserService;

@Singleton
public class AdministrationServiceImpl implements AdministrationService {

  private final GitlabApiClient gitlabApiClient;
  private final PublicationConfigurationService configurationService;
  private final UserService userService;

  public AdministrationServiceImpl(GitlabApiClient gitlabApiClient,
      PublicationConfigurationService configurationService,
      UserService userService) {
    this.gitlabApiClient = gitlabApiClient;
    this.configurationService = configurationService;
    this.userService = userService;
  }

  @Override
  public PublicationConfiguration getPublicationConfigurationForAdmin(Authentication authentication) {
    validateLoggedUserAdministrator(authentication);
    return configurationService.getPublicationConfigurationForAdmin(authentication);
  }

  @Override
  public GitlabProjectRepoDto testProjectRepositoryConnectionByPublicationConfig(Authentication authentication,
      PublicationConfiguration publicationConfiguration) {
    validateLoggedUserAdministrator(authentication);
    if (publicationConfiguration == null) {
      throw new ApiException(FaultType.INVALID_PARAMS, "Publication configuration cannot be null");
    }
    if (Utils.isStringEmpty(publicationConfiguration.getRepositoryPath())
        || Utils.isStringEmpty(publicationConfiguration.getPrivateToken())) {
      throw new ApiException(FaultType.INVALID_PARAMS, "Private token and repository path cannot be null");
    }
    try {
      GitlabProjectRepoDto gitlabProjectRepoDto = gitlabApiClient.getGitlabProjectByPublicationConfig(
          publicationConfiguration.getRepositoryPath(), publicationConfiguration.getPrivateToken());
      if (gitlabProjectRepoDto == null) {
        throw new ApiException(FaultType.INVALID_PARAMS, "Spojenie neprebehlo úspešné");
      }
      return gitlabProjectRepoDto;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ApiException(FaultType.INVALID_PARAMS, "Spojenie neprebehlo úspešné");
    }
  }

  @Override
  public void updatePublicationConfig(Long id, Authentication authentication,
      PublicationConfiguration publicationConfiguration) {
    validateLoggedUserAdministrator(authentication);
    configurationService.updatePublicationConfig(id, authentication, publicationConfiguration);
  }

  @Override
  public List<UserForAdminDto> getAllUsersForAdmin(Authentication authentication) {
    validateLoggedUserAdministrator(authentication);
    return userService.getAllUsersForAdmin(authentication);
  }

  @Override
  public List<UserForAdminDto> updateUsersPrivileges(Authentication authentication, Long userId,
      UpdateUserPrivilegesDto updateUserPrivilegesDto) {
    validateLoggedUserAdministrator(authentication);
    return userService.updateUserPrivileges(authentication, userId, updateUserPrivilegesDto);
  }

  private void validateLoggedUserAdministrator(Authentication authentication) {
    if (authentication.getAttributes().get("administrator").equals(false)) {
      throw new ApiException(FORBIDDEN, "Publication configuration is available only for admin");
    }
  }
}
