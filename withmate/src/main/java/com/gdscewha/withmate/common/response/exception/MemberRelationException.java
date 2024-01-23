package com.gdscewha.withmate.common.response.exception;

public class MemberRelationException  extends DefaultException {
    public MemberRelationException(ErrorCode statusCode) {
        super(statusCode);
    }
}