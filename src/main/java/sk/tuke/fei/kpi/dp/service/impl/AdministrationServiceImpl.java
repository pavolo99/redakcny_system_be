package sk.tuke.fei.kpi.dp.service.impl;

import io.micronaut.security.authentication.Authentication;
import javax.inject.Singleton;
import sk.tuke.fei.kpi.dp.dto.provider.gitlab.GitlabProjectRepoDto;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.exception.FaultType;
import sk.tuke.fei.kpi.dp.model.entity.PublicationConfiguration;
import sk.tuke.fei.kpi.dp.provider.GitlabApiClient;
import sk.tuke.fei.kpi.dp.service.AdministrationService;

@Singleton
public class AdministrationServiceImpl implements AdministrationService {

  private final GitlabApiClient gitlabApiClient;

  public AdministrationServiceImpl(GitlabApiClient gitlabApiClient) {
    this.gitlabApiClient = gitlabApiClient;
  }

  @Override
  public GitlabProjectRepoDto getGitlabProjectByPublicationConfig(Authentication authentication,
      PublicationConfiguration publicationConfiguration) {
    if (publicationConfiguration == null) {
      throw new ApiException(FaultType.INVALID_PARAMS, "Publication configuration cannot be null");
    }
    GitlabProjectRepoDto gitlabProjectRepoDto = gitlabApiClient.getGitlabProjectByPublicationConfig(
            publicationConfiguration.getRepositoryPath(), publicationConfiguration.getPrivateToken());
    if (gitlabProjectRepoDto == null) {
      throw new ApiException(FaultType.INVALID_PARAMS, "Gitlab project repo is null");
    }
    return gitlabProjectRepoDto;
  }
}
