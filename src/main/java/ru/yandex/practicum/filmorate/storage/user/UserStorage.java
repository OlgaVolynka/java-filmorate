package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    long id = 0;

    Long countId();

    List<User> findAll();

    User create(User user);

    User updateUser(User user);

    User getUserById(Long id);

    Set<Long> getSetIdFriends(Long userId);

    void deleteFriends(Long id, Long friendId);

    void addFriends(long id, long friendId);
}
