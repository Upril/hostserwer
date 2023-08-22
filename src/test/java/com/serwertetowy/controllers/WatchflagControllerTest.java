package com.serwertetowy.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serwertetowy.config.JWTAuthFilter;
import com.serwertetowy.config.JwtService;
import com.serwertetowy.config.SecurityConfiguration;
import com.serwertetowy.entities.User;
import com.serwertetowy.entities.WatchFlags;
import com.serwertetowy.repos.WatchFlagRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static com.serwertetowy.config.Role.ADMIN;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WatchFlagController.class)
@ContextConfiguration(classes = {JwtService.class, JWTAuthFilter.class})
@AutoConfigureMockMvc
public class WatchflagControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    WatchFlagRepository watchFlagRepository;
    @Autowired
    ObjectMapper objectMapper;
    WatchFlags watchFlag = new WatchFlags(1L,"Watcheded");
    User adminUser = new User("admin","admin","admin@mail.com","secret",ADMIN);

    @Test
    void when_saveWatchFlag_thenReturn_Watchflag() throws Exception {
        WatchFlagController.WatchFlagRequest request = new WatchFlagController.WatchFlagRequest("Watcheded");
        Mockito.when(watchFlagRepository.save(Mockito.any(WatchFlags.class))).thenReturn(watchFlag);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/watchflag").with(user(adminUser))
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
