package ru.kortov.topjava.graduation.error;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends AppException {
    public EntityNotFoundException(String msg) {
        super(HttpStatus.NOT_FOUND, msg);
    }
}