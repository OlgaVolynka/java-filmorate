package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage) {

        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = new UserService(inMemoryUserStorage);
    }

    @GetMapping("/users")
    public List<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    @PostMapping(value = "/users")

    public User create(@Valid @RequestBody User user) {
        return inMemoryUserStorage.create(user);
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
        return inMemoryUserStorage.updateUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriends(@Valid @PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriends(@Valid @PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        userService.deleteFriends(id, friendId);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@Valid @PathVariable Long id, @PathVariable Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> getFriendsById(@Valid @PathVariable("id") Long userId) {
        return userService.getSetFriends(userId);
    }

    @GetMapping("/users/{id}")
    public User getUserById(@Valid @PathVariable("id") Long userId) {
        return userService.getUserById(userId);
    }
}
