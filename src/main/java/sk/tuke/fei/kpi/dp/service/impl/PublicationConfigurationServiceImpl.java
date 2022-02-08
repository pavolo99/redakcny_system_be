package sk.tuke.fei.kpi.dp.service.impl;

import static sk.tuke.fei.kpi.dp.exception.FaultType.FORBIDDEN;
import static sk.tuke.fei.kpi.dp.exception.FaultType.INVALID_PARAMS;

import io.micronaut.security.authentication.Authentication;
import javax.inject.Singleton;
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
    validateLoggedUserAdministrator(authentication);
    return getPublicationConfiguration();
  }

  @Override
  public PublicationConfiguration getAndValidatePublicationConfiguration(String articlePublicationName) {
    if (articlePublicationName == null || articlePublicationName.isEmpty()) {
      throw new ApiException(INVALID_PARAMS, "Article publication file name cannot be empty");
    }
    PublicationConfiguration publicationConfiguration = getPublicationConfiguration();
    if (publicationConfiguration == null || isEmpty(publicationConfiguration.getBranch())
        || isEmpty(publicationConfiguration.getCommitMessage())
        || isEmpty(publicationConfiguration.getPathToArticle())
        || isEmpty(publicationConfiguration.getPrivateToken())
        || isEmpty(publicationConfiguration.getRepositoryPath())
        || publicationConfiguration.getProvider() == null) {
      throw new ApiException(FaultType.INVALID_PARAMS, "Publication configuration is not complete");
    }
    return publicationConfiguration;
  }

  @Override
  public void updatePublicationConfig(Long id, Authentication authentication,
      PublicationConfiguration publicationConfiguration) {
    validateLoggedUserAdministrator(authentication);
    if (!id.equals(publicationConfiguration.getId())) {
      throw new ApiException(INVALID_PARAMS, "Id in path variable is not same as in dto");
    }
    configurationRepository.update(publicationConfiguration);
  }

  private PublicationConfiguration getPublicationConfiguration() {
    return configurationRepository.findAll().iterator().next();
  }

  private void validateLoggedUserAdministrator(Authentication authentication) {
    if (authentication.getAttributes().get("administrator").equals(false)) {
      throw new ApiException(FORBIDDEN, "Publication configuration is available only for admin");
    }
  }

  private boolean isEmpty(String string) {
    return string == null || string.isEmpty();
  }
}
