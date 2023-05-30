package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
@Primary
@Qualifier("bd")
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

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from users");

        if (rs.next()) {
            User newUser = new User();
            newUser.setEmail(rs.getString("email").trim());
            newUser.setLogin(rs.getString("login").trim());
            newUser.setName(rs.getString("name").trim());
            newUser.setBirthday(rs.getDate("birthday").toLocalDate());

            newUser.setId(rs.getInt("id"));
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
                    (long) rs.getInt("id"),
                    rs.getString("email").trim(),
                    rs.getString("login").trim(),
                    rs.getString("name").trim(),
                    rs.getDate("birthday").toLocalDate()
            );

            return newUser;
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new DataNotFoundException("пользователь " + id + " не найден");
        }
    }

    @Override
    public void addFriends(long id, long friendId) {

        String sqlQuery = "insert into users_friend(user_id, friend_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                id, friendId
        );
    }

    @Override
    public void deleteFriends(Long id, Long friendId) {

        String sqlQuery = "delete from users_friend where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);

    }

    @Override
    public Set<Long> getSetIdFriends(Long userId) {

        Set<Long> listIdUser = new HashSet<>();

        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from users_friend where user_id = ?", userId);

        while (rs.next()) {

            listIdUser.add((long) rs.getInt("friend_id"));
        }
        return listIdUser;
    }
}
