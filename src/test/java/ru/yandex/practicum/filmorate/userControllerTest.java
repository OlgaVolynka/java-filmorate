package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class userControllerTest {
    protected User user = new User(0, "o_kyzina@mqil.ru", "1429644", "Olga", LocalDate.of(1987, 7, 17));
    UserController userController = new UserController();

    @BeforeEach
    void init() {
        user = new User(0, "o_kyzina@mqil.ru", "1429644", "Olga", LocalDate.of(1987, 7, 17));
        userController = new UserController();
    }

    @Test
    void Test1_addNewUser() {
        userController.create(user);
        List<User> listUser = userController.findAll();

        assertEquals(1, listUser.size(), "Список User не корректный");
        assertEquals(user.getName(), listUser.get(0).getName(), "Имена не совпадают");
        assertEquals(user.getEmail(), listUser.get(0).getEmail(), "Email не совпадают");
        assertEquals(user.getBirthday(), listUser.get(0).getBirthday(), "Дата рождения не совпадают");
        assertEquals(user.getLogin(), listUser.get(0).getLogin(), "Логин не совпадают");
    }

    @Test
    void Test2_addNewUserWithFailName() {
        user.setName("");
        userController.create(user);
        List<User> listUser = userController.findAll();

        assertEquals(1, listUser.size(), "Список User не корректный");
        assertEquals(user.getLogin(), listUser.get(0).getName(), "При пустом имени, имя не заменяется на логин");
    }

    @Test
    void Test3_addNewUserWithFaiLogin() {
        user.setLogin("");

        ValidationException exUser = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                userController.create(user);
            }
        });
        List<User> listUser = userController.findAll();

        assertEquals(0, listUser.size(), "Список User не корректный");
        assertEquals("логин не может быть пустым и содержать пробелы", exUser.getMessage(), "Проверка на пустой логин не проходит");
    }

    @Test
    void Test4_addNewUserWithFaiLogin() {
        user.setBirthday(LocalDate.now().plusYears(1));

        ValidationException exUser = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                userController.create(user);
            }
        });
        List<User> listUser = userController.findAll();

        assertEquals(0, listUser.size(), "Список User не корректный");
        assertEquals("дата рождения не может быть в будущем", exUser.getMessage(), "Проверка на дату рождения не проходит");
    }

    @Test
    void Test5_addNewUserWithFailEmail() {
        user.setEmail("o_kyzina.mail.ru");

        ValidationException exUser = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                userController.create(user);
            }
        });

        user.setEmail("");
        ValidationException exUser2 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                userController.create(user);
            }
        });
        List<User> listUser = userController.findAll();

        assertEquals(0, listUser.size(), "Список User не корректный");
        assertEquals("электронная почта не может быть пустой и должна содержать символ @", exUser.getMessage(), "Проверка на формат email не проходит");
        assertEquals("электронная почта не может быть пустой и должна содержать символ @", exUser2.getMessage(), "Проверка на пустой email не проходит");
    }

    @Test
    void Test6_addNewUserWithFaiLogin() {
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
        assertEquals("пользователя с данным id не существует", exUser.getMessage(), "не выбрасывается исключение при неверном Id");
    }
}