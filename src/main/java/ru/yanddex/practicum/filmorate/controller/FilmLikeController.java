package ru.yanddex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectDateValidationException;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectIdValidationException;
import ru.yanddex.practicum.filmorate.model.Film;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import ru.yanddex.practicum.filmorate.service.FilmService;
import ru.yanddex.practicum.filmorate.service.LikeService;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;

import java.util.List;

@RestController
@Slf4j
public class FilmLikeController {

    private final FilmService filmService;
    private final LikeService likeService;

    @Autowired
    public FilmLikeController(FilmService filmService, LikeService likeService) {
        this.filmService = filmService;
        this.likeService = likeService;
    }

    @GetMapping("/films")
    public List<Film> getAll() {
        return filmService.getAll();
    }

    @PostMapping("/films")
    public Film create(@RequestBody @Valid Film film) throws IncorrectDateValidationException,
            IncorrectIdValidationException, IncorrectIdToGetException {
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
        likeService.addLike(userId,
                filmId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId)
            throws IncorrectIdToGetException {
        likeService.deleteLike(userId,
                filmId);
    }

    @GetMapping("/films/popular")
    public List<Film> getBestFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getBestFilms(count);
    }


}

