package com.example.notfound_backend.exception;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException() {
        super("접근 권한이 없습니다.");
    }
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
