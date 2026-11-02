
package com.curescript.controller;

import com.curescript.dto.PrescriptionAiResponseDTO;
import com.curescript.service.PrescriptionAiService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionAiController {

    private final PrescriptionAiService prescriptionAiService;

    public PrescriptionAiController(
            PrescriptionAiService prescriptionAiService) {
        this.prescriptionAiService = prescriptionAiService;
    }

    @PostMapping("/{prescriptionId}/extract")
    public ResponseEntity<PrescriptionAiResponseDTO> extractPrescriptionInfo(
            @PathVariable Long prescriptionId) throws Exception {

        PrescriptionAiResponseDTO response =
                prescriptionAiService.extractAndSaveMedicines(
                        prescriptionId);

        return ResponseEntity.ok(response);
    }


}
