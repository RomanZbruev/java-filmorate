package ru.yanddex.practicum.filmorate.dao;

import org.springframework.jdbc.support.KeyHolder;
import ru.yanddex.practicum.filmorate.model.Film;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;

import java.util.Optional;

public interface FilmGenreStorageDao {

    Optional<Film> addFilmGenres(Film film, Optional<Film> filmOptional) throws IncorrectIdToGetException;

    public void deleteByFilmId(Integer id);
}
