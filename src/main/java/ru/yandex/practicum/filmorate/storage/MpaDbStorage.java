package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mpa> getAllMpa() {

        List<Mpa> mpas = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from mpa ");

        while (rs.next()) {
            Mpa mpa = new Mpa(
            rs.getInt("mpa_id"),
            rs.getString("mpa_name")

            );
            mpas.add(mpa);
        }
        return mpas;
    }

    public Mpa getMpaById(Integer id) {

        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from mpa where mpa_id = ?", id);

        if (rs.next()) {
            Mpa mpa = new Mpa(
                    rs.getInt("mpa_id"),
                    rs.getString("mpa_name")

            );
            return mpa;
        }

        throw new DataNotFoundException("Не найден id mpa");

    }
}
