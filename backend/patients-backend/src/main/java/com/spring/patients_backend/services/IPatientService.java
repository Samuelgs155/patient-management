package com.spring.patients_backend.services;

import com.spring.patients_backend.entities.Patient;

import java.util.List;

public interface IPatientService {

    List<Patient> findAll();
    Patient findById(Long id);
    Patient create(Patient p);
    Patient update(Long id, Patient p);
    void delete(Long id);
}
