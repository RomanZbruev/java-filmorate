package ru.yanddex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectIdValidationException;
import ru.yanddex.practicum.filmorate.dao.RatingStorageDao;
import ru.yanddex.practicum.filmorate.model.Rating;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RatingService {

    private final RatingStorageDao ratingStorage;

    @Autowired
    public RatingService(RatingStorageDao ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    public List<Rating> getAll() {
        return ratingStorage.getAll();
    }

    public Rating getRatingById(Integer id) throws IncorrectIdToGetException {
        if (ratingStorage.getRatingById(id).isPresent()){
            return ratingStorage.getRatingById(id).get();
        }
        throw new IncorrectIdToGetException("Рейтинга с таким айди не существует");
    }

    public Rating create(Rating rating){
        Optional<Rating> optionalRating = ratingStorage.create(rating);
        if (optionalRating.isPresent()){
            return optionalRating.get();
        }
        else {
            throw new ValidationException("Возрастной рейтинг не создан");
        }
    }

    public Rating update(Rating rating) throws IncorrectIdValidationException {
        Optional<Rating> optionalRating = ratingStorage.update(rating);
        if (optionalRating.isPresent()){
            return optionalRating.get();
        }
        else {
            throw new IncorrectIdValidationException("Рейтинга с таким айди на существует");
        }
    }
}
