package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {
    protected User user = new User(0, "o_kyzina@mqil.ru", "1429644", "Olga", LocalDate.of(1987, 7, 17));
    InMemoryUserStorage userStorage = new InMemoryUserStorage();
    UserService userService = new UserService(userStorage);
    UserController userController = new UserController(userStorage);
    private Validator validator;

    @BeforeEach
    void init() {
        user = new User(0, "o_kyzina@mqil.ru", "1429644", "Olga", LocalDate.of(1987, 7, 17));
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        userController = new UserController(userStorage);
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    private List<String> getValidateErrorMsg(User validUser) {
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        return violations
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    @Test
    void test1_addNewUser() {
        userController.create(user);
        List<User> listUser = userController.findAll();

        assertEquals(1, listUser.size(), "Список User не корректный");
        assertEquals(user.getName(), listUser.get(0).getName(), "Имена не совпадают");
        assertEquals(user.getEmail(), listUser.get(0).getEmail(), "Email не совпадают");
        assertEquals(user.getBirthday(), listUser.get(0).getBirthday(), "Дата рождения не совпадают");
        assertEquals(user.getLogin(), listUser.get(0).getLogin(), "Логин не совпадают");
    }

    @Test
    void test2_addNewUserWithFailName() {
        user.setName("");
        userController.create(user);
        List<User> listUser = userController.findAll();

        assertEquals(1, listUser.size(), "Список User не корректный");
        assertEquals(user.getLogin(), listUser.get(0).getName(), "При пустом имени, имя не заменяется на логин");
    }

    @Test
    void test3_addNewUserWithFaiLogin() {
        user.setLogin("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        List<String> massages = getValidateErrorMsg(user);
        assertTrue(!violations.isEmpty(), "ошибка валидации при проверке класса");

        assertEquals(1, massages.size(), "Проверка не корректный логин не проходит");
        assertTrue(massages.contains("логин не может быть пустым и содержать пробелы"), "Неверное сообщение об ошибке");
    }

    @Test
    void test4_addNewUserWithFaiLogin() {
        user.setBirthday(LocalDate.now().plusYears(1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        List<String> massages = getValidateErrorMsg(user);
        assertTrue(!violations.isEmpty(), "ошибка валидации при проверке класса");

        assertEquals(1, massages.size(), "Проверка даты рождения не проходит");
        assertTrue(massages.contains("дата рождения не может быть в будущем"), "Неверное сообщение об ошибке");
    }

    @Test
    void test5_addNewUserWithFailEmail() {
        user.setEmail("o_kyzina.mail.ru@");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        List<String> massages = getValidateErrorMsg(user);
        assertTrue(!violations.isEmpty(), "ошибка валидации при проверке класса");

        assertEquals(1, massages.size(), "Проверка не корректный email не проходит");
        assertTrue(massages.contains("электронная почта не может быть пустой и должна содержать символ @"), "Неверное сообщение об ошибке");
    }

    @Test
    void test6_updateUserWithFailId() {
        userController.create(user);
        User newUser = new User(0, "o_kyzina@mail.ru", "1429644", "Olga", LocalDate.of(1987, 7, 17));

        ValidationException exUser = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                userController.updateUser(newUser);
            }
        });
        List<User> listUser = userController.findAll();

        assertEquals(1, listUser.size(), "Список User не корректный");
        assertEquals("500 INTERNAL_SERVER_ERROR \"пользователя с данным id не существует\"", exUser.getMessage(), "не выбрасывается исключение при неверном Id");
    }
}