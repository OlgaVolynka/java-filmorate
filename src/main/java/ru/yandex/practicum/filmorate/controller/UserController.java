package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 0;

    private Long countId() {
        return ++id;
    }

    @GetMapping("/users")
    public List<User> findAll() {
        log.info("Получен запрос GET users");
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/users")

    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос POST user");
        String name = user.getName();

        if (name == null || name.isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(countId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
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
}
