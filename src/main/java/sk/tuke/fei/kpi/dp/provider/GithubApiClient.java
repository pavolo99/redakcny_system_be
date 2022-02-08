package sk.tuke.fei.kpi.dp.provider;

import static io.micronaut.http.HttpHeaders.ACCEPT;
import static io.micronaut.http.HttpHeaders.USER_AGENT;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.client.annotation.Client;
import org.reactivestreams.Publisher;
import sk.tuke.fei.kpi.dp.dto.provider.github.GithubUserDto;

@Header(name = USER_AGENT, value = "Github HTTP Client")
@Header(name = ACCEPT, value = "application/vnd.github.v3+json, application/json")
@Client(id = "https://api.github.com")
public interface GithubApiClient {

  @Get("/user")
  Publisher<GithubUserDto> getLoggedGithubUser(@Header(HttpHeaders.AUTHORIZATION) String authorization);

}
