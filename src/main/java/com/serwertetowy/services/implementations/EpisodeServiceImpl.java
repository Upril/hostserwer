package com.serwertetowy.services.implementations;

import com.serwertetowy.entities.Episodes;
import com.serwertetowy.entities.Series;
import com.serwertetowy.exceptions.SeriesNotFoundException;
import com.serwertetowy.repos.EpisodesRepository;
import com.serwertetowy.repos.SeriesRepository;
import com.serwertetowy.services.dto.EpisodeSummary;
import com.serwertetowy.services.EpisodesService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EpisodeServiceImpl implements EpisodesService {
    private static final String FORMAT = "classpath:videos/%s.mp4";
    private EpisodesRepository episodesRepository;
    private SeriesRepository seriesRepository;
    @Autowired
    private ResourceLoader resourceLoader;
    @Override
    @Transactional
    public EpisodeSummary getEpisode(Integer id){
        return episodesRepository.findEpisodeSummaryById(id);
    }
    @Override
    public Mono<Resource> getEpisodeData(String title){
        return Mono.fromSupplier(()->resourceLoader.getResource(String.format(FORMAT,title)));
    }
    @Override
    @Transactional
    public List<EpisodeSummary> getEpisodesBySeries(Integer seriesId){
        return episodesRepository.findEpisodeSummaryBySeriesId(seriesId);
    }
    @Override
    public void saveEpisode(MultipartFile file, String name, List<String> languagesList, Integer seriesId) throws IOException {
        Path root = Paths.get("target/classes/videos");
        Optional<Series> series = seriesRepository.findById(seriesId);
        if(series.isEmpty()) throw new SeriesNotFoundException();//may delete later
        Series seriesFr = series.get();
        Episodes newEpisode = new Episodes(name,seriesFr,languagesList);
        episodesRepository.save(newEpisode);
        Files.copy(file.getInputStream(), root.resolve(name+".mp4"));
    }


}

