package sk.tuke.fei.kpi.dp.service.impl;

import static sk.tuke.fei.kpi.dp.exception.FaultType.INVALID_PARAMS;

import io.micronaut.context.annotation.Value;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.security.authentication.Authentication;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.tuke.fei.kpi.dp.dto.provider.gitlab.GitlabCommitActionDto;
import sk.tuke.fei.kpi.dp.dto.provider.gitlab.GitlabCommitDto;
import sk.tuke.fei.kpi.dp.exception.ApiException;
import sk.tuke.fei.kpi.dp.model.entity.Article;
import sk.tuke.fei.kpi.dp.model.entity.Image;
import sk.tuke.fei.kpi.dp.model.entity.PublicationConfiguration;
import sk.tuke.fei.kpi.dp.provider.GitlabApiClient;
import sk.tuke.fei.kpi.dp.service.PublicationConfigurationService;
import sk.tuke.fei.kpi.dp.service.PublicationService;

@Singleton
public class PublicationServiceImpl implements PublicationService {

  private final GitlabApiClient gitlabApiClient;
  private final PublicationConfigurationService configurationService;
  private final Logger logger = LoggerFactory.getLogger(PublicationServiceImpl.class);

  @Value("${micronaut.application.back-end-url}")
  String backEndUrl;

  public PublicationServiceImpl(GitlabApiClient gitlabApiClient,
      PublicationConfigurationService publicationConfigurationService) {
    this.gitlabApiClient = gitlabApiClient;
    this.configurationService = publicationConfigurationService;
  }

  @Override
  public void publishArticleToProjectRepository(Authentication authentication, Article article) {
    logger.info("About to publish article to project repository " + article.getId());
    // TODO handle gitlab.com and github.com domains
    String articlePublicationName = article.getPublicFileName();
    PublicationConfiguration configuration = configurationService.getAndValidatePublicationConfiguration(
        articlePublicationName);

    String pathToArticle = replaceTemplateForPathToArticle(articlePublicationName, configuration.getPathToArticle());
    String pathToImage = pathToArticle.substring(0, pathToArticle.lastIndexOf("/") + 1);

    GitlabCommitActionDto articleCommitAction = new GitlabCommitActionDto();
    articleCommitAction.setFile_path(pathToArticle);
    articleCommitAction.setContent(buildArticleContentWithMetaData(article));
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
      logger.error("A file with this name already exists");
      throw new ApiException(INVALID_PARAMS, "A file with this name already exists");
    } else if (e.getMessage().contains("invalid pathToArticle")) {
      logger.error("Invalid path to article");
      throw new ApiException(INVALID_PARAMS, "Invalid path to article");
    } else if ("You can only create or edit files when you are on a branch".equals(e.getMessage())) {
      logger.error("Branch does not exist");
      throw new ApiException(INVALID_PARAMS, "Branch does not exist");
    } else if ("401 Unauthorized".equals(e.getMessage())) {
      logger.error("Unauthorized, make sure that private token is correct");
      throw new ApiException(INVALID_PARAMS, "Unauthorized, make sure that private token is correct");
    } else {
      logger.error("Unexpected http client response error exception occurred");
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

  private String buildArticleContentWithMetaData(Article article) {
    LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Paris"));
    StringBuilder sb = new StringBuilder();
    sb.append("---\n");
    sb.append("Title: ").append(article.getName()).append("\n");
    sb.append("Abstract: ").append(article.getArticleAbstract()).append("\n");
    sb.append("Published at: ").append(formatDateTime(now)).append("\n");
    sb.append("Key words: ").append(article.getKeyWords()).append("\n");
    sb.append("Authors:\n");
    article.getArticleCollaborators()
        .stream()
        .filter(articleCollaborator -> Boolean.TRUE.equals(articleCollaborator.getAuthor()))
        .forEach(collaborator -> sb.append("  - ")
            .append(collaborator.getUser().getFirstName()).append(" ")
            .append(collaborator.getUser().getLastName()).append("\n"));
    sb.append("---\n");
    String removedBackendUrlsArticle = article.getText().replace(backEndUrl + "/image/content/", "");
    sb.append(removedBackendUrlsArticle);
    return sb.toString();
  }

  private String formatDateTime(LocalDateTime localDateTime) {
    return localDateTime.getDayOfMonth() + "." + localDateTime.getMonth().getValue() + "."
        + localDateTime.getYear() + " " + localDateTime.getHour() + ":"
        + (localDateTime.getMinute() < 10 ? ('0' + localDateTime.getMinute()) : localDateTime.getMinute());
  }
}
