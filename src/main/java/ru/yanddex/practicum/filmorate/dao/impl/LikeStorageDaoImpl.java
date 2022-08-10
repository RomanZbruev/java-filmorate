package ru.yanddex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yanddex.practicum.filmorate.dao.LikeStorageDao;

@Slf4j
@Component
public class LikeStorageDaoImpl implements LikeStorageDao {

    private final JdbcTemplate jdbcTemplate;

    public LikeStorageDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
}
