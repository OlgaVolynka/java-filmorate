package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private long id = 0;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long countId() {
        return ++id;
    }

    //String email, String login, String name, LocalDate birthday
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from users");
//id, String email, String login, String name, LocalDate birthday
        if (rs.next()) {
            User newUser = new User();
            newUser.setEmail(rs.getString("email").trim());
            newUser.setLogin(rs.getString("login").trim());
            newUser.setName(rs.getString("name").trim());
            newUser.setBirthday(rs.getDate("birthday").toLocalDate());

            newUser.setId(rs.getInt("id"));
            //      newFilm.setMpa(rs.getInt("mpa"));

            users.add(newUser);

        }

        return users;

    }

    @Override
    public User create(User user) {

        user.setId(countId());
        String sqlQuery = "insert into users(id, email, login, name, birthday) " +
                "values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );

        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "update users set " +
                "email = ?, login = ?, name = ?, birthday = ?  " +
                "where id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()

        );
        return getUserById(user.getId());
    }


    @Override
    public User getUserById(Long id) {

        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);
        if (rs.next()) {
            log.info("Пользователь: {} {}", rs.getString("id"), rs.getString("name"));
            // вы заполните данные пользователя в следующем уроке
            User newUser = new User(
                    rs.getString("email").trim(),
                    rs.getString("login").trim(),
                    rs.getString("name").trim(),
                    rs.getDate("birthday").toLocalDate()
            );

            newUser.setId(rs.getInt("id"));
            return newUser;
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new DataNotFoundException("пользователь " + id + " не найден");
        }
    }
}
