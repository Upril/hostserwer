package com.serwertetowy.controllers;
import com.serwertetowy.exceptions.FileDownloadException;
import com.serwertetowy.exceptions.FileEmptyException;
import com.serwertetowy.exceptions.FileUploadException;
import com.serwertetowy.services.dto.EpisodeSummary;
import com.serwertetowy.services.EpisodesService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/episode")
@AllArgsConstructor
public class EpisodesController {
    private EpisodesService episodesService;
    //episode saving in /target/classes/videos, to change to a cloud based file storage
    @PostMapping()
    public ResponseEntity<EpisodeSummary> saveEpisode(@RequestParam("file")MultipartFile file, @RequestParam("name")String name, @RequestParam("languages")List<String> languagesList, @RequestParam("seriesId")Integer seriesId) throws IOException {
        return new ResponseEntity<>(episodesService.saveEpisode(file,name,languagesList,seriesId),HttpStatus.OK);
    }
    //method for updating episode video data
    @PutMapping("/{id}/data")
    public ResponseEntity<EpisodeSummary>putEpisodeData(@PathVariable("id") Long id, @RequestParam("file")MultipartFile file) throws IOException {
        return new ResponseEntity<>(episodesService.putEpisodeData(id,file),HttpStatus.OK);
    }
    //method for updating episode data without the video
    @PutMapping("/{id}")
    public ResponseEntity<EpisodeSummary>putEpisode(@PathVariable("id") Long id, @RequestParam("name")String name, @RequestParam("languages")List<String> languagesList, @RequestParam("seriesId")Integer seriesId) throws IOException {
        return new ResponseEntity<>(episodesService.putEpisode(id,name,languagesList,seriesId),HttpStatus.OK);
    }
    //Webflux method for video streaming in ranges of bytes, ensuring fast video load times
    @GetMapping(value = "/{id}/play",produces = "video/mp4")
    public Mono<Resource> getEpisodeData(@PathVariable Integer id, @RequestHeader("Range") String range){
        System.out.println("range in bytes: "+range);
        EpisodeSummary episodeSummary = episodesService.getEpisode(id);
        return episodesService.getEpisodeData(episodeSummary.getTitle());
    }
    //Webflux methods to get episodes images, not sure if needed, should work
//    @GetMapping(value = "/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
//    public Mono<Resource> getEpisodeImage(@PathVariable Integer id){
//        return episodesService.getEpisodeImageData(id);
//    }
    //Episode summary information: id, title and languages, may include icon in the future
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
    public ResponseEntity<StreamingResponseBody> streamFromS3(@PathVariable("filename") String filename) throws IOException, FileDownloadException {
        StreamingResponseBody body = episodesService.streamFile(filename);
        return new ResponseEntity<>(body,HttpStatus.OK);
    }

    private boolean isValidFile(MultipartFile multipartFile){
        if (Objects.isNull(multipartFile.getOriginalFilename())){
            return false;
        }
        return !multipartFile.getOriginalFilename().trim().equals("");
    }
}
