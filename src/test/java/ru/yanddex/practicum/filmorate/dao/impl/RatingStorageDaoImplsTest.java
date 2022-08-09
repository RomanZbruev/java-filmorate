package ru.yanddex.practicum.filmorate.dao.impl;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yanddex.practicum.filmorate.dao.RatingStorageDao;
import ru.yanddex.practicum.filmorate.model.Rating;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingStorageDaoImplsTest {

    private final RatingStorageDao storage;

    @Test
    void getRatingByIdTest() {
        Optional<Rating> optional = storage.getRatingById(1);
        assertThat(optional).isPresent()
                .hasValueSatisfying(rating -> assertThat(rating)
                        .hasFieldOrPropertyWithValue("name", "G"));
    }
}
