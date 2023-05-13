package com.serwertetowy.services;

import com.serwertetowy.entities.Episodes;
import com.serwertetowy.entities.Series;
import com.serwertetowy.exceptions.EpisodeNotFoundException;
import com.serwertetowy.exceptions.SeriesNotFoundException;
import com.serwertetowy.repos.EpisodesRepository;
import com.serwertetowy.repos.SeriesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class EpisodeServiceImpl implements EpisodesService {
    private EpisodesRepository episodesRepository;
    private SeriesRepository seriesRepository;
    @Override
    public Episodes getEpisode(Integer id){
        Optional<Episodes> episode = episodesRepository.findById(id);
        if(!episode.isPresent()) throw new EpisodeNotFoundException();
        else return episode.get();
    }
    public List<Episodes> getEpisodesBySeries(Integer seriesId){
        return episodesRepository.findBySeriesId(seriesId);
    }
    public void saveEpisode(MultipartFile file, String name, Set<String> languagesSet, Integer seriesId) throws IOException {
        Optional<Series> series = seriesRepository.findById(seriesId);
        if(!series.isPresent()) throw new SeriesNotFoundException();
        Episodes newEpisode = new Episodes(name,series.get(),languagesSet,file.getBytes());
        episodesRepository.save(newEpisode);
    }

}

