package ru.yanddex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectDateValidationException;
import ru.yanddex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class UserController {
    private final HashMap<Integer, User> userStorage = new HashMap<>();
    private int id = 0;

    @GetMapping("/users")
    public List<User> giveAll() {
        List<User> users = new ArrayList<>();
        for (Integer id : userStorage.keySet()) {
            users.add(userStorage.get(id));
        }
        log.info("Получен запрос на просмотр списка пользователей");
        return users;
    }

    @PostMapping("/users")
    public User create(@RequestBody @Valid User user) {
        id++;
        user.setId(id);
        if(user.getName().isBlank()){
            user.setName(user.getLogin());
        }
        userStorage.put(id, user);
        log.info("Пользователь успешно добавлен: {}", user);
        return user;
    }

    @PutMapping("/users")
    public User update(@RequestBody @Valid User user) throws IncorrectDateValidationException {
            if (userStorage.containsKey(user.getId())) {
                if(user.getName().isBlank()){
                    user.setName(user.getLogin());
                }
                userStorage.put(id, user);
                log.info("Пользователь обновлен: {}", user);
                return user;
            } else {
                IncorrectDateValidationException exception =
                        new IncorrectDateValidationException("Такого пользователя нет в базе!");
                log.warn(exception.getMessage());
                throw exception;
            }
        }

}




