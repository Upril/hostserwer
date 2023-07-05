package com.serwertetowy.controllers;

import com.serwertetowy.entities.User;
import com.serwertetowy.entities.WatchFlags;
import com.serwertetowy.services.UserService;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;
    //ObjectMapper objectMapper = new ObjectMapper();
    private final Long userId = 1L;
    private final String firstname = "John";
    private final String lastname = "Darksouls";
    private final String email = "john.darksouls@example.com";
    private final String password = "password";

    @Test
    public void testRegisterWithoutImage() throws Exception {
        // Mock the behavior of userService.registerUser()
        User user = new User(firstname,lastname,email,password);
        Mockito.when(userService.registerUser(Mockito.any(User.class))).thenReturn(user);

        //POST request without an image file
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/user/register")
                .param("firstname",firstname)
                .param("lastname",lastname)
                .param("email",email)
                .param("password",password)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value(firstname))
                .andExpect(jsonPath("$.lastname").value(lastname))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.password").value(password));

        Mockito.verify(userService).registerUser(user);
    }
    @Test
    public void testRegisterWithImage() throws Exception {
        User user = new User(firstname, lastname, email, password);
        Mockito.when(userService.registerUserWithImage(Mockito.any(User.class),Mockito.any(MultipartFile.class)))
                .thenReturn(user);
        //mock multipart file
        InputStream imageInputStream = getClass().getResourceAsStream("classpath:/images/defalt.jpg");
        MockMultipartFile imageFile = new MockMultipartFile("file","defalt.jpg", MediaType.IMAGE_JPEG_VALUE, imageInputStream);

        //POST request with an image file
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/user/register")
                        .file(imageFile)
                        .param("firstname", firstname)
                        .param("lastname", lastname)
                        .param("email", email)
                        .param("password", password)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value(firstname))
                .andExpect(jsonPath("$.lastname").value(lastname))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.password").value(password));

        Mockito.verify(userService).registerUserWithImage(user, imageFile);
    }
    @Test
    public void testGetAllUsers() throws Exception {
        UserSummary userSummary1 = new UserSummary() {
            @Override
            public Long getId() {
                return 1L;
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
        };
        UserSummary userSummary2 = new UserSummary() {
            @Override
            public Long getId() {
                return 2L;
            }

            @Override
            public String getFirstname() {
                return "Jane";
            }

            @Override
            public String getLastname() {
                return "Fortnite";
            }

            @Override
            public String getEmail() {
                return "jane@fortnite.com";
            }
        };
        List<UserSummary> expected = List.of(userSummary1,userSummary2);

        Mockito.when(userService.getAllUsers()).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expected.size()))
                .andExpect(jsonPath("$[0].firstname").value(expected.get(0).getFirstname()))
                .andExpect(jsonPath("$[0].lastname").value(expected.get(0).getLastname()))
                .andExpect(jsonPath("$[1].firstname").value(expected.get(1).getFirstname()))
                .andExpect(jsonPath("$[1].lastname").value(expected.get(1).getLastname()));

        Mockito.verify(userService).getAllUsers();
    }
    @Test
    public void testGetUserImage() throws Exception {
        byte[] imageBytes = "testImage".getBytes();
        Resource resource = new ByteArrayResource(imageBytes);
        Mockito.when(userService.getUserImage(userId)).thenReturn(resource);
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/{id}/image", userId)
                        .contentType(MediaType.IMAGE_JPEG))
                .andExpect(status().isOk())
                .andExpect(content().bytes(imageBytes))
                .andReturn()
                .getResponse();

        Mockito.verify(userService).getUserImage(userId);
        String contentType = response.getContentType();
        assertEquals(MediaType.IMAGE_JPEG_VALUE, contentType);
    }
    @Test
    public void testGetUserWatchlist() throws Exception {
        WatchFlags watchFlag1 = new WatchFlags(1L,"Watching");
        WatchFlags watchFlag2 = new WatchFlags(2L,"Finished");
        UserSummary userSummary = new UserSummary() {
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
        };

        SeriesSummary seriesSummary1 = new SeriesSummary(1L,"Series 1", "Description 1",
                null,null);
        SeriesSummary seriesSummary2 = new SeriesSummary(1L,"Series 2", "Description 2",
                null,null);
        UserSeriesSummary userSeriesSummary1 = new UserSeriesSummary() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public SeriesSummary getSeriesSummary() {
                return seriesSummary1;
            }

            @Override
            public UserSummary getUserSummary() {
                return userSummary;
            }

            @Override
            public WatchFlags getWatchflag() {
                return watchFlag1;
            }
        };
        UserSeriesSummary userSeriesSummary2 = new UserSeriesSummary() {
            @Override
            public Long getId() {
                return 2L;
            }

            @Override
            public SeriesSummary getSeriesSummary() {
                return seriesSummary2;
            }

            @Override
            public UserSummary getUserSummary() {
                return userSummary;
            }

            @Override
            public WatchFlags getWatchflag() {
                return watchFlag2;
            }
        };

        List<UserSeriesSummary> userSeriesSummaryList = Arrays.asList(userSeriesSummary1,userSeriesSummary2);
        List<UserController.WatchlistDto> expected = Arrays.asList(
                new UserController.WatchlistDto(seriesSummary1,watchFlag1.getName()),
                new UserController.WatchlistDto(seriesSummary2,watchFlag2.getName())
        );
        Mockito.when(userService.getWatchlist(userId)).thenReturn(userSeriesSummaryList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/{id}/watchlist", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expected.size()))
                .andExpect(jsonPath("$[0].seriesSummary.name").value(expected.get(0).seriesSummary().getName()))
                .andExpect(jsonPath("$[0].seriesSummary.description").value(expected.get(0).seriesSummary().getDescription()))
                .andExpect(jsonPath("$[0].watchFlag").value(expected.get(0).watchFlag()))
                .andExpect(jsonPath("$[1].seriesSummary.name").value(expected.get(1).seriesSummary().getName()))
                .andExpect(jsonPath("$[1].seriesSummary.description").value(expected.get(1).seriesSummary().getDescription()))
                .andExpect(jsonPath("$[1].watchFlag").value(expected.get(1).watchFlag()));
        Mockito.verify(userService).getWatchlist(userId);
    }
}
