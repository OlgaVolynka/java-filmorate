package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private static final LocalDate MIN_DATA = LocalDate.of(1895, 12, 28);


    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public void addLike(long id, long userId) {

        inMemoryFilmStorage.getFilmById(id);

        inMemoryFilmStorage.getFilmById(id).addLike(userId);
    }

    public void deleteLike(Long id, Long userId) {

        inMemoryFilmStorage.getFilmById(id);

        if (!inMemoryFilmStorage.getFilms().get(id).getLikes().contains(userId)) {
            throw new DataNotFoundException("не найден id пользователя");
        }
        inMemoryFilmStorage.getFilmById(id).removeLike(userId);

    }

    public List<Film> getPopular(Integer count) {
        List<Film> filmSet = new ArrayList<>(inMemoryFilmStorage.getFilms().values());
        return filmSet.stream().sorted((p0, p1) -> {
            int comp = p0.getLikes().size() - (p1.getLikes().size());
            return -1 * comp;
        }).limit(count).collect(Collectors.toList());
    }

    public Film getFilmById(Long id) {

        return inMemoryFilmStorage.getFilmById(id);
    }

    public Film createFilm(Film film) {

        if (film.getReleaseDate().isBefore(MIN_DATA)) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }

        return inMemoryFilmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {

        inMemoryFilmStorage.getFilmById(film.getId());

        if (film.getReleaseDate().isBefore(MIN_DATA)) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        } else {
            return inMemoryFilmStorage.updateFilm(film);
        }
    }
}
