package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;


@Getter
@Setter
public class Film {

    protected long id;

    protected String name;

    protected String description;
    protected LocalDate releaseDate;
    protected long duration;

    public Film(long id, String name, String description, LocalDate releaseDate, long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film() {
    }
}
