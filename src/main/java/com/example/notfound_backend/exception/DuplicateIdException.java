package com.example.notfound_backend.exception;

// 사용자정의 예외 - ID중복
public class DuplicateIdException extends RuntimeException {
    public DuplicateIdException(String message) {
        super(message);
    }
}
