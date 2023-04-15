package ru.yandex.practicum.filmorate.controller;

public class ValidationException extends Error {
    public ValidationException(String message) {
        super(message);
    }
}
