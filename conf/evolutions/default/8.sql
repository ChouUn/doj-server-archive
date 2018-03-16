# --- !add_users.sql

# --- !Ups

INSERT INTO "auth_user" (account, email, password, nickname, is_active) VALUES
  ('admin', 'fateud@qq.com', 'admin', 'admin', TRUE),
  ('test', 'chouunsoft@gmail.com', 'test', 'test', TRUE);

INSERT INTO "auth_role" ("name") VALUES
  ('admin'),
  ('default');

INSERT INTO "auth_user_role" ("user_id", "role_id") VALUES
  ((SELECT id FROM "auth_user" WHERE "account" = 'admin'), (SELECT id FROM "auth_role" WHERE "name" = 'admin')),
  ((SELECT id FROM "auth_user" WHERE "account" = 'test'), (SELECT id FROM "auth_role" WHERE "name" = 'default'));

# --- !Downs

DELETE FROM "auth_user_role" ur
WHERE ur.user_id IN (
  SELECT id
  FROM "auth_user" u
  WHERE u."account" IN ('admin', 'test')
) AND ur.role_id IN (
  SELECT id
  FROM "auth_role" r
  WHERE r."name" IN ('admin', 'default')
);

DELETE FROM "auth_role"
WHERE "name" IN ('admin', 'default');

DELETE FROM "auth_user"
WHERE account IN ('admin', 'test');
