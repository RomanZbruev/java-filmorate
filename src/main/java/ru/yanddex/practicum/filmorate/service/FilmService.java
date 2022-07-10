package ru.yanddex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yanddex.practicum.filmorate.model.Film;
import ru.yanddex.practicum.filmorate.model.User;
import ru.yanddex.practicum.filmorate.service.exception.NotWithLikeException;
import ru.yanddex.practicum.filmorate.storage.film.FilmStorage;
import ru.yanddex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage){
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(User user, Film film){
        film.getLikes().add(user.getId());
        log.info("Лайк успешно добавлен");
    }

    public void deleteLike(User user, Film film) throws NotWithLikeException {
        if(film.getLikes().contains(user.getId())){
            film.getLikes().remove(user.getId());
            log.info("Лайк успешно удален");
        }
        else {
            NotWithLikeException exception = new NotWithLikeException("Этот пользователь не оставлял лайк для фильма");
            log.warn(exception.getMessage());
            throw exception;
        }
    }

    public List<Film> getBestFilms(Integer count){
        log.info(String.format("Показан  список %s лучших фильмов",count));
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt(f ->-1*f.getLikes().size())) // сортируем в порядке убывания
                .limit(count) // 10 лучших фильмов
                .collect(Collectors.toList());
    }


    public FilmStorage getFilmStorage() {
        return filmStorage;
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

}
