package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;

@Component
@Slf4j
public class GenreDbStorage {

    private final JdbcTemplate jdbcTemplate;


    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public LinkedHashSet<Genre> getAllGenres() {


        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from genres ");

        while (rs.next()) {
            Genre genre = new Genre(
                    rs.getInt("id"),
                    rs.getString("genres_name")
            );
            genres.add(genre);
        }

        return genres;

    }

    public Genre getGenresById(Integer id) {

        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from genres where id = ?", id);

        if (rs.next()) {
            Genre genre = new Genre(
                    rs.getInt("id"),
                    rs.getString("genres_name")
            );
            return genre;
        }

        throw new DataNotFoundException("Не найден id жанра");

    }
}
