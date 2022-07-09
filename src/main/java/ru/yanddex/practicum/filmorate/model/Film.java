package ru.yanddex.practicum.filmorate.model;

import lombok.Data;



import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
public class Film {

    private Set<Integer> likes;
    private int id;
    @NotBlank(message = "Название фильма должно быть заполнено")
    private String name;
    @Size(max = 200, message = "Описание фильма не должно превышать 200 знаков")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Длительность фильма - положительная величина")
    private long duration;


    public Film(String name, String description, String releaseDate, long duration){
        this.name = name;
        this.description = description;
        this.releaseDate = LocalDate.parse(releaseDate);
        this.duration = duration;
    }
}
