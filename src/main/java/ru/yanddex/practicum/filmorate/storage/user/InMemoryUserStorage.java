package ru.yanddex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectIdValidationException;
import ru.yanddex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{
    private final HashMap<Integer, User> userStorage = new HashMap<>();
    private int id = 0;

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        for (Integer id : userStorage.keySet()) {
            users.add(userStorage.get(id));
        }
        log.info("Получен запрос на просмотр списка пользователей");
        return users;
    }

    @Override
    public User create(User user) {
        id++;
        user.setId(id);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userStorage.put(id, user);
        log.info("Пользователь успешно добавлен: {}", user);
        return user;
    }

    @Override
    public User update(User user) throws IncorrectIdValidationException {
        if (userStorage.containsKey(user.getId())) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            userStorage.put(id, user);
            log.info("Пользователь обновлен: {}", user);
            return user;
        } else {
            IncorrectIdValidationException exception =
                    new IncorrectIdValidationException("Такого пользователя нет в базе!");
            log.warn(exception.getMessage());
            throw exception;
        }
    }

    @Override
    public User getUserById(int id) throws IncorrectIdToGetException {
        if(userStorage.containsKey(id)){
            return userStorage.get(id);
        }
        else {
            IncorrectIdToGetException exception = new IncorrectIdToGetException("Такого пользователя нет в базе");
            log.warn(exception.getMessage());
            throw exception;
        }
    }

    @Override
    public void deleteUserById(Integer id) throws IncorrectIdToGetException{
        if (userStorage.containsKey(id)){
            userStorage.remove(id);
        }
        else {
            IncorrectIdToGetException exception = new IncorrectIdToGetException("Такого пользователя нет в базе");
            log.warn(exception.getMessage());
            throw exception;
        }
    }
    @Override
    public void clearStorage(){
        userStorage.clear();
        id=0;
    }
}
