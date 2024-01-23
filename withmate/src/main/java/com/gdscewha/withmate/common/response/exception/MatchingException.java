package com.gdscewha.withmate.common.response.exception;

public class MatchingException extends DefaultException {
    public MatchingException(ErrorCode statusCode) {
        super(statusCode);
    }
}