# --- !user_permission.sql

# --- !Ups

CREATE TABLE "user_permission" (
  "id"            SERIAL  NOT NULL PRIMARY KEY,
  "user_id"       INTEGER NOT NULL,
  "permission_id" INTEGER NOT NULL
);

CREATE UNIQUE INDEX "user_permission_user_id_permission_id_uniq"
  ON "user_permission" ("user_id", "permission_id");

ALTER TABLE "user_permission"
  ADD CONSTRAINT "user_permission_permission_id_fk_permission_id"
    FOREIGN KEY ("permission_id")
    REFERENCES "permission" ("id")
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

ALTER TABLE "user_permission"
  ADD CONSTRAINT "user_permission_user_id_fk_user_id"
    FOREIGN KEY ("user_id")
    REFERENCES "user" ("id")
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

# --- !Downs

ALTER TABLE "user_permission"
  DROP CONSTRAINT "user_permission_permission_id_fk_permission_id";

ALTER TABLE "user_permission"
  DROP CONSTRAINT "user_permission_user_id_fk_user_id";

DROP TABLE "user_permission";