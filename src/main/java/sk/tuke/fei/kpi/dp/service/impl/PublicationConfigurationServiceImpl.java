package sk.tuke.fei.kpi.dp.service.impl;

import static sk.tuke.fei.kpi.dp.exception.FaultType.INVALID_PARAMS;

import io.micronaut.security.authentication.Authentication;
import javax.inject.Singleton;
import sk.tuke.fei.kpi.dp.common.Utils;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.exception.FaultType;
import sk.tuke.fei.kpi.dp.model.entity.PublicationConfiguration;
import sk.tuke.fei.kpi.dp.model.repository.PublicationConfigurationRepository;
import sk.tuke.fei.kpi.dp.service.PublicationConfigurationService;

@Singleton
public class PublicationConfigurationServiceImpl implements PublicationConfigurationService {

  private final PublicationConfigurationRepository configurationRepository;

  public PublicationConfigurationServiceImpl(PublicationConfigurationRepository configurationRepository) {
    this.configurationRepository = configurationRepository;
  }

  @Override
  public PublicationConfiguration getPublicationConfigurationForAdmin(Authentication authentication) {
    return getPublicationConfiguration();
  }

  @Override
  public PublicationConfiguration getAndValidatePublicationConfiguration(String articlePublicationName) {
    if (articlePublicationName == null || articlePublicationName.isEmpty()) {
      throw new ApiException(INVALID_PARAMS, "Article publication file name cannot be empty");
    }
    PublicationConfiguration publicationConfiguration = getPublicationConfiguration();
    if (publicationConfiguration == null || Utils.isStringEmpty(publicationConfiguration.getBranch())
        || Utils.isStringEmpty(publicationConfiguration.getCommitMessage())
        || Utils.isStringEmpty(publicationConfiguration.getPathToArticle())
        || Utils.isStringEmpty(publicationConfiguration.getPrivateToken())
        || Utils.isStringEmpty(publicationConfiguration.getRepositoryPath())
        || publicationConfiguration.getDomain() == null) {
      throw new ApiException(FaultType.INVALID_PARAMS, "Publication configuration is not complete");
    }
    return publicationConfiguration;
  }

  @Override
  public void updatePublicationConfig(Long id, Authentication authentication,
      PublicationConfiguration publicationConfiguration) {
    if (!id.equals(publicationConfiguration.getId())) {
      throw new ApiException(INVALID_PARAMS, "Id in path variable is not same as in dto");
    }
    configurationRepository.update(publicationConfiguration);
  }

  private PublicationConfiguration getPublicationConfiguration() {
    return configurationRepository.findAll().iterator().next();
  }
}
