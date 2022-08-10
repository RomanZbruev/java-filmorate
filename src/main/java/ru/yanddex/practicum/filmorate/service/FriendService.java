package ru.yanddex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yanddex.practicum.filmorate.dao.FilmStorageDao;
import ru.yanddex.practicum.filmorate.dao.FriendStorageDao;
import ru.yanddex.practicum.filmorate.model.User;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;

import java.util.List;

@Slf4j
@Service
public class FriendService {

    private final FriendStorageDao friendStorageDao;


    @Autowired
    public FriendService(FriendStorageDao friendStorageDao) {
        this.friendStorageDao = friendStorageDao;
    }

    public void addFriend(Integer userId, Integer friendId) throws IncorrectIdToGetException {
        try {
            friendStorageDao.addFriend(userId, friendId);
            log.info("Пользователь успешно добавлен в друзья");
        } catch (DataIntegrityViolationException e) {
            log.warn(e.getMessage());
            throw new IncorrectIdToGetException("Ошибка добавления в друзья");
        }

    }

    public void deleteFriend(Integer userId, Integer friendId) throws IncorrectIdToGetException {
        try {
            friendStorageDao.deleteFriend(userId, friendId);
            log.info("Пользователь успешно удален из друзей");
        } catch (DataIntegrityViolationException e) {
            IncorrectIdToGetException exception =
                    new IncorrectIdToGetException("Ошибка удаления из друзей");
            log.warn(exception.getMessage());
            throw exception;
        }
    }

    public List<User> getCommonFriends(Integer user1Id, Integer user2Id) throws IncorrectIdToGetException {
        try {
            return friendStorageDao.getCommonFriends(user1Id, user2Id);
        } catch (DataIntegrityViolationException e) {
            log.warn(e.getMessage());
            throw new IncorrectIdToGetException("Ошибка получения списка общих друзей");
        }
    }

    public List<User> getFriendList(Integer userId) throws IncorrectIdToGetException {
        try {
            return friendStorageDao.getFriends(userId);
        } catch (DataIntegrityViolationException e) {
            log.warn(e.getMessage());
            throw new IncorrectIdToGetException("Ошибка получения списка общих друзей");
        }
    }
}
