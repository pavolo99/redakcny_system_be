package sk.tuke.fei.kpi.dp.auth.github;

import static io.micronaut.http.HttpHeaders.ACCEPT;
import static io.micronaut.http.HttpHeaders.USER_AGENT;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.client.annotation.Client;
import org.reactivestreams.Publisher;
import sk.tuke.fei.kpi.dp.dto.GithubUserDto;

@Header(name = USER_AGENT, value = "Micronaut HTTP Client")
@Header(name = ACCEPT, value = "application/vnd.github.v3+json, application/json")
@Client(id = "githubv3")
public interface GithubApiClient {

  @Get("/user")
  Publisher<GithubUserDto> getUser(@Header(HttpHeaders.AUTHORIZATION) String authorization);

}
