package com.serwertetowy;

import com.serwertetowy.entities.Episodes;
import com.serwertetowy.entities.Series;
import com.serwertetowy.repos.EpisodesRepository;
import com.serwertetowy.repos.SeriesRepository;
import com.serwertetowy.services.dto.EpisodeSummary;
import com.serwertetowy.services.implementations.EpisodeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class EpisodeServiceImplTest {
    @Mock
    EpisodesRepository episodesRepository = mock(EpisodesRepository.class);
    @Mock
    SeriesRepository seriesRepository = mock(SeriesRepository.class);
    @InjectMocks
    EpisodeServiceImpl service;
    @Test
    void when_getEpisode_thenReturn_EpisodeSummary(){
        EpisodeSummary episodeSummary = new EpisodeSummary() {
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
        when(episodesRepository.findEpisodeSummaryById(1)).thenReturn(episodeSummary);
        EpisodeSummary actual = service.getEpisode(1);
        assertEquals(episodeSummary.getTitle(), actual.getTitle());
        verify(episodesRepository, times(1)).findEpisodeSummaryById(1);
    }
    @Test
    void when_getEpisodesBySeries_thenReturn_EpisodeSummaryList(){
        EpisodeSummary episodeSummary = new EpisodeSummary() {
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
        List<EpisodeSummary> expected = List.of(episodeSummary);
        when(episodesRepository.findEpisodeSummaryBySeriesId(1)).thenReturn(expected);

        List<EpisodeSummary> actual = service.getEpisodesBySeries(1);
        assertEquals(actual,expected);
        verify(episodesRepository,times(1)).findEpisodeSummaryBySeriesId(1);
    }
    @Test
    void when_getEpisodeData_thenReturn_Mono(){
        assertInstanceOf(Mono.class, service.getEpisodeData("classpath:videos/tetujemy.mp4"));
    }
    @Test
    @DirtiesContext
    void when_saveEpisode_thenReturn_EpisodeSummary() throws IOException {
        //to trzeba naprawic
        MockMultipartFile file = new MockMultipartFile("tetujemy","tetujemy.mp4", MediaType.TEXT_PLAIN_VALUE,"Hello Świże".getBytes());
        Series series = new Series(1L,"tet","tetowa",null,null,null,null);
        Episodes episode = new Episodes("Title",series,new ArrayList<>(){{add("Polish");add("English");}});
        EpisodeSummary expected = new EpisodeSummary() {
            @Override
            public String getTitle() {
                return "tetujemy";
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
        when(seriesRepository.findById(anyInt())).thenReturn(Optional.of(series));
        when(episodesRepository.save(any())).thenReturn(episode);
        when(episodesRepository.findEpisodeSummaryById(1)).thenReturn(expected);
        EpisodeSummary actual = service.saveEpisode(file,"tetujemy",
                new ArrayList<>(){{add("Polish");add("English");}},series.getId().intValue());

        assertTrue(expected.getId().equals(actual.getId()) && expected.getTitle().equals(actual.getTitle()));
        verify(seriesRepository,times(1)).findById(anyInt());
        verify(episodesRepository, times(2));

    }
}
