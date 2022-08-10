package ru.yanddex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yanddex.practicum.filmorate.controller.exception.IncorrectIdValidationException;
import ru.yanddex.practicum.filmorate.model.User;
import ru.yanddex.practicum.filmorate.service.FriendService;
import ru.yanddex.practicum.filmorate.service.UserService;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserFriendController {

    private final UserService userService;
    private final FriendService friendService;

    @Autowired
    public UserFriendController(UserService userService, FriendService friendService) {
        this.userService = userService;
        this.friendService = friendService;
    }

    @GetMapping("/users")
    public List<User> getAll() {
        return userService.getAll();
    }

    @PostMapping("/users")
    public User create(@RequestBody @Valid User user) {
        return userService.create(user);
    }

    @PutMapping("/users")
    public User update(@RequestBody @Valid User user) throws IncorrectIdValidationException {
        return userService.update(user);
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable("id") Integer id) throws IncorrectIdToGetException {
        return userService.getUserById(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId)
            throws IncorrectIdToGetException {
        friendService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId)
            throws IncorrectIdToGetException {
        friendService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriendList(@PathVariable("id") Integer id) throws IncorrectIdToGetException {
        return friendService.getFriendList(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId)
            throws IncorrectIdToGetException {
        return friendService.getCommonFriends(id, otherId);
    }

}




