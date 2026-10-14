 package com.curescript.controller;

import com.curescript.dto.PrescriptionDTO;
import com.curescript.service.PrescriptionService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

 @RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(
            PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping
    public ResponseEntity<PrescriptionDTO> createPrescription(
            @RequestBody PrescriptionDTO prescriptionDTO) {

        PrescriptionDTO createdPrescription =
                prescriptionService.createPrescription(prescriptionDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdPrescription);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionDTO> getPrescriptionById(
            @PathVariable Long id) {

        PrescriptionDTO prescriptionDTO =
                prescriptionService.getPrescriptionById(id);

        return ResponseEntity.ok(prescriptionDTO);
    }

     @GetMapping("/patient/{patientId}")
     public ResponseEntity<List<PrescriptionDTO>>
     getPrescriptionsByPatientId(
             @PathVariable Long patientId) {

         List<PrescriptionDTO> prescriptions =
                 prescriptionService.getPrescriptionsByPatientId(
                         patientId
                 );

         return ResponseEntity.ok(prescriptions);
     }
}