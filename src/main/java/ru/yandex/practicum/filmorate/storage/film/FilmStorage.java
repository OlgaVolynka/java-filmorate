package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public interface FilmStorage {
    Map<Long, Film> films = new HashMap<>();
    LocalDate MIN_DATA = LocalDate.of(1895, 12, 28);
    long id = 0;

    Long countId();

    List<Film> findAll();

    Film createFilm(Film film);

    Film updateFilm(Film film);

     Film getFilmById(Long id);
}
