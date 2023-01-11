INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO restaurant (name, address)
VALUES ('Dodo', 'Ленинградская 10'),
       ('Вкусно и точка', 'Полевая 20'),
       ('KFC', 'Московское ш. 200'),
       ('Вилка Ложка', 'Ленинградская 20');

INSERT INTO dish (name, price, restaurant_id)
VALUES ('Пицца Моцарелла', 400, 1),
       ('Сырники', 150, 1),
       ('Ролл с курицей', 200, 1),
       ('Гамбургер', 150, 2),
       ('Картошка', 100, 2),
       ('Биг мак', 300, 2),
       ('Боксмастер', 200, 3),
       ('Крылья', 250, 3);

INSERT INTO menu (menu_date, restaurant_id)
VALUES (CURRENT_DATE, 1),
       (dateadd(MONTH, -1, CURRENT_DATE), 1),
       (dateadd(MONTH, -2, CURRENT_DATE), 1),
       (CURRENT_DATE, 2),
       (dateadd(MONTH, -1, CURRENT_DATE), 3);

INSERT INTO dish_in_menu (menu_id, dish_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (4, 5),
       (4, 6),
       (5, 7),
       (5, 8);

INSERT INTO vote (date_vote, time_vote, user_id, restaurant_id)
VALUES (CURRENT_DATE, '00:00', 1, 1),
       (dateadd(MONTH, -1, CURRENT_DATE), '9:00', 1, 3),
       (CURRENT_DATE, '15:00', 2, 2),
       (dateadd(MONTH, -1, CURRENT_DATE), '13:00', 2, 3);

