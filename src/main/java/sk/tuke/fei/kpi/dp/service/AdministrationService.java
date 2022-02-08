package sk.tuke.fei.kpi.dp.service;

import io.micronaut.security.authentication.Authentication;
import sk.tuke.fei.kpi.dp.dto.GitlabProjectRepoDto;
import sk.tuke.fei.kpi.dp.model.entity.PublicationConfiguration;

public interface AdministrationService {

  GitlabProjectRepoDto getGitlabProjectByPublicationConfig(Authentication authentication,
      PublicationConfiguration publicationConfiguration);

}
