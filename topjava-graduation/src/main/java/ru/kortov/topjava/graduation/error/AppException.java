package ru.kortov.topjava.graduation.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AppException extends ResponseStatusException {

    public AppException(HttpStatus status, String message) {
        super(status, message);
    }

    @Override
    public String getMessage() {
        return getReason();
    }
}
