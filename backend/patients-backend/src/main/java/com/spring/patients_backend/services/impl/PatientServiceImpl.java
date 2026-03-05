package com.spring.patients_backend.services.impl;

import com.spring.patients_backend.entities.Patient;
import com.spring.patients_backend.repositories.PatientRepository;
import com.spring.patients_backend.services.IPatientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatientServiceImpl implements IPatientService {

    private final PatientRepository repo;

    public PatientServiceImpl(PatientRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Patient> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Patient findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado: " + id));
    }

    @Transactional
    public Patient create(Patient p) {
        if (repo.existsByDni(p.getDni())) {
            throw new IllegalArgumentException("Ya existe un paciente con DNI: " + p.getDni());
        }
        return repo.save(p);
    }

    @Transactional
    public Patient update(Long id, Patient p) {
        Patient current = findById(id);

        // Si cambia DNI, validar unicidad
        if (!current.getDni().equals(p.getDni()) && repo.existsByDni(p.getDni())) {
            throw new IllegalArgumentException("Ya existe un paciente con DNI: " + p.getDni());
        }

        current.setNombre(p.getNombre());
        current.setApellidos(p.getApellidos());
        current.setDni(p.getDni());
        current.setFechaNacimiento(p.getFechaNacimiento());
        current.setTelefono(p.getTelefono());
        current.setEmail(p.getEmail());

        return repo.save(current);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Paciente no encontrado: " + id);
        }
        repo.deleteById(id);
    }
}
