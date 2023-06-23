package com.serwertetowy.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serwertetowy.entities.Tags;
import com.serwertetowy.repos.TagRepository;
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

@WebMvcTest(TagController.class)
public class TagControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    TagRepository tagRepository;
    ObjectMapper objectMapper = new ObjectMapper();
    Tags tag = new Tags(1L,"Joe");
    @Test
    void when_addTag_thenReturn_Tag() throws Exception {
        TagController.TagRequest tagRequest = new TagController.TagRequest("Joe");
        Mockito.when(tagRepository.save(tag)).thenReturn(tag);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagRequest)))
                .andExpect(status().isOk());
    }
    @Test
    void when_getById_thenReturn_Tag() throws Exception{
        Mockito.when(tagRepository.findById(1)).thenReturn(Optional.of(tag));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tags/{id}",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Joe"));
    }
    @Test
    void when_getTags_thenReturn_TagList() throws Exception{
        Mockito.when(tagRepository.findAllByOrderByIdAsc()).thenReturn(List.of(tag));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Joe"));
    }
    @Test
    void when_deleteTag_thenReturn_OK() throws Exception{
        Mockito.when(tagRepository.existsById(1)).thenReturn(true);
        Mockito.doNothing().when(tagRepository).deleteById(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tags/{id}",1))
                .andExpect(status().isOk());
        Mockito.verify(tagRepository).deleteById(1);
    }
    @Test
    void when_deleteTag_and_TagNotExist_thenReturn_NotFound() throws Exception{
        Mockito.when(tagRepository.existsById(1)).thenReturn(false);
        Mockito.doNothing().when(tagRepository).deleteById(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tags/{id}",1))
                .andExpect(status().isNotFound());
    }
    @Test
    void when_updateTag_thenReturn_Tag() throws Exception{
        TagController.TagRequest request = new TagController.TagRequest("newName");
        Mockito.when(tagRepository.findById(1)).thenReturn(Optional.of(tag));
        Mockito.when(tagRepository.save(Mockito.any(Tags.class))).thenReturn(tag);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/tags/{id}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.name()));
    }
    @Test
    void when_updateTag_and_TagNotExist_thenReturn_NotFound() throws Exception{
        TagController.TagRequest request = new TagController.TagRequest("newName");
        Mockito.when(tagRepository.findById(1)).thenReturn(Optional.empty());
        Mockito.when(tagRepository.save(Mockito.any(Tags.class))).thenReturn(tag);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/tags/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }


}
