package ru.yanddex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectIdValidationException;
import ru.yanddex.practicum.filmorate.model.User;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;
import ru.yanddex.practicum.filmorate.dao.UserStorageDao;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserStorageDao userStorage;

    @Autowired
    public UserService(UserStorageDao userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getUserById(Integer id) throws IncorrectIdToGetException {
        if (userStorage.getUserById(id).isPresent()) {
            return userStorage.getUserById(id).get();
        } else {
            throw new IncorrectIdToGetException("Пользователя с таким айди не существует");
        }
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        Optional<User> userOpt = userStorage.create(user);
        if (userOpt.isPresent()) {
            return userOpt.get();
        } else {
            throw new ValidationException("Пользователь не создан");
        }
    }

    public User update(User user) throws IncorrectIdValidationException {
        Optional<User> userOpt = userStorage.update(user);
        if (userOpt.isPresent()) {
            return userOpt.get();
        } else {
            throw new IncorrectIdValidationException("Пользователя с таким айди не существует");
        }
    }



}
