package ru.yanddex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yanddex.practicum.filmorate.dao.UserStorageDao;
import ru.yanddex.practicum.filmorate.model.Film;
import ru.yanddex.practicum.filmorate.model.User;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageDaoImplsTest {
    private final UserStorageDao storage;

    @Test
    void getUserByIdTest() throws IncorrectIdToGetException {
        Optional<User> optional = storage.getUserById(1);
        assertThat(optional).isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("name", "name"));
    }

    @Test
    void createUserTest() {
        User user = new User();
        user.setName("my_name");
        user.setEmail("new_email");
        user.setLogin("new_login");
        user.setBirthday(LocalDate.of(1998,1,15));

        Optional<User> optionalUser = storage.create(user);

        assertThat(optionalUser).isPresent().hasValueSatisfying(userO -> assertThat(userO)
                .hasFieldOrPropertyWithValue("name","my_name"));

    }
}
