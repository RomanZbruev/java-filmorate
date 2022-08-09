package ru.yanddex.practicum.filmorate.model;

import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
public class Film {

    private List<Genre> genres = new ArrayList<>();

    private int id;

    @NotBlank(message = "Название фильма должно быть заполнено")
    private String name;

    @Size(max = 200, message = "Описание фильма не должно превышать 200 знаков")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Длительность фильма - положительная величина")
    private long duration;

    private Rating mpa;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
