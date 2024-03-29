package ru.yanddex.practicum.filmorate.dao;

import ru.yanddex.practicum.filmorate.controller.exception.IncorrectIdValidationException;
import ru.yanddex.practicum.filmorate.model.User;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;

import java.util.List;
import java.util.Optional;

public interface UserStorageDao {

    public List<User> getAll();

    public Optional<User> create(User user);

    public Optional<User> update(User user) throws IncorrectIdValidationException;

    public Optional<User> getUserById(int id) throws IncorrectIdToGetException;

    public boolean deleteUserById(Integer id) throws IncorrectIdToGetException;




}
