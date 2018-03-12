# --- !add_users.sql

# --- !Ups

INSERT INTO "auth_user" (account, email, password, nickname, is_active) VALUES
  ('admin', 'fateud@qq.com', 'admin', 'admin', TRUE),
  ('test', 'chouunsoft@gmail.com', 'test', 'test', TRUE);

# --- !Downs

DELETE FROM "auth_user"
WHERE account IN ('admin', 'test');
