package com.serwertetowy.controllers;

import com.serwertetowy.entities.WatchFlags;
import com.serwertetowy.exceptions.WatchflagNotFoundException;
import com.serwertetowy.repos.WatchFlagRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        return watchFlag.map(watchFlags -> new ResponseEntity<>(watchFlags, HttpStatus.OK)).orElseThrow(WatchflagNotFoundException::new);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(WatchflagNotFoundException.class)
    public Map<String,String> handleConstraintExceptions(WatchflagNotFoundException ex){
        Map<String,String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String name = ((FieldError) error).getField();
            String msg = error.getDefaultMessage();
            errors.put(name,msg);
        });
        return errors;
    }


}
