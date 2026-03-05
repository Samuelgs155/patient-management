package com.spring.patients_backend.repositories;

import com.spring.patients_backend.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByDni(String dni);
    boolean existsByDni(String dni);

}
