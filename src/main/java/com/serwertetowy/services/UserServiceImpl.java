package com.serwertetowy.services;

import com.serwertetowy.entities.User;
import com.serwertetowy.repos.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository repository;
    @Autowired
    private ResourceLoader resourceLoader;
    @Override
    public User registerUser(User user) throws IOException {
        user.setPassword(user.getPassword());
        user.setImageData(resourceLoader.getResource("classpath:/images/defalt.jpg").getContentAsByteArray());
        repository.save(user);
        return user;
    }

    @Override
    public User registerUserWithImage(User user, MultipartFile file) throws IOException {
        user.setPassword(user.getPassword());
        user.setImageData(file.getBytes());
        repository.save(user);
        return user;
    }

    @Override
    public List<UserSummary> getAllUsers() {
        return repository.findAllUserData();
    }

    @Override
    public UserSummary getUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public User getUserById(Long id) {
        User u = repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        return u;
    }
    public Resource getUserImage(Long id){
        byte[] image = repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND)).getImageData();
        return new ByteArrayResource(image);

    }

}
