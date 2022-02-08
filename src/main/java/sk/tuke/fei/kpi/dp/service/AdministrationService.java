package sk.tuke.fei.kpi.dp.service;

import io.micronaut.security.authentication.Authentication;
import java.util.List;
import sk.tuke.fei.kpi.dp.dto.UserForAdminDto;
import sk.tuke.fei.kpi.dp.dto.provider.gitlab.GitlabProjectRepoDto;
import sk.tuke.fei.kpi.dp.dto.update.UpdateUserPrivilegesDto;
import sk.tuke.fei.kpi.dp.model.entity.PublicationConfiguration;

public interface AdministrationService {

  PublicationConfiguration getPublicationConfigurationForAdmin(Authentication authentication);

  GitlabProjectRepoDto testProjectRepositoryConnectionByPublicationConfig(Authentication authentication,
      PublicationConfiguration publicationConfiguration);

  void updatePublicationConfig(Long id, Authentication authentication,
      PublicationConfiguration publicationConfiguration);

  List<UserForAdminDto> getAllUsersForAdmin(Authentication authentication);

  List<UserForAdminDto> updateUsersPrivileges(Authentication authentication, Long userId,
      UpdateUserPrivilegesDto updateUserPrivilegesDto);
}
