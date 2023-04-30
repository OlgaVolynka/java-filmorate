package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

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
public class FilmControllerTest {
    protected Film film = new Film("Социальные сети", "фильм о создании фэйсбук", LocalDate.of(2010, 11, 28), 120);

    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    FilmService filmService = new FilmService(inMemoryFilmStorage);

    FilmController filmController = new FilmController(inMemoryFilmStorage, filmService);
    private Validator validator;


    @BeforeEach
    void init() {
        film = new Film("Социальные сети", "фильм о создании фэйсбук", LocalDate.of(2010, 11, 28), 120);

       inMemoryFilmStorage = new InMemoryFilmStorage();
      filmService = new FilmService(inMemoryFilmStorage);
        filmController = new FilmController(inMemoryFilmStorage, filmService);;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    private List<String> getValidateErrorMsg(Film validFilm) {
        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        return violations
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    @Test
    void test1_addNewFilm() {
        filmController.createFilm(film);
        List<Film> listFilm = filmController.findAll();

        assertEquals(1, listFilm.size(), "Список Film не корректный");
        assertEquals(film.getName(), listFilm.get(0).getName(), "названия фильмов не совпадают");
        assertEquals(film.getDescription(), listFilm.get(0).getDescription(), "описания фильмов не совпадают");
        assertEquals(film.getReleaseDate(), listFilm.get(0).getReleaseDate(), "Даты релиза не совпадают");
        assertEquals(film.getDuration(), listFilm.get(0).getDuration(), "Длительность фильма не совпадают");
    }

    @Test
    void test2_addNewFilmWithFailName() {
        film.setName(null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        List<String> massages = getValidateErrorMsg(film);
        assertTrue(!violations.isEmpty(), "ошибка валидации при проверке класса");

        assertEquals(2, massages.size(), "Проверка на пустое имя не проходит");
        assertTrue(massages.contains("название не может быть пустым"), "Неверное сообщение об ошибке");
    }

    @Test
    void test3_addNewFilmWithFaiDescription() {
        film.setDescription("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" +
                "++++++++++++++++++++++++++++++++++++++++++++++++++" + //50 символов
                "++++++++++++++++++++++++++++++++++++++++++++++++++" +
                "++++++++++++++++++++++++++++++++++++++++++++++++++");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        List<String> massages = getValidateErrorMsg(film);
        assertTrue(!violations.isEmpty(), "ошибка валидации при проверке класса");

        assertEquals(1, massages.size(), "Проверка на длину Description не проходит");
        assertTrue(massages.contains("максимальная длина описания — 200 символов"), "Неверное сообщение об ошибке");
    }

    @Test
    void test4_addNewFilmWithFaiReleaseDate() {
        film.setReleaseDate(LocalDate.of(1894, 1, 1));
        ValidationException exFilm = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.createFilm(film);
            }
        });

        List<Film> listFilm = filmController.findAll();

        assertEquals(0, listFilm.size(), "Список Film не корректный");
        assertEquals("400 BAD_REQUEST \"дата релиза — не раньше 28 декабря 1895 года\"", exFilm.getMessage(), "Проверка на дату релиза не проходит");
    }

    @Test
    void test5_addNewFilmWithFailDuration() {

        film.setDuration(-120);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        List<String> massages = getValidateErrorMsg(film);
        assertTrue(!violations.isEmpty(), "продолжительность фильма должна быть положительной");

        assertEquals(1, massages.size(), "Проверка на положительный Duration не проходит");
        assertTrue(massages.contains("продолжительность фильма должна быть положительной"), "Неверное сообщение об ошибке");
    }

    @Test
    void test6_updateFilmWithFailId() {
        filmController.createFilm(film);
        Film newFilm = new Film("Социальные сети", "фильм о создании фэйсбук", LocalDate.of(2010, 11, 28), 125);
        newFilm.setId(120);
        ValidationException exFilm = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.updateFilm(newFilm);
            }
        });

        List<Film> listFilm = filmController.findAll();

        assertEquals(1, listFilm.size(), "Список Film не корректный");
        assertEquals("500 INTERNAL_SERVER_ERROR \"фильма с данным id не существует\"", exFilm.getMessage(), "не выбрасывается исключение при неправильном Id");
    }
}
