package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Service
@Getter
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private static final LocalDate MIN_DATA = LocalDate.of(1895, 12, 28);
    private long id = 0;

    @Override
    public Long countId() {
        return ++id;
    }

    @Override
    public List<Film> findAll() {
        log.info("Получен запрос GET films");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        log.info("Получен запрос POST film");

        if (film.getReleaseDate().isBefore(MIN_DATA)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "дата релиза — не раньше 28 декабря 1895 года");
        }

        film.setId(countId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Получен запрос PUT film");
        if (films.containsKey(film.getId())) {
            if (film.getReleaseDate().isBefore(MIN_DATA)) {
                throw new ValidationException(HttpStatus.BAD_REQUEST, "дата релиза — не раньше 28 декабря 1895 года");
            } else {
                films.put(film.getId(), film);
                return film;
            }
        } else throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "фильма с данным id не существует");
    }

    public void addLike(long filmID, long userId) {

        films.get(filmID).addLike(userId);
    }

    public void removeLike(long filmID, long userId) {

        films.get(filmID).removeLike(userId);
    }
}
