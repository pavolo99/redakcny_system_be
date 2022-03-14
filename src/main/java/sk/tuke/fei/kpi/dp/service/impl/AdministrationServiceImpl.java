package sk.tuke.fei.kpi.dp.service.impl;

import static sk.tuke.fei.kpi.dp.exception.FaultType.FORBIDDEN;

import io.micronaut.security.authentication.Authentication;
import java.util.List;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private final Logger logger = LoggerFactory.getLogger(AdministrationServiceImpl.class);

  public AdministrationServiceImpl(GitlabApiClient gitlabApiClient,
      PublicationConfigurationService configurationService,
      UserService userService) {
    this.gitlabApiClient = gitlabApiClient;
    this.configurationService = configurationService;
    this.userService = userService;
  }

  @Override
  public PublicationConfiguration getPublicationConfigurationForAdmin(Authentication authentication) {
    logger.info("About to get publication configuration for admin " + authentication.getName());
    validateLoggedUserAdministrator(authentication);
    return configurationService.getPublicationConfigurationForAdmin(authentication);
  }

  @Override
  public GitlabProjectRepoDto testProjectRepositoryConnectionByPublicationConfig(Authentication authentication,
      PublicationConfiguration publicationConfiguration) {
    logger.info("About to test project repository connection by publication config " + authentication.getName());
    validateLoggedUserAdministrator(authentication);
    if (publicationConfiguration == null) {
      logger.error("Publication configuration cannot be null");
      throw new ApiException(FaultType.INVALID_PARAMS, "Publication configuration cannot be null");
    }
    if (Utils.isStringEmpty(publicationConfiguration.getRepositoryPath())
        || Utils.isStringEmpty(publicationConfiguration.getPrivateToken())) {
      logger.error("Private token and repository path cannot be null");
      throw new ApiException(FaultType.INVALID_PARAMS, "Private token and repository path cannot be null");
    }
    try {
      GitlabProjectRepoDto gitlabProjectRepoDto = gitlabApiClient.getGitlabProjectByPublicationConfig(
          publicationConfiguration.getRepositoryPath(), publicationConfiguration.getPrivateToken());
      if (gitlabProjectRepoDto == null) {
        logger.error("Connection was not established");
        throw new ApiException(FaultType.INVALID_PARAMS, "Connection was not established");
      }
      return gitlabProjectRepoDto;
    } catch (Exception e) {
      logger.error("Connection was not established");
      throw new ApiException(FaultType.INVALID_PARAMS, "Connection was not established");
    }
  }

  @Override
  public void updatePublicationConfig(Long id, Authentication authentication,
      PublicationConfiguration publicationConfiguration) {
    logger.info("About to update publication config " + authentication.getName());
    validateLoggedUserAdministrator(authentication);
    configurationService.updatePublicationConfig(id, authentication, publicationConfiguration);
  }

  @Override
  public List<UserForAdminDto> getAllUsersForAdmin(Authentication authentication) {
    logger.info("About to get all users for admin " + authentication.getName());
    validateLoggedUserAdministrator(authentication);
    return userService.getAllUsersForAdmin(authentication);
  }

  @Override
  public List<UserForAdminDto> updateUsersPrivileges(Authentication authentication, Long userId,
      UpdateUserPrivilegesDto updateUserPrivilegesDto) {
    logger.info("About to update users privileges " + userId);
    validateLoggedUserAdministrator(authentication);
    return userService.updateUserPrivileges(authentication, userId, updateUserPrivilegesDto);
  }

  private void validateLoggedUserAdministrator(Authentication authentication) {
    if (authentication.getAttributes().get("administrator").equals(false)) {
      logger.error("Publication configuration is available only for admin");
      throw new ApiException(FORBIDDEN, "Publication configuration is available only for admin");
    }
  }
}
