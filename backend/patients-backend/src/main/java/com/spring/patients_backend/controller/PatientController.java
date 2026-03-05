package com.spring.patients_backend.controller;

import com.spring.patients_backend.dto.requests.PatientRequestDTO;
import com.spring.patients_backend.dto.responses.PatientResponseDTO;
import com.spring.patients_backend.entities.Patient;
import com.spring.patients_backend.mappers.PatientMapper;
import com.spring.patients_backend.services.IPatientService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    private final IPatientService service;

    public PatientController(IPatientService service) {
        this.service = service;
    }

    @GetMapping
    public List<PatientResponseDTO> all() {
        return service.findAll()
                .stream()
                .map(PatientMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public PatientResponseDTO one(@PathVariable Long id) {
        return PatientMapper.toDTO(service.findById(id));
    }

    @PostMapping
    public PatientResponseDTO create(@Valid @RequestBody PatientRequestDTO dto) {
        Patient patient = PatientMapper.toEntity(dto);
        return PatientMapper.toDTO(service.create(patient));
    }

    @PutMapping("/{id}")
    public PatientResponseDTO update(@PathVariable Long id,
                                     @Valid @RequestBody PatientRequestDTO dto) {

        Patient patient = PatientMapper.toEntity(dto);
        return PatientMapper.toDTO(service.update(id, patient));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
