package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FilmService {
    private final UserDbStorage userDbStorage;
    private static final LocalDate MIN_DATA = LocalDate.of(1895, 12, 28);
    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmDbStorage;

    @Autowired
    public FilmService(FilmDbStorage filmDbStorage, JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.filmDbStorage = filmDbStorage;
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    public void addLike(long id, long userId) {

        filmDbStorage.getFilmById(id);

        String sqlQuery = "insert into film_likes(user_id, film_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                userId,
                id

        );
    }

    public void deleteLike(Long id, Long userId) {

        filmDbStorage.getFilmById(id);
        userDbStorage.getUserById(userId);

        String sqlQuery = "delete from film_likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, id, userId);

    }

    public List<Film> getPopular(Integer count) {
        List<Film> filmSet = new ArrayList<>();

        SqlRowSet rs = jdbcTemplate.queryForRowSet("select films.ID, count(film_likes.USER_ID) as count_user  from films left outer join film_likes on FILMS.ID = FILM_LIKES.FILM_ID group by FILMS.ID order by count_user desc limit ?", count);
        while (rs.next()) {
            filmSet.add(filmDbStorage.getFilmById((long) rs.getInt("id")));
        }

        return filmSet;
    }

    public ArrayList<Film> getFilmById(Long id) {

        return new ArrayList<>(Collections.singleton(filmDbStorage.getFilmById(id)));
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
}
