package com.api.parkings.users.model;

import org.springframework.context.support.BeanDefinitionDsl.Role;

public enum EnumRole {
    ADMIN,
    SOCIO;

    public static Role fromString(String roleStr) {
        try {
            return Role.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}