package ru.yanddex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yanddex.practicum.filmorate.controller.exception.IncorrectIdValidationException;
import ru.yanddex.practicum.filmorate.dao.GenreStorageDao;
import ru.yanddex.practicum.filmorate.model.Genre;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class GenreService {

    private final GenreStorageDao genreStorage;

    @Autowired
    public GenreService(GenreStorageDao genreStorageDao) {
        this.genreStorage = genreStorageDao;
    }

    public List<Genre> getAll() {
        return genreStorage.getAll();
    }

    public Genre getGenreById(Integer id) throws IncorrectIdToGetException {
        if (genreStorage.getGenreById(id).isPresent()) {
            return genreStorage.getGenreById(id).get();
        }
        else {
            throw new IncorrectIdToGetException("Жанра с таким айди не существует");
        }
    }

    public Genre create(Genre genre){
        Optional<Genre> genreOptional = genreStorage.create(genre);
        if(genreOptional.isPresent()){
            return genreOptional.get();
        }
        else {
            throw new ValidationException("Жанра не создан");
        }
    }

    public Genre update (Genre genre) throws IncorrectIdValidationException {
        Optional<Genre> genreOptional = genreStorage.update(genre);
        if (genreOptional.isPresent()){
            return genreOptional.get();
        }
        else {
            throw new IncorrectIdValidationException("Жанра с таким айди не существует");
        }
    }


}
