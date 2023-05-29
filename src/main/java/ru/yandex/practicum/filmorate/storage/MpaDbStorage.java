package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.LinkedHashSet;

@Component
@Slf4j
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;


    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public LinkedHashSet<Mpa> getAllMpa() {


        LinkedHashSet<Mpa> mpas = new LinkedHashSet<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from mpa ");
//id, String email, String login, String name, LocalDate birthday
        while (rs.next()) {
            Mpa mpa = Mpa.forValues(rs.getInt("mpa_id"));
            mpas.add(mpa);
        }

        return mpas;

    }
   /* public Genre getGenresById(Integer id) {

        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from genres where id = ?", id);
//id, String email, String login, String name, LocalDate birthday
        if (rs.next()) {
            Genre genre = new Genre(
                    rs.getInt("id"),
                    rs.getString("genres_name")
            );
            return genre;
        }

        return null;

    }*/


}
