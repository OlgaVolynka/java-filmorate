package ru.yandex.practicum.filmorate.exeption;

public class DataAlreadyExist extends RuntimeException {
    public DataAlreadyExist(String message){

        super(message);
    }
}
