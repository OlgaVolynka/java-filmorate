package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
public interface FilmStorage {

    long id = 0;

    Long countId();

    List<Film> findAll();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(Long id);

    List<Genre> setGenres(Long id);

    List<Film> getPopular(Integer count);
}
