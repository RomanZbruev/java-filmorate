package ru.yanddex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;


@Data
public class User {

    private Set<Integer> friends;
    private int id;
    @Email(message = "Почта должна содержать @")
    @NotBlank(message = "Почта не должна быть пустой")
    private String email;
    @Pattern(regexp = "[a-zA-Z0-9]+",message = "Логин должен состоять из букв и цифр без пробелов")
    @NotBlank(message = "Логин не может быть пустым")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    public User(String email, String login, String name, String birthday){
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = LocalDate.parse(birthday);
    }

}
