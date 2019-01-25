# --- !add_users.sql

# --- !Ups

INSERT INTO "user" (account, email, password, nickname, is_active) VALUES
  ('admin', 'fateud@qq.com', 'admin', 'admin', TRUE),
  ('test', 'chouunsoft@gmail.com', 'test', 'test', TRUE);

INSERT INTO "role" ("name") VALUES
  ('admin'),
  ('default');

INSERT INTO "user_role" ("user_id", "role_id") VALUES
  ((SELECT id FROM "user" WHERE "account" = 'admin'), (SELECT id FROM "role" WHERE "name" = 'admin')),
  ((SELECT id FROM "user" WHERE "account" = 'test'), (SELECT id FROM "role" WHERE "name" = 'default'));

# --- !Downs

DELETE FROM "user_role" ur
WHERE ur.user_id IN (
  SELECT id
  FROM "user" u
  WHERE u."account" IN ('admin', 'test')
) AND ur.role_id IN (
  SELECT id
  FROM "role" r
  WHERE r."name" IN ('admin', 'default')
);

DELETE FROM "role"
WHERE "name" IN ('admin', 'default');

DELETE FROM "user"
WHERE account IN ('admin', 'test');
