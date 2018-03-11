# --- !auth_role_permission.sql

# --- !Ups

create table "auth_role_permission" ("id" SERIAL NOT NULL PRIMARY KEY,"role_id" INTEGER NOT NULL,"permission_id" INTEGER NOT NULL);

create unique index "auth_role_permission_role_id_permission_id_uniq" on "auth_role_permission" ("role_id","permission_id");

alter table "auth_role_permission" add constraint "auth_role_permission_permission_id_fk_auth_permission_id" foreign key("permission_id") references "auth_permission"("id") on update NO ACTION on delete NO ACTION;

alter table "auth_role_permission" add constraint "auth_role_permission_role_id_fk_auth_role_id" foreign key("role_id") references "auth_role"("id") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "auth_role_permission" drop constraint "auth_role_permission_permission_id_fk_auth_permission_id";

alter table "auth_role_permission" drop constraint "auth_role_permission_role_id_fk_auth_role_id";

drop table "auth_role_permission";