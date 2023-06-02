package com.serwertetowy.controllers;

import com.serwertetowy.entities.User;
import com.serwertetowy.services.UserService;
import com.serwertetowy.services.UserSummary;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping(value = "/api/v1/user/register", params = "file")
    public ResponseEntity<User> register(@RequestBody User user, @RequestParam("file")MultipartFile file) throws IOException {
        return new ResponseEntity<>(userService.registerUserWithImage(user, file), HttpStatus.OK);
    }
    @PostMapping("/api/v1/user/register")
    public ResponseEntity<User> registerNoImage(@RequestBody User user) throws IOException {
        return new ResponseEntity<>(userService.registerUser(user), HttpStatus.OK);
    }
    @GetMapping("/api/v1/user/all")
    public ResponseEntity<List<UserSummary>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
    @GetMapping("/api/v1/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id){
        Long i = Long.valueOf(id);
        return new ResponseEntity<>(userService.getUserById(i), HttpStatus.OK);
    }
    @GetMapping(value = "/api/v1/user/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    ResponseEntity<Resource> getUserImage(@PathVariable Long id){
        return new ResponseEntity<>(userService.getUserImage(id), HttpStatus.OK);
    }
}
