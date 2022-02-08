package sk.tuke.fei.kpi.dp.auth.gitlab;

import static io.micronaut.http.HttpHeaders.ACCEPT;
import static io.micronaut.http.HttpHeaders.USER_AGENT;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import org.reactivestreams.Publisher;
import sk.tuke.fei.kpi.dp.dto.GitlabCommitDto;
import sk.tuke.fei.kpi.dp.dto.GitlabProjectRepoDto;
import sk.tuke.fei.kpi.dp.dto.GitlabUserDto;

@Header(name = USER_AGENT, value = "Gitlab HTTP client")
@Header(name = ACCEPT, value = "application/json, application/json")
@Client("https://git.kpi.fei.tuke.sk/api/v4")
public interface GitlabApiClient {

  @Get("/user")
  Publisher<GitlabUserDto> getLoggedGitlabUser(@Header(HttpHeaders.AUTHORIZATION) String authorization);

  @Get("/projects/{projectRepositoryId}")
  GitlabProjectRepoDto getGitlabProjectByPublicationConfig(@PathVariable String projectRepositoryId,
      @Header("PRIVATE-TOKEN") String authorization);

  @Post("/projects/{projectRepositoryId}/repository/commits")
  void commitArticleToProjectRepository(@PathVariable String projectRepositoryId,
      @Header("PRIVATE-TOKEN") String authorization, @Body GitlabCommitDto gitlabCommitDto);

}

