package com.techniners.EmailApp.data.models;

public enum RoleType {
    ROLE_ADMIN("ADMIN_ROLE"), ROLE_USER("ROLE_USER");
    private final String roleType;

    RoleType(String roleType){
        this.roleType = roleType;
    }
}
