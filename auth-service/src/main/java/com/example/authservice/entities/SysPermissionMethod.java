package com.example.authservice.entities;

public enum SysPermissionMethod {
    GET(0),
    POST(1),
    PUT(2),
    DELETE(3);

    private final Integer value;

    private SysPermissionMethod(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }
}
