package com.smunity.server.global.exception;

public class DepartmentNotFoundException extends RuntimeException {

    public DepartmentNotFoundException(String name) {
        super("Department '%s' not found".formatted(name));
    }
}
