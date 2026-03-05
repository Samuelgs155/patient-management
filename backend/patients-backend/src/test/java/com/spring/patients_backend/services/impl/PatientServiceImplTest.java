package com.spring.patients_backend.services.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.spring.patients_backend.entities.Patient;
import com.spring.patients_backend.repositories.PatientRepository;
import com.spring.patients_backend.services.IPatientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    PatientRepository repo;

    @InjectMocks
    private PatientServiceImpl service;

    private Patient samplePatient(Long id, String dni) {
        Patient p = new Patient();
        p.setId(id);
        p.setNombre("Juan");
        p.setApellidos("Pérez");
        p.setDni(dni);
        p.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        p.setTelefono("600000000");
        p.setEmail("juan@test.com");
        return p;
    }

    @Test
    void findAll_returnsPatients() {
        when(repo.findAll()).thenReturn(List.of(samplePatient(1L, "123A")));

        List<Patient> res = service.findAll();

        assertEquals(1, res.size());
        verify(repo).findAll();
    }

    @Test
    void findById_whenExists_returnsPatient() {
        when(repo.findById(1L)).thenReturn(Optional.of(samplePatient(1L, "123A")));

        Patient p = service.findById(1L);

        assertEquals("123A", p.getDni());
        verify(repo).findById(1L);
    }

    @Test
    void findById_whenMissing_throws() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.findById(99L));
        verify(repo).findById(99L);
    }

    @Test
    void create_whenDniNotExists_saves() {
        Patient toCreate = samplePatient(null, "ABC1");
        when(repo.existsByDni("ABC1")).thenReturn(false);
        when(repo.save(any(Patient.class))).thenAnswer(inv -> {
            Patient saved = inv.getArgument(0);
            saved.setId(10L);
            return saved;
        });

        Patient created = service.create(toCreate);

        assertNotNull(created.getId());
        assertEquals("ABC1", created.getDni());
        verify(repo).existsByDni("ABC1");
        verify(repo).save(toCreate);
    }

    @Test
    void create_whenDniExists_throwsAndDoesNotSave() {
        Patient toCreate = samplePatient(null, "DUP1");
        when(repo.existsByDni("DUP1")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.create(toCreate));

        verify(repo).existsByDni("DUP1");
        verify(repo, never()).save(any());
    }

    @Test
    void update_whenChangeDniToExisting_throws() {
        Patient current = samplePatient(1L, "OLD");
        Patient incoming = samplePatient(null, "NEW");

        when(repo.findById(1L)).thenReturn(Optional.of(current));
        when(repo.existsByDni("NEW")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.update(1L, incoming));

        verify(repo).findById(1L);
        verify(repo).existsByDni("NEW");
        verify(repo, never()).save(any());
    }

    @Test
    void delete_whenExists_deletes() {
        when(repo.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repo).existsById(1L);
        verify(repo).deleteById(1L);
    }

    @Test
    void delete_whenMissing_throws() {
        when(repo.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> service.delete(1L));

        verify(repo).existsById(1L);
        verify(repo, never()).deleteById(anyLong());
    }
}