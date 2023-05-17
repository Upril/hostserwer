package com.serwertetowy.services;

import com.serwertetowy.entities.User;
import com.serwertetowy.exceptions.UserNotFoundException;
import com.serwertetowy.repos.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository repository;
    @Override
    public User registerUser(User user) {
        user.setPassword(user.getPassword());
        repository.save(user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> u = repository.findById(id);
        if(u.isPresent()) return u.get();
        else throw new UserNotFoundException();
    }

}
