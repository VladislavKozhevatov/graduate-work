package ru.skypro.homework.Exception;

public class InvalidFileTypeException extends RuntimeException {
    public InvalidFileTypeException(String message) {
        super(message);
    }
}
