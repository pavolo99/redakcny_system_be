package sk.tuke.fei.kpi.dp.service.impl;

import static sk.tuke.fei.kpi.dp.exception.FaultType.INVALID_PARAMS;

import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.security.authentication.Authentication;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import sk.tuke.fei.kpi.dp.auth.gitlab.GitlabApiClient;
import sk.tuke.fei.kpi.dp.dto.GitlabCommitActionDto;
import sk.tuke.fei.kpi.dp.dto.GitlabCommitDto;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.model.entity.Article;
import sk.tuke.fei.kpi.dp.model.entity.Image;
import sk.tuke.fei.kpi.dp.model.entity.PublicationConfiguration;
import sk.tuke.fei.kpi.dp.service.PublicationConfigurationService;
import sk.tuke.fei.kpi.dp.service.PublicationService;

@Singleton
public class PublicationServiceImpl implements PublicationService {

  private final GitlabApiClient gitlabApiClient;
  private final PublicationConfigurationService configurationService;

  public PublicationServiceImpl(GitlabApiClient gitlabApiClient,
      PublicationConfigurationService publicationConfigurationService) {
    this.gitlabApiClient = gitlabApiClient;
    this.configurationService = publicationConfigurationService;
  }

  @Override
  public void publishArticleToProjectRepository(Authentication authentication, Article article) {
    // TODO handle GITHUB provider
    String articlePublicationName = article.getPublicFileName();
    PublicationConfiguration configuration = configurationService.getAndValidatePublicationConfiguration(
        articlePublicationName);

    String pathToArticle = replaceTemplateForPathToArticle(articlePublicationName, configuration.getPathToArticle());
    String pathToImage = pathToArticle.substring(0, pathToArticle.lastIndexOf("/") + 1);

    GitlabCommitActionDto articleCommitAction = new GitlabCommitActionDto();
    articleCommitAction.setFile_path(pathToArticle);
    // TODO handle image url replacement properly
    articleCommitAction.setContent(article.getText().replace("http://192.168.0.59:8080/image/content/", ""));
    List<GitlabCommitActionDto> commitActions = article.getImages()
        .stream()
        .map(image -> createGitlabImageCommitAction(pathToImage, image))
        .collect(Collectors.toList());

    commitActions.add(articleCommitAction);

    GitlabCommitDto commit = createGitlabCommit(configuration, authentication, commitActions, article.getName());
    try {
      gitlabApiClient.commitArticleToProjectRepository(configuration.getRepositoryPath(), configuration.getPrivateToken(), commit);
    } catch (HttpClientResponseException e) {
      handleHttpClientResponseException(e);
    }
  }

  private void handleHttpClientResponseException(HttpClientResponseException e) {
    if ("A file with this name already exists".equals(e.getMessage())) {
      throw new ApiException(INVALID_PARAMS, "A file with this name already exists");
    } else if (e.getMessage().contains("invalid pathToArticle")) {
      throw new ApiException(INVALID_PARAMS, "Invalid path to article");
    } else if ("You can only create or edit files when you are on a branch".equals(e.getMessage())) {
      throw new ApiException(INVALID_PARAMS, "Branch does not exist");
    } else if ("401 Unauthorized".equals(e.getMessage())) {
      throw new ApiException(INVALID_PARAMS, "Unauthorized, make sure that private token is correct");
    } else {
      throw new ApiException(INVALID_PARAMS, "Unexpected http client response error exception occurred");
    }
  }

  private String replaceTemplateForPathToArticle(String articlePublicationName, String pathToArticleTemplate) {
    LocalDate now = LocalDate.now();
    return pathToArticleTemplate
        .replace("{year}", String.valueOf(now.getYear()))
        .replace("{month}", String.valueOf(now.getMonth().getValue()))
        .replace("{slug}", articlePublicationName);
  }

  private GitlabCommitActionDto createGitlabImageCommitAction(String generalPath, Image image) {
    GitlabCommitActionDto gitlabCommitActionDto = new GitlabCommitActionDto();
    gitlabCommitActionDto.setFile_path(generalPath + image.getName());
    gitlabCommitActionDto.setContent(Base64.getEncoder().encodeToString(image.getImageContent()));
    gitlabCommitActionDto.setEncoding("base64");
    return gitlabCommitActionDto;
  }

  private GitlabCommitDto createGitlabCommit(PublicationConfiguration configuration,
      Authentication authentication, List<GitlabCommitActionDto> commitActions, String articleName) {
    Map<String, Object> loggedUser = authentication.getAttributes();

    GitlabCommitDto gitlabCommitDto = new GitlabCommitDto();
    gitlabCommitDto.setActions(commitActions);
    String formattedCommitMessage = configuration.getCommitMessage().replace("{articleName}", articleName);
    gitlabCommitDto.setCommit_message(formattedCommitMessage);
    gitlabCommitDto.setAuthor_email((String) loggedUser.get("email"));
    gitlabCommitDto.setAuthor_name(loggedUser.get("firstName") + " " + loggedUser.get("lastName"));
    gitlabCommitDto.setBranch(configuration.getBranch());
    return gitlabCommitDto;
  }
}
