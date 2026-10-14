package com.curescript.controller;

import com.curescript.dto.LoginRequestDTO;
import com.curescript.dto.PatientDTO;
import com.curescript.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;


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
          @Valid @RequestBody PatientDTO patientDTO) {

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

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(
            @PathVariable Long id,
         @Valid @RequestBody PatientDTO patientDTO) {

        PatientDTO updatedPatient =
                patientService.updatePatient(id, patientDTO);

        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(
            @PathVariable Long id) {

        patientService.deletePatient(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<PatientDTO> loginPatient(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        PatientDTO patientDTO =
                patientService.loginPatient(loginRequestDTO);

        return ResponseEntity.ok(patientDTO);
    }

}