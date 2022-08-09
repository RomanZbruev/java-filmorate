package ru.yanddex.practicum.filmorate.dao.impl;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectDateValidationException;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectIdValidationException;
import ru.yanddex.practicum.filmorate.dao.FilmStorageDao;
import ru.yanddex.practicum.filmorate.model.Film;
import ru.yanddex.practicum.filmorate.model.Rating;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmStorageDaoImplTest {
    private final FilmStorageDao storage;

    @Test
    void getFilmByIdTest() throws IncorrectIdToGetException {
        Optional<Film> optional = storage.getFilmById(1);
        assertThat(optional).isPresent().hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("name", "name"));
    }

    @Test
    void createFilmTest() throws IncorrectIdValidationException, IncorrectDateValidationException {
        Rating mpa = new Rating();
        mpa.setId(1);
        mpa.setName("mpa");

        Film film = new Film();
        film.setName("new_name");
        film.setDescription("new_description");
        film.setReleaseDate(LocalDate.of(2010, 1, 1));
        film.setDuration(111);
        film.setMpa(mpa);
        film.setGenres(new ArrayList<>());

        Optional<Film> optionalFilm = storage.create(film);

        assertThat(optionalFilm).isPresent().hasValueSatisfying(filmO -> assertThat(filmO).hasFieldOrPropertyWithValue("name", "new_name"));
    }
}
