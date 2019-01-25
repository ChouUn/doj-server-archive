-- !user.sql

-- !Ups

CREATE TABLE "user"
(
  "id"        SERIAL                                       NOT NULL,
  "account"   VARCHAR(64)                                  NOT NULL UNIQUE,
  "email"     VARCHAR(254)                                 NOT NULL UNIQUE,
  "password"  VARCHAR(256)                                 NOT NULL,
  "name"      VARCHAR(64)                                  NOT NULL,
  "is_active" BOOLEAN   DEFAULT TRUE                       NOT NULL,
  "signin_at" TIMESTAMP DEFAULT NULL,
  "signup_at" TIMESTAMP DEFAULT (now() at time zone 'utc') NOT NULL,
  PRIMARY KEY ("id")
);

CREATE INDEX "user_name_like"
  ON "user" ("name");

-- !Downs

DROP TABLE "user";
