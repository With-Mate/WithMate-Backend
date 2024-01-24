package com.gdscewha.withmate.common.response.exception;

public class JourneyException extends DefaultException {
public JourneyException(ErrorCode statusCode){
        super(statusCode);
        }
}