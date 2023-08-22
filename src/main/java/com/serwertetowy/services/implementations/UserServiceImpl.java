package com.serwertetowy.services.implementations;

import com.serwertetowy.entities.User;
import com.serwertetowy.exceptions.*;
import com.serwertetowy.repos.*;
import com.serwertetowy.services.EpisodesService;
import com.serwertetowy.services.UserService;
import com.serwertetowy.services.dto.UserSummary;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    //method to retrieve list of users from db
    @Override
    public List<UserSummary> getAllUsers() {
        return userRepository.findAllUserData();
    }

    //get user from db
    @Override
    public UserSummary getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //get user data by id
    @Override
    public UserSummary getUserSummaryById(Long id, String authIdentity) {
        if(userRepository.findById(id).orElseThrow(UserNotFoundException::new).isDeleted()) throw new UserDeletedException();
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if(!Objects.equals(user.getEmail(), authIdentity)) throw new FailedToAuthenticateException();
        return new UserSummary() {
            @Override
            public Long getId() {
                return user.getId();
            }

            @Override
            public String getFirstname() {
                return user.getFirstname();
            }

            @Override
            public String getLastname() {
                return user.getLastname();
            }

            @Override
            public String getEmail() {
                return user.getEmail();
            }

            @Override
            public Boolean getDeleted() {
                return user.isDeleted();
            }
        };
    }
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    //get user profile picture
    @Override
    public Resource getUserImage(Long id){
        if(!userRepository.existsById(id)) throw new UserNotFoundException();
        byte[] image = userRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getImageData();
        return new ByteArrayResource(image);
    }

    //verify user as the request sender for put requests
    private User getUserByIdAndAuthenticate(Long id, String authIdentity){
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if (!Objects.equals(user.getEmail(), authIdentity)) {
            throw new FailedToAuthenticateException();
        }
        return user;
    }
    //change profile picture for user
    @Override
    public void putUserImage(MultipartFile file, Long id, String authIdentity) throws IOException, FileEmptyException {
        if(userRepository.findById(id).orElseThrow(UserNotFoundException::new).isDeleted()) throw new UserDeletedException();
        if (file.isEmpty()) throw new FileEmptyException("Image file is mandatory");
        User user = getUserByIdAndAuthenticate(id,authIdentity);
        user.setImageData(file.getBytes());
        userRepository.save(user);
    }

    //change user data
    @Override
    public UserSummary putUser(Long id, String firstname, String lastname, String email,String authIdentity) {
        if(userRepository.findById(id).orElseThrow(UserNotFoundException::new).isDeleted()) throw new UserDeletedException();
        User user = getUserByIdAndAuthenticate(id, authIdentity);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        userRepository.save(user);
        return userRepository.findByEmail(email);
    }

    //ban user
    @Override
    public void deleteUser(Long id) {
        if(userRepository.findById(id).orElseThrow(UserNotFoundException::new).isDeleted()) throw new UserDeletedException();
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.setDeleted(true);
        userRepository.save(user);
    }

    //unban user
    @Override
    public void restoreUser(Long id) {
        if(userRepository.findById(id).orElseThrow(UserNotFoundException::new).isDeleted()){
            User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
            user.setDeleted(false);
            userRepository.save(user);
        } else {
            throw new UserNotDeletedException();
        }
    }


}
