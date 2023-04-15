package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class filmControllerTest {
    protected Film film = new Film();
    FilmController filmController = new FilmController();

    @BeforeEach
    void init() {
        film = new Film(0, "Социальные сети", "фильм о создании фэйсбук", LocalDate.of(2010, 11, 28), 120);
        filmController = new FilmController();
    }

    @Test
    void Test1_addNewFilm() {
        filmController.createFilm(film);
        List<Film> listFilm = filmController.findAll();

        assertEquals(1, listFilm.size(), "Список Film не корректный");
        assertEquals(film.getName(), listFilm.get(0).getName(), "названия фильмов не совпадают");
        assertEquals(film.getDescription(), listFilm.get(0).getDescription(), "описания фильмов не совпадают");
        assertEquals(film.getReleaseDate(), listFilm.get(0).getReleaseDate(), "Даты релиза не совпадают");
        assertEquals(film.getDuration(), listFilm.get(0).getDuration(), "Длительность фильма не совпадают");
    }

    @Test
    void Test2_addNewFilmWithFailName() {
        film.setName("");

        ValidationException exFilm = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.createFilm(film);
            }
        });
        List<Film> listFilm = filmController.findAll();

        assertEquals(0, listFilm.size(), "Список Film не корректный");
        assertEquals("название не может быть пустым", exFilm.getMessage(), "Проверка на пустое имя не проходит");
    }

    @Test
    void Test3_addNewFilmWithFaiDescription() {
        film.setDescription("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" +
                "++++++++++++++++++++++++++++++++++++++++++++++++++" + //50 символов
                "++++++++++++++++++++++++++++++++++++++++++++++++++" +
                "++++++++++++++++++++++++++++++++++++++++++++++++++");

        ValidationException exFilm = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.createFilm(film);
            }
        });
        List<Film> listFilm = filmController.findAll();

        assertEquals(0, listFilm.size(), "Список Film не корректный");
        assertEquals("максимальная длина описания — 200 символов", exFilm.getMessage(), "Проверка на длину Description не проходит");
    }

    @Test
    void Test4_addNewFilmWithFaiReleaseDate() {
        film.setReleaseDate(LocalDate.of(1894, 1, 1));
        ValidationException exFilm = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.createFilm(film);
            }
        });

        List<Film> listFilm = filmController.findAll();

        assertEquals(0, listFilm.size(), "Список Film не корректный");
        assertEquals("дата релиза — не раньше 28 декабря 1895 года", exFilm.getMessage(), "Проверка на дату релиза не проходит");
    }

    @Test
    void Test5_addNewFilmWithFailDuration() {
        film.setDuration(-120);
        ValidationException exFilm = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.createFilm(film);
            }
        });

        List<Film> listFilm = filmController.findAll();

        assertEquals(0, listFilm.size(), "Список Film не корректный");
        assertEquals("продолжительность фильма должна быть положительной", exFilm.getMessage(), "Проверка на положительный Duration не проходит");
    }

    @Test
    void Test6_updateFilmWithFailId() {
        filmController.createFilm(film);
        Film newFilm = new Film(0, "Социальные сети", "фильм о создании фэйсбук", LocalDate.of(2010, 11, 28), 125);

        ValidationException exFilm = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.updateFilm(newFilm);
            }
        });

        List<Film> listFilm = filmController.findAll();

        assertEquals(1, listFilm.size(), "Список Film не корректный");
        assertEquals("фильма с данным id не существует", exFilm.getMessage(), "не выбрасывается исключение при неправильном Id");
    }
}
