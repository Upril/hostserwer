package com.serwertetowy.services;

import com.serwertetowy.entities.Episodes;
import com.serwertetowy.entities.Languages;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface EpisodesService {
    Episodes getEpisode(Integer id);
    List<Episodes> getEpisodesBySeries(Integer seriesId);
    void saveEpisode(MultipartFile file, String name, Set<Languages> languagesSet, Integer seriesId) throws IOException;
}
