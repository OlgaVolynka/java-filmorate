package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 0;

    private long countId() {
        return ++id;
    }

    @GetMapping("/users")
    public List<User> findAll() {
        log.info("Получен запрос GET users");
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/users")

    public User create(@RequestBody User user) {
        log.info("Получен запрос POST user");

        validate(user);
        if (user.getName().isBlank() || user.getName() == null) {
            user.setName(user.getLogin());
        }

        user.setId(countId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        log.info("Получен запрос PUT user");
        if (users.containsKey(user.getId())) {
            validate(user);
            if (user.getName().isBlank() || user.getName() == null) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            return user;
        } else throw new ValidationException("пользователя с данным id не существует");
    }

    protected void validate(User user) {
        if (user.getEmail().isBlank() || user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin().isBlank() || user.getLogin() == null) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("дата рождения не может быть в будущем");
        }
    }
}
