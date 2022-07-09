package ru.yanddex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectDateValidationException;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectIdValidationException;
import ru.yanddex.practicum.filmorate.model.Film;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import ru.yanddex.practicum.filmorate.service.FilmService;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;
import ru.yanddex.practicum.filmorate.service.exception.NotWithLikeException;

import java.util.List;

@RestController
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> getAll() {
        return null;
    }

    @PostMapping("/films")
    public Film create(@RequestBody @Valid Film film) throws IncorrectDateValidationException {
        return filmService.getFilmStorage().create(film);
    }

    @PutMapping("/films")
    public Film update(@RequestBody @Valid Film film) throws IncorrectIdValidationException,
            IncorrectDateValidationException {
        return filmService.getFilmStorage().update(film);
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable("id") Integer id) throws IncorrectIdToGetException {
        return filmService.getFilmStorage().getFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId)
            throws IncorrectIdToGetException {
        filmService.addLike(filmService.getUserStorage().getUserById(userId),
                filmService.getFilmStorage().getFilmById(filmId));
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId)
            throws IncorrectIdToGetException, NotWithLikeException {
        filmService.deleteLike(filmService.getUserStorage().getUserById(userId),
                filmService.getFilmStorage().getFilmById(filmId));
    }

    @GetMapping("/films/popular")
    public List<Film> getTenBestFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getTenBestFilms(count);
    }
}

