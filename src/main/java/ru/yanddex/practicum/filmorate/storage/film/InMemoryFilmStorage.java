package ru.yanddex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectDateValidationException;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectIdValidationException;
import ru.yanddex.practicum.filmorate.model.Film;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{

    private final static LocalDate filmBirthday = LocalDate.of(1895, 12, 28);
    private final HashMap<Integer, Film> filmStorage = new HashMap<>();
    private int id = 0;

    @Override
    public List<Film> getAll() {
        List<Film> films = new ArrayList<>();
        for (Integer id : filmStorage.keySet()) {
            films.add(filmStorage.get(id));
        }
        log.info("Получен запрос на просмотр списка фильмов");
        return films;
    }

    @Override
    public Film create(Film film) throws IncorrectDateValidationException {
        if (film.getReleaseDate().isAfter(filmBirthday)) {
            id++;
            film.setId(id);
            filmStorage.put(id, film);
            log.info("Фильм успешно добавлен: {}", film);
            return film;
        } else {
            IncorrectDateValidationException exception =
                    new IncorrectDateValidationException("Введена некорректная дата релиза фильма");
            log.warn(exception.getMessage());
            throw exception;
        }
    }

    @Override
    public Film update(Film film) throws IncorrectIdValidationException, IncorrectDateValidationException {
        if (film.getReleaseDate().isAfter(filmBirthday)) {
            if (filmStorage.containsKey(film.getId())) {
                filmStorage.put(id, film);
                log.info("Фильм обновлен: {}", film);
                return film;
            } else {
                IncorrectIdValidationException exception =
                        new IncorrectIdValidationException("Такого фильма нет в базе!");
                log.warn(exception.getMessage());
                throw exception;
            }
        } else {
            IncorrectDateValidationException exception =
                    new IncorrectDateValidationException("Введена некорректная дата релиза фильма");
            log.warn(exception.getMessage());
            throw exception;
        }
    }

    @Override
    public Film getFilmById(Integer id) throws IncorrectIdToGetException {
        if(filmStorage.containsKey(id)){
            return filmStorage.get(id);
        }
        else {
            IncorrectIdToGetException exception = new IncorrectIdToGetException("Такого фильма нет в базе");
            log.warn(exception.getMessage());
            throw exception;
        }
    }

    @Override
    public void clearStorage(){
        filmStorage.clear();
        id=0;
    }
}
