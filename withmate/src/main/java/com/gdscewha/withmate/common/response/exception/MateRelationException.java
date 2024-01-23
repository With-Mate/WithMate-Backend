package com.gdscewha.withmate.common.response.exception;

public class MateRelationException extends DefaultException {
    public MateRelationException(ErrorCode statusCode){
        super(statusCode);
    }
}