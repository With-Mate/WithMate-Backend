package com.gdscewha.withmate.common.response.exception;

public class WeekException extends DefaultException {
    public WeekException(ErrorCode statusCode){
        super(statusCode);
    }
}