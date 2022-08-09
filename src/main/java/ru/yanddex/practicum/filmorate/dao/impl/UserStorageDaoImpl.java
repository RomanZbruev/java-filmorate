package ru.yanddex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yanddex.practicum.filmorate.dao.UserStorageDao;
import ru.yanddex.practicum.filmorate.model.User;

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
public class UserStorageDaoImpl implements UserStorageDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserStorageDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT u.USER_ID as user_id,\n" +
                "       u.USER_EMAIL as user_email,\n" +
                "       u.USER_LOGIN as user_login,\n" +
                "       u.USER_NAME as user_name,\n" +
                "       u.USER_BIRTHDAY as user_birthday\n" +
                "FROM USERS u;";
        return jdbcTemplate.query(sqlQuery,this::makeUser);
    }

    @Override
    public Optional<User> create(User user) {
        String sqlQuery = "INSERT INTO users (user_email, " +
                "user_login, user_name , user_birthday ) VALUES (?,?,?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4,Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        return getUserById(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public Optional<User> update(User user) {
        String sqlQuery = "UPDATE USERS SET USER_EMAIL = ?, USER_LOGIN = ?, USER_NAME = ?, USER_BIRTHDAY = ?\n" +
                "WHERE USER_ID = ?;";
        return jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()) == 0?
                        Optional.empty():
                        Optional.of(user);
    }

    @Override
    public Optional<User> getUserById(int id) {
        String sqlQuery = "SELECT us.USER_ID as user_id,\n " +
                "       us.USER_EMAIL as user_email,\n" +
                "       us.USER_LOGIN as user_login,\n" +
                "       us.USER_NAME as user_name,\n" +
                "       us.USER_BIRTHDAY as user_birthday\n" +
                "FROM USERS us " +
                "WHERE us.USER_ID = ?;";
        List< User> result = jdbcTemplate.query(sqlQuery, this::makeUser,id);
        return result.size()==0?
                Optional.empty():
                Optional.of(result.get(0));
    }

    @Override
    public boolean deleteUserById(Integer id) {
        String sqlQuery = "DELETE FROM USERS\n" +
                "                WHERE USERS.USER_ID = ?;";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    @Override
    public List<User> getFriends(Integer id) {
        String sqlQuery = "SELECT u.USER_ID as user_id,\n" +
                "       u.USER_EMAIL as user_email,\n" +
                "       u.USER_LOGIN as user_login,\n" +
                "       u.USER_NAME as user_name,\n " +
                "       u.USER_BIRTHDAY as user_birthday\n" +
                "FROM USERS u\n" +
                "WHERE u.USER_ID IN (\n" +
                "    SELECT uf.UF_FRIEND_ID\n" +
                "        FROM USERFRIENDS uf\n" +
                "        WHERE uf.UF_USER_ID = ?\n" +
                "    );";
        return jdbcTemplate.query(sqlQuery,this::makeUser,id);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sqlQuery = "INSERT INTO USERFRIENDS (UF_USER_ID, UF_FRIEND_ID)" +
                " VALUES ( ?,? );";
        jdbcTemplate.update(sqlQuery,userId,friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sqlQuery = "DELETE FROM USERFRIENDS\n" +
                "       WHERE UF_USER_ID = ? AND UF_FRIEND_ID =?;";
        jdbcTemplate.update(sqlQuery,userId,friendId);
    }

    @Override
    public List<User> getCommonFriends(int user1, int user2) {
        String sqlQuery = "SELECT u.USER_ID as user_id,\n" +
                "       u.USER_EMAIL as user_email,\n" +
                "       u.USER_LOGIN as user_login,\n" +
                "       u.USER_NAME as user_name,\n" +
                "       u.USER_BIRTHDAY as user_birthday\n" +
                "    FROM USERS u\n" +
                "    WHERE u.USER_ID IN (\n" +
                "        SELECT uf.UF_FRIEND_ID\n" +
                "            FROM USERFRIENDS uf\n" +
                "        WHERE uf.UF_USER_ID = ?\n" +
                "        INTERSECT\n" +
                "        SELECT uf.UF_FRIEND_ID \n" +
                "            FROM USERFRIENDS uf\n" +
                "        WHERE uf.UF_USER_ID = ?\n" +
                "        );";
        return jdbcTemplate.query(sqlQuery,this::makeUser,user1,user2);
    }

    private User makeUser(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("user_id"));
        user.setEmail(resultSet.getString("user_email"));
        user.setLogin(resultSet.getString("user_login"));
        user.setName(resultSet.getString("user_name"));
        user.setBirthday(LocalDate.parse(resultSet.getString("user_birthday"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return user;
    }
}
