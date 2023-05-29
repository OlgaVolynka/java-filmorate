package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private long id = 0;

    LinkedHashSet<String> genreList = new LinkedHashSet<>();


    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        genreList.add("Комедия");
        genreList.add("Драма");

    }

    @Override
    public Long countId() {
        return ++id;
    }

    @Override
    public List<Film> findAll() {

        List<Film> films = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from films");

        while (rs.next()) {
            Film newFilm = new Film(
                    rs.getString("name").trim(),
                    rs.getString("description").trim(),
                    rs.getDate("releaseDate").toLocalDate(),
                    rs.getInt("duration")

            );

            newFilm.setId(rs.getInt("id"));

            newFilm.setMpa(Mpa.forValues(rs.getInt("mpa")));

            List<Genre> listGenres = setGenres(Long.valueOf(rs.getInt("id")));

            newFilm.setGenres(listGenres);

            films.add(newFilm);

        }

        return films;
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(countId());

        String sqlQuery = "insert into films(id, name, description, releaseDate, duration, rate,mpa) " +
                "values (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId()

        );

        List<Genre> genreList = film.getGenres();
        if (genreList != null) {
            for (Genre genreId : genreList) {
                String sqlQuery2 = "insert into films_genres (FILM_ID, GENRE_ID)" +
                        "values (?,?)";
                jdbcTemplate.update(sqlQuery2, film.getId(), genreId.getId());
            }
        }

        return getFilmById(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {

        String sqlQuery = "update films set " +
                "name = ?, description = ?, releaseDate = ?, duration = ?, rate = ?, mpa = ?  " +
                "where id = ?";
        jdbcTemplate.update(sqlQuery,

                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId()

        );
        String sqlQueryDel = "delete from films_genres where FILM_ID = ?";
        jdbcTemplate.update(sqlQueryDel, film.getId());
//
        List<Genre> genreList = film.getGenres();
        if (genreList != null) {
            for (Genre genreId : genreList) {
                String sqlQuery2 = "merge into films_genres key (GENRE_ID) " +
                        "values (?,?)";

                jdbcTemplate.update(sqlQuery2, film.getId(), genreId.getId());
            }
        }

        return getFilmById(film.getId());
    }

    @Override
    @Transactional
    public Film getFilmById(Long id) {

        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from films where id = ?", id);
        if (rs.next()) {
            log.info("Найден фильм: {} {}", rs.getString("id"), rs.getString("name"));
            // вы заполните данные пользователя в следующем уроке
            Film newFilm = new Film(
                    rs.getString("name").trim(),
                    rs.getString("description").trim(),
                    rs.getDate("releaseDate").toLocalDate(),
                    rs.getInt("duration")

            );

            newFilm.setId(rs.getInt("id"));

            newFilm.setMpa(Mpa.forValues(rs.getInt("mpa")));

            List<Genre> listGenres = setGenres(id);

            newFilm.setGenres(listGenres);

            return newFilm;
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new DataNotFoundException("фильм " + id + " не найден");
        }
    }

    public List<Genre> setGenres(Long id) {

        List<Genre> listGenres = new ArrayList<>();

        String sqlQuery2 = "select film_id, genre_id, genres_name from films_genres left join genres on films_genres.genre_id = genres.id where film_id = ?";

        SqlRowSet rs2 = jdbcTemplate.queryForRowSet(sqlQuery2, id);


        while (rs2.next()) {
            Genre newGenre = new Genre(
                    rs2.getInt("genre_id"),
                    rs2.getString("GENRES_NAME"));
            listGenres.add(newGenre);

        }
        return listGenres;

    }

}


