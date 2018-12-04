# --- !auth_role.sql

# --- !Ups

CREATE TABLE "auth_role" (
  "id"         SERIAL                                   NOT NULL PRIMARY KEY,
  "name"       VARCHAR(128)                             NOT NULL UNIQUE,
  "created_at" TIMESTAMP DEFAULT timezone('utc', now()) NOT NULL,
  "updated_at" TIMESTAMP DEFAULT timezone('utc', now()) NOT NULL,
  "version"    INTEGER DEFAULT 1                        NOT NULL
);

CREATE TRIGGER "auth_role_update_trigger"
  BEFORE UPDATE
  ON "auth_role"
  FOR EACH ROW EXECUTE PROCEDURE entity_update_func();

# --- !Downs

DROP TABLE "auth_role";
