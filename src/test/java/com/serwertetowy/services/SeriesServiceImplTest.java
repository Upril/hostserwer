package com.serwertetowy.services;

import com.serwertetowy.entities.Series;
import com.serwertetowy.entities.Tags;
import com.serwertetowy.repos.SeriesRepository;
import com.serwertetowy.repos.TagRepository;
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
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
}
