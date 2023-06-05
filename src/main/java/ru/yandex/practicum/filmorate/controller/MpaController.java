package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MpaController {
    private final MpaDbStorage mpaDbStorage;

    @GetMapping(value = "/mpa")
    public List<Mpa> findAll() {
        log.info("Получен запрос GET genres");
        return mpaDbStorage.getAllMpa();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpaById(@Valid @PathVariable("id") Integer id) {
        log.info("Получен запрос GET film by id");
        return mpaDbStorage.getMpaById(id);
    }
}
