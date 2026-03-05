package com.spring.patients_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.patients_backend.dto.requests.PatientRequestDTO;
import com.spring.patients_backend.entities.Patient;
import com.spring.patients_backend.repositories.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PatientControllerIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PatientRepository repo;

    @BeforeEach
    void clean() {
        repo.deleteAll();
    }

    @Test
    void create_thenGetAll_returnsCreatedPatient() throws Exception {
        PatientRequestDTO req = PatientRequestDTO.builder()
                .nombre("Ana")
                .apellidos("García")
                .dni("XYZ9")
                .fechaNacimiento(LocalDate.of(1985, 5, 20))
                .telefono("611111111")
                .email("ana@test.com")
                .build();

        // POST /api/patients
        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.dni").value("XYZ9"));

        // GET /api/patients
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].dni").value("XYZ9"));
    }

    @Test
    void getOne_returns404or400_whenNotFound_dependsOnYourHandler() throws Exception {
        // En tu service lanzas IllegalArgumentException -> handler devuelve 400
        mockMvc.perform(get("/api/patients/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Paciente no encontrado")));
    }

    @Test
    void update_changesFields() throws Exception {
        // Insertar paciente directamente vía repo (parte de integración)
        Patient p = new Patient();
        p.setNombre("Juan");
        p.setApellidos("Pérez");
        p.setDni("DNI1");
        p.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        p.setEmail("juan@test.com");
        p = repo.save(p);

        PatientRequestDTO update = PatientRequestDTO.builder()
                .nombre("Juan Updated")
                .apellidos("Pérez")
                .dni("DNI1")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .telefono("600000000")
                .email("juan2@test.com")
                .build();

        mockMvc.perform(put("/api/patients/{id}", p.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Updated"))
                .andExpect(jsonPath("$.email").value("juan2@test.com"));
    }

    @Test
    void delete_removesPatient() throws Exception {
        Patient p = new Patient();
        p.setNombre("A");
        p.setApellidos("B");
        p.setDni("DEL1");
        p = repo.save(p);

        mockMvc.perform(delete("/api/patients/{id}", p.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void create_invalidRequest_returns400_andValidationErrors() throws Exception {
        String badJson = """
          {"email":"bad@test.com"}
        """;

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isBadRequest());
    }
}