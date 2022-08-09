package ru.yanddex.practicum.filmorate.dao;

import ru.yanddex.practicum.filmorate.controller.exception.IncorrectDateValidationException;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectIdValidationException;
import ru.yanddex.practicum.filmorate.model.Film;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;

import java.util.List;
import java.util.Optional;

public interface FilmStorageDao {

    public List<Film> getAll();

    public Optional<Film> create(Film film) throws IncorrectDateValidationException, IncorrectIdValidationException;

    public Optional<Film> update(Film film) throws IncorrectIdValidationException,
            IncorrectDateValidationException;

    public Optional<Film> getFilmById(Integer id) throws IncorrectIdToGetException;

    public boolean deleteFilmById(Integer id) throws IncorrectIdToGetException;

    public void addLike(Integer userId, Integer filmId);

    public boolean deleteLike(Integer userId, Integer filmId);

    public List<Film> getBestFilms(Integer limit);

}
