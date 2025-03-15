package com.maids.Library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maids.Library.exception.ResourceNotFoundException;
import com.maids.Library.model.Patron;
import com.maids.Library.service.PatronService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(PatronController.class)
class PatronControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatronService patronService;

    @Autowired
    private ObjectMapper objectMapper;

    private Patron patron1;
    private Patron patron2;

    @BeforeEach
    public void setUp(){
        patron1 = new Patron(1L,"Ahmad Ahmad", "Ahmad@gmail.com");
        patron2 = new Patron(1L,"Mohammad Ahmad", "Mohammad@gmail.com");
    }

    @Test
    @DisplayName("GET /api/patrons - Should return all patrons")
    void testGetAllPatrons() throws Exception {
        List<Patron> patrons = Arrays.asList(patron1, patron2);
        Mockito.when(patronService.findAll()).thenReturn(patrons);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/patrons"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ahmad Ahmad"))
                .andExpect(jsonPath("$[1].name").value("Mohammad Ahmad"));
    }

    @Test
    @DisplayName("GET /api/patrons/{id} - Should return patron with valid ID")
    void testGetPatronById_ValidPatronId() throws Exception {
        Mockito.when(patronService.findById(1L)).thenReturn(patron1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/patrons/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.name").value("Ahmad Ahmad"));
    }

    @Test
    @DisplayName("GET /api/patrons/{id} - Should return 404 for invalid ID")
    void testGetPatronById_InvalidPatronId() throws Exception {
        Mockito.when(patronService.findById(1L)).thenThrow(new ResourceNotFoundException("Patron Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/patrons/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Patron Not Found"));
    }

    @Test
    @DisplayName("POST /api/patrons - Should create a new patron")
    void testCreatePatron_ValidPatron() throws Exception {
        Mockito.when(patronService.save(Mockito.any(Patron.class))).thenReturn(patron1);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patron1)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.name").value("Ahmad Ahmad"));
    }

    @Test
    @DisplayName("POST /api/patrons - Should return 400 for invalid name")
    void testCreatePatron_InvalidName() throws Exception {
        Patron patron = new Patron(1L, "", "Ahmad@gmail.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patron)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name is mandatory"));
    }

    @Test
    @DisplayName("POST /api/patrons - Should return 400 for name exceeding maximum length")
    void testCreatePatron_NameExceedsMaxLength() throws Exception {
        String longName = StringUtils.repeat("a", 300);
        Patron patron = new Patron(1L, longName, "Ahmad@gmail.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patron)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name must be less than 255 characters"));
    }

    @Test
    @DisplayName("POST /api/patrons - Should return 400 for invalid contact information")
    void testCreatePatron_InvalidContactInformation() throws Exception {
        Patron patron = new Patron(1L, "Ahmad Ahmad", "");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patron)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.contactInformation").value("Contact information is mandatory"));
    }

    @Test
    @DisplayName("POST /api/patrons - Should return 400 for invalid contact information")
    void testCreatePatron_InvalidContactInformation_InvalidEmail() throws Exception {
        Patron patron = new Patron(1L, "Ahmad Ahmad", "not an Email");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patron)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.contactInformation").value("Contact information must be a valid email address"));
    }

    @Test
    @DisplayName("PUT /api/patrons/{id} - Should update an existing patron")
    void testUpdatePatron_ValidPatron() throws Exception {
        Mockito.when(patronService.update(Mockito.eq(1L), Mockito.any(Patron.class))).thenReturn(patron1);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/patrons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patron1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.name").value("Ahmad Ahmad"));
    }

    @Test
    @DisplayName("PUT /api/patrons/{id} - Should return 404 for invalid ID")
    void testUpdatePatron_InvalidPatronId() throws Exception {
        Mockito.when(patronService.update(Mockito.eq(1L), Mockito.any(Patron.class)))
                .thenThrow(new ResourceNotFoundException("Patron Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/patrons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patron1)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Patron Not Found"));
    }

    @Test
    @DisplayName("DELETE /api/patrons/{id} - Should delete a patron and return 204 No Content")
    void testDeletePatron_ValidPatronId() throws Exception {
        doNothing().when(patronService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/patrons/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(patronService, Mockito.times(1)).delete(1L);
    }

    @Test
    @DisplayName("DELETE /api/patrons/{id} - Should return 404 for invalid patron ID")
    void testDeletePatron_InvalidPatronId() throws Exception {
        doThrow(new ResourceNotFoundException("Patron Not Found"))
                .when(patronService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/patrons/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Patron Not Found"));

        Mockito.verify(patronService, Mockito.times(1)).delete(1L);
    }




}