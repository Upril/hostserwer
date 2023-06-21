package com.serwertetowy.controllers;

import com.serwertetowy.services.RatingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serwertetowy.services.dto.RatingSummary;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RatingController.class)
public class RatingControllerTest {
    @MockBean
    RatingService ratingService;
    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    RatingSummary expected = new RatingSummary() {
        @Override
        public Long getId() {
            return 1L;
        }

        @Override
        public Long getSeriesId() {
            return 1L;
        }

        @Override
        public Long getUserId() {
            return 1L;
        }

        @Override
        public short getPlotRating() {
            return 1;
        }

        @Override
        public short getMusicRating() {
            return 2;
        }

        @Override
        public short getGraphicsRating() {
            return 3;
        }

        @Override
        public short getCharactersRating() {
            return 4;
        }

        @Override
        public short getGeneralRating() {
            return 5;
        }
    };
    @Test
    void when_saveRating_thenReturn_OK() throws Exception {
        record PostRatingRequest(Long userId, Long seriesId,
                                 short plotRating, short musicRating, short graphicsRating,
                                 short charactersRating, short generalRating){}
        PostRatingRequest request = new PostRatingRequest(1L,1L,(short)1,(short)2,(short)3,(short)4,(short)5);
        Mockito.doNothing().when(ratingService).saveRating(1L,1L,(short)1,(short)2,(short)3,(short)4,(short)5);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
    @Test
    void when_putRating_thenReturn_RatingSummary()throws Exception{
        record PutRatingRequest(short plotRating, short musicRating, short graphicsRating,
                                short charactersRating, short generalRating){}
        PutRatingRequest request = new PutRatingRequest((short)1,(short)2,(short)3,(short)4,(short)5);

        Mockito.when(ratingService.putRating(1L,(short)1,(short)2,(short)3,(short)4,(short)5)).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/ratings/{id}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.plotRating").value(1))
                .andExpect(jsonPath("$.musicRating").value(2))
                .andExpect(jsonPath("$.graphicsRating").value(3))
                .andExpect(jsonPath("$.generalRating").value(5));
    }
    @Test
    void when_deleteRating_thenReturn_Ok() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/ratings/{id}",1))
                .andExpect(status().isOk());
        Mockito.verify(ratingService).deleteRatingById(1L);
    }
    @Test
    void when_GetRatingsByUser_thenReturn_ListOfratingSummary() throws Exception {
        List<RatingSummary> ratingSummaryList = List.of(expected);
        Mockito.when(ratingService.getRatingsByUser(Mockito.anyLong())).thenReturn(ratingSummaryList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/ratings/user/{id}",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].plotRating").value(1))
                .andExpect(jsonPath("$[0].musicRating").value(2))
                .andExpect(jsonPath("$[0].graphicsRating").value(3))
                .andExpect(jsonPath("$[0].generalRating").value(5));
        Mockito.verify(ratingService).getRatingsByUser(1L);
    }
    @Test
    void when_GetRatingsBySeries_thenReturn_ListOfratingSummary() throws Exception {
        List<RatingSummary> ratingSummaryList = List.of(expected);
        Mockito.when(ratingService.getRatingsBySeries(Mockito.anyLong())).thenReturn(ratingSummaryList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/ratings/series/{id}",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].plotRating").value(1))
                .andExpect(jsonPath("$[0].musicRating").value(2))
                .andExpect(jsonPath("$[0].graphicsRating").value(3))
                .andExpect(jsonPath("$[0].generalRating").value(5));
        Mockito.verify(ratingService).getRatingsBySeries(1L);
    }
    @Test
    void when_GetRatingById_ThenReturn_RatingSummary() throws Exception {
        Mockito.when(ratingService.getRating(1L)).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/ratings/{id}",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.plotRating").value(1))
                .andExpect(jsonPath("$.musicRating").value(2))
                .andExpect(jsonPath("$.graphicsRating").value(3))
                .andExpect(jsonPath("$.generalRating").value(5));
        Mockito.verify(ratingService).getRating(1L);
    }



}
