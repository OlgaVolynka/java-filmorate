package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.HashSet;
import java.util.Set;

@Component
@Service
public class UserService {
    InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriends(long id, long friendId) {

        if (!userStorage.getUsers().containsKey(id)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "не найден id пользователя");
        }
        if (!userStorage.getUsers().containsKey(friendId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "не найден id друга");
        }
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriends(Long id, Long friendId) {

        if (!userStorage.getUsers().containsKey(id)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "не найден id пользователя");
        }
        if (!userStorage.getUsers().containsKey(friendId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "не найден id друга");
        }
        userStorage.removeFriend(id, friendId);
    }

    public Set<User> getCommonFriends(Long id, Long friendId) {

        if (!userStorage.getUsers().containsKey(id)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "не найден id пользователя");
        }
        if (!userStorage.getUsers().containsKey(friendId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "не найден id второго пользователя");
        }

        Set<Long> friendsList = userStorage.getUsers().get(id).getFriends(); //получили список друзей
        Set<Long> secondFriendsList = userStorage.getUsers().get(friendId).getFriends(); //получили список друзей

        Set<User> listUser = new HashSet<>();
        for (Long aLong : friendsList) {
            for (Long aLong1 : secondFriendsList) {
                if (aLong == aLong1) {
                    listUser.add(userStorage.getUsers().get(aLong));
                }
            }
        }
        return listUser;
    }

    public Set<User> getSetFriends(Long userId) {

        if (!userStorage.getUsers().containsKey(userId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "не найден id пользователя");
        }

        Set<Long> listId = userStorage.getUsers().get(userId).getFriends();
        Set<User> listUser = new HashSet<>();
        for (Long aLong : listId) {
            listUser.add(userStorage.getUsers().get(aLong));
        }
        return listUser;
    }

    public User getUserById(Long userId) {

        if (!userStorage.getUsers().containsKey(userId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "не найден id пользователя");
        }
        return userStorage.getUsers().get(userId);
    }
}
