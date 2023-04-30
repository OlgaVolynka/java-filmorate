package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FilmService {
    InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public void addLike(long id, long userId) {

        if (!inMemoryFilmStorage.getFilms().containsKey(id)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "не найден id фильма");
        }
        inMemoryFilmStorage.addLike(id, userId);
    }

    public void deleteLike(Long id, Long userId) {

        if (!inMemoryFilmStorage.getFilms().containsKey(id)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "не найден id фильма");
        }

        if (!inMemoryFilmStorage.getFilms().get(id).getLikes().contains(userId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "не найден id пользователя");
        }

        inMemoryFilmStorage.removeLike(id, userId);
    }

    public List<Film> getTop10(Integer count) {
        List<Film> filmSet = new ArrayList<>(inMemoryFilmStorage.getFilms().values());
        return filmSet.stream().sorted((p0, p1) -> {
            int comp = p0.getLikes().size() - (p1.getLikes().size());
            return -1 * comp;
        }).limit(count).collect(Collectors.toList());
    }

    public Film getFilmById(Long id) {

        if (!inMemoryFilmStorage.getFilms().containsKey(id)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "не найден id фильма");
        }
        return inMemoryFilmStorage.getFilms().get(id);
    }
}
