-- !Ups

CREATE TABLE "user"
(
  id        SERIAL PRIMARY KEY,

  account   VARCHAR(64)                                  NOT NULL UNIQUE,
  email     VARCHAR(254)                                 NOT NULL UNIQUE,
  password  VARCHAR(256)                                 NOT NULL,

  nickname  VARCHAR(64)                                  NOT NULL,

  is_active BOOLEAN   DEFAULT TRUE                       NOT NULL,
  signin_at TIMESTAMP,
  signup_at TIMESTAMP DEFAULT (now() at time zone 'utc') NOT NULL
);

CREATE INDEX user_nickname_idx
  ON "user" (nickname);

-- !Downs

DROP TABLE "user";
