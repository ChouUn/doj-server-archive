# --- !user_role.sql

# --- !Ups

CREATE TABLE "user_role" (
  "id"      SERIAL  NOT NULL PRIMARY KEY,
  "user_id" INTEGER NOT NULL,
  "role_id" INTEGER NOT NULL
);

CREATE UNIQUE INDEX "user_role_user_id_role_id_uniq"
  ON "user_role" ("user_id", "role_id");

ALTER TABLE "user_role"
  ADD CONSTRAINT "user_role_user_id_fk_user_id"
    FOREIGN KEY ("user_id") REFERENCES "user" ("id")
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

ALTER TABLE "user_role"
  ADD CONSTRAINT "user_role_role_id_fk_role_id"
    FOREIGN KEY ("role_id")
    REFERENCES "role" ("id")
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

# --- !Downs

ALTER TABLE "user_role"
  DROP CONSTRAINT "user_role_user_id_fk_user_id";

ALTER TABLE "user_role"
  DROP CONSTRAINT "user_role_role_id_fk_role_id";

DROP TABLE "user_role";
