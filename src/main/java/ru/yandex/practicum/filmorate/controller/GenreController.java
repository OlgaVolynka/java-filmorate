package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;

import java.util.LinkedHashSet;

@RequiredArgsConstructor
@RestController
@Slf4j
public class GenreController {

    private final GenreDbStorage genreDbStorage;

    @GetMapping(value = "/genres")
    public LinkedHashSet<Genre> findAll() {
        log.info("Получен запрос GET genres");
        return genreDbStorage.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getUserById(@PathVariable("id") Integer id) {
        log.info("Получен запрос GET genre by id");
        return genreDbStorage.getGenresById(id);
    }
}
