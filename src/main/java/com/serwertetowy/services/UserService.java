package com.serwertetowy.services;

import com.serwertetowy.entities.User;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User registerUser(User user) throws IOException;
    User registerUserWithImage(User user, MultipartFile file) throws IOException;
    List<UserSummary> getAllUsers();
    UserSummary getUserByEmail(String email);
    User getUserById(Long id);
    Resource getUserImage(Long id);
}
