package sk.tuke.fei.kpi.dp.service.impl;

import io.micronaut.security.authentication.Authentication;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;
import sk.tuke.fei.kpi.dp.dto.UpdateArticleDto;
import sk.tuke.fei.kpi.dp.dto.VersionDto;
import sk.tuke.fei.kpi.dp.dto.VersionSimpleDto;
import sk.tuke.fei.kpi.dp.dto.VersionViewDto;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.exception.FaultType;
import sk.tuke.fei.kpi.dp.mapper.VersionMapper;
import sk.tuke.fei.kpi.dp.model.entity.Article;
import sk.tuke.fei.kpi.dp.model.entity.User;
import sk.tuke.fei.kpi.dp.model.entity.Version;
import sk.tuke.fei.kpi.dp.model.repository.VersionRepository;
import sk.tuke.fei.kpi.dp.service.ArticleService;
import sk.tuke.fei.kpi.dp.service.UserService;
import sk.tuke.fei.kpi.dp.service.VersionService;

@Singleton
public class VersionServiceImpl implements VersionService {

  private final VersionRepository versionRepository;
  private final VersionMapper versionMapper;
  private final UserService userService;
  private final ArticleService articleService;

  public VersionServiceImpl(VersionRepository versionRepository,
      VersionMapper versionMapper, UserService userService, ArticleService articleService) {
    this.versionRepository = versionRepository;
    this.versionMapper = versionMapper;
    this.userService = userService;
    this.articleService = articleService;
  }

  @Override
  public VersionViewDto getVersions(Authentication authentication, Long articleId) {
    List<Version> versions = versionRepository.getVersionsByArticle(articleId);
    VersionViewDto versionViewDto = new VersionViewDto();
    if (versions.size() <  1) {
      return versionViewDto;
    }
    List<VersionSimpleDto> simpleVersionsDtoList = new ArrayList<>();
    for (int i = 0; i < versions.size(); i++) {
      VersionSimpleDto versionSimpleDto = versionMapper.versionToVersionSimpleDto(versions.get(i));
      versionSimpleDto.setOrder(i + 1);
      simpleVersionsDtoList.add(versionSimpleDto);
    }
    versionViewDto.setVersionSimpleDtoList(simpleVersionsDtoList);

    versionViewDto.setTotalVersionCount(versions.size());
    versionViewDto.setCurrentVersionText(versions.get(versions.size() - 1).getText());

    return versionViewDto;
  }

  @Override
  public VersionDto getVersionDetail(Authentication authentication, Long versionId) {
    Version version = versionRepository.getVersionByIdFetchCreatedBy(versionId);
    if (version == null) {
      throw new ApiException(FaultType.RECORD_NOT_FOUND, "Version was not found");
    }
    return versionMapper.versionToVersionDto(version);
  }

  @Override
  public VersionViewDto createCurrentVersionFromExisting(Authentication authentication,
      Long versionId) {

    Version existingVersion = versionRepository.getVersionByIdFetchArticle(versionId);
    if (existingVersion == null) {
      throw new ApiException(FaultType.RECORD_NOT_FOUND, "Version was not found");
    }
    Article article = existingVersion.getArticle();
    User loggedUser = userService.findUserById(Long.parseLong(authentication.getName()));
    UpdateArticleDto updateArticleDto = new UpdateArticleDto(article.getId(), article.getName(), existingVersion.getText());
    articleService.updateArticle(authentication, article.getId(), updateArticleDto);

    Version newCurrentVersion = new Version(existingVersion.getText(), loggedUser, article);
    Version savedVersion = versionRepository.save(newCurrentVersion);
    return getVersions(authentication, savedVersion.getArticle().getId());
  }

}
