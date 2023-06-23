package com.serwertetowy.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serwertetowy.entities.WatchFlags;
import com.serwertetowy.repos.WatchFlagRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WatchFlagController.class)
public class WatchflagControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    WatchFlagRepository watchFlagRepository;
    @Autowired
    ObjectMapper objectMapper;
    WatchFlags watchFlag = new WatchFlags(1L,"Watcheded");

    @Test
    void when_saveWatchFlag_thenReturn_Watchflag() throws Exception {
        WatchFlagController.WatchFlagRequest request = new WatchFlagController.WatchFlagRequest("Watcheded");
        Mockito.when(watchFlagRepository.save(Mockito.any(WatchFlags.class))).thenReturn(watchFlag);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/watchflag")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Watcheded"));
    }
    @Test
    void when_getWatchFlag_thenReturn_Watchflag() throws Exception {
        Mockito.when(watchFlagRepository.findById(1)).thenReturn(Optional.of(watchFlag));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/watchflag/{id}",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Watcheded"));
    }
    @Test
    void when_getAllWatchFlags_thenReturn_WatchflagList() throws Exception {
        Mockito.when(watchFlagRepository.findAll()).thenReturn(List.of(watchFlag));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/watchflag"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Watcheded"));
    }

}
