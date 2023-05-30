package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LikeDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public void addLike(long id, long userId) {

        String sqlQuery = "insert into film_likes(user_id, film_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                userId,
                id

        );
    }

    public void deleteLike(Long id, Long userId) {

        String sqlQuery = "delete from film_likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, id, userId);

    }

}
