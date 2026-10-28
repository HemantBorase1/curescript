 package com.curescript.controller;

import com.curescript.dto.PrescriptionDTO;
import com.curescript.service.PrescriptionService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
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
         @Valid @RequestBody PrescriptionDTO prescriptionDTO) {

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

     @PostMapping("/upload-image")
     public ResponseEntity<PrescriptionDTO> uploadPrescriptionImage(
             @RequestParam("file") org.springframework.web.multipart.MultipartFile file)
             throws java.io.IOException {

         // We'll connect the upload and database-saving logic
         // in the next step.
         throw new UnsupportedOperationException(
                 "Prescription image upload integration is not configured yet"
         );
     }


     @PostMapping(
             value = "/upload-image",
             consumes = "multipart/form-data"
     )
     public ResponseEntity<PrescriptionDTO> uploadPrescriptionImage(
             @RequestParam("file") MultipartFile file,
             @RequestParam(value = "doctorName", required = false)
             String doctorName) throws IOException {

         PrescriptionDTO savedPrescription =
                 prescriptionService.uploadPrescriptionImage(
                         file,
                         doctorName
                 );

         return ResponseEntity
                 .status(HttpStatus.CREATED)
                 .body(savedPrescription);
     }


 }