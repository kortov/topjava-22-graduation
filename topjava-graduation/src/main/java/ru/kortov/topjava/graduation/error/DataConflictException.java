package ru.kortov.topjava.graduation.error;

// TODO: extend AppException
public class DataConflictException extends RuntimeException {
    public DataConflictException(String msg) {
        super(msg);
    }
}