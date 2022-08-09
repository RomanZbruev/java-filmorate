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
        return filmService.getAll();
    }

    @PostMapping("/films")
    public Film create(@RequestBody @Valid Film film) throws IncorrectDateValidationException, IncorrectIdValidationException {
        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film update(@RequestBody @Valid Film film) throws IncorrectIdValidationException,
            IncorrectDateValidationException {
        return filmService.update(film);
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable("id") Integer id) throws IncorrectIdToGetException {
        return filmService.getFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId)
            throws IncorrectIdToGetException {
        filmService.addLike(userId,
                filmId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId)
            throws IncorrectIdToGetException{
        filmService.deleteLike(userId,
                filmId);
    }

    @GetMapping("/films/popular")
    public List<Film> getBestFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getBestFilms(count);
    }


}

