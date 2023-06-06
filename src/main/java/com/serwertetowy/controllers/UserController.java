package com.serwertetowy.controllers;

import com.serwertetowy.entities.User;
import com.serwertetowy.services.UserService;
import com.serwertetowy.services.dto.SeriesSummary;
import com.serwertetowy.services.dto.UserSeriesSummary;
import com.serwertetowy.services.dto.UserSummary;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private UserService userService;
    record WatchlistDto(SeriesSummary seriesSummary, String watchFlag){}
    //register user with file set in form-data
    @PostMapping(value = "/api/v1/user/register")
    public ResponseEntity<User> register(@RequestParam String firstname, @RequestParam String lastname,
                                         @RequestParam String email, @RequestParam String password ,
                                         @RequestParam(required = false) MultipartFile file) throws IOException {
        User user = new User(firstname, lastname, email, password);
        if(file == null) return new ResponseEntity<>(userService.registerUser(user), HttpStatus.OK);
        else return new ResponseEntity<>(userService.registerUserWithImage(user, file), HttpStatus.OK);
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
    @GetMapping("/api/v1/user/{id}/watchlist")
    public ResponseEntity<List<WatchlistDto>> getUserWatchlist(@PathVariable Long id){
        List<UserSeriesSummary> userSeriesSummaryList = userService.getWatchlist(id);
        List<WatchlistDto> watchlistDtoList = new ArrayList<>();
        for(UserSeriesSummary userSeriesSummary: userSeriesSummaryList){
            watchlistDtoList.add(new WatchlistDto(userSeriesSummary.getSeriesSummary(), userSeriesSummary.getWatchflag().getName()));
        }
        return new ResponseEntity<>(watchlistDtoList, HttpStatus.OK);
    }
    @PutMapping("/api/v1/user/{id}/image")
    ResponseEntity<Void> putUserImage(@PathVariable Long id, @RequestParam MultipartFile file) throws IOException {
        userService.putUserImage(file, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
