package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
@Qualifier("bd")
@Slf4j
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorage mpaDbStorage;

    @Override
    public List<Film> findAll() {

        return jdbcTemplate.query("select * from FILMS f, MPA m where f.MPA = m.MPA_ID order by f.ID", this::makeFilm);
    }

    @Override
    public Film createFilm(Film film) {

        String sqlQuery = "insert into films( name, description, releaseDate, duration,mpa) " +
                "values ( ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        film.setId(keyHolder.getKey().longValue());
        setGenre(film);

        return getFilmById(film.getId());

    }

    public void setGenre(Film film) {
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }
        String sqlQuery2 = "merge into films_genres key (GENRE_ID)" + "values (?,?)";


        List<Genre> genres = new ArrayList<>(film.getGenres());

        jdbcTemplate.batchUpdate(sqlQuery2, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, (int) film.getId());
                ps.setInt(2, genres.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });

    }

    public Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Film newFilm = new Film(
                rs.getInt("id"),
                rs.getString("name").trim(),
                rs.getString("description").trim(),
                rs.getDate("releaseDate").toLocalDate(),
                rs.getInt("duration")

        );

        newFilm.setMpa(new Mpa(rs.getInt("MPA.mpa_id"), rs.getString("MPA.mpa_name")));
        ;

        List<Genre> listGenres = setOfGenres(newFilm.getId());

        newFilm.setGenres(listGenres);


        return newFilm;
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

        setGenre(film);

        return getFilmById(film.getId());
    }


    @Override
    public Film getFilmById(Long id) {

        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from films where id = ?", id);
        if (rs.next()) {
            log.info("Найден фильм: {} {}", rs.getString("id"), rs.getString("name"));
            final String sqlQuery = "select * from FILMS f, MPA m where f.MPA = m.MPA_ID and f.id = ?";
            return jdbcTemplate.query(sqlQuery, this::makeFilm, id).get(0);

        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new DataNotFoundException("фильм " + id + " не найден");
        }
    }

    public List<Genre> setOfGenres(Long id) {

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

    @Override
    public List<Film> getPopular(Integer count) {
        final String sqlQuery = "select *  from films f left outer join film_likes on f.ID = FILM_LIKES.FILM_ID left outer join MPA m on f.MPA = m.MPA_ID group by f.ID order by count(film_likes.USER_ID) desc limit ?";
        return jdbcTemplate.query(sqlQuery, this::makeFilm, count);
    }
}




