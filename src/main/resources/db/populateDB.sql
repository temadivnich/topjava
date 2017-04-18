DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password');

INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals(description, datetime, calories, userid) VALUES
  ('Завтрак', TIMESTAMP '2015-05-30 10:00', 500, 100000),
  ('Обед', TIMESTAMP '2015-05-30 13:00', 1000, 100000),
  ('Ужин', TIMESTAMP '2015-05-30 20:00', 500, 100000),
  ('Завтрак', TIMESTAMP '2015-05-31 10:00', 1000, 100000),
  ('Обед', TIMESTAMP '2015-05-31 13:00', 500, 100000),
  ('Ужин', TIMESTAMP '2015-05-31 20:00', 510, 100000),
  ('Админ ланч', TIMESTAMP '2015-06-01 14:00', 510, 100001),
  ('Админ ужин', TIMESTAMP '2015-06-01 21:00', 1500, 100001);