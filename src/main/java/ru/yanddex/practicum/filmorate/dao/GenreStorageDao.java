package ru.yanddex.practicum.filmorate.dao;


import ru.yanddex.practicum.filmorate.model.Genre;


import java.util.List;
import java.util.Optional;

public interface GenreStorageDao {

    public List<Genre> getAll();


    public Optional<Genre> getGenreById(Integer id);

    public Optional<Genre> create(Genre genre);

    public Optional<Genre> update(Genre genre);

    public List<Genre> getGenreByFilmId(Integer id);

    public void deleteByFilmId(Integer id);

    public List<Genre> getGenresByIds(List<Integer> ids);
}
