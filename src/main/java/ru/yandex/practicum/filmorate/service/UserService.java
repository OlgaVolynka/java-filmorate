package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriends(long id, long friendId) {

        userStorage.getUserById(id);
        userStorage.getUserById(friendId);

        userStorage.getUserById(id).addFriend(friendId);
        userStorage.getUserById(friendId).addFriend(id);

    }

    public void deleteFriends(Long id, Long friendId) {

        userStorage.getUserById(id);
        userStorage.getUserById(friendId);

        if (!userStorage.getUserById(id).getFriends().contains(friendId)) {
            throw new DataNotFoundException("пользователи не состоят в друзьях");
        }

        userStorage.getUserById(id).removeFriend(friendId);
        userStorage.getUserById(friendId).removeFriend(id);

    }

    public Set<User> getCommonFriends(Long id, Long friendId) {

        userStorage.getUserById(id);
        userStorage.getUserById(friendId);

        Set<Long> friendsList = userStorage.getUsers().get(id).getFriends(); //получили список друзей
        Set<Long> secondFriendsList = userStorage.getUsers().get(friendId).getFriends(); //получили список друзей


        Set<User> listUser = secondFriendsList.stream()
                .filter(friendsList::contains)
                .map(userId -> userStorage.getUsers().get(userId))
                .collect(Collectors.toSet());

        return listUser;
    }

    public Set<User> getSetFriends(Long userId) {

        userStorage.getUserById(userId);

        Set<Long> listId = userStorage.getUsers().get(userId).getFriends();

        Set<User> listUser = listId.stream()
                .map(user -> userStorage.getUserById(user))
                .collect(Collectors.toSet());

        return listUser;
    }

    public User getUserById(Long userId) {

        userStorage.getUserById(userId);
        return userStorage.getUserById(userId);
    }

    public User create(User user) {

        String name = user.getName();

        if (name == null || name.isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.create(user);
    }

    public User updateUser(User user) {

        userStorage.getUserById(user.getId());
        if (user.getName().isBlank() || user.getName() == null) {
            user.setName(user.getLogin());
        }
        userStorage.updateUser(user);
        return user;
    }
}
