CREATE SCHEMA IF NOT EXISTS redakcny_system;

CREATE TABLE IF NOT EXISTS redakcny_system.SYSTEM_USER
(
    ID               SERIAL      NOT NULL,
    USERNAME         VARCHAR(50) NOT NULL,
    FIRST_NAME       VARCHAR(50),
    LAST_NAME        VARCHAR(50),
    EMAIL            VARCHAR(50),
    AUTH_PROVIDER    VARCHAR(50) NOT NULL,
    ROLE             VARCHAR(50) NOT NULL,
    IS_ADMINISTRATOR BOOLEAN     NOT NULL DEFAULT FALSE,
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS redakcny_system.ARTICLE_STATUS
(
    ID    SERIAL      NOT NULL,
    STATE VARCHAR(50) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS redakcny_system.ARTICLE
(
    ID                   SERIAL         NOT NULL,
    NAME                 VARCHAR(50)    NOT NULL,
    TEXT                 VARCHAR(100000) NOT NULL,
    KEY_WORDS            VARCHAR(50),
    ABSTRACT             VARCHAR(1000),
    PUBLIC_FILE_NAME     VARCHAR(50),
    PUBLICATION_DECISION VARCHAR(50),
    REVIEW_NUMBER        INT            NOT NULL,
    ARTICLE_STATUS_ID    INT            NOT NULL,
    CREATED_AT           TIMESTAMP      NOT NULL,
    UPDATED_AT           TIMESTAMP      NOT NULL,
    CREATED_BY_ID        INT            NOT NULL,
    UPDATED_BY_ID        INT            NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (ARTICLE_STATUS_ID) REFERENCES redakcny_system.article_status (ID),
    FOREIGN KEY (CREATED_BY_ID) REFERENCES redakcny_system.SYSTEM_USER (ID),
    FOREIGN KEY (UPDATED_BY_ID) REFERENCES redakcny_system.SYSTEM_USER (ID)
);

CREATE TABLE IF NOT EXISTS redakcny_system.IMAGE
(
    ID            SERIAL       NOT NULL,
    NAME          VARCHAR(200) NOT NULL,
    IMAGE_CONTENT BYTEA        NOT NULL,
    ARTICLE_ID    INT          NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (ARTICLE_ID) REFERENCES redakcny_system.ARTICLE (ID),
    UNIQUE (NAME, ARTICLE_ID)
);

CREATE TABLE IF NOT EXISTS redakcny_system.COMMENT
(
    ID             SERIAL       NOT NULL,
    IS_RESOLVED    BOOLEAN      NOT NULL,
    TEXT           VARCHAR(1000) NOT NULL,
    UPDATED_AT     TIMESTAMP    NOT NULL,
    RANGE_FROM     INT          NOT NULL,
    RANGE_TO       INT          NOT NULL,
    COMMENTED_TEXT VARCHAR(100000),
    ARTICLE_ID     INT          NOT NULL,
    CREATED_BY_ID  INT          NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (ARTICLE_ID) REFERENCES redakcny_system.ARTICLE (ID),
    FOREIGN KEY (CREATED_BY_ID) REFERENCES redakcny_system.SYSTEM_USER (ID)
);

CREATE TABLE IF NOT EXISTS redakcny_system.COMMENT_REPLY
(
    ID            SERIAL       NOT NULL,
    TEXT          VARCHAR(1000) NOT NULL,
    UPDATED_AT    TIMESTAMP    NOT NULL,
    COMMENT_ID    INT          NOT NULL,
    CREATED_BY_ID INT          NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (COMMENT_ID) REFERENCES redakcny_system.COMMENT (ID),
    FOREIGN KEY (CREATED_BY_ID) REFERENCES redakcny_system.SYSTEM_USER (ID)
);

CREATE TABLE IF NOT EXISTS redakcny_system.VERSION
(
    ID            SERIAL         NOT NULL,
    TEXT          VARCHAR(100000) NOT NULL,
    CREATED_AT    TIMESTAMP      NOT NULL,
    ARTICLE_ID    INT            NOT NULL,
    CREATED_BY_ID INT            NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (ARTICLE_ID) REFERENCES redakcny_system.ARTICLE (ID),
    FOREIGN KEY (CREATED_BY_ID) REFERENCES redakcny_system.SYSTEM_USER (ID)
);

CREATE TABLE IF NOT EXISTS redakcny_system.ARTICLE_COLLABORATOR
(
    ID         SERIAL  NOT NULL,
    IS_OWNER   BOOLEAN NOT NULL DEFAULT FALSE,
    CAN_EDIT   BOOLEAN NOT NULL DEFAULT FALSE,
    IS_AUTHOR  BOOLEAN NOT NULL DEFAULT FALSE,
    ARTICLE_ID INT     NOT NULL,
    USER_ID    INT     NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (ARTICLE_ID) REFERENCES redakcny_system.ARTICLE (ID),
    FOREIGN KEY (USER_ID) REFERENCES redakcny_system.SYSTEM_USER (ID),
    UNIQUE (ARTICLE_ID, USER_ID)
);

CREATE TABLE IF NOT EXISTS redakcny_system.PUBLICATION_CONFIGURATION
(
    ID              SERIAL NOT NULL,
    DOMAIN          VARCHAR(30),
    PATH_TO_ARTICLE VARCHAR(70),
    BRANCH          VARCHAR(70),
    REPOSITORY_PATH VARCHAR(70),
    PRIVATE_TOKEN   VARCHAR(70),
    COMMIT_MESSAGE  VARCHAR(70),
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS redakcny_system.ARTICLE_COLLABORATION_SESSION
(
    ID            SERIAL          NOT NULL,
    TEXT          VARCHAR(100000) NOT NULL,
    CAN_USER_EDIT BOOLEAN         NOT NULL,
    ARTICLE_ID    INT             NOT NULL,
    USER_ID       INT             NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (ARTICLE_ID) REFERENCES redakcny_system.ARTICLE (ID),
    FOREIGN KEY (USER_ID) REFERENCES redakcny_system.SYSTEM_USER (ID),
    UNIQUE (ARTICLE_ID, USER_ID)
);

-- init ARTICLE_STATUS data
INSERT INTO redakcny_system.ARTICLE_STATUS(STATE)
VALUES ('WRITING');
INSERT INTO redakcny_system.ARTICLE_STATUS(STATE)
VALUES ('IN_REVIEW');
INSERT INTO redakcny_system.ARTICLE_STATUS(STATE)
VALUES ('APPROVED');
INSERT INTO redakcny_system.ARTICLE_STATUS(STATE)
VALUES ('ARCHIVED');

-- init PUBLICATION_CONFIGURATION data
INSERT INTO redakcny_system.PUBLICATION_CONFIGURATION(domain, path_to_article, branch, repository_path,
                                                      private_token, commit_message)
VALUES ('GIT_KPI_FEI_TUKE_SK', 'content/clanky/{year}/{month}/{slug}/index.md', 'main', '', '',
        'Publish article {articleName}');

-- init SYSTEM_USER data
INSERT INTO redakcny_system.SYSTEM_USER(username, first_name, last_name, email, auth_provider, role,
                                        is_administrator)
VALUES ('sergej.chodarev', 'Sergej', 'Chodarev', 'sergej.chodarev@tuke.sk', 'GITLAB', 'EDITOR',
        true),
       ('pd565js', 'Pavol', 'Dlugoš', 'pavol.dlugos@student.tuke.sk', 'GITLAB', 'AUTHOR', true);
