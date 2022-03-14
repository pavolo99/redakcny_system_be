package sk.tuke.fei.kpi.dp.service.impl;

import static sk.tuke.fei.kpi.dp.exception.FaultType.INVALID_PARAMS;

import io.micronaut.security.authentication.Authentication;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.tuke.fei.kpi.dp.common.Utils;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.exception.FaultType;
import sk.tuke.fei.kpi.dp.model.entity.PublicationConfiguration;
import sk.tuke.fei.kpi.dp.model.repository.PublicationConfigurationRepository;
import sk.tuke.fei.kpi.dp.service.PublicationConfigurationService;

@Singleton
public class PublicationConfigurationServiceImpl implements PublicationConfigurationService {

  private final PublicationConfigurationRepository configurationRepository;
  private final Logger logger = LoggerFactory.getLogger(PublicationConfigurationServiceImpl.class);

  public PublicationConfigurationServiceImpl(PublicationConfigurationRepository configurationRepository) {
    this.configurationRepository = configurationRepository;
  }

  @Override
  public PublicationConfiguration getPublicationConfigurationForAdmin(Authentication authentication) {
    logger.info("About to get publication config");
    return getPublicationConfiguration();
  }

  @Override
  public PublicationConfiguration getAndValidatePublicationConfiguration(String articlePublicationName) {
    logger.info("About to get and validate publication config");
    if (articlePublicationName == null || articlePublicationName.isEmpty()) {
      logger.error("Article publication file name cannot be empty");
      throw new ApiException(INVALID_PARAMS, "Article publication file name cannot be empty");
    }
    PublicationConfiguration publicationConfiguration = getPublicationConfiguration();
    if (publicationConfiguration == null || Utils.isStringEmpty(publicationConfiguration.getBranch())
        || Utils.isStringEmpty(publicationConfiguration.getCommitMessage())
        || Utils.isStringEmpty(publicationConfiguration.getPathToArticle())
        || Utils.isStringEmpty(publicationConfiguration.getPrivateToken())
        || Utils.isStringEmpty(publicationConfiguration.getRepositoryPath())
        || publicationConfiguration.getDomain() == null) {
      logger.error("Publication configuration is not complete");
      throw new ApiException(FaultType.INVALID_PARAMS, "Publication configuration is not complete");
    }
    return publicationConfiguration;
  }

  @Override
  public void updatePublicationConfig(Long id, Authentication authentication,
      PublicationConfiguration publicationConfiguration) {
    logger.info("About to update publication config " + id);
    if (!id.equals(publicationConfiguration.getId())) {
      logger.error("Id " + id + " in path variable is not same as in dto");
      throw new ApiException(INVALID_PARAMS, "Id in path variable is not same as in dto");
    }
    configurationRepository.update(publicationConfiguration);
  }

  private PublicationConfiguration getPublicationConfiguration() {
    return configurationRepository.findAll().iterator().next();
  }
}
