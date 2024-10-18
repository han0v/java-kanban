package com.faiz.tasktreker.exceptions;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(final String message) {
        super(message);
    }

    public ManagerSaveException(String message, IOException e) {
        super(message, e);
    }

    // Добавленный конструктор для IllegalArgumentException
    public ManagerSaveException(String message, IllegalArgumentException e) {
        super(message, e);
    }
}