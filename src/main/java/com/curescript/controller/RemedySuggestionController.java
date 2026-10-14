package com.curescript.controller;

import com.curescript.dto.RemedySuggestionDTO;
import com.curescript.service.RemedySuggestionService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/remedies")
public class RemedySuggestionController {

    private final RemedySuggestionService remedySuggestionService;

    public RemedySuggestionController(
            RemedySuggestionService remedySuggestionService) {
        this.remedySuggestionService = remedySuggestionService;
    }

    @PostMapping
    public ResponseEntity<RemedySuggestionDTO> createRemedySuggestion(
       @Valid @RequestBody RemedySuggestionDTO remedySuggestionDTO) {

        RemedySuggestionDTO createdSuggestion =
                remedySuggestionService.createRemedySuggestion(
                        remedySuggestionDTO
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdSuggestion);
    }
    @GetMapping("/{id}")
    public ResponseEntity<RemedySuggestionDTO> getRemedySuggestionById(
            @PathVariable Long id) {

        RemedySuggestionDTO suggestion =
                remedySuggestionService.getRemedySuggestionById(id);

        return ResponseEntity.ok(suggestion);
    }
    @GetMapping("/medicine/{medicineId}")
    public ResponseEntity<RemedySuggestionDTO>
    getRemedySuggestionByMedicineId(
            @PathVariable Long medicineId) {

        RemedySuggestionDTO suggestion =
                remedySuggestionService
                        .getRemedySuggestionByMedicineId(medicineId);

        return ResponseEntity.ok(suggestion);
    }
}