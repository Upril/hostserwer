package com.serwertetowy.controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.serwertetowy.exceptions.EpisodeNotFoundException;
import com.serwertetowy.exceptions.FileEmptyException;
import com.serwertetowy.exceptions.FileUploadException;
import com.serwertetowy.exceptions.SeriesNotFoundException;
import com.serwertetowy.services.EpisodesService;
import com.serwertetowy.services.dto.EpisodeSummary;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("api/v1/episode")
@AllArgsConstructor
public class EpisodesController {
    private EpisodesService episodesService;
    private final AmazonS3 s3Client;
    //episode saving in /target/classes/videos, to change to a cloud based file storage
    record EpisodesPutRequest(@Size(min = 1, max = 256) @NotBlank String name, @NotNull @NotEmpty List<String> languagesList, @Min(1) @NotNull Integer seriesId){}
    @PostMapping()
    public ResponseEntity<EpisodeSummary> saveEpisode(@RequestParam(value = "file") @NotNull MultipartFile file,
                                                      @RequestParam(value = "name") @NotBlank(message = "Name is mandatory") @NotNull(message = "Name is mandatory") @Size(min = 1, message = "Name is mandatory") String name,
                                                      @RequestParam(value = "languages") @NotNull @NotEmpty List<String> languagesList,
                                                      @RequestParam(value = "seriesId") @NotNull(message = "Name is mandatory") @Min(1) Integer seriesId) throws IOException, FileEmptyException {
        if (file == null || file.isEmpty()){
            throw new FileEmptyException("File is empty. Cannot save an empty file");
        }
        boolean isValidFile = isValidFile(file);
        List<String> allowedFileExtensions = new ArrayList<>(List.of("mp4","mov"));
        if (isValidFile && allowedFileExtensions.contains(FilenameUtils.getExtension(file.getOriginalFilename()))){
            return new ResponseEntity<>(episodesService.saveEpisode(file,name,languagesList,seriesId),HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    //method for updating episode video data
    @PutMapping("/{id}/data")
    public ResponseEntity<EpisodeSummary>putEpisodeData(@PathVariable("id") Long id, @RequestParam("file")MultipartFile file) throws IOException {
        return new ResponseEntity<>(episodesService.putEpisodeData(id,file),HttpStatus.OK);
    }
    //method for updating episode data without the video
    @PutMapping("/{id}")
    public ResponseEntity<EpisodeSummary>putEpisode(@PathVariable("id") Long id,@RequestBody @Valid EpisodesPutRequest request) throws IOException {
        return new ResponseEntity<>(episodesService.putEpisode(id, request.name, request.languagesList, request.seriesId),HttpStatus.OK);
    }
    //Webflux method for video streaming in ranges of bytes, ensuring fast video load times
    @GetMapping(value = "/{id}/play",produces = "video/mp4")
    public Mono<Resource> getEpisodeData(@PathVariable Integer id, @RequestHeader("Range") String range){
        System.out.println("range in bytes: "+range);
        return episodesService.getEpisodeData(id);
    }
    //Episode summary information: id, title and languages
    @GetMapping("/{id}")
    public ResponseEntity<EpisodeSummary> getEpisodebyId(@PathVariable("id")Integer id){
        EpisodeSummary episode = episodesService.getEpisode(id);
        return new ResponseEntity<>(episode, HttpStatus.OK);
    }
    @PostMapping("/upload")
    public ResponseEntity<EpisodeSummary> uploadFileToS3(@RequestParam("file") MultipartFile file, @RequestParam("name")String name, @RequestParam("languages")List<String> languagesList, @RequestParam("seriesId")Integer seriesId) throws FileEmptyException, IOException, FileUploadException {
        if (file.isEmpty()){
            throw new FileEmptyException("File is empty. Cannot save an empty file");
        }
        boolean isValidFile = isValidFile(file);
        List<String> allowedFileExtensions = new ArrayList<>(List.of("mp4","mov"));
        if (isValidFile && allowedFileExtensions.contains(FilenameUtils.getExtension(file.getOriginalFilename()))){
            EpisodeSummary episodeSummary = episodesService.uploadFile(file,name,languagesList,seriesId);
            return new ResponseEntity<>(episodeSummary,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/stream/{filename}")
    public ResponseEntity<StreamingResponseBody> streamFromS3(@PathVariable("filename") String filename, @RequestHeader("Range") String range){
        //This whole thing should be done from cloudflare
        return null;
    }

    private boolean isValidFile(MultipartFile multipartFile){
        if (Objects.isNull(multipartFile.getOriginalFilename())){
            return false;
        }
        return !multipartFile.getOriginalFilename().trim().equals("");
    }

    private Map<String,String> createMessage(Exception ex){
        Map<String,String> errors = new HashMap<>();
        errors.put("error",ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FileEmptyException.class)
    public Map<String,String> handleFileEmptyExceptions(FileEmptyException ex){
        return createMessage(ex);
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EpisodeNotFoundException.class)
    public Map<String,String> handleEpisodenotFoundExceptions(EpisodeNotFoundException ex){
        return createMessage(ex);
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SeriesNotFoundException.class)
    public Map<String,String> handleEpisodenotFoundExceptions(SeriesNotFoundException ex){
        return createMessage(ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MultipartException.class)
    public Map<String,String> handleMultipartExceptions(MultipartException ex){
        Map<String,String> errors = new HashMap<>();
        errors.put("error", "File is mandatory");
        return errors;
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
    public Map<String,String> handleConstraintExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach((error) -> {
            String name = String.valueOf(error.getPropertyPath());
            String msg = error.getMessage();
            errors.put(name, msg);
        });
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
