package com.curescript.service;

import com.curescript.dto.LoginRequestDTO;
import com.curescript.dto.PatientDTO;
import com.curescript.entity.Patient;
import com.curescript.repository.PatientRepository;
import org.springframework.stereotype.Service;
import com.curescript.exception.ResourceNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    public PatientService(PatientRepository patientRepository,
                          PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.passwordEncoder=passwordEncoder;
    }

    // Register a new patient
    public PatientDTO registerPatient(PatientDTO patientDTO) {

        if (patientRepository.existsByEmail(patientDTO.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        Patient patient = new Patient();

        patient.setName(patientDTO.getName());
        patient.setEmail(patientDTO.getEmail());
        patient.setPassword(passwordEncoder.encode(patientDTO.getPassword()));

        Patient savedPatient = patientRepository.save(patient);

        PatientDTO responseDTO = new PatientDTO();

        responseDTO.setId(savedPatient.getId());
        responseDTO.setName(savedPatient.getName());
        responseDTO.setEmail(savedPatient.getEmail());

        return responseDTO;
    }

    public PatientDTO loginPatient(LoginRequestDTO loginRequestDTO) {

        Patient patient = patientRepository
                .findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        boolean passwordMatches = passwordEncoder.matches(
                loginRequestDTO.getPassword(),
                patient.getPassword()
        );

        if (!passwordMatches) {
            throw new RuntimeException("Invalid email or password");
        }

        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(patient.getId());
        patientDTO.setName(patient.getName());
        patientDTO.setEmail(patient.getEmail());

        return patientDTO;
    }
    // Get patient by ID
    public PatientDTO getPatientById(Long id) {

        Optional<Patient> optionalPatient =
                patientRepository.findById(id);

        if (optionalPatient.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Patient not found with ID: " + id
            );
        }

        Patient patient = optionalPatient.get();

        PatientDTO patientDTO = new PatientDTO();

        patientDTO.setId(patient.getId());
        patientDTO.setName(patient.getName());
        patientDTO.setEmail(patient.getEmail());

        return patientDTO;
    }

    // Get all patients
    public List<PatientDTO> getAllPatients() {

        List<Patient> patients = patientRepository.findAll();

        List<PatientDTO> patientDTOs = new ArrayList<>();

        for (Patient patient : patients) {

            PatientDTO patientDTO = new PatientDTO();

            patientDTO.setId(patient.getId());
            patientDTO.setName(patient.getName());
            patientDTO.setEmail(patient.getEmail());

            patientDTOs.add(patientDTO);
        }

        return patientDTOs;
    }
    public PatientDTO updatePatient(Long id, PatientDTO patientDTO) {

        Optional<Patient> optionalPatient =
                patientRepository.findById(id);

        if (optionalPatient.isEmpty()) {
            throw new RuntimeException(
                    "Patient not found with ID: " + id
            );
        }

        Patient patient = optionalPatient.get();

        patient.setName(patientDTO.getName());
        patient.setEmail(patientDTO.getEmail());

        Patient updatedPatient = patientRepository.save(patient);

        PatientDTO responseDTO = new PatientDTO();

        responseDTO.setId(updatedPatient.getId());
        responseDTO.setName(updatedPatient.getName());
        responseDTO.setEmail(updatedPatient.getEmail());

        return responseDTO;
    }
    // Delete patient by ID
    public void deletePatient(Long id) {

        Optional<Patient> optionalPatient =
                patientRepository.findById(id);

        if (optionalPatient.isEmpty()) {
            throw new RuntimeException(
                    "Patient not found with ID: " + id
            );
        }

        patientRepository.delete(optionalPatient.get());
    }
}
