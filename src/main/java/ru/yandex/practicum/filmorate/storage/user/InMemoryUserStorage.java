/*package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Getter
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long id = 0;

    @Override
    public Long countId() {
        return ++id;
    }

    @Override
    public List<User> findAll() {

        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {

        user.setId(countId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {

        users.put(user.getId(), user);
        return user;
    }

    public User getUserById(Long id) {

        if (!users.containsKey(id)) {
            throw new DataNotFoundException("пользователь " + id + " не найден");
        }
        return users.get(id);
    }
}*/
