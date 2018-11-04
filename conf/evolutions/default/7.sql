# --- !auth_role_permission.sql

# --- !Ups

CREATE TABLE "auth_role_permission" (
  "id"            SERIAL  NOT NULL PRIMARY KEY,
  "role_id"       INTEGER NOT NULL,
  "permission_id" INTEGER NOT NULL
);

CREATE UNIQUE INDEX "auth_role_permission_role_id_permission_id_uniq"
  ON "auth_role_permission" ("role_id", "permission_id");

ALTER TABLE "auth_role_permission"
  ADD CONSTRAINT "auth_role_permission_permission_id_fk_auth_permission_id"
    FOREIGN KEY ("permission_id")
    REFERENCES "auth_permission" ("id")
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

ALTER TABLE "auth_role_permission"
  ADD CONSTRAINT "auth_role_permission_role_id_fk_auth_role_id"
    FOREIGN KEY ("role_id")
    REFERENCES "auth_role" ("id")
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

# --- !Downs

ALTER TABLE "auth_role_permission"
  DROP CONSTRAINT "auth_role_permission_permission_id_fk_auth_permission_id";

ALTER TABLE "auth_role_permission"
  DROP CONSTRAINT "auth_role_permission_role_id_fk_auth_role_id";

DROP TABLE "auth_role_permission";