package com.smunity.server.global.exception;

public class DepartmentNotFoundException extends RuntimeException {

    public DepartmentNotFoundException(String name) {
        super(String.format("Department '%s' not found.", name));
    }
}
