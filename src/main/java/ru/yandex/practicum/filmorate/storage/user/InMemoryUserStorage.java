package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Component
@Getter
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> users = new HashMap<>();
    private long id = 0;

    @Override
    public Long countId() {
        return ++id;
    }

    @Override
    public List<User> findAll() {
        log.info("Получен запрос GET users");
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        log.info("Получен запрос POST user");
        String name = user.getName();

        if (name == null || name.isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(countId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("Получен запрос PUT user");
        if (users.containsKey(user.getId())) {

            if (user.getName().isBlank() || user.getName() == null) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            return user;
        } else
            throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "пользователя с данным id не существует");
    }

    public void addFriend(long userID, long friendId) {

        users.get(userID).addFriend(friendId);
        users.get(friendId).addFriend(userID);

    }

    public void removeFriend(Long userID, Long friendId) {

        if (!users.get(userID).getFriends().contains(friendId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "пользователи не состоят в друзьях");
        }
        users.get(userID).removeFriend(friendId);
        users.get(friendId).removeFriend(userID);
    }
}
