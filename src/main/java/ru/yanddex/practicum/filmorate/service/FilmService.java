package ru.yanddex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectDateValidationException;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectIdValidationException;
import ru.yanddex.practicum.filmorate.model.Film;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;
import ru.yanddex.practicum.filmorate.dao.FilmStorageDao;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilmService {

    private final FilmStorageDao filmStorage;

    @Autowired
    public FilmService(FilmStorageDao filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getFilmById(Integer id) throws IncorrectIdToGetException {
        if (filmStorage.getFilmById(id).isPresent()) {
            return filmStorage.getFilmById(id).get();
        } else {
            throw new IncorrectIdToGetException("Фильма с таким айди не существует");
        }
    }

    public Film create(Film film) throws IncorrectDateValidationException,
            IncorrectIdValidationException, IncorrectIdToGetException {
        Optional<Film> filmOptional = filmStorage.create(film);
        if (filmOptional.isPresent()) {
            return filmOptional.get();
        } else {
            throw new ValidationException("Фильм не создан");
        }
    }

    public Film update(Film film) throws IncorrectIdValidationException, IncorrectDateValidationException {
        Optional<Film> filmOptional = filmStorage.update(film);
        if (filmOptional.isPresent()) {
            return filmOptional.get();
        } else {
            throw new IncorrectIdValidationException("Фильма с таким айди не существует");
        }
    }



    public List<Film> getBestFilms(Integer count) {
        try {
            return filmStorage.getBestFilms(count);
        } catch (DataIntegrityViolationException e) {
            log.warn(e.getMessage());
            throw new RuntimeException("Ошибка получения списка фильмов");
        }
    }


}
