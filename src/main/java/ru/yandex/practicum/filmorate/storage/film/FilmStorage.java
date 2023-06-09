package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
public interface FilmStorage {

    List<Film> findAll();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(Long id);

    List<Film> getPopular(Integer count);
}
