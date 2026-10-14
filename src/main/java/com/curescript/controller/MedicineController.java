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

    @PostMapping
    public ResponseEntity<MedicineDTO> createMedicine(
          @Valid @RequestBody MedicineDTO medicineDTO) {

        MedicineDTO createdMedicine =
                medicineService.createMedicine(medicineDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdMedicine);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineDTO> getMedicineById(
            @PathVariable Long id) {

        MedicineDTO medicineDTO =
                medicineService.getMedicineById(id);

        return ResponseEntity.ok(medicineDTO);
    }

    @GetMapping("/prescription/{prescriptionId}")
    public ResponseEntity<List<MedicineDTO>>
    getMedicinesByPrescriptionId(
            @PathVariable Long prescriptionId) {

        List<MedicineDTO> medicines =
                medicineService.getMedicinesByPrescriptionId(
                        prescriptionId
                );

        return ResponseEntity.ok(medicines);
    }
}