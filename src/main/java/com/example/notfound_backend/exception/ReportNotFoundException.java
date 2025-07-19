package com.example.notfound_backend.exception;

public class ReportNotFoundException extends RuntimeException {
    public ReportNotFoundException() {
      super("Report not found 삭제할 신고 존재하지 않음.");
    }
    public ReportNotFoundException(String message) {
        super(message);
    }
}
