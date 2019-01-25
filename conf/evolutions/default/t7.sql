# --- !role_permission.sql

# --- !Ups

CREATE TABLE "role_permission" (
  "id"            SERIAL  NOT NULL PRIMARY KEY,
  "role_id"       INTEGER NOT NULL,
  "permission_id" INTEGER NOT NULL
);

CREATE UNIQUE INDEX "role_permission_role_id_permission_id_uniq"
  ON "role_permission" ("role_id", "permission_id");

ALTER TABLE "role_permission"
  ADD CONSTRAINT "role_permission_permission_id_fk_permission_id"
    FOREIGN KEY ("permission_id")
    REFERENCES "permission" ("id")
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

ALTER TABLE "role_permission"
  ADD CONSTRAINT "role_permission_role_id_fk_role_id"
    FOREIGN KEY ("role_id")
    REFERENCES "role" ("id")
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

# --- !Downs

ALTER TABLE "role_permission"
  DROP CONSTRAINT "role_permission_permission_id_fk_permission_id";

ALTER TABLE "role_permission"
  DROP CONSTRAINT "role_permission_role_id_fk_role_id";

DROP TABLE "role_permission";