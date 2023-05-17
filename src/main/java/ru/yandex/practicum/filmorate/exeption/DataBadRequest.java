package ru.yandex.practicum.filmorate.exeption;

public class DataBadRequest extends RuntimeException {

    public DataBadRequest(String message) {
        super(message);
    }
}
