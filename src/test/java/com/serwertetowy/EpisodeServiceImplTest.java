package com.serwertetowy;

import com.serwertetowy.entities.Episodes;
import com.serwertetowy.entities.Series;
import com.serwertetowy.repos.EpisodesRepository;
import com.serwertetowy.repos.SeriesRepository;
import com.serwertetowy.services.EpisodeServiceImpl;
import com.serwertetowy.services.EpisodesService;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EpisodeServiceImplTest {
    EpisodesRepository episodesRepository = mock(EpisodesRepository.class);
    SeriesRepository seriesRepository = mock(SeriesRepository.class);
    EpisodesService service = new EpisodeServiceImpl(episodesRepository, seriesRepository);
    Series expSeries = new Series(1,"tet","tetowa",null,null);
    @Test
    void getEpisode(){
        Episodes expected = new Episodes("testName",expSeries,null);
        when(episodesRepository.findById(1)).thenReturn(Optional.of(expected));
        when(episodesRepository.existsById(1)).thenReturn(true);

        Episodes actual = service.getEpisodeData(1);
        assertEquals(Optional.of(expected),Optional.of(actual));
        verify(episodesRepository, times(1)).findById(1);

    }
    @Test
    void getEpisodesBySeries(){}
    @Test
    void saveEpisode(){}
}
