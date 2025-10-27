package com.api.parkings.users.service;

import java.util.List;

import com.api.parkings.users.model.Users;
import com.api.parkings.users.model.EnumRole;

public interface IUsersService {
    public List<Users> getAllUsers();

    public Users getUserById(Integer id);

    public Users createUser(Users user);

    public Users updateUser(Integer id, Users user);

    public void deleteUser(Integer id);

    public Users findByEmail(String email);

    public void createUserIfNotExists(String email, String password, EnumRole role);
}
