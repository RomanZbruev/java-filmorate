package ru.yanddex.practicum.filmorate.model;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Objects;


@Data
public class Rating {


    private int id;

    @NotBlank
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return id == rating.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
