package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FilmService {

    @Qualifier("bd")
    private final UserStorage userDbStorage;
    private final LikeDbStorage likeDbStorage;
    private static final LocalDate MIN_DATA = LocalDate.of(1895, 12, 28);
    @Qualifier("bd")
    private final FilmStorage filmDbStorage;


    public void addLike(long id, long userId) {

        filmDbStorage.getFilmById(id);
        likeDbStorage.addLike(id, userId);

    }

    public void deleteLike(Long id, Long userId) {

        filmDbStorage.getFilmById(id);
        userDbStorage.getUserById(userId);
        likeDbStorage.deleteLike(id, userId);

    }

    public List<Film> getPopular(Integer count) {
        List<Film> filmSet = filmDbStorage.getPopular(count);

        return filmSet;
    }

    public Film getFilmById(Long id) {

        return filmDbStorage.getFilmById(id);
    }

    public Film createFilm(Film film) {

        if (film.getReleaseDate().isBefore(MIN_DATA)) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }

        return filmDbStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {

        filmDbStorage.getFilmById(film.getId());

        if (film.getReleaseDate().isBefore(MIN_DATA)) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        } else {
            return filmDbStorage.updateFilm(film);

        }
    }

    public List<Film> findAll() {
        return filmDbStorage.findAll();
    }

}
