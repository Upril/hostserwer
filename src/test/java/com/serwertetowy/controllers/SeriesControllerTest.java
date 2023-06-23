package com.serwertetowy.controllers;

import com.serwertetowy.entities.Series;
import com.serwertetowy.entities.WatchFlags;
import com.serwertetowy.services.SeriesService;
import com.serwertetowy.services.dto.SeriesSummary;
import com.serwertetowy.services.dto.UserSeriesSummary;
import com.serwertetowy.services.dto.UserSummary;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SeriesController.class)
public class SeriesControllerTest {
    @MockBean
    SeriesService seriesService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ResourceLoader resourceLoader;
    SeriesSummary expected = new SeriesSummary(1L,"tetowa","seriaTetowa",null,null);
    UserSummary userSummary = new UserSummary() {
        @Override
        public Long getId() {
            return 1L;
        }

        @Override
        public String getFirstname() {
            return "firstname";
        }

        @Override
        public String getLastname() {
            return "lastname";
        }

        @Override
        public String getEmail() {
            return "email";
        }
    };
    WatchFlags watchFlag = new WatchFlags(1L, "Watching");
    UserSeriesSummary userSeriesSummary = new UserSeriesSummary() {
        @Override
        public Long getId() {
            return 1L;
        }

        @Override
        public SeriesSummary getSeriesSummary() {
            return expected;
        }

        @Override
        public UserSummary getUserSummary() {
            return userSummary;
        }

        @Override
        public WatchFlags getWatchflag() {
            return watchFlag;
        }
    };
    @Test
    void when_SaveSeriesWithoutFile_thenReturn_Series() throws Exception {
//        Series savedSeries = new Series();
//        savedSeries.setId(1L);
//        savedSeries.setName(expected.getName());
//        savedSeries.setDescription(expected.getDescription());
//
//        Mockito.when(seriesService.saveSeries(expected.getName(), expected.getDescription(), List.of(1,2,3)))
//                .thenReturn(savedSeries);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/series")
//                .param("name","tetowa")
//                .param("description","seriaTetowa")
//                .param("tags","1","2","3")
//                        .param("file", (String) null))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("tetowa"));
    }
    @Test
    void when_SaveSeriesWithfile_thenReturn_SeriesSummary()throws Exception{
        Series savedSeries = new Series(1L,expected.getName(),expected.getDescription());
        MockMultipartFile file = new MockMultipartFile("file","test.jpg", MediaType.IMAGE_JPEG_VALUE,
                "TestFileContent".getBytes());
        Mockito.when(seriesService.saveSeriesWithImage(file, expected.getName(), expected.getDescription(), List.of(1,2,3))).thenReturn(savedSeries);
        Mockito.when(seriesService.getSeriesById(anyInt())).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/series")
                .file(file)
                .param("name",expected.getName())
                .param("description",expected.getDescription())
                        .param("tags","1","2","3")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        Mockito.verify(seriesService).saveSeriesWithImage(file,expected.getName(),expected.getDescription(),List.of(1,2,3));

    }
    @Test
    void when_GetAllSeries_thenReturn_Listof_SeriesSummary() throws Exception {
        List<SeriesSummary> expectedList = List.of(expected);
        Mockito.when(seriesService.getAllSeries()).thenReturn(expectedList);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/series"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("tetowa"))
                .andExpect(jsonPath("$[0].description").value("seriaTetowa"));
        Mockito.verify(seriesService).getAllSeries();
    }
    @Test
    void when_GetSeries_thenReturn_SeriesSummary() throws Exception {
        Mockito.when(seriesService.getSeriesById(1)).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/series/{id}",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("tetowa"))
                .andExpect(jsonPath("$.description").value("seriaTetowa"));
        Mockito.verify(seriesService).getSeriesById(1);
    }
    @Test
    void when_getSeriesImage_thenReturn_ImageMono() throws Exception{
        byte[] imageBytes = resourceLoader.getResource("classpath:/images/defalt.jpg").getContentAsByteArray();
        Mono<Resource> imageMono = Mono.just(new ByteArrayResource(imageBytes));
        Mockito.when(seriesService.getSeriesImageData(1)).thenReturn(imageMono);

        webTestClient.get()
                .uri("/api/v1/series/{id}/image",1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.IMAGE_JPEG)
                .expectBody(byte[].class).isEqualTo(imageBytes);
        Mockito.verify(seriesService).getSeriesImageData(1);
    }
    @Test
    void when_addSeriesToWatchlist_thenReturn_UserSeriesSummary() throws Exception {
        Mockito.when(seriesService.addToWatchlist(expected.getId().intValue(),userSummary.getId().intValue(),watchFlag.getId().intValue())).thenReturn(userSeriesSummary);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/series/addToWatchlist")
                .param("seriesId",expected.getId().toString())
                .param("userId",userSummary.getId().toString())
                .param("watchflagId",watchFlag.getId().toString())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userSeriesSummary.getId()))
                .andExpect(jsonPath("$.watchflag.name").value(userSeriesSummary.getWatchflag().getName()))
                .andExpect(jsonPath("$.seriesSummary.name").value(userSeriesSummary.getSeriesSummary().getName()));
    }
}
