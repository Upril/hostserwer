package com.serwertetowy.controllers;

import com.serwertetowy.entities.Series;
import com.serwertetowy.exceptions.SeriesNotFoundException;
import com.serwertetowy.services.SeriesService;
import com.serwertetowy.services.dto.SeriesSummary;
import com.serwertetowy.services.dto.UserSeriesSummary;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("api/v1/series")
public class SeriesController {
    private SeriesService seriesService;
    //post series method, a series needs to exist before the episode upload
    @PostMapping
    public ResponseEntity<SeriesSummary> saveSeries(@RequestParam(required = false) @NotBlank(message = "Name is mandatory") @Size(min = 1, message = "Name is mandatory") String name,
                                                    @RequestParam(required = false) @NotBlank(message = "Description is mandatory") @Size(min = 1,message = "Description is mandatory") String description,
                                                    @RequestParam(required = false) List<Integer> tags, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        Series series;
        if (file == null) series = seriesService.saveSeries(name,description,tags);
        else series = seriesService.saveSeriesWithImage(file,name,description,tags);
        SeriesSummary seriesSummary = seriesService.getSeriesById(series.getId().intValue());
        return new ResponseEntity<>(seriesSummary,HttpStatus.OK);
    }
    //get request for all series info without the episode video including detailed tag information
    @GetMapping
    public ResponseEntity<List<SeriesSummary>> getAllSeries(){
        return new ResponseEntity<>(seriesService.getAllSeries(), HttpStatus.OK);
    }
    //get request for given series info without the episode video including detailed tag information
    @GetMapping("/{id}")
    public ResponseEntity<SeriesSummary> getSeriesById(@PathVariable("id") @Min(1) Integer id){
        return new ResponseEntity<>(seriesService.getSeriesById(id), HttpStatus.OK);
    }
    @GetMapping(value = "/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @Transactional
    public Mono<Resource> getSeriesImage(@PathVariable("id") @Min(1) Integer id){
        return seriesService.getSeriesImageData(id);
    }
    //post method allowing the user to add a given series into their watchlist, may move it to the user controller
    @Deprecated
    @PostMapping("/addToWatchlist")
    public ResponseEntity<UserSeriesSummary> addSeriesToWatchlist(@RequestParam Integer seriesId, @RequestParam Integer userId, @RequestParam Integer watchflagId){
        return new ResponseEntity<>(seriesService.addToWatchlist(seriesId,userId, watchflagId), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Map<String,String> handleMissingRequestParameterExceptions(MissingServletRequestParameterException ex){
        Map<String,String> errors = new HashMap<>();
        errors.put(ex.getParameterName(),ex.getMessage());
        return errors;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String,String> handleConstraintExceptions(ConstraintViolationException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach((error) -> {
            String name = String.valueOf(error.getPropertyPath()).substring(11);
            String msg =  error.getMessage();
            errors.put(name,msg);
        });
        return errors;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Map<String,String> handleMethodArgumentTypeMismatchExceptions(MethodArgumentTypeMismatchException ex){
        Map<String,String> errors = new HashMap<>();
        errors.put(ex.getName(), "Id not valid");
        return errors;
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SeriesNotFoundException.class)
    public Map<String,String> handleSeriesNotFoundExceptions(SeriesNotFoundException ex){
        Map<String,String> errors = new HashMap<>();
        errors.put("error",ex.getMessage());
        return errors;
    }



}
