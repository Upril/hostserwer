package com.serwertetowy.services;

import com.serwertetowy.entities.User;
import com.serwertetowy.exceptions.FileEmptyException;
import com.serwertetowy.services.dto.UserSummary;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    //method to retrieve list of users from db without profile pics
    List<UserSummary> getAllUsers();
    //get user from db without profile pics
    UserSummary getUserByEmail(String email);
    //get all user info
    User getUserById(Long id);
    //get only user profile pic
    Resource getUserImage(Long id);
    //update user profile pic and user data
    void putUserImage(MultipartFile file, Long id, String authIdentity) throws IOException, FileEmptyException;
    UserSummary putUser(Long id, String firstname, String lastname, String email,String authIdentity);
    void deleteUser(Long id);
    void restoreUser(Long id);
    UserSummary getUserSummaryById(Long id,String authIdentity);
}
