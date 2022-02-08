package sk.tuke.fei.kpi.dp.service;

import io.micronaut.security.authentication.Authentication;
import sk.tuke.fei.kpi.dp.dto.VersionDto;
import sk.tuke.fei.kpi.dp.dto.view.VersionViewDto;

public interface VersionService {

  VersionViewDto getVersions(Authentication authentication, Long articleId);

  VersionDto getVersionDetail(Authentication authentication, Long versionId);

  VersionViewDto createCurrentVersionFromExisting(Authentication authentication, Long versionId);

}
