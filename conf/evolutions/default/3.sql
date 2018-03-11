# --- !auth_role.sql

# --- !Ups

CREATE TABLE "auth_role" (
  "id"         SERIAL                                NOT NULL PRIMARY KEY,
  "name"       VARCHAR(128)                          NOT NULL UNIQUE,
  "created_at" TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  "updated_at" TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  "version"    INTEGER DEFAULT 1                     NOT NULL
);

CREATE INDEX "auth_role_name_like"
  ON "auth_role" ("name");

CREATE TRIGGER "auth_role_update_trigger"
  BEFORE UPDATE
  ON "auth_role"
  FOR EACH ROW EXECUTE PROCEDURE entity_update_func();

# --- !Downs

DROP TABLE "auth_role";
