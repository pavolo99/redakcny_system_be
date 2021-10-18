-- V1.0__initial_schema.sql
-- CREATE SEQUENCE HIBERNATE_SEQUENCE START 4;

CREATE TABLE IF NOT EXISTS ARTICLE_STATUS
(
    ID    SERIAL      NOT NULL,
    STATE VARCHAR(50) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS ARTICLE
(
    ID                   SERIAL         NOT NULL,
    NAME                 VARCHAR(50)    NOT NULL,
    TEXT                 VARCHAR(10000) NOT NULL,
    KEY_WORDS            VARCHAR(100)   NOT NULL,
    ABSTRACT             VARCHAR(200)   NOT NULL,
    PUBLIC_FILE_NAME     VARCHAR(50)    NOT NULL,
    PUBLICATION_DECISION VARCHAR(50)    NOT NULL,
    REVIEW_NUMBER        INT            NOT NULL,
    ARTICLE_STATUS_ID    INT            NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (ARTICLE_STATUS_ID) REFERENCES ARTICLE_STATUS (ID)
);

CREATE TABLE IF NOT EXISTS IMAGE
(
    ID            SERIAL       NOT NULL,
    NAME          VARCHAR(200) NOT NULL,
    IMAGE_CONTENT BYTEA        NOT NULL,
    ARTICLE_ID    INT          NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (ARTICLE_ID) REFERENCES ARTICLE (ID)
);

CREATE TABLE IF NOT EXISTS COMMENT
(
    ID         SERIAL       NOT NULL,
    RESOLVED   BOOLEAN      NOT NULL,
    TEXT       VARCHAR(200) NOT NULL,
    ARTICLE_ID INT          NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (ARTICLE_ID) REFERENCES ARTICLE (ID)
);

CREATE TABLE IF NOT EXISTS COMMENT_REPLY
(
    ID         SERIAL       NOT NULL,
    TEXT       VARCHAR(200) NOT NULL,
    COMMENT_ID INT          NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (COMMENT_ID) REFERENCES COMMENT (ID)
);

CREATE TABLE IF NOT EXISTS VERSION
(
    ID         SERIAL         NOT NULL,
    TEXT       VARCHAR(10000) NOT NULL,
    ORDER_BY   INT            NOT NULL,
    ARTICLE_ID INT            NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (ARTICLE_ID) REFERENCES ARTICLE (ID)
);

-- init ARTICLE_STATUS data

INSERT INTO public.ARTICLE_STATUS(STATE)
VALUES ('WRITING');
INSERT INTO public.ARTICLE_STATUS(STATE)
VALUES ('IN_REVIEW');
INSERT INTO public.ARTICLE_STATUS(STATE)
VALUES ('APPROVED');
INSERT INTO public.ARTICLE_STATUS(STATE)
VALUES ('ARCHIVED');
