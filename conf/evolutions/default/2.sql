# --- !auth_user.sql

# --- !Ups

CREATE TABLE auth_user
(
  "id"         SERIAL                                   NOT NULL PRIMARY KEY,
  "account"    VARCHAR(128)                             NOT NULL UNIQUE,
  "email"      VARCHAR(254)                             NOT NULL UNIQUE,
  "password"   VARCHAR(256)                             NOT NULL,
  "nickname"   VARCHAR(64)                              NOT NULL,
  "last_login" TIMESTAMP DEFAULT NULL,
  "is_active"  BOOLEAN DEFAULT TRUE                     NOT NULL,
  "created_at" TIMESTAMP DEFAULT timezone('utc', now()) NOT NULL,
  "updated_at" TIMESTAMP DEFAULT timezone('utc', now()) NOT NULL,
  "version"    INTEGER DEFAULT 1                        NOT NULL
);

CREATE INDEX "auth_user_nickname_like"
  ON "auth_user" ("nickname");

CREATE TRIGGER "auth_user_update_trigger"
  BEFORE UPDATE
  ON "auth_user"
  FOR EACH ROW EXECUTE PROCEDURE entity_update_func();

# --- !Downs

DROP TABLE "auth_user";
