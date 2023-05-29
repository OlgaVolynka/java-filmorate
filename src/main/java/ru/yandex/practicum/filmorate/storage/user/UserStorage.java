package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    long id = 0;

    Long countId();

    List<User> findAll();

    User create(User user);

    User updateUser(User user);
    User getUserById(Long id);

}
