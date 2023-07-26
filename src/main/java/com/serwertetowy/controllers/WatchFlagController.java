package com.serwertetowy.controllers;

import com.serwertetowy.entities.WatchFlags;
import com.serwertetowy.repos.WatchFlagRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    public record WatchFlagRequest(@NotBlank(message = "Flag name is mandatory") @Size(min = 1, message = "Flag name can't be empty") String flag){}
    //save new watchflag using the record with flag data
    @PostMapping
    public ResponseEntity<WatchFlags> saveWatchFlag(@RequestBody @Valid WatchFlagRequest request){
        WatchFlags watchFlag = new WatchFlags();
        watchFlag.setName(request.flag);
        watchFlagRepository.save(watchFlag);
        return new ResponseEntity<>(watchFlag, HttpStatus.OK);
    }
    //get request for all watchflags
    @GetMapping
    public ResponseEntity<List<WatchFlags>> getAllWatchFlags(){
        return new ResponseEntity<>(watchFlagRepository.findAll(),HttpStatus.OK);
    }
    //get request for specific watchflag
    @GetMapping("/{id}")
    public ResponseEntity<WatchFlags> getWatchFlag(@PathVariable("id") @Min(1) Integer id){
        Optional<WatchFlags> watchFlag = watchFlagRepository.findById(id);
        return watchFlag.map(watchFlags -> new ResponseEntity<>(watchFlags, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}
