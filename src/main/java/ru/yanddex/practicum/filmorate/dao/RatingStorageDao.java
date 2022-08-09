package ru.yanddex.practicum.filmorate.dao;


import ru.yanddex.practicum.filmorate.model.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingStorageDao {

    public List<Rating> getAll();

    public Optional<Rating> getRatingById(Integer id);

    public Optional<Rating> create(Rating rating);

    public Optional<Rating> update(Rating rating);

}
