package com.serwertetowy.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serwertetowy.services.SeriesService;
import com.serwertetowy.services.dto.SeriesSummary;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SeriesController.class)
public class SeriesControllerTest {
    @MockBean
    SeriesService seriesService;
    @Autowired
    MockMvc mockMvc;
    //ObjectMapper objectMapper = new ObjectMapper();
    SeriesSummary expected = new SeriesSummary(1L,"tetowa","seriaTetowa",null,null);
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

}
