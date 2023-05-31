package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
@Primary
@Qualifier("bd")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;


    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {

        return jdbcTemplate.query("select * from users", this::makeUser);

    }

    @Override
    public User create(User user) {

        String sqlQuery = "insert into users(email, login, name, birthday) " +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));

            return stmt;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());

        return getUserById(user.getId());

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

            final String sqlQuery = "select * from users where id = ?";
            return jdbcTemplate.query(sqlQuery, this::makeUser, id).get(0);

        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new DataNotFoundException("пользователь " + id + " не найден");
        }
    }

    public User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                (long) rs.getInt("id"),
                rs.getString("email").trim(),
                rs.getString("login").trim(),
                rs.getString("name").trim(),
                rs.getDate("birthday").toLocalDate()

        );
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
