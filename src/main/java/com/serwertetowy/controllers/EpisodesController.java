package com.serwertetowy.controllers;

import com.serwertetowy.entities.Episodes;
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
import java.util.Set;

@RestController
@RequestMapping("api/v1/episode")
@AllArgsConstructor
public class EpisodesController {
    private EpisodesService episodesService;
    record video(Integer id, String title/*, Set<String> languages*/){}
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
                .body(new ByteArrayResource(episodesService.getEpisode(id).getData()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<video> getEpisodebyId(@PathVariable("id")Integer id){
        Episodes episode = episodesService.getEpisode(id);
        video video = new video(episode.getId(), episode.getTitle()/*, episode.getLanguages()*/);
        return new ResponseEntity<>(video, HttpStatus.OK);
    }
    @GetMapping("{seriesId}/all")
    public ResponseEntity<List<video>> getEpisodesBySeries(@PathVariable("seriesId")Integer id){
        List<video> videoList=new ArrayList<>();
        List<Episodes> episodesList = episodesService.getEpisodesBySeries(id);
        for(Episodes e:episodesList){
            videoList.add(new video(e.getId(),e.getTitle()/*,e.getLanguages()*/));
        }
        return new ResponseEntity<>(videoList, HttpStatus.OK);
    }
}
