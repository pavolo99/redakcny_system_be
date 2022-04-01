package sk.tuke.fei.kpi.dp.service.impl;

import io.micronaut.security.authentication.Authentication;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.tuke.fei.kpi.dp.common.Utils;
import sk.tuke.fei.kpi.dp.dto.ArticleConnectedCollaboratorDto;
import sk.tuke.fei.kpi.dp.dto.ArticleEditDto;
import sk.tuke.fei.kpi.dp.dto.UserDto;
import sk.tuke.fei.kpi.dp.dto.create.PreliminaryArticleTextDto;
import sk.tuke.fei.kpi.dp.mapper.ArticleCollaboratorMapper;
import sk.tuke.fei.kpi.dp.mapper.ArticleMapper;
import sk.tuke.fei.kpi.dp.mapper.UserMapper;
import sk.tuke.fei.kpi.dp.model.entity.Article;
import sk.tuke.fei.kpi.dp.model.entity.ArticleCollaborationSession;
import sk.tuke.fei.kpi.dp.model.entity.User;
import sk.tuke.fei.kpi.dp.model.repository.ArticleCollaborationSessionRepository;
import sk.tuke.fei.kpi.dp.service.ArticleCollaborationSessionService;
import sk.tuke.fei.kpi.dp.service.ArticleService;
import sk.tuke.fei.kpi.dp.service.UserService;

@Singleton
public class ArticleCollaborationSessionServiceImpl implements ArticleCollaborationSessionService {

  private final ArticleCollaborationSessionRepository sessionRepository;
  private final ArticleService articleService;
  private final ArticleMapper articleMapper;
  private final UserMapper userMapper;
  private final UserService userService;
  private final ArticleCollaboratorMapper collaboratorMapper;
  private final Logger logger = LoggerFactory.getLogger(ArticleCollaborationSessionServiceImpl.class);

  public ArticleCollaborationSessionServiceImpl(
      ArticleCollaborationSessionRepository sessionRepository,
      ArticleService articleService, ArticleMapper articleMapper,
      UserMapper userMapper, UserService userService,
      ArticleCollaboratorMapper collaboratorMapper) {
    this.sessionRepository = sessionRepository;
    this.articleService = articleService;
    this.articleMapper = articleMapper;
    this.userMapper = userMapper;
    this.userService = userService;
    this.collaboratorMapper = collaboratorMapper;
  }

  @Override
  @Transactional
  public ArticleEditDto getArticleAndCreateSession(Authentication authentication, Long articleId) {
    User loggedUser = userService.findUserById(Long.parseLong(authentication.getName()));
    List<ArticleCollaborationSession> articleSessions = sessionRepository.findByArticleId(articleId);
    Article article = articleService.findArticleById(articleId);
    ArticleCollaborationSession existingLoggedUsersSession = getSessionByUserAndArticle(articleId, loggedUser, articleSessions);
    Set<UserDto> connectedUsers = articleSessions.stream()
        .map(session -> userMapper.userToUserDto(session.getUser()))
        .collect(Collectors.toSet());
    if (existingLoggedUsersSession != null) {
      ArticleEditDto articleEditDto = articleMapper.articleToArticleDto(article);
      articleEditDto.setText(existingLoggedUsersSession.getText());
      addAllConnectedUsersToArticle(articleEditDto, loggedUser, connectedUsers);
      articleEditDto.setAllCollaborators(article.getArticleCollaborators().stream().map(userMapper::collaboratorToUserDto).collect(Collectors.toList()));

      boolean canLoggedUserEdit = getIfUserCanEditArticle(loggedUser, article)
          && articleSessions.stream()
          .filter(session -> !loggedUser.getId().equals(session.getUser().getId()))
          .noneMatch(ArticleCollaborationSession::getCanUserEdit);
      ArticleCollaborationSession editableSession = articleSessions.stream()
          .filter(ArticleCollaborationSession::getCanUserEdit).findFirst()
          .orElse(null);
      Long userIdWhoCanEditArticle = canLoggedUserEdit ? loggedUser.getId() : (editableSession == null ? null : editableSession.getUser().getId());
      articleEditDto.setUserIdWhoCanEdit(userIdWhoCanEditArticle);
      if (canLoggedUserEdit != existingLoggedUsersSession.getCanUserEdit()) {
        existingLoggedUsersSession.setCanUserEdit(canLoggedUserEdit);
        updateSession(existingLoggedUsersSession);
      }
      return articleEditDto;
    }

    boolean canLoggedUserEdit = getIfUserCanEditArticle(loggedUser, article);
    ArticleEditDto articleEditDto = articleMapper.articleToArticleDto(article);
    if (articleSessions.isEmpty()) {
      ArticleCollaborationSession articleCollaborationSession = new ArticleCollaborationSession(
          article.getText(), canLoggedUserEdit, loggedUser, article);

      createSession(articleCollaborationSession);
      articleEditDto.setUserIdWhoCanEdit(canLoggedUserEdit ? loggedUser.getId() : null);
    } else {
      ArticleCollaborationSession existingEditableSession = articleSessions.stream()
          .filter(session -> Boolean.TRUE.equals(session.getCanUserEdit().equals(Boolean.TRUE)))
          .findFirst().orElse(null);
      ArticleCollaborationSession articleCollaborationSession = new ArticleCollaborationSession(
          canLoggedUserEdit, loggedUser, article);
      if (existingEditableSession == null) {
        ArticleCollaborationSession randomArticleSession = articleSessions.stream().findAny().orElse(null);
        articleCollaborationSession.setText(randomArticleSession == null ? article.getText() : randomArticleSession.getText());
        articleEditDto.setText(articleCollaborationSession.getText());
      } else {
        articleCollaborationSession.setText(existingEditableSession.getText());
        articleEditDto.setText(articleCollaborationSession.getText());
        canLoggedUserEdit = false;
        articleCollaborationSession.setCanUserEdit(false);
      }
      createSession(articleCollaborationSession);
      articleEditDto.setUserIdWhoCanEdit(canLoggedUserEdit ? loggedUser.getId() : (existingEditableSession == null ? null : existingEditableSession.getUser().getId()));
    }
    addAllConnectedUsersToArticle(articleEditDto, loggedUser, connectedUsers);
    articleEditDto.setAllCollaborators(article.getArticleCollaborators().stream().map(userMapper::collaboratorToUserDto).collect(Collectors.toList()));
    return articleEditDto;
  }

  @Override
  public void disconnectFromArticleSession(Authentication authentication, Long articleId) {
    User loggedUser = new User(Long.parseLong(authentication.getName()));
    List<ArticleCollaborationSession> articleSessions = sessionRepository.findByArticleId(articleId);
    ArticleCollaborationSession sessionToDelete = getSessionByUserAndArticle(articleId, loggedUser, articleSessions);
    if (sessionToDelete != null) {
      if (Boolean.TRUE.equals(sessionToDelete.getCanUserEdit())) {
        for (ArticleCollaborationSession session : articleSessions) {
          if (session != sessionToDelete) {
            session.setText(sessionToDelete.getText());
            updateSession(session);
          }
        }
      }
      try {
        sessionRepository.delete(sessionToDelete);
      } catch (Exception e) {
        logger.error("Error occurred when removing session " + sessionToDelete.getId());
      }
    }
  }

  @Override
  @Transactional
  public ArticleEditDto savePreliminaryArticleText(Authentication authentication, Long articleId,
      PreliminaryArticleTextDto preliminaryArticleTextDto) {
    User loggedUser = new User(Long.parseLong(authentication.getName()));
    List<ArticleCollaborationSession> articleSessions = sessionRepository.findByArticleId(articleId);
    Article article = null;
    Set<UserDto> allConnectedUsers = new HashSet<>();
    for (ArticleCollaborationSession articleSession : articleSessions) {
      if (articleSession.getUser().getId().equals(loggedUser.getId())) {
        article = articleSession.getArticle();
      }
      allConnectedUsers.add(userMapper.userToUserDto(articleSession.getUser()));
      articleSession.setText(preliminaryArticleTextDto.getText());
      updateSession(articleSession);
    }
    if (article == null) {
      return getArticleAndCreateSession(authentication, articleId);
    }
    ArticleEditDto articleEditDto = articleMapper.articleToArticleDto(article);
    addAllConnectedUsersToArticle(articleEditDto, loggedUser, allConnectedUsers);
    articleEditDto.setAllCollaborators(article.getArticleCollaborators().stream().map(userMapper::collaboratorToUserDto).collect(Collectors.toList()));
    articleEditDto.setUserIdWhoCanEdit(loggedUser.getId());
    return articleEditDto;
  }

  @Override
  @Transactional
  public void leaveArticleEditingToOtherCollaborator(Authentication authentication, Long articleId,
      Long userId) {
    long loggedUserId = Long.parseLong(authentication.getName());
    List<ArticleCollaborationSession> articleSessions = sessionRepository.findByArticleId(articleId)
        .stream()
        .filter(session -> session.getUser().getId().equals(userId) || session.getUser().getId()
            .equals(loggedUserId))
        .peek(session -> session.setCanUserEdit(!session.getCanUserEdit()))
        .collect(Collectors.toList());
    try {
      sessionRepository.updateAll(articleSessions);
    } catch (Exception e) {
      logger.error("Error occurred when saving article sessions");
    }
  }

  @Override
  @Transactional
  public List<ArticleConnectedCollaboratorDto> getAllConnectedCollaboratorsToArticle(
      Authentication authentication, Long articleId) {
    return sessionRepository.findByArticleId(articleId)
        .stream()
        .map(session -> {
          ArticleConnectedCollaboratorDto articleConnectedCollaboratorDto = collaboratorMapper.articleCollaboratorToArticleConnectedCollaboratorDto(
              session);
          Boolean canUserPotentialyEditArticle = session.getArticle().getArticleCollaborators()
              .stream().filter(articleCollaborator -> articleCollaborator.getUser().getId()
                  .equals(articleConnectedCollaboratorDto.getUserDto().getId())).findFirst().get()
              .getCanEdit();
          articleConnectedCollaboratorDto.setCanUserEdit(canUserPotentialyEditArticle);
          return articleConnectedCollaboratorDto;
        })
        .sorted(Comparator.comparing(collaborator -> {
          UserDto userDto = collaborator.getUserDto();
          return Utils.isStringEmpty(userDto.getFullName()) ? userDto.getUsername() : userDto.getFullName();
        }))
        .collect(Collectors.toList());
  }

  @Override
  public void updateArticleTextForEverySession(Long articleId, String articleText) {
    sessionRepository.findByArticleId(articleId).forEach(articleCollaborationSession -> {
      articleCollaborationSession.setText(articleText);
      updateSession(articleCollaborationSession);
    });
  }

  private boolean getIfUserCanEditArticle(User user, Article article) {
    return article.getArticleCollaborators()
        .stream()
        .anyMatch(collaborator -> collaborator.getUser().getId().equals(user.getId())
            && collaborator.getCanEdit());
  }

  private void createSession(ArticleCollaborationSession articleCollaborationSession) {
    try {
      sessionRepository.save(articleCollaborationSession);
    } catch (Exception e) {
      logger.error("Error occurred when creating article sessions");
    }
  }

  private void updateSession(ArticleCollaborationSession articleCollaborationSession) {
    try {
      sessionRepository.update(articleCollaborationSession);
    } catch (Exception e) {
      logger.error("Error occurred when updating article sessions " + articleCollaborationSession.getId());
    }
  }

  private void addAllConnectedUsersToArticle(ArticleEditDto articleEditDto, User loggedUser,
      Set<UserDto> connectedUsers) {
    articleEditDto.getAllConnectedUsers().clear();
    articleEditDto.getAllConnectedUsers().addAll(connectedUsers);
    if (connectedUsers.stream().noneMatch(userDto -> userDto.getId().equals(loggedUser.getId()))) {
      articleEditDto.getAllConnectedUsers().add(userMapper.userToUserDto(loggedUser));
    }
    articleEditDto.getAllConnectedUsers()
        .sort(Comparator.comparing(u -> Utils.isStringEmpty(u.getFullName()) ? u.getUsername() : u.getFullName()));
  }

  private ArticleCollaborationSession getSessionByUserAndArticle(Long articleId, User user,
      List<ArticleCollaborationSession> articleSessions) {
    return articleSessions
        .stream()
        .filter(session -> session.getArticle().getId().equals(articleId) && user.getId()
            .equals(session.getUser().getId()))
        .findFirst()
        .orElse(null);
  }
}
