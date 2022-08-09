package ru.yanddex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectDateValidationException;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectIdValidationException;
import ru.yanddex.practicum.filmorate.dao.FilmStorageDao;
import ru.yanddex.practicum.filmorate.dao.GenreStorageDao;
import ru.yanddex.practicum.filmorate.model.Film;
import ru.yanddex.practicum.filmorate.model.Genre;
import ru.yanddex.practicum.filmorate.model.Rating;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FilmStorageDaoImpl implements FilmStorageDao {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorageDao genreStorage;

    @Autowired
    public FilmStorageDaoImpl(JdbcTemplate jdbcTemplate, GenreStorageDao genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT fs.FILM_ID as film_id,\n" +
                "       fs.FILM_NAME as film_name,\n" +
                "       fs.FILM_DESCRIPTION as film_description,\n" +
                "       fs.FILM_RELEASE_DATE as film_release_date,\n" +
                "       fs.FILM_DURATION as film_duration,\n" +
                "       r.RATING_ID as rating_id,\n" +
                "       r.RATING_NAME as rating_name\n" +
                "    FROM FILMS fs\n" +
                "    join RATINGS R on R.RATING_ID = fs.FILM_RATING_ID;";
        return jdbcTemplate.query(sqlQuery, this::makeFilm);
    }

    @Override
    public Optional<Film> create(Film film) throws IncorrectDateValidationException {

        String sqlQuery = "INSERT INTO FILMS " +
                "(FILM_NAME, FILM_DESCRIPTION, FILM_RELEASE_DATE, FILM_DURATION , FILM_RATING_ID)" +
                " VALUES (?,?,?,?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return addFilmGenres(film, keyHolder);
    }

    @Override
    public Optional<Film> update(Film film) throws IncorrectIdValidationException, IncorrectDateValidationException {
        try {
            String sqlQuery = "update FILMS set\n" +
                    "    FILM_NAME = ?, FILM_DESCRIPTION = ?, FILM_RELEASE_DATE = ?, FILM_DURATION = ?," +
                    " FILM_RATING_ID = ?\n" +
                    "    where FILM_ID = ?;";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
                stmt.setString(1, film.getName());
                stmt.setString(2, film.getDescription());
                stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
                stmt.setLong(4, film.getDuration());
                stmt.setInt(5, film.getMpa().getId());
                stmt.setInt(6, film.getId());
                return stmt;
            }, keyHolder);

            return addFilmGenres(film, keyHolder);
        } catch (Exception e) {
            throw new IncorrectIdValidationException("Получены некорректные данные");
        }
    }

    private Optional<Film> addFilmGenres(Film film, KeyHolder keyHolder) {
        Optional<Film> optionalFilm = getFilmById(Objects.requireNonNull(keyHolder.getKey()).intValue());
        optionalFilm.ifPresent(film1 -> {
            if (!film1.getGenres().isEmpty()) {
                genreStorage.deleteByFilmId(film.getId());
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
            optionalFilm.ifPresent(film1 -> {
                film1.setGenres(genreStorage.getGenresByIds(film.getGenres().stream()
                        .mapToInt(Genre::getId).boxed().collect(Collectors.toList())));
            });
        } else {
            optionalFilm.ifPresent(film1 -> film1.setGenres(new ArrayList<>()));
        }
        return optionalFilm;
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        String sqlQuery = "SELECT                      fs.FILM_ID as film_id,\n" +
                "                fs.FILM_NAME as film_name,\n" +
                "                fs.FILM_DESCRIPTION as film_description,\n" +
                "                fs.FILM_RELEASE_DATE as film_release_date,\n" +
                "                fs.FILM_DURATION as film_duration,\n" +
                "                r.RATING_ID as rating_id,\n" +
                "                r.RATING_NAME as rating_name\n" +
                "                FROM FILMS fs\n" +
                "                join RATINGS R on R.RATING_ID = fs.FILM_RATING_ID\n" +
                "                WHERE fs.FILM_ID=? ;";
        List<Film> res = jdbcTemplate.query(sqlQuery, this::makeFilm, id);
        return res.size() == 0 ?
                Optional.empty() :
                Optional.of(res.get(0));
    }

    @Override
    public boolean deleteFilmById(Integer id) throws IncorrectIdToGetException {
        String sqlQuery = "DELETE FROM FILMS\n " +
                "WHERE FILMS.FILM_ID = ?;  ";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    @Override
    public void addLike(Integer userId, Integer filmId) {
        String sqlQuery = "INSERT INTO LIKES (L_USER_ID, L_FILM_ID) VALUES ( ?,? );";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    @Override
    public boolean deleteLike(Integer userId, Integer filmId) {
        String sqlQuery = "DELETE FROM LIKES\n " +
                "WHERE LIKES.L_USER_ID = ? AND LIKES.L_FILM_ID = ?";
        return jdbcTemplate.update(sqlQuery, userId, filmId) > 0;
    }

    @Override
    public List<Film> getBestFilms(Integer limit) {
        String sqlQuery = "SELECT f.FILM_ID as film_id,\n" +
                "          f.FILM_NAME as film_name,\n" +
                "          f.FILM_DESCRIPTION as film_description,\n" +
                "          f.FILM_RELEASE_DATE as film_release_date,\n" +
                "          f.FILM_DURATION as film_duration,\n" +
                "          r.RATING_ID as rating_id,\n" +
                "          r.RATING_NAME as rating_name\n" +
                "          FROM FILMS f\n" +
                "          join RATINGS r on r.RATING_ID = f.FILM_RATING_ID\n" +
                "          WHERE f.FILM_ID IN (\n" +
                "               SELECT f.FILM_ID\n" +
                "               FROM FILMS f\n" +
                "               LEFT JOIN LIKES L on f.FILM_ID = L.L_FILM_ID\n" +
                "               GROUP BY f.FILM_ID\n" +
                "               ORDER BY count(l.L_USER_ID) DESC LIMIT ?\n" +
                "                   );";

        return jdbcTemplate.query(sqlQuery, this::makeFilm, limit);
    }

    private Film makeFilm(ResultSet resultSet, int row) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getInt("film_id"));
        film.setName(resultSet.getString("film_name"));
        film.setDescription(resultSet.getString("film_description"));
        film.setReleaseDate(LocalDate.parse(resultSet.getString("film_release_date"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        film.setDuration(resultSet.getInt("film_duration"));
        Rating mpa = new Rating();
        mpa.setId(resultSet.getInt("rating_id"));
        mpa.setName(resultSet.getString("rating_name"));
        film.setGenres(genreStorage.getGenreByFilmId(resultSet.getInt("film_id")));
        film.setMpa(mpa);
        return film;
    }
}
