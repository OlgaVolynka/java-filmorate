package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.sql.In;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Film {

    private long id;
    @NotNull(message = "название не может быть пустым")
    @NotBlank(message = "название не может быть пустым")
    private String name;
    @Size(min = 0, max = 200, message = "максимальная длина описания — 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "продолжительность фильма должна быть положительной")
    private long duration;
    private List<Genre> genres;
    private Mpa mpa = Mpa.G;
    private Integer rate = null;


    public Film(String name, String description, LocalDate releaseDate, long duration) {

        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;

    }

    public Film() {

    }

    public List<Genre> getGenres() {
        return genres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id && duration == film.duration && Objects.equals(name, film.name) && Objects.equals(description, film.description) && Objects.equals(releaseDate, film.releaseDate) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, releaseDate, duration);
    }
}
