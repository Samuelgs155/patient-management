package com.spring.patients_backend.repositories;

import com.spring.patients_backend.entities.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class PatientRepositoryTest {

    @Autowired
    PatientRepository repo;

    private Patient p(String dni) {
        Patient p = new Patient();
        p.setNombre("Test");
        p.setApellidos("User");
        p.setDni(dni);
        p.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        p.setEmail("t@test.com");
        return p;
    }

    @Test
    void existsByDni_returnsTrueWhenPresent() {
        repo.save(p("DNI1"));

        assertTrue(repo.existsByDni("DNI1"));
        assertFalse(repo.existsByDni("DNI2"));
    }

    @Test
    void findByDni_returnsPatient() {
        repo.save(p("DNI9"));

        var found = repo.findByDni("DNI9");

        assertTrue(found.isPresent());
        assertEquals("DNI9", found.get().getDni());
    }
}