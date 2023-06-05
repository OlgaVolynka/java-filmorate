package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Film {
    public interface UpdateId {
    }

    @NotNull(groups = {UpdateId.class})
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
    private Mpa mpa;
    private Long rate;

    public Film(long id, String name, String description, LocalDate releaseDate, long duration) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;

    }

    public Film() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
