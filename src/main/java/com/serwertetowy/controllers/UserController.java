package com.serwertetowy.controllers;

import com.serwertetowy.entities.User;
import com.serwertetowy.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping("/api/v1/user/register")
    public ResponseEntity<User> register(@RequestBody User user){
        return new ResponseEntity<>(userService.registerUser(user), HttpStatus.OK);
    }
    @GetMapping("/api/v1/user/all")
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
    @GetMapping("/api/v1/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id){
        Long i = Long.valueOf(id);
        return new ResponseEntity<>(userService.getUserById(i), HttpStatus.OK);
    }
}
