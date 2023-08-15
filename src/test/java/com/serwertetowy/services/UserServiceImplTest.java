package com.serwertetowy.services;

import com.serwertetowy.entities.*;
import com.serwertetowy.exceptions.FileEmptyException;
import com.serwertetowy.exceptions.UserDeletedException;
import com.serwertetowy.repos.*;
import com.serwertetowy.services.dto.RatingSummary;
import com.serwertetowy.services.dto.UserSeriesData;
import com.serwertetowy.services.dto.UserSeriesSummary;
import com.serwertetowy.services.dto.UserSummary;
import com.serwertetowy.services.implementations.UserServiceImpl;
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
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @Mock
    UserSeriesRepository userSeriesRepository;
    @Mock
    SeriesRepository seriesRepository;
    @Mock
    SeriesTagsRepository seriesTagsRepository;
    @Mock
    TagRepository tagRepository;
    @Mock
    EpisodesService episodesService;
    @Mock
    WatchFlagRepository watchFlagRepository;
    @Mock
    RatingRepository ratingRepository;
    @Mock
    ResourceLoader resourceLoader;

    @InjectMocks
    UserServiceImpl userService;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUserWithImage() throws IOException {
        User user = new User();
        user.setId(1L);
        user.setFirstname("test");
        user.setLastname("testuser");
        user.setPassword("password");
        byte[] imageData = "test image data".getBytes();
        MockMultipartFile file = new MockMultipartFile("file","test.jpg","image/jpeg",imageData);

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        User result = userService.registerUserWithImage(user,file);

        Mockito.verify(userRepository, times(1)).save(Mockito.any(User.class));
        Assertions.assertNotNull(result);
        Assertions.assertArrayEquals(imageData,user.getImageData());
    }
    @Test
    void testRegisterUserDefaultImage() throws IOException {
        User user = new User();
        user.setId(1L);
        user.setFirstname("test");
        user.setLastname("testuser");
        user.setPassword("password");

        File file = new File("src/main/resources/images/defalt.jpg");
        MultipartFile mpfile = new MockMultipartFile("defalt.jpg", new FileInputStream(file));
        Mockito.when(resourceLoader.getResource("classpath:/images/defalt.jpg")).thenReturn(mpfile.getResource());
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        User result = userService.registerUser(user);

        Mockito.verify(userRepository, times(1)).save(user);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(user.getImageData());
    }
    @Test
    void testGetAllUsers() {
        UserSummary userSummary1 = new UserSummary() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getFirstname() {
                return "John";
            }

            @Override
            public String getLastname() {
                return "Darksouls";
            }

            @Override
            public String getEmail() {
                return "darksouls@gmail.com";
            }

            @Override
            public Boolean getDeleted() {
                return false;
            }
        };
        UserSummary userSummary2 = new UserSummary() {
            @Override
            public Long getId() {
                return 2L;
            }

            @Override
            public String getFirstname() {
                return "John";
            }

            @Override
            public String getLastname() {
                return "Bloodborne";
            }

            @Override
            public String getEmail() {
                return "bloodborne@gmail.com";
            }

            @Override
            public Boolean getDeleted() {
                return false;
            }
        };

        List<UserSummary> expected = new ArrayList<>();
        expected.add(userSummary1);
        expected.add(userSummary2);

        when(userRepository.findAllUserData()).thenReturn(expected);
        List<UserSummary> result = userService.getAllUsers();

        verify(userRepository, times(1)).findAllUserData();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected,result);
    }
    @Test
    void testGetUserByEmail() throws UserDeletedException {
        UserSummary expected = new UserSummary() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getFirstname() {
                return "John";
            }

            @Override
            public String getLastname() {
                return "Darksouls";
            }

            @Override
            public String getEmail() {
                return "darksouls@gmail.com";
            }

            @Override
            public Boolean getDeleted() {
                return false;
            }
        };

        when(userRepository.findByEmail(anyString())).thenReturn(expected);

        UserSummary result = userService.getUserByEmail("darksouls@gmail.com");

        verify(userRepository, times(1)).findByEmail(anyString());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected,result);
    }
    @Test
    void testGetUserById() {
        // Mock input data
        Long userId = 1L;

        // Mock user
        User user = new User();
        user.setId(1L);
        user.setFirstname("test");
        user.setLastname("testuser");
        user.setPassword("password");

        // Mock repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Call the service method
        User result = userService.getUserById(userId);

        // Verify repository interactions
        verify(userRepository, times(1)).findById(userId);

        // Verify the returned result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(user, result);
    }
    @Test
    void testGetUserByIdNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class, () -> userService.getUserById(userId));

        verify(userRepository, times(1)).findById(userId);
    }
    @Test
    void testGetUserRatingsById() {
        Long userId = 1L;
        RatingSummary ratingSummary = new RatingSummary() {
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
                return 0;
            }

            @Override
            public short getMusicRating() {
                return 0;
            }

            @Override
            public short getGraphicsRating() {
                return 0;
            }

            @Override
            public short getCharactersRating() {
                return 0;
            }

            @Override
            public short getGeneralRating() {
                return 0;
            }
        };
        RatingSummary ratingSummary1 = new RatingSummary() {
            @Override
            public Long getId() {
                return 2L;
            }

            @Override
            public Long getSeriesId() {
                return 2L;
            }

            @Override
            public Long getUserId() {
                return 1L;
            }

            @Override
            public short getPlotRating() {
                return 0;
            }

            @Override
            public short getMusicRating() {
                return 0;
            }

            @Override
            public short getGraphicsRating() {
                return 0;
            }

            @Override
            public short getCharactersRating() {
                return 0;
            }

            @Override
            public short getGeneralRating() {
                return 0;
            }
        };
        List<RatingSummary> expected = List.of(ratingSummary,ratingSummary1);

        when(ratingRepository.findByUserId(userId)).thenReturn(expected);

        List<RatingSummary> result = userService.getUserRatingsById(userId);

        verify(ratingRepository, times(1)).findByUserId(userId);
        Assertions.assertEquals(expected,result);
        Assertions.assertNotNull(result);
    }
    @Test
    void testGetUserImage() {
        // Mock input data
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setFirstname("test");
        user.setLastname("testuser");
        user.setPassword("password");
        byte[] imageData = "test image data".getBytes();
        user.setImageData(imageData);

        // Mock repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Call the service method
        Resource result = userService.getUserImage(userId);

        // Verify the returned result
        verify(userRepository, times(1)).findById(userId);
        Assertions.assertNotNull(result);
        ByteArrayResource byteArrayResource = (ByteArrayResource) result;
        Assertions.assertArrayEquals(imageData, byteArrayResource.getByteArray());
    }
    @Test
    void testGetUserImageNotFound() {
        // Mock input data
        Long userId = 1L;

        // Mock repository behavior when the user is not found
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Call the service method and expect a ResponseStatusException
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.getUserImage(userId));

        // Verify repository interactions
        verify(userRepository, times(1)).findById(userId);
    }
    @Test
    void testGetWatchlist() {
        // Mock input data
        Long userId = 1L;
        UserSeriesData userSeriesData1 = new UserSeriesData() {
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
            public Long getWatchFlagsId() {
                return 1L;
            }
        };
        UserSeriesData userSeriesData2 = new UserSeriesData() {
            @Override
            public Long getId() {
                return 2L;
            }

            @Override
            public Long getSeriesId() {
                return 2L;
            }

            @Override
            public Long getUserId() {
                return 1L;
            }

            @Override
            public Long getWatchFlagsId() {
                return 1L;
            }
        };
        List<UserSeriesData> userSeriesDataList = List.of(userSeriesData1,userSeriesData2);

        Tags tag1 = new Tags();
        tag1.setId(1L);
        tag1.setName("Tag 1");

        Tags tag2 = new Tags();
        tag2.setId(2L);
        tag2.setName("Tag 2");

        WatchFlags watchFlag = new WatchFlags();
        watchFlag.setId(1L);
        watchFlag.setName("Watching");

        Series series1 = seriesAssemble("Series 1","Description 1",List.of(1,2));
        Series series2 = seriesAssemble("Series 2","Description 2",List.of(2));

        SeriesTags seriesTags1 = new SeriesTags(series1,tag1);
        SeriesTags seriesTags2 = new SeriesTags(series1,tag2);
        SeriesTags seriesTags3 = new SeriesTags(series2,tag2);

        // Mock repository behavior
        when(watchFlagRepository.findById(1)).thenReturn(Optional.of(watchFlag));
        when(userSeriesRepository.findByUserId(userId)).thenReturn(userSeriesDataList);
        when(seriesRepository.findById(1)).thenReturn(Optional.of(series1));
        when(seriesRepository.findById(2)).thenReturn(Optional.of(series2));
        when(seriesTagsRepository.findBySeriesId(series1.getId())).thenReturn(List.of(seriesTags1,seriesTags2));
        when(seriesTagsRepository.findBySeriesId(series2.getId())).thenReturn(List.of(seriesTags3));
        lenient().when(tagRepository.findById(1)).thenReturn(Optional.of(tag1));
        when(tagRepository.findById(2)).thenReturn(Optional.of(tag2));

        // Call the service method
        List<UserSeriesSummary> result = userService.getWatchlist(userId);

        // Verify the returned result
        verify(userSeriesRepository, times(1)).findByUserId(userId);
        verify(tagRepository, times(1)).findById(1);
        verify(tagRepository, times(2)).findById(2);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());

        UserSeriesSummary userSeriesSummary1 = result.get(0);
        Assertions.assertEquals(userSeriesData1.getId(), userSeriesSummary1.getId());
        Assertions.assertNotNull(userSeriesSummary1.getSeriesSummary());
        Assertions.assertEquals(series1.getId(), userSeriesSummary1.getSeriesSummary().getId());
        Assertions.assertEquals(series1.getName(), userSeriesSummary1.getSeriesSummary().getName());
        Assertions.assertEquals(series1.getDescription(), userSeriesSummary1.getSeriesSummary().getDescription());
        Assertions.assertEquals(1, userSeriesSummary1.getSeriesSummary().getSeriesTags().size());
        Assertions.assertNotNull(userSeriesSummary1.getSeriesSummary().getEpisodes());
        Assertions.assertEquals(0, userSeriesSummary1.getSeriesSummary().getEpisodes().size());
        Assertions.assertNotNull(userSeriesSummary1.getWatchflag());

        UserSeriesSummary userSeriesSummary2 = result.get(1);
        Assertions.assertEquals(userSeriesData2.getId(), userSeriesSummary2.getId());
        Assertions.assertNotNull(userSeriesSummary2.getSeriesSummary());
        Assertions.assertEquals(series2.getId(), userSeriesSummary2.getSeriesSummary().getId());
        Assertions.assertEquals(series2.getName(), userSeriesSummary2.getSeriesSummary().getName());
        Assertions.assertEquals(series2.getDescription(), userSeriesSummary2.getSeriesSummary().getDescription());
        Assertions.assertEquals(1, userSeriesSummary2.getSeriesSummary().getSeriesTags().size());
        Assertions.assertNotNull(userSeriesSummary2.getSeriesSummary().getEpisodes());
        Assertions.assertEquals(0, userSeriesSummary2.getSeriesSummary().getEpisodes().size());
        Assertions.assertNotNull(userSeriesSummary2.getWatchflag());
    }
    @Test
    void testPutUserImage() throws IOException, FileEmptyException {
        // Mock input data
        Long userId = 1L;
        byte[] imageData = "test image data".getBytes();
        MockMultipartFile file = new MockMultipartFile("image.jpg", imageData);

        // Mock user data
        User user = new User();
        user.setId(userId);

        // Mock repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Call the service method
        userService.putUserImage(file, userId);

        // Verify repository interactions
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);

        // Verify that the image data was updated
        Assertions.assertArrayEquals(imageData, user.getImageData());
    }
    @Test
    void testPutUser() {
        // Mock input data
        Long userId = 1L;
        String firstname = "John";
        String lastname = "Darksouls";
        String email = "john.darksouls@example.com";

        // Mock user data
        User user = new User();
        user.setId(userId);
        UserSummary expected = new UserSummary() {
            @Override
            public Long getId() {
                return userId;
            }

            @Override
            public String getFirstname() {
                return firstname;
            }

            @Override
            public String getLastname() {
                return lastname;
            }

            @Override
            public String getEmail() {
                return email;
            }

            @Override
            public Boolean getDeleted() {
                return false;
            }
        };

        // Mock repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(email)).thenReturn(expected);

        // Call the service method
        UserSummary result = userService.putUser(userId, firstname, lastname, email,"authidentity");

        // Verify that the user data was updated
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).findByEmail(email);
        Assertions.assertEquals(firstname, user.getFirstname());
        Assertions.assertEquals(lastname, user.getLastname());
        Assertions.assertEquals(email, user.getEmail());
        Assertions.assertEquals(expected, result);
    }
    Series seriesAssemble(String name, String description, List<Integer> tagIds){
        Tags tag1 = new Tags();
        tag1.setId(1L);
        tag1.setName("Tag 1");

        Tags tag2 = new Tags();
        tag2.setId(2L);
        tag2.setName("Tag 2");

        when(tagRepository.findById(1)).thenReturn(Optional.of(tag1));
        when(tagRepository.findById(2)).thenReturn(Optional.of(tag2));
        //assembling series tag data from request data
        Set<SeriesTags> seriesTagsSet = new HashSet<>();
        Series series = new Series(name,description);
        for (Integer id : tagIds){
            seriesTagsSet.add(new SeriesTags(series,tagRepository.findById(id).orElseThrow(
                    ()->new ResponseStatusException(HttpStatus.NOT_FOUND))));
        }
        series.setSeriesTags(seriesTagsSet);
        return series;
    }
}
