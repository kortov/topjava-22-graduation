package ru.kortov.topjava.graduation.exception;

// TODO: extend AppException
public class DataConflictException extends RuntimeException {
    public DataConflictException(String msg) {
        super(msg);
    }
}