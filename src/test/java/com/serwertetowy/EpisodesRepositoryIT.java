package com.serwertetowy;

import com.serwertetowy.entities.Episodes;
import com.serwertetowy.entities.Series;
import com.serwertetowy.repos.EpisodesRepository;
import com.serwertetowy.repos.SeriesRepository;
import com.serwertetowy.services.dto.EpisodeSummary;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
public class EpisodesRepositoryIT {
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    EpisodesRepository episodesRepository;
    @Autowired
    SeriesRepository seriesRepository;
    Series expSeries = new Series(1L,"tet","tetowa",null,null,null,null);
    @AfterEach
    void tearDown(){
        episodesRepository.deleteAll();
        entityManager.flush();
    }
    @Test
    void saveEpisodeAndThenGetEpisodeSummary(){
        if(!seriesRepository.existsById(expSeries.getId().intValue())){
            seriesRepository.save(expSeries);
            entityManager.flush();
        }
        Episodes episode = new Episodes("Title",expSeries,new ArrayList<>(){{add("Polish");add("English");}});
        episodesRepository.save(episode);
        EpisodeSummary expected = new EpisodeSummary() {
            @Override
            public String getTitle() {
                return "Title";
            }

            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public List<String> getLanguages() {
                ArrayList<String> langs = new ArrayList<>();
                langs.add("Polish");
                langs.add("English");
                return langs;
            }
        };
        EpisodeSummary actual = episodesRepository.findEpisodeSummaryById(1);
        assertTrue(episodesRepository.existsById(1));
        assertTrue(expected.getTitle().equals(actual.getTitle()) && expected.getId().equals(actual.getId()) && expected.getLanguages().get(1).equals(actual.getLanguages().get(1)));
    }

    @Test
    @DirtiesContext
    void getEpisodesBySeries(){
        if(!seriesRepository.existsById(expSeries.getId().intValue())){
            seriesRepository.save(expSeries);
            entityManager.flush();
        }
        Episodes episode1 = new Episodes("Title",expSeries,new ArrayList<>(){{add("Polish");add("English");}});
        episodesRepository.save(episode1);
        entityManager.flush();
        List<EpisodeSummary> actual = episodesRepository.findEpisodeSummaryBySeriesId(expSeries.getId().intValue());

        List<EpisodeSummary> expected = List.of(new EpisodeSummary() {
            @Override
            public String getTitle() {
                return "Title";
            }

            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public List<String> getLanguages() {
                ArrayList<String> langs = new ArrayList<>();
                langs.add("Polish");
                langs.add("English");
                return langs;
            }
        });
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getTitle(), actual.get(0).getTitle());
    }
}
