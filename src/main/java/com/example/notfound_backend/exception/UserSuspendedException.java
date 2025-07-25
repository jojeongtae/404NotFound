package com.example.notfound_backend.exception;

public class UserSuspendedException extends RuntimeException {
    public UserSuspendedException(String message) {
        super(message);
    }
    public UserSuspendedException() {
        super("활동 정지된 사용자 입니다.");
    }
}
