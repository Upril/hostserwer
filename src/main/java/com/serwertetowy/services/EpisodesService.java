package com.serwertetowy.services;

import com.serwertetowy.services.dto.EpisodeSummary;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

public interface EpisodesService {
    //Episode summary information: id, title and languages, may include icon in the future
    EpisodeSummary getEpisode(Integer id);
    //Episode data streaming
    Mono<Resource> getEpisodeData(String title);
    //method to retrieve episode icon, not sure if needed
    //Mono<Resource> getEpisodeImageData();
    //method used by series service to get episode lists
    List<EpisodeSummary> getEpisodesBySeries(Integer seriesId);
    //method saving sent mp4 files to storage, may be replaced by cloud based storage
    EpisodeSummary saveEpisode(MultipartFile file, String name, List<String> languagesList, Integer seriesId) throws IOException;
    EpisodeSummary putEpisodeData(Long id, MultipartFile file) throws IOException;
    EpisodeSummary putEpisode(Long id, String name, List<String> languagesList, Integer seriesId) throws IOException;
}
