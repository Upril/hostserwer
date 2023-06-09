package com.serwertetowy.services;

import com.serwertetowy.entities.User;
import com.serwertetowy.services.dto.RatingSummary;
import com.serwertetowy.services.dto.UserSeriesSummary;
import com.serwertetowy.services.dto.UserSummary;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    //methods to save a new user with or without sent profile picture
    User registerUser(User user) throws IOException;
    User registerUserWithImage(User user, MultipartFile file) throws IOException;
    //method to retrieve list of users from db without profile pics
    List<UserSummary> getAllUsers();
    //get user from db without profile pics
    UserSummary getUserByEmail(String email);
    //get all user info
    User getUserById(Long id);
    List<RatingSummary> getUserRatingsById(Long id);
    //get only user profile pic
    Resource getUserImage(Long id);
    //get watchlist info for given user
    List<UserSeriesSummary> getWatchlist(Long id);
    //update user profile pic and user data
    void putUserImage(MultipartFile file, Long id)throws IOException;
    UserSummary putUser(Long id, String firstname, String lastname, String email);
}
