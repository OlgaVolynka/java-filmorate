package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {

    private Map<Long, Film> films = new HashMap<>();
    public final static LocalDate MIN_DATA = LocalDate.of(1985, 12, 28);
    long id = 0;

    private long countId() {
        return ++id;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        log.info("Получен запрос GET films");
        return new ArrayList<>(films.values());
    }

    @PostMapping(value = "/films")
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        log.info("Получен запрос POST film");

        validate(film);
        film.setId(countId());
        films.put(film.getId(), film);
        return film;

    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        log.info("Получен запрос PUT film");
        if (films.containsKey(film.getId())) {
            validate(film);
            films.put(film.getId(), film);
            return film;
        } else throw new ValidationException("фильма с данным id не существует");
    }

    protected void validate(Film film) throws ValidationException {
        if (film.getName().isBlank() || film.getName() == null) {
            throw new ValidationException("название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(MIN_DATA)) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
    }


}
