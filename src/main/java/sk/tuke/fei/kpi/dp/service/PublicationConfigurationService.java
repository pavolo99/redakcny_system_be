package sk.tuke.fei.kpi.dp.service;

import io.micronaut.security.authentication.Authentication;
import sk.tuke.fei.kpi.dp.model.entity.PublicationConfiguration;

public interface PublicationConfigurationService {

  PublicationConfiguration getPublicationConfigurationForAdmin(Authentication authentication);

  PublicationConfiguration getAndValidatePublicationConfiguration(String articlePublicationName);

  void updatePublicationConfig(Long id, Authentication authentication,
      PublicationConfiguration publicationConfiguration);
}
