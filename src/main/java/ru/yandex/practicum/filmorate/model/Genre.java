package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Builder
@Getter
@Setter
public class Genre {





    private  Integer id;
    private String name;

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
/*

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Genre {
    COMEDY(1, "Комедия"),
    DRAMA(2, "Драма"),
    CARTOON(3, "Мультфильм"),
    THRILLER(4, "Триллер"),
    DOCUMENTARY(5, "Документальный"),
    ACTION(6, "Боевик");

    private final int id;
    private final String name;

    Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @JsonCreator
    public static Genre forValues(@JsonProperty("id")int id) {
        for (Genre genre : Genre.values()) {
            if (genre.id == id) {
                return genre;
            }
        }

        throw new DataNotFoundException ("не найден жанр");
    }*/

