package com.serwertetowy;

import com.serwertetowy.entities.Series;
import com.serwertetowy.repos.EpisodesRepository;
import com.serwertetowy.repos.SeriesRepository;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EpisodeServiceImplTest {
    EpisodesRepository episodesRepository = mock(EpisodesRepository.class);
    SeriesRepository seriesRepository = mock(SeriesRepository.class);
//    EpisodesService service = new EpisodeServiceImpl(episodesRepository, seriesRepository);
    Series expSeries = new Series(1L,"tet","tetowa",null,null,null);
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
}
