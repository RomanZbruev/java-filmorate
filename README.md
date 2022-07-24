# java-filmorate
Template repository for Filmorate project.

## Разработка архитектуры базы данных для приложения Filmorate

Таблицы Films и Users имеют связь типа "многие-ко-многим" через связующую таблицу Likes, которая имеет составной первичный ключ из film_id и user_id

Таблица Films имеет следующие связи: 
- Связь типа "многие-ко-многим" с таблицей Genres. Реализовано посредством связующей таблицы FilmGenres, которая имеет составной первичный ключ из film_id и genre_id.
FilmGenres, соответсвенно, связана с Genres и Films как "многие-к одному".
- Связь "один-к-одному" с таблицой Ratings. 

Таблица Users имеет следующие связи:
- Связь типа "многие-ко-многим" сама на себя - через таблицу UserFriends. Связующая таблица UserFriends имеет составной первичный ключ из user_id и friend_id и содержит информацию о статусе дружбы пользователей.

**ЕR-диаграмма:**
![ER-диаграмма](https://github.com/RomanZbruev/java-filmorate/blob/ER-diagram/drawSQL-export-2022-07-24_14_24.png)



## Примеры запросов 

### Получение всех фильмов
```
SELECT *

FROM films;
```

### Получение всех пользователей 

```
SELECT *

FROM users;
```


### Получение N лучших фильмов
```
SELECT f.*,
       l.likes
FROM films AS f
INNER JOIN(SELECT film_id,
      COUNT(user_id) AS likes
      FROM likes
      GROUP BY film_id)
        AS l ON f.film_id = l.film_id
ORDER BY l.likes DESC 
LIMIT N;
```


### Получение списка общих друзей двух пользователей с id1 и id2
```
SELECT ufs.*
FROM userfriends AS ufs
INNER JOIN users AS u ON ufs.friend_id = u.user_id
WHERE ufs.user_id = id1 AND ufs.status = 'true'
UNION
SELECT ufs.*
FROM userfriends AS ufs
INNER JOIN users AS u ON ufs.friend_id = u.user_id
WHERE ufs.user_id = id2 AND ufs.status = 'true'

```


