package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @GetMapping(value = "/films")
    public List<Film> findAll() {
        log.info("Получен запрос GET films");
        return filmService.findAll();
    }

    @PostMapping(value = "/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос POST film");
        return filmService.createFilm(film);
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Validated(User.UpdateId.class) @RequestBody Film film) {
        log.info("Получен запрос PUT film");
        return filmService.updateFilm(film);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public void addLike(@Valid @PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.info("Получен запрос PUT addLike");
        filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public void deleteLike(@Valid @PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.info("Получен запрос DELETE like");
        filmService.deleteLike(id, userId);
    }

    @GetMapping(value = "/films/popular")
    public List<Film> getPopular(@Valid @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        log.info("Получен запрос GET TOP10");

        return filmService.getPopular(count);
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@Valid @PathVariable("id") Long id) {
        log.info("Получен запрос GET film by id");
        return filmService.getFilmById(id);
    }


}
