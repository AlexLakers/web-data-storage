CREATE DATABASE files_repository;

CREATE SCHEMA files_storage;

CREATE TABLE role(
                     id BIGSERIAL PRIMARY KEY ,
                     name VARCHAR(128) NOT NULL UNIQUE
);

INSERT INTO role(name)
VALUES ('MODERATOR'),('USER'),('ADMIN');


CREATE TABLE files_storage.account(
                                      id BIGSERIAL PRIMARY KEY,
                                      first_name VARCHAR(128) NOT NULL,
                                      last_name VARCHAR(128) NOT NULL,
                                      birth_date DATE NOT NULL ,
                                      login VARCHAR(128) NOT NULL UNIQUE,
                                      password VARCHAR(128) NOT NULL,
                                      role_id BIGINT NOT NULL REFERENCES role(id) ON UPDATE CASCADE ON DELETE CASCADE ,
                                      folder VARCHAR(128) NOT NULL UNIQUE
);


CREATE TABLE files_storage.file_info(
                                        id BIGSERIAL PRIMARY KEY ,
                                        name VARCHAR(128) NOT NULL,
                                        upload_date DATE not null,
                                        size BIGINT NOT NULL ,
                                        account_id BIGINT NOT NULL REFERENCES account(id) ON UPDATE CASCADE ON DELETE CASCADE
);