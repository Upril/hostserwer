package com.serwertetowy.repos;

import com.serwertetowy.entities.Rating;
import com.serwertetowy.entities.Series;
import com.serwertetowy.entities.User;
import com.serwertetowy.services.dto.RatingSummary;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
public class RatingRepositoryIT {
    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    SeriesRepository seriesRepository;
    @Autowired
    UserRepository userRepository;
    @PersistenceContext
    EntityManager entityManager;
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
    @AfterEach
    void tearDown() {
        ratingRepository.deleteAll();
        entityManager.flush();
    }

    @BeforeEach
    void beforeAll() {
        if(!seriesRepository.existsById(expSeries.getId().intValue())){
            seriesRepository.save(expSeries);
            entityManager.flush();
        }
        if(!userRepository.existsById(expUser.getId())){
            userRepository.save(expUser);
            entityManager.flush();
        }
    }

    @Test
    void when_saved_and_findById_thenReturn_RatingSummary() {
        Rating rating = new Rating(expSeries,expUser, (short) 1,(short)1,(short)1,(short)1,(short)1);
        ratingRepository.save(rating);
        RatingSummary actual = ratingRepository.findById(1L);
        assertTrue(ratingRepository.existsById(1));
        assertEquals(expected.getCharactersRating(), actual.getCharactersRating());
        assertEquals(expected.getGeneralRating(), actual.getGeneralRating());
    }
    @Test
    void when_saved_and_findBySeriesId_ThenReturn_Listof_RatingSummary(){
        Rating rating = new Rating(expSeries,expUser, (short) 1,(short)1,(short)1,(short)1,(short)1);
        ratingRepository.save(rating);
        List<RatingSummary> expectedList = List.of(expected);
        List<RatingSummary> actual = ratingRepository.findBySeriesId(expSeries.getId());
        assertEquals(expectedList.size(), actual.size());
        assertEquals(expectedList.get(0).getGeneralRating(), actual.get(0).getGeneralRating());
        assertEquals(expectedList.get(0).getGraphicsRating(), actual.get(0).getGraphicsRating());
        assertEquals(expectedList.get(0).getCharactersRating(), actual.get(0).getCharactersRating());
    }
    @Test
    void when_saved_and_findByUserId_ThenReturn_Listof_RatingSummary(){
        Rating rating = new Rating(expSeries,expUser, (short) 1,(short)1,(short)1,(short)1,(short)1);
        ratingRepository.save(rating);
        List<RatingSummary> expectedList = List.of(expected);
        List<RatingSummary> actual = ratingRepository.findByUserId(expUser.getId());
        assertEquals(expectedList.size(), actual.size());
        assertEquals(expectedList.get(0).getGeneralRating(), actual.get(0).getGeneralRating());
        assertEquals(expectedList.get(0).getGraphicsRating(), actual.get(0).getGraphicsRating());
        assertEquals(expectedList.get(0).getCharactersRating(), actual.get(0).getCharactersRating());
    }
}
