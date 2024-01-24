package com.gdscewha.withmate.common.response.exception;

public class StickerException extends DefaultException {
    public StickerException(ErrorCode statusCode){
        super(statusCode);
    }
}
