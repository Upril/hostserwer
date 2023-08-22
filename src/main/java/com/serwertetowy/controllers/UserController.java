package com.serwertetowy.controllers;

import com.serwertetowy.exceptions.*;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.serwertetowy.auth.AuthenticationService.getIdentity;

@RestController
@AllArgsConstructor
public class UserController {
    @Autowired
    AuthenticationManager authenticationManager;

    private UserService userService;

    //dto for neat request response data construction, used to add watchflag info to series in watchlist
    record WatchlistDto(SeriesSummary seriesSummary, String watchFlag){}

    //get all users with only id, names and email, has to be authenticated as an admin or manager
    @GetMapping("/api/v1/user/all")
    public ResponseEntity<List<UserSummary>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    //get user with only id, name and email, has to be authenticated
    @GetMapping("/api/v1/user/{id}")
    ResponseEntity<UserSummary> getUser(@PathVariable @Min(1) Long id){
        String authIdentity = getIdentity();
        return new ResponseEntity<>(userService.getUserSummaryById(id,authIdentity), HttpStatus.OK);
    }

    //get image for given user
    @GetMapping(value = "/api/v1/user/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    ResponseEntity<Resource> getUserImage(@PathVariable @Min(1) Long id){
        return new ResponseEntity<>(userService.getUserImage(id), HttpStatus.OK);
    }

    //put request to change user profile picture
    @PutMapping("/api/v1/user/{id}/image")
    ResponseEntity<Void> putUserImage(@PathVariable @Min(1) Long id, @RequestParam @NotBlank @NotNull MultipartFile file) throws IOException, FileEmptyException {
        String authIdentity = getIdentity();
        userService.putUserImage(file, id, authIdentity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //put request to change user data, has to be authenticated
    @PutMapping("/api/v1/user/{id}")
    ResponseEntity<UserSummary> putUser(@PathVariable @Min(1) Long id, @RequestParam(required = false) @NotNull String firstname, @RequestParam(required = false) @NotNull String lastname, @RequestParam(required = false) @NotNull String email){
        String authIdentity = getIdentity();
        return new ResponseEntity<>(userService.putUser(id,firstname,lastname,email,authIdentity),HttpStatus.OK);
    }

    //mark a user as deleted - ban a user, user cant be changed and access server features
    @DeleteMapping("/api/v1/user/delete/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable @Min(1) Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //unban the user, must be a manager or admin
    @PutMapping("/api/v1/user/restore/{id}")
    ResponseEntity<Void> restoreUser(@PathVariable @Min(1) Long id){
        userService.restoreUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //method used to construct error messages to be returned in a response
    private Map<String,String> messageCreator(Exception ex){
        Map<String,String> errors = new HashMap<>();
        errors.put("error",ex.getMessage());
        return errors;
    }

    //exception handlers for capturing input errors and returning error messages
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            UserNotFoundException.class,
            UserNotDeletedException.class,
            UserDeletedException.class,
            MultipartException.class,
            FileEmptyException.class,
            FailedToAuthenticateException.class,
    })
    public Map<String,String> handleExceptions(Exception ex){
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
