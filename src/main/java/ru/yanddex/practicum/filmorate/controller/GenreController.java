package ru.yanddex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectIdValidationException;
import ru.yanddex.practicum.filmorate.model.Genre;
import ru.yanddex.practicum.filmorate.model.User;
import ru.yanddex.practicum.filmorate.service.GenreService;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/genres")
    public List<Genre> getAll(){
        return genreService.getAll();
    }


    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable("id") Integer id) throws IncorrectIdToGetException{
        return genreService.getGenreById(id);
    }

    @PostMapping("/genres")
    public Genre create(@RequestBody @Valid Genre genre) {
        return genreService.create(genre);
    }


    @PutMapping("/genres")
    public Genre update(@RequestBody @Valid Genre genre) throws IncorrectIdValidationException {
        return genreService.update(genre);
    }

}
