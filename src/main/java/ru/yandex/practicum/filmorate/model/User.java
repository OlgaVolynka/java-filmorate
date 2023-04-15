package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class User {
    protected int id;
    protected String email;
    protected String login;
    protected String name;
    protected LocalDate birthday;

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
