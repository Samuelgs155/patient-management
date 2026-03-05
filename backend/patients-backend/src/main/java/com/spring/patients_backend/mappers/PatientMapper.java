package com.spring.patients_backend.mappers;


import com.spring.patients_backend.dto.requests.PatientRequestDTO;
import com.spring.patients_backend.dto.responses.PatientResponseDTO;
import com.spring.patients_backend.entities.Patient;

public class PatientMapper {

    public static PatientResponseDTO toDTO(Patient patient) {
        return PatientResponseDTO.builder()
                .id(patient.getId())
                .nombre(patient.getNombre())
                .apellidos(patient.getApellidos())
                .dni(patient.getDni())
                .fechaNacimiento(patient.getFechaNacimiento())
                .telefono(patient.getTelefono())
                .email(patient.getEmail())
                .build();
    }

    public static Patient toEntity(PatientRequestDTO dto) {
        return Patient.builder()
                .nombre(dto.getNombre())
                .apellidos(dto.getApellidos())
                .dni(dto.getDni())
                .fechaNacimiento(dto.getFechaNacimiento())
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .build();
    }
}