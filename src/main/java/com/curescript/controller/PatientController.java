package com.curescript.controller;

import com.curescript.dto.PatientDTO;
import com.curescript.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/register")
    public ResponseEntity<PatientDTO> registerPatient(
            @RequestBody PatientDTO patientDTO) {

        PatientDTO registeredPatient =
                patientService.registerPatient(patientDTO);

        return new ResponseEntity<>(
                registeredPatient,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(
            @PathVariable Long id) {

        PatientDTO patientDTO = patientService.getPatientById(id);

        return ResponseEntity.ok(patientDTO);
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {

        List<PatientDTO> patients = patientService.getAllPatients();

        return ResponseEntity.ok(patients);
    }
}