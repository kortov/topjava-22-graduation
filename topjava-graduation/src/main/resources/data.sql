INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);
-- todo поменять время на now() и другие данные

INSERT INTO restaurant (name, address)
VALUES ('Токио City', 'Просвещения 72'),
       ('BAHROMA', 'Просвещения 48'),
       ('Бургер Кинг', 'Энгельса 154'),
       ('Додо Пицца', 'Художников 26');

INSERT INTO vote (time_vote, date_vote, user_id, rest_id)
VALUES ('00:00', CURRENT_DATE, 1, 1),
       ('10:00', '2020-01-06', 1, 3),
       ('13:00', CURRENT_DATE, 2, 2),
       ('13:00', '2020-01-06', 2, 3);

INSERT INTO dish (name, price, rest_id)
VALUES ('Мисо суп', 330, 1),
       ('Ролл Филадельфия', 1000, 1),
       ('Пуэр', 500, 1),
       ('Домашний супчик', 400, 2),
       ('Плов узбекский', 250, 2),
       ('Бургер из говядины', 200, 2),
       ('Гриль Кинг', 350, 3),
       ('Кола', 100, 3);

INSERT INTO menu (menu_date, rest_id)
VALUES (CURRENT_DATE, 1),
       ('2020-01-31', 1),
       ('2020-01-29', 1),
       (CURRENT_DATE, 2),
       ('2020-01-31', 3);

INSERT INTO dish_in_menu (menu_id, dish_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (4, 5),
       (4, 6),
       (5, 7),
       (5, 8);