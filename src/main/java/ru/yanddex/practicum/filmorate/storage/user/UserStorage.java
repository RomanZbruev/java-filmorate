package ru.yanddex.practicum.filmorate.storage.user;

import ru.yanddex.practicum.filmorate.controller.exception.IncorrectIdValidationException;
import ru.yanddex.practicum.filmorate.model.User;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;

import java.util.List;

public interface UserStorage {

    public List<User> getAll();

    public User create(User user);

    public User update(User user) throws IncorrectIdValidationException;

    public User getUserById(int id) throws IncorrectIdToGetException;

    public void deleteUserById(Integer id) throws IncorrectIdToGetException;

    public void clearStorage();
}
