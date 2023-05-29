/*package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Repository
@Getter
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long id = 0;

    @Override
    public Long countId() {
        return ++id;
    }

    @Override
    public List<Film> findAll() {

        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {

        film.setId(countId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {

        films.put(film.getId(), film);
        return film;
    }

    public Optional<Film> getFilmById(Long id) {

        if (!films.containsKey(id)) {
            throw new DataNotFoundException("фильм " + id + " не найден");
        }
        return Optional<films.get(id)>;
    }


}*/
