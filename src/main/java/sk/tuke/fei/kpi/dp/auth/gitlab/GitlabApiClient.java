package sk.tuke.fei.kpi.dp.auth.gitlab;

import static io.micronaut.http.HttpHeaders.ACCEPT;
import static io.micronaut.http.HttpHeaders.USER_AGENT;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.client.annotation.Client;
import org.reactivestreams.Publisher;
import sk.tuke.fei.kpi.dp.dto.GitlabUserDto;

@Header(name = USER_AGENT, value = "Micronaut HTTP Client")
@Header(name = ACCEPT, value = "application/json, application/json")
@Client("https://git.kpi.fei.tuke.sk/api/v4")
public interface GitlabApiClient {

  @Get("/user")
  Publisher<GitlabUserDto> getLoggedGitlabUser(@Header("Authorization") String authorization);
}

