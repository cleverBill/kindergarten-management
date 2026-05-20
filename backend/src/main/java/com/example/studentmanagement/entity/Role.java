package com.example.studentmanagement.entity;

public enum Role {
    ROLE_SUPER_ADMIN("超级管理员"),
    ROLE_PRINCIPAL("园长"),
    ROLE_TEACHER("教师"),
    ROLE_FINANCE("财务"),
    ROLE_CLEANER("保洁"),
    ROLE_USER("普通用户");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
