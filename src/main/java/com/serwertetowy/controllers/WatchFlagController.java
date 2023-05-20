package com.serwertetowy.controllers;

import com.serwertetowy.entities.User;
import com.serwertetowy.entities.WatchFlags;
import com.serwertetowy.repos.WatchFlagRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/watchflag")
@AllArgsConstructor
public class WatchFlagController {
    private WatchFlagRepository watchFlagRepository;
    record WatchFlagRequest(String flag){}
    @PostMapping
    public ResponseEntity<WatchFlags> saveWatchFlag(@RequestBody WatchFlagRequest request){
        WatchFlags watchFlag = new WatchFlags();
        watchFlag.setName(request.flag);
        watchFlagRepository.save(watchFlag);
        return new ResponseEntity<>(watchFlag, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<WatchFlags>> getAllWatchFlags(){
        return new ResponseEntity<>(watchFlagRepository.findAll(),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<WatchFlags> getWatchFlag(@PathVariable("id") Integer id){
        Optional<WatchFlags> watchFlag = watchFlagRepository.findById(id);
        return watchFlag.map(watchFlags -> new ResponseEntity<>(watchFlags, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
