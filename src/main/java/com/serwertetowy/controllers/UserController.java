package com.serwertetowy.controllers;

import com.serwertetowy.exceptions.FileEmptyException;
import com.serwertetowy.exceptions.UserDeletedException;
import com.serwertetowy.exceptions.UserNotDeletedException;
import com.serwertetowy.exceptions.UserNotFoundException;
import com.serwertetowy.services.UserService;
import com.serwertetowy.services.dto.SeriesSummary;
import com.serwertetowy.services.dto.UserSummary;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class UserController {
    @Autowired
    AuthenticationManager authenticationManager;
    private UserService userService;
    //dto for neat request response data construction, used to add watchflag info to series in watchlist
    record WatchlistDto(SeriesSummary seriesSummary, String watchFlag){}
    //get all users with only id, names and email
    @GetMapping("/api/v1/user/all")
    public ResponseEntity<List<UserSummary>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
    @GetMapping("/api/v1/user/{id}")
    ResponseEntity<UserSummary> getUser(@PathVariable @Min(1) Long id){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String authIdentity = null;
        if (auth != null){
            authIdentity = auth.getName();
        }
        return new ResponseEntity<>(userService.getUser(id,authIdentity), HttpStatus.OK);
    }
    //get image for given user
    @GetMapping(value = "/api/v1/user/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    ResponseEntity<Resource> getUserImage(@PathVariable @Min(1) Long id){
        return new ResponseEntity<>(userService.getUserImage(id), HttpStatus.OK);
    }
    //put request to change user profile picture
    @PutMapping("/api/v1/user/{id}/image")
    ResponseEntity<Void> putUserImage(@PathVariable @Min(1) Long id, @RequestParam @NotBlank @NotNull MultipartFile file) throws IOException, FileEmptyException {
        userService.putUserImage(file, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/api/v1/user/{id}")
    ResponseEntity<UserSummary> putUser(@PathVariable @Min(1) Long id, @RequestParam(required = false) @NotNull String firstname, @RequestParam(required = false) @NotNull String lastname, @RequestParam(required = false) @NotNull String email){
        return new ResponseEntity<>(userService.putUser(id,firstname,lastname,email),HttpStatus.OK);
    }
    @DeleteMapping("/api/v1/user/delete/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable @Min(1) Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/api/v1/user/restore/{id}")
    ResponseEntity<Void> restoreUser(@PathVariable @Min(1) Long id){
        userService.restoreUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    private Map<String,String> messageCreator(Exception ex){
        Map<String,String> errors = new HashMap<>();
        errors.put("error",ex.getMessage());
        return errors;
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public Map<String,String> handleUserNotFoundExceptions(UserNotFoundException ex){
        return messageCreator(ex);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotDeletedException.class)
    public Map<String,String> handleUserNotFoundExceptions(UserNotDeletedException ex){
        return messageCreator(ex);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserDeletedException.class)
    public Map<String,String> handleUserDeletedExceptions(UserDeletedException ex){
        return messageCreator(ex);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MultipartException.class)
    public Map<String,String> handleMultipartExceptions(MultipartException ex){
        return messageCreator(ex);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FileEmptyException.class)
    public Map<String,String> handleFileEmptyExceptions(FileEmptyException ex){
        return messageCreator(ex);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String,String> handleConstraintExceptions(ConstraintViolationException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach((error) -> {
            String name = String.valueOf(error.getPropertyPath());
            String msg =  error.getMessage();
            errors.put(name,msg);
        });
        return errors;
    }
}
