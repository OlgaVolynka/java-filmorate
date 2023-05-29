package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

@RestController
@Slf4j
public class GenreController {

    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreController(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    @GetMapping(value = "/genres")
    public LinkedHashSet<Genre> findAll() {
        log.info("Получен запрос GET genres");
        return genreDbStorage.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getUserById(@Valid @PathVariable("id") Integer id) {
        log.info("Получен запрос GET genre by id");
        return genreDbStorage.getGenresById(id);
    }

}
