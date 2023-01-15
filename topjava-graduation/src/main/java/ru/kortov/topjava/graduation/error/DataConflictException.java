package ru.kortov.topjava.graduation.error;

import org.springframework.http.HttpStatus;

public class DataConflictException extends AppException {
    public DataConflictException(String msg) {
        super(HttpStatus.CONFLICT, msg);
    }
}