package com.gdscewha.withmate.common.response.exception;

public class CategoryException extends DefaultException {
    public CategoryException(ErrorCode statusCode) {
        super(statusCode);
    }
}