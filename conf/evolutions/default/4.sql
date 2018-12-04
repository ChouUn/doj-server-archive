# --- !auth_permission.sql

# --- !Ups

CREATE TABLE "auth_permission" (
  "id"         SERIAL                                   NOT NULL PRIMARY KEY,
  "entity"     VARCHAR(128)                             NOT NULL,
  "operation"  VARCHAR(128)                             NOT NULL,
  "created_at" TIMESTAMP DEFAULT timezone('utc', now()) NOT NULL,
  "updated_at" TIMESTAMP DEFAULT timezone('utc', now()) NOT NULL,
  "version"    INTEGER DEFAULT 1                        NOT NULL
);

CREATE INDEX "auth_permission_entity_idx"
  ON "auth_permission" ("entity");

CREATE UNIQUE INDEX "auth_permission_entity_operation_uniq"
  ON "auth_permission" ("entity", "operation");

CREATE TRIGGER "auth_permission_update_trigger"
  BEFORE UPDATE
  ON "auth_permission"
  FOR EACH ROW EXECUTE PROCEDURE entity_update_func();

# --- !Downs

DROP TABLE "auth_permission";
