package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    private final UserDbStorage userDbStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserDbStorage userDbStorage, UserService userService) {

        this.userDbStorage = userDbStorage;
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> findAll() {
        log.info("Получен запрос GET users");
        return userDbStorage.findAll();
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос POST user");
        return userService.create(user);
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос PUT user");
        return userService.updateUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriends(@Valid @PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        log.info("Получен запрос PUT addFriends");
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriends(@Valid @PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        log.info("Получен запрос DELETE friend");
        userService.deleteFriends(id, friendId);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@Valid @PathVariable Long id, @PathVariable Long otherId) {
        log.info("Получен запрос GET common friends");
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> getFriendsById(@Valid @PathVariable("id") Long userId) {
        log.info("Получен запрос GET friend by id");
        return userService.getSetFriends(userId);
    }

    @GetMapping("/users/{id}")
    public User getUserById(@Valid @PathVariable("id") Long userId) {
        log.info("Получен запрос GET user by id");
        return userService.getUserById(userId);
    }
}
