package com.serwertetowy.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serwertetowy.entities.User;
import com.serwertetowy.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;
    ObjectMapper objectMapper = new ObjectMapper();
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
}
