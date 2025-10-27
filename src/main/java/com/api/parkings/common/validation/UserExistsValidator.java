package com.api.parkings.common.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.parkings.users.service.IUsersService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UserExistsValidator implements ConstraintValidator<UserExists, Integer> {

    @Autowired
    private IUsersService usersService;

    @Override
    public boolean isValid(Integer ownerId, ConstraintValidatorContext context) {
        if (ownerId == null) {
            return true;
        }

        try {
            return usersService.getUserById(ownerId) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
