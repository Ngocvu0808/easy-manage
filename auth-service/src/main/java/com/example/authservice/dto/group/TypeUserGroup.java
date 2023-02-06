package com.example.authservice.dto.group;

public enum TypeUserGroup {
    USER(0),
    GROUP(1);

    private final Integer value;

    private TypeUserGroup(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }
}
