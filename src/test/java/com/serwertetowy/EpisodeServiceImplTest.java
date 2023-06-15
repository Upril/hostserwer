package com.serwertetowy;

import com.serwertetowy.entities.Episodes;
import com.serwertetowy.entities.Series;
import com.serwertetowy.repos.EpisodesRepository;
import com.serwertetowy.repos.SeriesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class EpisodeServiceImplTest {
    EpisodesRepository episodesRepository = mock(EpisodesRepository.class);
    SeriesRepository seriesRepository = mock(SeriesRepository.class);
//    EpisodesService service = new EpisodeServiceImpl(episodesRepository, seriesRepository);
    Series expSeries = new Series(1L,"tet","tetowa",null,null,null,null);
    @Test
    void getEpisode(){
//        Episodes expected = new Episodes("testName",expSeries,null);
//        when(episodesRepository.findById(1)).thenReturn(Optional.of(expected));
//        when(episodesRepository.existsById(1)).thenReturn(true);
//
//        Episodes actual = service.getEpisodeData(1);
//        assertEquals(Optional.of(expected),Optional.of(actual));
//        verify(episodesRepository, times(1)).findById(1);

    }
    @Test
    void getEpisodesBySeries(){
//        EpisodeSummary episodeSummary = new EpisodeSummary() {
//            @Override
//            public String getTitle() {
//                return "Title";
//            }
//
//            @Override
//            public Long getId() {
//                return 1L;
//            }
//        };
//        List<EpisodeSummary> expected = List.of(episodeSummary);
//        when(episodesRepository.findEpisodeSummaryBySeriesId(1)).thenReturn(expected);
//
//        List<EpisodeSummary> actual = service.getEpisodesBySeries(1);
//        assertEquals(actual,expected);
//        verify(episodesRepository,times(1)).findEpisodeSummaryBySeriesId(1);
    }
    @Test
    void saveEpisode() throws IOException {
//        MultipartFile file = mock(MultipartFile.class);
//        Episodes testEpisode = new Episodes("Title",expSeries,file.getBytes());
//        service.saveEpisode(file, "Title",expSeries.getId());
//        verify(episodesRepository, times(1)).save(testEpisode);
    }
    @Test
    @DirtiesContext
    void saveVideo() throws IOException {
//        if(!seriesRepository.existsById(expSeries.getId().intValue())){
//            seriesRepository.save(expSeries);
//            entityManager.flush();
//        }
//        MultipartFile file = mock(MultipartFile.class);
//        Path root = Paths.get("target/classes/videos");
//        Episodes episode = new Episodes("Title",expSeries,new ArrayList<>(){{add("Polish");add("English");}});
//        episodesRepository.save(episode);
//        entityManager.flush();
//        Files.copy(file.getInputStream(), root.resolve(episode.getTitle()+".mp4"));
//        assertTrue(episodesRepository.existsById(1));
//    }
}
