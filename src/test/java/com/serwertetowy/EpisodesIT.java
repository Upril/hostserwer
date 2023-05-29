package com.serwertetowy;

import com.serwertetowy.entities.Episodes;
import com.serwertetowy.entities.Series;
import com.serwertetowy.repos.EpisodesRepository;
import com.serwertetowy.repos.SeriesRepository;
import com.serwertetowy.services.EpisodeSummary;
import com.serwertetowy.services.EpisodesService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@SpringBootTest
@Transactional
public class EpisodesIT {
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    EpisodesService service;
    @Autowired
    EpisodesRepository episodesRepository;
    @Autowired
    SeriesRepository seriesRepository;
    Series expSeries = new Series(1L,"tet","tetowa",null,null,null);
    @AfterEach
    void tearDown(){
        episodesRepository.deleteAll();
        seriesRepository.deleteAll();
        entityManager.flush();
    }
    @Test
    void getEpisode(){
//        seriesRepository.save(expSeries);
//        entityManager.flush();
//        Episodes expected = new Episodes("Title",expSeries,null);
//        episodesRepository.save(expected);
//        Episodes actual = service.getEpisodeData(1);
//        assertEquals(expected,actual);
    }
    @Test
    @DirtiesContext
    void saveEpisode() throws IOException {
        seriesRepository.save(expSeries);
        entityManager.flush();
        MultipartFile file = mock(MultipartFile.class);
        service.saveEpisode(file,"Title",1);
        entityManager.flush();
        assertTrue(episodesRepository.existsById(1));
    }
    @Test
    @DirtiesContext
    void getEpisodesBySeries(){
        seriesRepository.save(expSeries);
        EpisodeSummary episodeSummary = new EpisodeSummary() {
            @Override
            public String getTitle() {
                return "Title";
            }

            @Override
            public Long getId() {
                return 1L;
            }
        };
        List<EpisodeSummary> expected = List.of(episodeSummary);
        episodesRepository.save(new Episodes("Title",expSeries,null));
        entityManager.flush();
        List<EpisodeSummary> actual = service.getEpisodesBySeries(1);
        assertEquals(expected.size(), actual.size());
    }
}
