package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @Autowired

    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @GetMapping(value = "/films")
    public List<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    @PostMapping(value = "/films")

    public Film createFilm(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.createFilm(film);
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public void addLike(@Valid @PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public void deleteLike(@Valid @PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping(value = "/films/popular")
    public List<Film> getTop10(@Valid @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        return filmService.getTop10(count);
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@Valid @PathVariable("id") Long id) {
        return filmService.getFilmById(id);
    }
}
