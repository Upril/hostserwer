package com.serwertetowy.controllers;

import com.serwertetowy.entities.Episodes;
import com.serwertetowy.services.dto.EpisodeSummary;
import com.serwertetowy.services.EpisodesService;
import org.springframework.core.io.Resource;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/episode")
@AllArgsConstructor
public class EpisodesController {
    private EpisodesService episodesService;
    //episode saving in /target/classes/videos, to change to a cloud based file storage
    @PostMapping()
    public ResponseEntity<Episodes> saveEpisode(@RequestParam("file")MultipartFile file, @RequestParam("name")String name, @RequestParam("languages")List<String> languagesList, @RequestParam("seriesId")Integer seriesId) throws IOException {
        episodesService.saveEpisode(file,name,languagesList,seriesId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //Webflux method for video streaming in ranges of bytes, ensuring fast video load times
    @GetMapping(value = "/{id}/play",produces = "video/mp4")
    public Mono<Resource> getEpisodeData(@PathVariable Integer id, @RequestHeader("Range") String range){
        System.out.println("range in bytes: "+range);
        EpisodeSummary episodeSummary = episodesService.getEpisode(id);
        return episodesService.getEpisodeData(episodeSummary.getTitle());
    }
    //Episode summary information: id, title and languages, may include icon in the future
    @GetMapping("/{id}")
    public ResponseEntity<EpisodeSummary> getEpisodebyId(@PathVariable("id")Integer id){
        EpisodeSummary episode = episodesService.getEpisode(id);
        return new ResponseEntity<>(episode, HttpStatus.OK);
    }
}
