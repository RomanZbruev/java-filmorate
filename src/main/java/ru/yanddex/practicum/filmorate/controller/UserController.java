package ru.yanddex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yanddex.practicum.filmorate.controller.exception.IncorrectIdValidationException;
import ru.yanddex.practicum.filmorate.model.User;
import ru.yanddex.practicum.filmorate.service.UserService;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;
import ru.yanddex.practicum.filmorate.service.exception.NotInFriendsException;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAll() {
        return userService.getUserStorage().getAll();
    }

    @PostMapping("/users")
    public User create(@RequestBody @Valid User user) {
        return userService.getUserStorage().create(user);
    }

    @PutMapping("/users")
    public User update(@RequestBody @Valid User user) throws IncorrectIdValidationException {
        return userService.getUserStorage().update(user);
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable("id") Integer id) throws IncorrectIdToGetException {
        return userService.getUserStorage().getUserById(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer idFriend)
            throws IncorrectIdToGetException {
        userService.addFriend(userService.getUserStorage().getUserById(id),
                userService.getUserStorage().getUserById(idFriend));
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer idFriend)
            throws IncorrectIdToGetException, NotInFriendsException {
        userService.deleteFriend(userService.getUserStorage().getUserById(id),
                userService.getUserStorage().getUserById(idFriend));
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriendList(@PathVariable("id") Integer id) throws IncorrectIdToGetException {
        return userService.getFriendList(userService.getUserStorage().getUserById(id));
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId)
            throws IncorrectIdToGetException {
        return userService.getCommonFriends(userService.getUserStorage().getUserById(id),
                userService.getUserStorage().getUserById(otherId));
    }

}




