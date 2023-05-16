package com.serwertetowy.services;

import com.serwertetowy.entities.Episodes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EpisodesService {
    EpisodeSummary getEpisode(Integer id);
    Episodes getEpisodeData(Integer id);
    List<EpisodeSummary> getEpisodesBySeries(Integer seriesId);
    void saveEpisode(MultipartFile file, String name/*, Set<String> languagesSet*/, Integer seriesId) throws IOException;
}
