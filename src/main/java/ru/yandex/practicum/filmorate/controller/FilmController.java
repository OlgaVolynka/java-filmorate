package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private static final LocalDate MIN_DATA = LocalDate.of(1895, 12, 28);
    private long id = 0;

    private Long countId() {
        return ++id;
    }

    @GetMapping(value = "/films")
    public List<Film> findAll() {
        log.info("Получен запрос GET films");
        return new ArrayList<>(films.values());
    }

    @PostMapping(value = "/films")

    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос POST film");

        if (film.getReleaseDate().isBefore(MIN_DATA)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "дата релиза — не раньше 28 декабря 1895 года");
        }

        film.setId(countId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
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
}
