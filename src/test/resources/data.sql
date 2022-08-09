--Рейтинги
INSERT INTO RATINGS (RATING_NAME) VALUES ('G');
INSERT INTO RATINGS (RATING_NAME) VALUES ('PG');
INSERT INTO RATINGS (RATING_NAME) VALUES ('PG-13');
INSERT INTO RATINGS (RATING_NAME) VALUES ('R');
INSERT INTO RATINGS (RATING_NAME) VALUES ('NC-17');

--Жанры
INSERT INTO GENRES (GENRE_NAME) VALUES ('Комедия');
INSERT INTO GENRES (GENRE_NAME) VALUES ('Драма');
INSERT INTO GENRES (GENRE_NAME) VALUES ('Мультфильм');
INSERT INTO GENRES (GENRE_NAME) VALUES ('Триллер');
INSERT INTO GENRES (GENRE_NAME) VALUES ('Документальный');
INSERT INTO GENRES (GENRE_NAME) VALUES ('Боевик');

-- Пользователь
INSERT INTO USERS (USER_EMAIL, USER_LOGIN, USER_NAME, USER_BIRTHDAY)
VALUES ('mail@email.com', 'login', 'name', '2010-10-10');

-- Фильмы
INSERT INTO FILMS (FILM_NAME, FILM_DESCRIPTION, FILM_RELEASE_DATE,FILM_DURATION, FILM_RATING_ID)
VALUES ('name', 'description', '2010-10-10', 100, 1)