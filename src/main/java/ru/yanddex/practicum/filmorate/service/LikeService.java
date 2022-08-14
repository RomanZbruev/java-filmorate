package ru.yanddex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yanddex.practicum.filmorate.dao.LikeStorageDao;
import ru.yanddex.practicum.filmorate.service.exception.IncorrectIdToGetException;

@Slf4j
@Service
public class LikeService {

    private final LikeStorageDao likeStorage;

    @Autowired
    public LikeService(LikeStorageDao likeStorage) {
        this.likeStorage = likeStorage;
    }

    public void addLike(Integer userId, Integer filmId) throws IncorrectIdToGetException {
        try {
            likeStorage.addLike(userId, filmId);
            log.info("Лайк успешно добавлен");
        } catch (DataIntegrityViolationException e) {
            log.warn(e.getMessage());
            throw new IncorrectIdToGetException("Ошибка добавления лайка");
        }

    }

    public void deleteLike(Integer userId, Integer filmId) throws IncorrectIdToGetException {
        try {
            if (!likeStorage.deleteLike(userId, filmId)) {
                throw new IncorrectIdToGetException("Задан неправильный айди пользователя или лайка");
            }
        } catch (DataIntegrityViolationException e) {
            IncorrectIdToGetException exception =
                    new IncorrectIdToGetException("Ошибка удаления лайка");
            log.warn(exception.getMessage());
            throw exception;
        }
    }

}
