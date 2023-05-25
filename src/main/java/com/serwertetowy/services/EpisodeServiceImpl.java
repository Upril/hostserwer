package com.serwertetowy.services;

import com.serwertetowy.entities.Episodes;
import com.serwertetowy.entities.Series;
import com.serwertetowy.exceptions.EpisodeNotFoundException;
import com.serwertetowy.exceptions.SeriesNotFoundException;
import com.serwertetowy.repos.EpisodesRepository;
import com.serwertetowy.repos.SeriesRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
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
//    @Transactional
//    public Episodes getEpisodeData(Integer id){
//        Optional<Episodes> episode = episodesRepository.findById(id);
//        Episodes foundEpisode = new Episodes();
//        if(episode.isPresent()) foundEpisode = episode.get();
//        else throw new EpisodeNotFoundException();
//        foundEpisode.setData(getVideo(foundEpisode.getTitle()));
//        return foundEpisode;
//    }
    public Mono<Resource> getEpisodeData(String title){
        return Mono.fromSupplier(()->resourceLoader.getResource(String.format(FORMAT,title)));
    }

    @Transactional
    public List<EpisodeSummary> getEpisodesBySeries(Integer seriesId){
        return episodesRepository.findEpisodeSummaryBySeriesId(seriesId);
    }
    public void saveEpisode(MultipartFile file, String name/*, Set<String> languagesSet*/, Integer seriesId) throws IOException {
        Optional<Series> series = seriesRepository.findById(seriesId);
        if(!series.isPresent()) throw new SeriesNotFoundException();//may delete later
        Series seriesFr = series.get();
        Episodes newEpisode = new Episodes(name,seriesFr/*,languagesSet*/,file.getBytes());
        episodesRepository.save(newEpisode);
    }

}

