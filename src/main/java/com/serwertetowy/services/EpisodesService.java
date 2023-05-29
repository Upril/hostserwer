package com.serwertetowy.services;

import com.serwertetowy.entities.Episodes;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

public interface EpisodesService {
    EpisodeSummary getEpisode(Integer id);
    Mono<Resource> getEpisodeData(String title);
    List<EpisodeSummary> getEpisodesBySeries(Integer seriesId);
    void saveEpisode(MultipartFile file, String name/*, Set<String> languagesSet*/, Integer seriesId) throws IOException;
}
