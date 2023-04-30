package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserStorage {

    final Map<Long, User> users = new HashMap<>();
    long id = 0;

    Long countId();

    List<User> findAll();

    User create(User user);

    User updateUser(User user);

}
