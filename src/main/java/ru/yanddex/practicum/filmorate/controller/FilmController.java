package ru.yanddex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectDateValidationException;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectIdValidationException;
import ru.yanddex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private final static LocalDate filmBirthday = LocalDate.of(1895,12,28);
    private final HashMap<Integer, Film> filmStorage = new HashMap<>();
    private int id = 0;

    @GetMapping("/films")
    public List<Film> giveAll() {
        List<Film> films = new ArrayList<>();
        for (Integer id : filmStorage.keySet()) {
            films.add(filmStorage.get(id));
        }
        log.info("Получен запрос на просмотр списка фильмов");
        return films;
    }

    @PostMapping("/films")
    public Film create(@RequestBody @Valid Film film) throws IncorrectDateValidationException {
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

    @PutMapping("/films")
    public Film update(@RequestBody @Valid Film film) throws IncorrectDateValidationException {

            if (film.getReleaseDate().isAfter(filmBirthday)) {
                if (filmStorage.containsKey(film.getId())) {
                    filmStorage.put(id, film);
                    log.info("Фильм обновлен: {}", film);
                    return film;
                } else {
                    IncorrectDateValidationException exception =
                            new IncorrectIdValidationException("Такого фильма нет в базе!");
                    log.warn(exception.getMessage());
                    throw exception;
                }
            }
            else {
                IncorrectDateValidationException exception =
                        new IncorrectDateValidationException("Введена некорректная дата релиза фильма");
                log.warn(exception.getMessage());
                throw exception;
            }
        }

    }

