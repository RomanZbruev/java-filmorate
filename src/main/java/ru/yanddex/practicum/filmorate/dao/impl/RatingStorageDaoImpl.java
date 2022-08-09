package ru.yanddex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yanddex.practicum.filmorate.dao.RatingStorageDao;
import ru.yanddex.practicum.filmorate.model.Rating;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class RatingStorageDaoImpl implements RatingStorageDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RatingStorageDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Rating> getAll() {
        String sqlQuery = "SELECT r.rating_id as id,  " +
                "r.rating_name as name " +
                "FROM ratings r; ";
        return jdbcTemplate.query(sqlQuery, this::makeRating);
    }

    @Override
    public Optional<Rating> getRatingById(Integer id) {
        String sqlQuery = " SELECT r.rating_id as id," +
                "r.rating_name as name " +
                "FROM ratings r " +
                "WHERE r.rating_id = ?;";
        List<Rating> result = jdbcTemplate.query(sqlQuery, this::makeRating, id);
        return result.size() == 0 ?
                Optional.empty() :
                Optional.of(result.get(0));
    }

    @Override
    public Optional<Rating> create(Rating rating) {
        String sqlQuery = " INSERT INTO ratings (rating_name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, rating.getName());
            return stmt;
        }, keyHolder);
        return getRatingById(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public Optional<Rating> update(Rating rating) {
        String sqlQuery = " UPDATE RATINGS set rating_name = ? WHERE rating_id = ?;";
        return jdbcTemplate.update(sqlQuery,
                rating.getName(),
                rating.getId()) == 0 ?
                Optional.empty() :
                Optional.of(rating);
    }

    private Rating makeRating(ResultSet resultSet, int i) throws SQLException {
        Rating rating = new Rating();
        rating.setId(resultSet.getInt("id"));
        rating.setName(resultSet.getString("name"));
        return rating;
    }
}
