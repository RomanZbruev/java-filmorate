# java-filmorate
Template repository for Filmorate project.

## Разработка архитектуры базы данных для приложения Filmorate

Таблицы Film и User имеют связь типа "многие-ко-многим" через связующую таблицу Likes, которая имеет составной первичный ключ из film_id и user_id

Таблица Film имеет следующие связи: 
- Связь типа "многие-ко-многим" с таблицей Genre. Реализовано посредством связующей таблицы FilmGenre, которая имеет составной первичный ключ из film_id и genre_id.
FilmGenre, соответсвенно, связана с Genre и Film как "многие-к одному".
- Связь "один-к-одному" с таблицой Rating. 

Таблица User имеет следующие связи:
- Связь типа "многие-ко-многим" сама на себя - через таблицу UserFriends. Связующая таблица UserFriends имеет составной первичный ключ из user_id и friend_id и содержит информацию о статусе дружбы пользователей.

**ЕR-диаграмма:**
![ER-диаграмма](https://github.com/RomanZbruev/java-filmorate/blob/main/drawSQL-export-2022-07-24_13_08.png)



## Примеры запросов 

### Получение всех фильмов
```
SELECT *

FROM film;
```

### Получение всех пользователей 

```
SELECT *

FROM user;
```


### Получение N лучших фильмов
```
SELECT f.*,
       r.likes
FROM film AS f
JOIN( SELECT film_id
      COUNT(user_id) AS likes
      GROUP BY film_id)
        AS r ON f.film_id = r.film_id
ORDER BY r.likes DESC 
LIMIT N;
```


### Получение списка общих друзей двух пользователей с id1 и id2
```
SELECT u.*
FROM u
```


