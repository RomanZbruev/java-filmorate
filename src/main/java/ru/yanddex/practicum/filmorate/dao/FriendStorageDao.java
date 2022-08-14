package ru.yanddex.practicum.filmorate.dao;

import ru.yanddex.practicum.filmorate.model.User;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;

import java.util.List;

public interface FriendStorageDao {
    public List<User> getFriends(Integer id) throws IncorrectIdToGetException;

    public void addFriend(int userId, int friendId);

    public void deleteFriend(int userId, int friendId);

    List<User> getCommonFriends(int user1, int user2);
}
