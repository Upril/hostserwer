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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public EpisodeServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

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
    void when_saveEpisode_thenThow_Nullpointer() throws IOException {
        //to trzeba naprawic

        Series series = new Series(1L,"tet","tetowa",null,null,null,null);
        Episodes episode = new Episodes("Title",series,new ArrayList<>(){{add("Polish");add("English");}});
        when(seriesRepository.findById(anyInt())).thenReturn(Optional.of(series));
        when(episodesRepository.save(any())).thenReturn(episode);
        File file = new File("target/classes/tests/videos/tetujemy.mp4");
        MultipartFile mpfile = new MockMultipartFile("tetujemy.mp4", new FileInputStream(file));
        NullPointerException exception = assertThrows(NullPointerException.class,()-> service.saveEpisode(mpfile,"tetujemy",
                new ArrayList<>(){{add("Polish");add("English");}},series.getId().intValue()));
        assertEquals("Cannot invoke \"java.lang.Long.intValue()\" because the return value of" +
                " \"com.serwertetowy.entities.Episodes.getId()\" is null",exception.getMessage());
        Path path = Paths.get("target/classes/videos/tetujemy.mp4");
        assertTrue(Files.exists(path));
        verify(seriesRepository,times(1)).findById(anyInt());
        Files.delete(path);
    }
    @Test
    void when_putEpisodeData_thenReturn_EpisodeSummary() throws IOException {
        Series series = new Series(1L,"tet","tetowa",null,null,null,null);
        Episodes episode = new Episodes("tetujemy",series,new ArrayList<>(){{add("Polish");add("English");}});
        File file = new File("target/classes/tests/videos/tetujemy.mp4");
        MultipartFile mpfile = new MockMultipartFile("tetujemy.mp4", new FileInputStream(file));
        when(episodesRepository.findById(anyInt())).thenReturn(Optional.of(episode));
        Path pathReal = Path.of("target/classes/videos/tetujemy.mp4");
        if(!Files.exists(pathReal)) Files.copy(mpfile.getInputStream(), pathReal);
        NullPointerException exception = assertThrows(NullPointerException.class,()-> service.putEpisodeData(1L,mpfile));
        assertEquals("Cannot invoke \"java.lang.Long.intValue()\" because the return value of" +
                " \"com.serwertetowy.entities.Episodes.getId()\" is null",exception.getMessage());
        assertTrue(Files.exists(pathReal));
        Files.delete(pathReal);
    }

    @Test
    void when_putEpisode_thenReturn_EpisodeSummary() throws IOException {
        Series series = new Series(1L,"tet","tetowa",null,null,null,null);
        Episodes episode = new Episodes(1L,series,"tetujemy",new ArrayList<>(){{add("Polish");add("English");}});
        when(seriesRepository.findById(anyInt())).thenReturn(Optional.of(series));
        when(episodesRepository.findById(anyInt())).thenReturn(Optional.of(episode));
        when(episodesRepository.findEpisodeSummaryById(anyInt())).thenReturn(new EpisodeSummary() {
            @Override
            public String getTitle() {
                return "tetowanko";
            }

            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public List<String> getLanguages() {
                return new ArrayList<>(){{add("Polish");add("Nyakid");}};
            }
        });
        File file = new File("target/classes/tests/videos/tetujemy.mp4");
        MultipartFile mpfile = new MockMultipartFile("tetujemy.mp4", new FileInputStream(file));
        Path pathReal = Path.of("target/classes/videos/tetujemy.mp4");
        if(!Files.exists(pathReal)) Files.copy(mpfile.getInputStream(), pathReal);
        EpisodeSummary actual = service.putEpisode(1L, "tetowanko",new ArrayList<>(){{add("Polish");add("Nyakid");}},1);
        assertTrue(actual.getTitle().equals("tetowanko") && actual.getLanguages().contains("Nyakid"));
        Files.delete(Path.of("target/classes/videos/tetowanko.mp4"));
    }
}
