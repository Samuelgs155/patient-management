package com.spring.patients_backend.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.spring.patients_backend.dto.requests.PatientRequestDTO;
import com.spring.patients_backend.dto.responses.PatientResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.patients_backend.entities.Patient;
import com.spring.patients_backend.services.IPatientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean
    IPatientService service;

    private Patient sampleEntity(Long id) {
        Patient p = new Patient();
        p.setId(id);
        p.setNombre("Ana");
        p.setApellidos("García");
        p.setDni("XYZ9");
        p.setFechaNacimiento(LocalDate.of(1985, 5, 20));
        p.setTelefono("611111111");
        p.setEmail("ana@test.com");
        return p;
    }

    @Test
    void getAll_returnsListOfDtos() throws Exception {
        when(service.findAll()).thenReturn(List.of(sampleEntity(1L)));

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Ana"))
                .andExpect(jsonPath("$[0].dni").value("XYZ9"));

        verify(service).findAll();
    }

    @Test
    void getOne_returnsDto() throws Exception {
        when(service.findById(1L)).thenReturn(sampleEntity(1L));

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.dni").value("XYZ9"));

        verify(service).findById(1L);
    }

    @Test
    void create_validRequest_returnsDto() throws Exception {
        PatientRequestDTO req = PatientRequestDTO.builder()
                .nombre("Ana")
                .apellidos("García")
                .dni("XYZ9")
                .fechaNacimiento(LocalDate.of(1985, 5, 20))
                .telefono("611111111")
                .email("ana@test.com")
                .build();

        // el controller mapea dto->entity y luego service.create(entity)
        when(service.create(any(Patient.class))).thenAnswer(inv -> {
            Patient p = inv.getArgument(0);
            p.setId(10L);
            return p;
        });

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.dni").value("XYZ9"));

        verify(service).create(any(Patient.class));
    }

    @Test
    void create_invalidRequest_returns400() throws Exception {
        // faltan nombre/apellidos/dni (NotBlank)
        String badJson = """
            {"email":"bad@test.com"}
        """;

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_callsService() throws Exception {
        mockMvc.perform(delete("/api/patients/5"))
                .andExpect(status().isOk());

        verify(service).delete(5L);
    }
}