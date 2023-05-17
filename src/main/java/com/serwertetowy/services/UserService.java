package com.serwertetowy.services;

import com.serwertetowy.entities.User;

import java.util.List;

public interface UserService {
    User registerUser(User user);
    List<User> getAllUsers();
    User getUserByEmail(String email);
    User getUserById(Long id);
}
