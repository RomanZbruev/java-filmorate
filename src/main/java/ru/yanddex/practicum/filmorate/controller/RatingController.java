package ru.yanddex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectIdValidationException;
import ru.yanddex.practicum.filmorate.model.Genre;
import ru.yanddex.practicum.filmorate.model.Rating;
import ru.yanddex.practicum.filmorate.service.RatingService;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping("/mpa")
    public List<Rating> getAll(){
        return ratingService.getAll();
    }

    @GetMapping("/mpa/{id}")
    public Rating getRatingById(@PathVariable("id") Integer id) throws IncorrectIdToGetException {
        return ratingService.getRatingById(id);
    }

    @PostMapping("/mpa")
    public Rating create(@RequestBody @Valid Rating rating){
        return ratingService.create(rating);
    }

    @PutMapping("/mpa")
    public Rating update(@RequestBody @Valid Rating rating) throws IncorrectIdValidationException {
        return ratingService.update(rating);
    }
 }
