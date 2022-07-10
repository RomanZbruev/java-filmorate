package ru.yanddex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yanddex.practicum.filmorate.model.User;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;
import ru.yanddex.practicum.filmorate.service.exception.NotInFriendsException;
import ru.yanddex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage){
        this.userStorage = userStorage;
    }

    public void addFriend(User user1, User user2){
        user1.getFriends().add(user2.getId());
        user2.getFriends().add(user1.getId());
        log.info("Пользователь успешно добавлен в друзья");
    }

    public void deleteFriend(User user1, User user2) throws NotInFriendsException {
        int idFirstUser = user1.getId();
        int idSecondUser = user2.getId();
        if(user1.getFriends().contains(idSecondUser) && user2.getFriends().contains(idFirstUser)){
            user1.getFriends().remove(idSecondUser);
            user2.getFriends().remove(idFirstUser);
            log.info("Пользователь успешно удален из друзей");
        }
        else {
            NotInFriendsException exception =
                    new NotInFriendsException("Ошибка удаления из друзей.Пользователь не в списке друзей");
            log.warn(exception.getMessage());
            throw exception;
        }
    }

    public List<User> getCommonFriends(User user1, User user2){
        List<User> commonFriends = new ArrayList<>();
        Set<Integer> firstUserFriends = user1.getFriends();
        Set<Integer> secondUserFriends = user2.getFriends();
        if(firstUserFriends.size()!=0 && secondUserFriends.size()!=0){
        for (User user : userStorage.getAll()){
            if(firstUserFriends.contains(user.getId()) && secondUserFriends.contains(user.getId())){
                commonFriends.add(user);
            }
        }}
        return commonFriends;
    }

    public List<User> getFriendList(User user) throws IncorrectIdToGetException {
        List <User> friends = new ArrayList<>();
        for (int friend : user.getFriends()){
            friends.add(userStorage.getUserById(friend));
        }
        return friends;
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }
}
