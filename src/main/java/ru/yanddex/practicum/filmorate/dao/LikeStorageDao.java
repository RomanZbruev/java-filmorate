package ru.yanddex.practicum.filmorate.dao;

public interface LikeStorageDao {

    public void addLike(Integer userId, Integer filmId);

    public boolean deleteLike(Integer userId, Integer filmId);

}
