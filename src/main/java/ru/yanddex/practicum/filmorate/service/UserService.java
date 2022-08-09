package ru.yanddex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectIdValidationException;
import ru.yanddex.practicum.filmorate.model.User;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;
import ru.yanddex.practicum.filmorate.service.exception.NotInFriendsException;
import ru.yanddex.practicum.filmorate.dao.UserStorageDao;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserStorageDao userStorage;

    @Autowired
    public UserService(UserStorageDao userStorage){
        this.userStorage = userStorage;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getUserById(Integer id) throws IncorrectIdToGetException{
        if (userStorage.getUserById(id).isPresent()){
            return userStorage.getUserById(id).get();
        }
        else {
            throw new IncorrectIdToGetException("Пользователя с таким айди не существует");
        }
    }

    public User create(User user) {
        if(user.getName() == null || user.getName().isBlank()){
            user.setName(user.getLogin());
        }
        Optional<User> userOpt = userStorage.create(user);
        if(userOpt.isPresent()){
            return userOpt.get();
        }
        else {
            throw  new ValidationException("Пользователь не создан");
        }
    }

    public User update(User user) throws IncorrectIdValidationException{
        Optional<User> userOpt = userStorage.update(user);
        if(userOpt.isPresent()){
              return userOpt.get();
        }
        else {
            throw new IncorrectIdValidationException("Пользователя с таким айди не существует");
        }
    }

    public void addFriend(Integer userId, Integer friendId) throws IncorrectIdToGetException {
        try {
            userStorage.addFriend(userId, friendId);
            log.info("Пользователь успешно добавлен в друзья");
        } catch (DataIntegrityViolationException e){
            log.warn(e.getMessage());
            throw new IncorrectIdToGetException("Ошибка добавления в друзья");
        }

    }

    public void deleteFriend(Integer userId, Integer friendId) throws IncorrectIdToGetException {
         try {
            userStorage.deleteFriend(userId,friendId);
            log.info("Пользователь успешно удален из друзей");
        } catch (DataIntegrityViolationException e){
            IncorrectIdToGetException exception =
                    new IncorrectIdToGetException("Ошибка удаления из друзей");
            log.warn(exception.getMessage());
            throw exception;
        }
    }

    public List<User> getCommonFriends(Integer user1Id, Integer user2Id) throws IncorrectIdToGetException {
       try {
          return userStorage.getCommonFriends(user1Id,user2Id);
       }  catch (DataIntegrityViolationException e){
           log.warn(e.getMessage());
           throw new IncorrectIdToGetException("Ошибка получения списка общих друзей");
         }
    }

    public List<User> getFriendList(Integer userId) throws IncorrectIdToGetException {
        try {
            return userStorage.getFriends(userId);
        }catch (DataIntegrityViolationException e){
            log.warn(e.getMessage());
            throw new IncorrectIdToGetException("Ошибка получения списка общих друзей");
        }
    }

}
