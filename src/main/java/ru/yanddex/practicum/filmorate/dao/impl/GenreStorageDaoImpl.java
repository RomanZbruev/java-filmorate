package ru.yanddex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yanddex.practicum.filmorate.dao.GenreStorageDao;
import ru.yanddex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class GenreStorageDaoImpl implements GenreStorageDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreStorageDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = " SELECT g.genre_id as id, " +
                "g.genre_name as name " +
                "FROM genres g;";
        return jdbcTemplate.query(sqlQuery, this::makeGenre);
    }

    @Override
    public Optional<Genre> getGenreById(Integer id) {
        String sqlQuery = " SELECT g.genre_id as id, " +
                "g.genre_name as name " +
                "FROM genres g " +
                "WHERE g.genre_id = ?; ";
        List<Genre> result = jdbcTemplate.query(sqlQuery, this::makeGenre, id);
        return result.size() == 0 ?
                Optional.empty() :
                Optional.of(result.get(0));
    }

    @Override
    public Optional<Genre> create(Genre genre) {
        String sqlQuery = "INSERT INTO genres," +
                "genre_name) VALUES (?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, genre.getName());
            return stmt;
        }, keyHolder);
        return getGenreById(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public Optional<Genre> update(Genre genre) {
        String sqlQuery = "UPDATE GENRES SET genre_name = ? WHERE genre_id = ?;";
        return jdbcTemplate.update(sqlQuery,
                genre.getName(),
                genre.getId()) == 0 ?
                Optional.empty() :
                Optional.of(genre);
    }

    @Override
    public List<Genre> getGenreByFilmId(Integer id) {
        String sqlQuery = "SELECT GENRE_ID,GENRE_NAME\n" +
                "FROM GENRES\n" +
                "WHERE GENRE_ID IN (SELECT FG.FG_GENRE_ID\n" +
                "                       FROM FILMGENRES as FG\n" +
                "    WHERE FG.FG_FILM_ID = ?);";
        return jdbcTemplate.query(sqlQuery, this::makeGenre, id);
    }

    @Override
    public void deleteByFilmId(Integer id) {
        String sqlQuery = "DELETE  FROM  FILMGENRES\n" +
                "WHERE FG_FILM_ID = ?;";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public List<Genre> getGenresByIds(List<Integer> ids) {
        String helper = String.join(" ,", Collections.nCopies(ids.size(), "?"));
        String sqlQuery = String.format("SELECT GENRE_ID, GENRE_NAME \n" +
                "FROM GENRES \n" +
                "WHERE GENRE_ID IN (%s) ;", helper);
        return jdbcTemplate.query(sqlQuery, this::makeGenre, ids.toArray());
    }

    private Genre makeGenre(ResultSet resultSet, int i) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("genre_id"));
        genre.setName(resultSet.getString("genre_name"));
        return genre;
    }

}
