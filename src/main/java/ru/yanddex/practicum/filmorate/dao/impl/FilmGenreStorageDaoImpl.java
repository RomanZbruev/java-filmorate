package ru.yanddex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yanddex.practicum.filmorate.dao.FilmGenreStorageDao;
import ru.yanddex.practicum.filmorate.dao.GenreStorageDao;
import ru.yanddex.practicum.filmorate.model.Film;
import ru.yanddex.practicum.filmorate.model.Genre;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;


import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FilmGenreStorageDaoImpl implements FilmGenreStorageDao {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorageDao genreStorage;

    public FilmGenreStorageDaoImpl(JdbcTemplate jdbcTemplate, GenreStorageDao genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
    }

    @Override
    public Optional<Film> addFilmGenres(Film film, Optional<Film> filmOptional) throws IncorrectIdToGetException{
        filmOptional.ifPresent(film1 -> {
            if (!film1.getGenres().isEmpty()) {
                deleteByFilmId(film.getId());
            }
        });
        film.getGenres().forEach(genre -> {
            try {
                String sqlQuery = "INSERT INTO FILMGENRES (fg_film_id, fg_genre_id)" +
                        "VALUES (?,?)";
                jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
            } catch (DuplicateKeyException e) {
                log.warn(e.getMessage());
            }
        });

        if (film.getGenres() != null && film.getGenres().size() != 0) {
            filmOptional.ifPresent(film1 -> {
                film1.setGenres(genreStorage.getGenresByIds(film.getGenres().stream()
                        .mapToInt(Genre::getId).boxed().collect(Collectors.toList())));
            });
        } else {
            filmOptional.ifPresent(film1 -> film1.setGenres(new ArrayList<>()));
        }
        return filmOptional;
    }

    @Override
    public void deleteByFilmId(Integer id) {
        String sqlQuery = "DELETE  FROM  FILMGENRES\n" +
                "WHERE FG_FILM_ID = ?;";
        jdbcTemplate.update(sqlQuery, id);
    }
}

