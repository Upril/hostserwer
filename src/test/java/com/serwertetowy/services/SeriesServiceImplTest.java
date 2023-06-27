package com.serwertetowy.services;

import com.serwertetowy.entities.Series;
import com.serwertetowy.entities.SeriesTags;
import com.serwertetowy.entities.Tags;
import com.serwertetowy.repos.SeriesRepository;
import com.serwertetowy.repos.SeriesTagsRepository;
import com.serwertetowy.repos.TagRepository;
import com.serwertetowy.services.dto.EpisodeSummary;
import com.serwertetowy.services.dto.SeriesData;
import com.serwertetowy.services.dto.SeriesSummary;
import com.serwertetowy.services.implementations.SeriesServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class SeriesServiceImplTest {
    @Mock
    SeriesRepository seriesRepository;
    @Mock
    TagRepository tagRepository;
    @Mock
    SeriesTagsRepository seriesTagsRepository;
    @Mock
    private EpisodesService episodesService;
    @Mock
    private ResourceLoader resourceLoader;
    @InjectMocks
    private SeriesServiceImpl seriesService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testSaveSeries() throws IOException {

        String name = "Test Series";
        String description = "This is a test series";
        List<Integer> tagIds = Arrays.asList(1, 2, 3);

        byte[] imageData = "test image data".getBytes();
        when(tagRepository.findById(1)).thenReturn(Optional.of(new Tags(1L, "tet1")));
        when(tagRepository.findById(2)).thenReturn(Optional.of(new Tags(2L, "tet2")));
        when(tagRepository.findById(3)).thenReturn(Optional.of(new Tags(3L, "tet3")));
        File file = new File("src/main/resources/images/banner.jpg");
        MultipartFile mpfile = new MockMultipartFile("banner.jpg", new FileInputStream(file));
        when(resourceLoader.getResource("classpath:/images/banner.jpg")).thenReturn(mpfile.getResource());

        Series series = seriesService.seriesAssemble(name,description,tagIds);
        series.setImageData(imageData);
        when(seriesRepository.save(any(Series.class))).thenReturn(series);

        Series result = seriesService.saveSeries(name, description, tagIds);
        Mockito.verify(resourceLoader, times(1)).getResource("classpath:/images/banner.jpg");
        Mockito.verify(seriesRepository, times(1)).save(any(Series.class));

        Assertions.assertEquals(name, result.getName());
        Assertions.assertEquals(description, result.getDescription());
    }
    @Test
    void testSaveSeriesWithImage() throws IOException {
        String name = "Test Series";
        String description = "This is a test series";
        List<Integer> tagIds = Arrays.asList(1, 2, 3);
        byte[] fileContent = "test image data".getBytes();
        MockMultipartFile file = new MockMultipartFile("image.jpg", fileContent);
        when(tagRepository.findById(1)).thenReturn(Optional.of(new Tags(1L, "tet1")));
        when(tagRepository.findById(2)).thenReturn(Optional.of(new Tags(2L, "tet2")));
        when(tagRepository.findById(3)).thenReturn(Optional.of(new Tags(3L, "tet3")));

        Series series = seriesService.seriesAssemble(name,description,tagIds);
        series.setImageData(fileContent);

        when(seriesRepository.save(any(Series.class))).thenReturn(series);

        Series result = seriesService.saveSeriesWithImage(file, name, description, tagIds);

        verify(seriesRepository, times(1)).save(any(Series.class));

        Assertions.assertEquals(name, result.getName());
        Assertions.assertEquals(description, result.getDescription());
        Assertions.assertArrayEquals(fileContent, result.getImageData());
    }
    @Test
    void testSaveSeriesWithInvalidTag() {
        String name = "Test Series";
        String description = "This is a test series";
        List<Integer> tagIds = Arrays.asList(1, 2, 3);
        //test if throws Responsestatusexeption when chooses tag not present in tagrepository
        Assertions.assertThrows(ResponseStatusException.class,()->seriesService.saveSeries(name,description,tagIds));
    }
    @Test
    void testGetAllSeries() {
        //mocks
        SeriesData seriesData1 = new SeriesData() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getName() {
                return "Series1";
            }

            @Override
            public String getDescription() {
                return "Description1";
            }
        };
        SeriesData seriesData2 = new SeriesData() {
            @Override
            public Long getId() {
                return 2L;
            }

            @Override
            public String getName() {
                return "Series 2";
            }

            @Override
            public String getDescription() {
                return "Description 2";
            }
        };
        EpisodeSummary episodeSummary1 = new EpisodeSummary() {
            @Override
            public String getTitle() {
                return "Episode 1";
            }

            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public List<String> getLanguages() {
                return null;
            }
        };
        EpisodeSummary episodeSummary2 = new EpisodeSummary() {
            @Override
            public String getTitle() {
                return "Episode 2";
            }

            @Override
            public Long getId() {
                return 2L;
            }

            @Override
            public List<String> getLanguages() {
                return null;
            }
        };
        Tags tag1 = new Tags();
        tag1.setId(1L);
        tag1.setName("Tag 1");

        Tags tag2 = new Tags();
        tag2.setId(2L);
        tag2.setName("Tag 2");

        Series series1 = (new Series(seriesData1.getId(),seriesData1.getName(),seriesData1.getDescription()));
        Series series2 = (new Series(seriesData2.getId(),seriesData2.getName(),seriesData2.getDescription()));

        SeriesTags seriesTags1 = new SeriesTags(series1,tag1);
        SeriesTags seriesTags2 = new SeriesTags(series2,tag2);
        SeriesTags seriesTags3 = new SeriesTags(series1,tag2);

        List<EpisodeSummary> episodeSummaries= Arrays.asList(episodeSummary1,episodeSummary2);
        List<SeriesData> seriesDataList = Arrays.asList(seriesData1,seriesData2);

        when(seriesRepository.findAllData()).thenReturn(seriesDataList);
        when(seriesTagsRepository.findBySeriesId(seriesData1.getId())).thenReturn(Arrays.asList(seriesTags1,seriesTags3));
        when(seriesTagsRepository.findBySeriesId(seriesData2.getId())).thenReturn(List.of(seriesTags2));
        when(tagRepository.findById(1)).thenReturn(Optional.of(tag1));
        when(tagRepository.findById(2)).thenReturn(Optional.of(tag2));
        when(episodesService.getEpisodesBySeries(seriesData1.getId().intValue())).thenReturn(episodeSummaries);
        when(episodesService.getEpisodesBySeries(seriesData2.getId().intValue())).thenReturn(new ArrayList<>());

        List<SeriesSummary> result = seriesService.getAllSeries();

        verify(seriesRepository, times(1)).findAllData();
        verify(seriesTagsRepository, times(2)).findBySeriesId(anyLong());
        verify(tagRepository, times(3)).findById(anyInt());
        verify(episodesService, times(2)).getEpisodesBySeries(anyInt());

        Assertions.assertEquals(2, result.size());

        SeriesSummary seriesSummary1 = result.get(0);
        Assertions.assertEquals(seriesData1.getId(), seriesSummary1.getId());
        Assertions.assertEquals(seriesData1.getName(), seriesSummary1.getName());
        Assertions.assertEquals(seriesData1.getDescription(), seriesSummary1.getDescription());
        Assertions.assertEquals(episodeSummaries, seriesSummary1.getEpisodes());

        SeriesSummary seriesSummary2 = result.get(1);
        Assertions.assertEquals(seriesData2.getId(), seriesSummary2.getId());
        Assertions.assertEquals(seriesData2.getName(), seriesSummary2.getName());
        Assertions.assertEquals(seriesData2.getDescription(), seriesSummary2.getDescription());
        Assertions.assertEquals(1, seriesSummary2.getSeriesTags().size());
        Assertions.assertEquals(0, seriesSummary2.getEpisodes().size());
    }
    @Test
    void testGetSeriesById() {
        //mocks
        SeriesData seriesData = new SeriesData() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getName() {
                return "Series1";
            }

            @Override
            public String getDescription() {
                return "Description1";
            }
        };
        EpisodeSummary episodeSummary1 = new EpisodeSummary() {
            @Override
            public String getTitle() {
                return "Episode 1";
            }

            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public List<String> getLanguages() {
                return null;
            }
        };
        EpisodeSummary episodeSummary2 = new EpisodeSummary() {
            @Override
            public String getTitle() {
                return "Episode 2";
            }

            @Override
            public Long getId() {
                return 2L;
            }

            @Override
            public List<String> getLanguages() {
                return null;
            }
        };
        Tags tag1 = new Tags();
        tag1.setId(1L);
        tag1.setName("Tag 1");

        Tags tag2 = new Tags();
        tag2.setId(2L);
        tag2.setName("Tag 2");

        Series series1 = (new Series(seriesData.getId(),seriesData.getName(),seriesData.getDescription()));

        SeriesTags seriesTags1 = new SeriesTags(series1,tag1);
        SeriesTags seriesTags3 = new SeriesTags(series1,tag2);

        List<EpisodeSummary> episodeSummaries= Arrays.asList(episodeSummary1,episodeSummary2);

        when(seriesRepository.findSeriesDataById(anyInt())).thenReturn(seriesData);
        when(seriesTagsRepository.findBySeriesId(seriesData.getId())).thenReturn(Arrays.asList(seriesTags1,seriesTags3));
        when(tagRepository.findById(1)).thenReturn(Optional.of(tag1));
        when(tagRepository.findById(2)).thenReturn(Optional.of(tag2));
        when(episodesService.getEpisodesBySeries(seriesData.getId().intValue())).thenReturn(episodeSummaries);

        SeriesSummary result = seriesService.getSeriesById(1);

        verify(seriesRepository, times(1)).findSeriesDataById(1);
        verify(seriesTagsRepository, times(1)).findBySeriesId(anyLong());
        verify(tagRepository, times(2)).findById(anyInt());
        verify(episodesService, times(1)).getEpisodesBySeries(anyInt());

        Assertions.assertEquals(seriesData.getId(), result.getId());
        Assertions.assertEquals(seriesData.getName(), result.getName());
        Assertions.assertEquals(seriesData.getDescription(), result.getDescription());
        Assertions.assertEquals(episodeSummaries, result.getEpisodes());

    }
    @Test
    void testGetSeriesImageData() {
        Long id = 1L;
        byte[] imageData = "test image data".getBytes();

        Series series = new Series();
        series.setId(id);
        series.setImageData(imageData);

        when(seriesRepository.findById(id.intValue())).thenReturn(Optional.of(series));

        Mono<Resource> resultMono = seriesService.getSeriesImageData(id.intValue());
        Resource result = resultMono.block();

        verify(seriesRepository, times(1)).findById(id.intValue());

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ByteArrayResource);
        ByteArrayResource byteArrayResource = (ByteArrayResource) result;
        Assertions.assertArrayEquals(imageData, byteArrayResource.getByteArray());
    }
    @Test
    void testGetSeriesImageDataNotFound() {
        long id = 1L;
        when(seriesRepository.findById((int) id)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResponseStatusException.class, () -> seriesService.getSeriesImageData((int) id));
        verify(seriesRepository, times(1)).findById((int) id);
    }

}
