package ru.yanddex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectDateValidationException;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectIdValidationException;
import ru.yanddex.practicum.filmorate.dao.FilmGenreStorageDao;
import ru.yanddex.practicum.filmorate.dao.FilmStorageDao;
import ru.yanddex.practicum.filmorate.dao.GenreStorageDao;
import ru.yanddex.practicum.filmorate.model.Film;
import ru.yanddex.practicum.filmorate.model.Rating;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class FilmStorageDaoImpl implements FilmStorageDao {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorageDao genreStorage;

    private final FilmGenreStorageDao filmGenreStorage;

    @Autowired
    public FilmStorageDaoImpl(JdbcTemplate jdbcTemplate,
                              GenreStorageDao genreStorage,
                              FilmGenreStorageDao filmGenreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.filmGenreStorage = filmGenreStorage;
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
    public Optional<Film> create(Film film) throws IncorrectDateValidationException, IncorrectIdToGetException {

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
        Optional<Film> filmOptional = getFilmById(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return filmGenreStorage.addFilmGenres(film,filmOptional);
    }

    @Override
    public Optional<Film> update(Film film) throws IncorrectIdValidationException, IncorrectDateValidationException {
        try {
            String sqlQuery = "update FILMS set\n" +
                    "    FILM_NAME = ?, FILM_DESCRIPTION = ?, FILM_RELEASE_DATE = ?, FILM_DURATION = ?," +
                    " FILM_RATING_ID = ?\n" +
                    "    where FILM_ID = ?;";
            Optional<Film> filmOptional = jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId()) == 0?
                    Optional.empty():
                    Optional.of(film);
            return filmGenreStorage.addFilmGenres(film,getFilmById(film.getId()));//на основании полученного значения film
            //обновляем имеющееся в базе данных
        } catch (Exception e) {
            throw new IncorrectIdValidationException("Получены некорректные данные");
        }
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
