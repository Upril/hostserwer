package com.serwertetowy.controllers;

import com.serwertetowy.services.EpisodesService;
import com.serwertetowy.services.dto.EpisodeSummary;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EpisodesController.class)
public class EpisodesControllerTest {
    @MockBean
    EpisodesService episodesService;
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() throws IOException {
        EpisodeSummary episodeSummary = new EpisodeSummary() {
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
        EpisodeSummary newEpisodeSummary = new EpisodeSummary() {
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
                ArrayList<String> langs = new ArrayList<>();
                langs.add("Polish");
                langs.add("Nyakid");
                return langs;
            }
        };

        when(episodesService.getEpisode(anyInt())).thenReturn(episodeSummary);
        when(episodesService.getEpisodesBySeries(anyInt())).thenReturn(List.of(episodeSummary));
        when(episodesService.getEpisodeData(anyString())).thenReturn(Mono.empty());
        when(episodesService.saveEpisode(any(),anyString(),anyList(),anyInt())).thenReturn(episodeSummary);
        when(episodesService.putEpisodeData(anyLong(),any())).thenReturn(newEpisodeSummary);
        when(episodesService.putEpisode(anyLong(),anyString(), anyList(),anyInt())).thenReturn(newEpisodeSummary);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void when_saveEpisode_thenReturn_OK_and_EpisodeSummary() throws Exception {
        String name = "tetujemy";
        List<String> languages = Arrays.asList("English", "Polish");
        int seriesId = 1;
        EpisodeSummary expectedEpisodeSummary = new EpisodeSummary() {
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
        byte[] fileContent = Files.readAllBytes(Path.of("target/classes/tests/videos/tetujemy.mp4"));
        MockMultipartFile file = new MockMultipartFile("file", "tetujemy.mp4", MediaType.MULTIPART_FORM_DATA_VALUE, fileContent);
        mockMvc.perform(multipart("/api/v1/episode")
                .file(file)
                .param("name", name)
                .param("languages","English")
                .param("languages","Polish")
                .param("seriesId", Integer.toString(seriesId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(expectedEpisodeSummary.getTitle()))
                .andExpect(jsonPath("$.id").value(1));
        verify(episodesService, times(1)).saveEpisode(file,name,languages,seriesId);
    }

    @Test
    void putEpisodeData() throws Exception {
        List<String> languages = Arrays.asList("English", "Polish");
        long episodeId = 1L;
        int seriesId = 1;
        EpisodeSummary expectedEpisodeSummary = new EpisodeSummary() {
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
                return Arrays.asList("English","Nyakid");
            }
        };
        byte[] fileContent = Files.readAllBytes(Path.of("target/classes/tests/videos/tetujemy.mp4"));
        MockMultipartFile file = new MockMultipartFile("file", "tetujemy.mp4", MediaType.MULTIPART_FORM_DATA_VALUE, fileContent);
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/v1/episode/{id}/data",episodeId);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });
        mockMvc.perform(builder.file(file))
                .andExpect(status().isOk());
        verify(episodesService, times(1)).putEpisodeData(episodeId,file);
    }

    @Test
    void putEpisode() {
    }

    @Test
    void getEpisodeData() {
    }

    @Test
    void getEpisodebyId() {
    }
}
