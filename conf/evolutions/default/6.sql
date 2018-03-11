# --- !auth_user_permission.sql

# --- !Ups

CREATE TABLE "auth_user_permission" (
  "id"            SERIAL  NOT NULL PRIMARY KEY,
  "user_id"       INTEGER NOT NULL,
  "permission_id" INTEGER NOT NULL
);

CREATE UNIQUE INDEX "auth_user_permission_user_id_permission_id_uniq"
  ON "auth_user_permission" ("user_id", "permission_id");

ALTER TABLE "auth_user_permission"
  ADD CONSTRAINT "auth_user_permission_permission_id_fk_auth_permission_id" FOREIGN KEY ("permission_id") REFERENCES "auth_permission" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE "auth_user_permission"
  ADD CONSTRAINT "auth_user_permission_user_id_fk_auth_user_id" FOREIGN KEY ("user_id") REFERENCES "auth_user" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

# --- !Downs

ALTER TABLE "auth_user_permission"
  DROP CONSTRAINT "auth_user_permission_permission_id_fk_auth_permission_id";

ALTER TABLE "auth_user_permission"
  DROP CONSTRAINT "auth_user_permission_user_id_fk_auth_user_id";

DROP TABLE "auth_user_permission";