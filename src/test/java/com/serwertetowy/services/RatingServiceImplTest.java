package com.serwertetowy.services;

import com.serwertetowy.entities.Rating;
import com.serwertetowy.entities.Series;
import com.serwertetowy.entities.User;
import com.serwertetowy.exceptions.RatingNotFoundException;
import com.serwertetowy.repos.RatingRepository;
import com.serwertetowy.repos.SeriesRepository;
import com.serwertetowy.repos.UserRepository;
import com.serwertetowy.services.dto.RatingSummary;
import com.serwertetowy.services.implementations.RatingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class RatingServiceImplTest {
    @Mock
    RatingRepository ratingRepository;
    @Mock
    SeriesRepository seriesRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    UserService userService;
    @InjectMocks
    RatingServiceImpl ratingService;
    User expUser = new User(1L,"firstname","lastname","email","password");
    Series expSeries = new Series(1L,"tet","tetowa",null,null,null,null);
    RatingSummary expected = new RatingSummary() {
        @Override
        public Long getId() {
            return 1L;
        }

        @Override
        public Long getSeriesId() {
            return expSeries.getId();
        }

        @Override
        public Long getUserId() {
            return expUser.getId();
        }

        @Override
        public short getPlotRating() {
            return 1;
        }

        @Override
        public short getMusicRating() {
            return 1;
        }

        @Override
        public short getGraphicsRating() {
            return 1;
        }

        @Override
        public short getCharactersRating() {
            return 1;
        }

        @Override
        public short getGeneralRating() {
            return 1;
        }
    };

    public RatingServiceImplTest(){MockitoAnnotations.openMocks(this);}
    @Test
    void when_getRating_thenReturn_RatingSummary() {
        when(ratingRepository.findById(anyLong())).thenReturn(expected);
        RatingSummary actual = ratingService.getRating(1L);
        assertEquals(actual.getGraphicsRating(), expected.getGraphicsRating());
        assertEquals(actual.getGeneralRating(), expected.getGeneralRating());
    }

    @Test
    void when_getRatingsBySeries_thenReturn_Listof_RatingSummary() {
        when(ratingRepository.findBySeriesId(anyLong())).thenReturn(List.of(expected));
        when(seriesRepository.existsById(anyInt())).thenReturn(true);
        List<RatingSummary> actual = ratingService.getRatingsBySeries(1L);
        assertEquals(actual.get(0).getGraphicsRating(), expected.getGraphicsRating());
        assertEquals(actual.get(0).getGeneralRating(), expected.getGeneralRating());
    }

    @Test
    void when_getRatingsByUser_thenReturn_Listof_RatingSummary() {
        when(ratingRepository.findByUserId(anyLong())).thenReturn(List.of(expected));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        List<RatingSummary> actual = ratingService.getRatingsByUser(1L);
        assertEquals(actual.get(0).getGraphicsRating(), expected.getGraphicsRating());
        assertEquals(actual.get(0).getGeneralRating(), expected.getGeneralRating());
    }

    @Test
    void saveRating() {
        when(userService.getUserById(anyLong())).thenReturn(expUser);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(seriesRepository.existsById(anyInt())).thenReturn(true);
        when(seriesRepository.findById(anyInt())).thenReturn(Optional.of(expSeries));
        ratingService.saveRating(expUser.getId(),expSeries.getId(),(short)1,(short)1,(short)1,(short)1,(short)1, expUser.getEmail());
        verify(seriesRepository,times(1)).findById(anyInt());
        verify(userService,times(1)).getUserById(anyLong());
        verify(ratingRepository,times(1)).save(any());
    }
    @Test
    void putRating(){
        Long id = 1L;
        short plotRating = 4;
        short musicRating = 3;
        short graphicsRating = 5;
        short charactersRating = 2;
        short generalRating = 4;

        Rating rating = new Rating();
        rating.setId(id);
        rating.setUser(expUser);

        when(ratingRepository.findById(id.intValue())).thenReturn(Optional.of(rating));
        when(ratingRepository.findById(id)).thenReturn(rating.toRatingSummary());
        when(ratingRepository.save(rating)).thenReturn(rating);
        when(ratingRepository.existsById(anyInt())).thenReturn(true);

        RatingSummary actual = ratingService.putRating(id, plotRating, musicRating, graphicsRating, charactersRating, generalRating, expUser.getEmail());

        verify(ratingRepository, times(1)).findById(id.intValue());
        verify(ratingRepository, times(1)).save(rating);
        verify(ratingRepository, times(1)).findById(id);

        assertEquals(rating.getId(), actual.getId());
        assertEquals(plotRating, actual.getPlotRating());
        assertEquals(musicRating, actual.getMusicRating());
        assertEquals(graphicsRating, actual.getGraphicsRating());
        assertEquals(charactersRating, actual.getCharactersRating());
        assertEquals(generalRating, actual.getGeneralRating());
    }
    @Test
    void testPutRatingNotFound() {
        Long id = 1L;
        short plotRating = 4;
        short musicRating = 3;
        short graphicsRating = 5;
        short charactersRating = 2;
        short generalRating = 4;

        assertThrows(RatingNotFoundException.class, () -> ratingService.putRating(id, plotRating, musicRating, graphicsRating, charactersRating, generalRating,expUser.getEmail()));

        verify(ratingRepository, times(1)).existsById(id.intValue());
        verify(ratingRepository, never()).save(any(Rating.class));
    }
    @Test
    void testDeleteRatingById() {
        Long id = 1L;

        Rating rating = new Rating();
        rating.setId(id);
        rating.setUser(expUser);

        when(ratingRepository.findById(id.intValue())).thenReturn(Optional.of(rating));
        when(ratingRepository.existsById(anyInt())).thenReturn(true);

        ratingService.deleteRatingById(id,expUser.getEmail());

        verify(ratingRepository, times(1)).findById(id.intValue());
        verify(ratingRepository, times(1)).delete(rating);
    }
    @Test
    void testDeleteRatingByIdNotFound() {
        Long id = 1L;
        assertThrows(RatingNotFoundException.class, () -> ratingService.deleteRatingById(id,expUser.getEmail()));

        verify(ratingRepository, times(1)).existsById(id.intValue());
        verify(ratingRepository, never()).delete(any(Rating.class));
    }
}
