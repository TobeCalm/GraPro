create table GITHUBUSER
(
    id           int auto_increment,
    account_id   VARCHAR(100),
    name         VARCHAR(50),
    token        VARCHAR(36),
    gmt_create   BIGINT,
    gmt_modified BIGINT,
    constraint USER_PK
        primary key (id)
);