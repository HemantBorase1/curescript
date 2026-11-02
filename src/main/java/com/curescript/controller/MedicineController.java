
package com.curescript.controller;

import com.curescript.dto.MedicineDTO;
import com.curescript.service.MedicineService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    // Create Medicine
    @PostMapping
    public ResponseEntity<MedicineDTO> createMedicine(
            @Valid @RequestBody MedicineDTO medicineDTO) {

        MedicineDTO createdMedicine =
                medicineService.createMedicine(medicineDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdMedicine);
    }

    // Get Medicine By ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicineDTO> getMedicineById(
            @PathVariable Long id) {

        MedicineDTO medicineDTO =
                medicineService.getMedicineById(id);

        return ResponseEntity.ok(medicineDTO);
    }

    // Get All Medicines By Prescription ID
    @GetMapping("/prescription/{prescriptionId}")
    public ResponseEntity<List<MedicineDTO>> getMedicinesByPrescriptionId(
            @PathVariable Long prescriptionId) {

        List<MedicineDTO> medicines =
                medicineService.getMedicinesByPrescriptionId(prescriptionId);

        return ResponseEntity.ok(medicines);
    }

    // Verify Medicine
    @PutMapping("/{medicineId}/verify")
    public ResponseEntity<MedicineDTO> verifyMedicine(
            @PathVariable Long medicineId) {

        MedicineDTO verifiedMedicine =
                medicineService.verifyMedicine(medicineId);

        return ResponseEntity.ok(verifiedMedicine);
    }
    @PutMapping("/{medicineId}")
    public ResponseEntity<MedicineDTO> updateMedicine(
            @PathVariable Long medicineId,
            @RequestBody MedicineDTO medicineDTO) {

        MedicineDTO updatedMedicine =
                medicineService.updateMedicine(medicineId, medicineDTO);

        return ResponseEntity.ok(updatedMedicine);
    }
}

