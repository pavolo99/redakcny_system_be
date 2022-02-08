package sk.tuke.fei.kpi.dp.service;

import io.micronaut.security.authentication.Authentication;
import sk.tuke.fei.kpi.dp.model.entity.Article;

public interface PublicationService {

  void publishArticleToProjectRepository(Authentication authentication, Article article);

}
