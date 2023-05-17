package com.serwertetowy.controllers;

import com.serwertetowy.entities.Episodes;
import com.serwertetowy.services.EpisodeSummary;
import com.serwertetowy.services.EpisodesService;
import org.springframework.core.io.Resource;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/episode")
@AllArgsConstructor
public class EpisodesController {
    private EpisodesService episodesService;
    record video(Long id, String title/*, Set<String> languages*/){}
    @PostMapping()
    public ResponseEntity<Episodes> saveEpisode(@RequestParam("file")MultipartFile file, @RequestParam("name")String name/*, @RequestParam("languages")Set<String> languagesSet*/, @RequestParam("seriesId")Integer seriesId) throws IOException {
        episodesService.saveEpisode(file,name/*,languagesSet*/,seriesId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/play")
    public ResponseEntity<Resource> getVideoById(@PathVariable("id")Integer id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(episodesService.getEpisodeData(id).getData()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<EpisodeSummary> getEpisodebyId(@PathVariable("id")Integer id){
        EpisodeSummary episode = episodesService.getEpisode(id);
        return new ResponseEntity<>(episode, HttpStatus.OK);
    }
    @GetMapping("{seriesId}/all")
    public ResponseEntity<List<video>> getEpisodesBySeries(@PathVariable("seriesId")Integer id){
        List<video> videoList=new ArrayList<>();
        List<EpisodeSummary> episodesList = episodesService.getEpisodesBySeries(id);
        for(EpisodeSummary e:episodesList){
            videoList.add(new video(e.getId(),e.getTitle()/*,e.getLanguages()*/));
        }
        return new ResponseEntity<>(videoList, HttpStatus.OK);
    }
}
