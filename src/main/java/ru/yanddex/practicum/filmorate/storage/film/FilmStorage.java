package ru.yanddex.practicum.filmorate.storage.film;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectDateValidationException;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectIdValidationException;
import ru.yanddex.practicum.filmorate.model.Film;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;

import javax.validation.Valid;
import java.util.List;

public interface FilmStorage {

    public List<Film> getAll();

    public Film create(Film film) throws IncorrectDateValidationException;

    public Film update(Film film) throws IncorrectIdValidationException,
            IncorrectDateValidationException;

    public Film getFilmById(Integer id) throws IncorrectIdToGetException;

    public void clearStorage();
}
