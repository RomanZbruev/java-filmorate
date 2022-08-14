package ru.yanddex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yanddex.practicum.filmorate.dao.FriendStorageDao;
import ru.yanddex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
public class FriendStorageDaoImpl implements FriendStorageDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendStorageDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
        return jdbcTemplate.query(sqlQuery, this::makeUser, id);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sqlQuery = "INSERT INTO USERFRIENDS (UF_USER_ID, UF_FRIEND_ID)" +
                " VALUES ( ?,? );";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sqlQuery = "DELETE FROM USERFRIENDS\n" +
                "       WHERE UF_USER_ID = ? AND UF_FRIEND_ID =?;";
        jdbcTemplate.update(sqlQuery, userId, friendId);
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
        return jdbcTemplate.query(sqlQuery, this::makeUser, user1, user2);
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
