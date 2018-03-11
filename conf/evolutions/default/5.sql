# --- !auth_user_role.sql

# --- !Ups

CREATE TABLE "auth_user_role" (
  "id"      SERIAL  NOT NULL PRIMARY KEY,
  "user_id" INTEGER NOT NULL,
  "role_id" INTEGER NOT NULL
);

CREATE UNIQUE INDEX "auth_user_role_user_id_role_id_uniq"
  ON "auth_user_role" ("user_id", "role_id");

ALTER TABLE "auth_user_role"
  ADD CONSTRAINT "auth_user_role_user_id_fk_auth_user_id" FOREIGN KEY ("user_id") REFERENCES "auth_user" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE "auth_user_role"
  ADD CONSTRAINT "auth_user_role_role_id_fk_auth_role_id" FOREIGN KEY ("role_id") REFERENCES "auth_role" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

# --- !Downs

ALTER TABLE "auth_user_role"
  DROP CONSTRAINT "auth_user_role_user_id_fk_auth_user_id";

ALTER TABLE "auth_user_role"
  DROP CONSTRAINT "auth_user_role_role_id_fk_auth_role_id";

DROP TABLE "auth_user_role";
