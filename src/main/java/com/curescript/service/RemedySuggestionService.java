
package com.curescript.service;

import com.curescript.dto.RemedySuggestionDTO;
import com.curescript.entity.Medicine;
import com.curescript.entity.RemedySuggestion;
import com.curescript.exception.ResourceNotFoundException;
import com.curescript.repository.MedicineRepository;
import com.curescript.repository.RemedySuggestionRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RemedySuggestionService {

    private final RemedySuggestionRepository remedySuggestionRepository;
    private final MedicineRepository medicineRepository;

    public RemedySuggestionService(
            RemedySuggestionRepository remedySuggestionRepository,
            MedicineRepository medicineRepository) {

        this.remedySuggestionRepository = remedySuggestionRepository;
        this.medicineRepository = medicineRepository;
    }

    // Get the currently logged-in patient's email
    private String getLoggedInEmail() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }

    // Verify that the medicine belongs to the logged-in patient
    private void validateMedicineOwnership(Medicine medicine) {
        String loggedInEmail = getLoggedInEmail();

        String ownerEmail = medicine.getPrescription()
                .getPatient()
                .getEmail();

        if (!loggedInEmail.equalsIgnoreCase(ownerEmail)) {
            throw new AccessDeniedException(
                    "You are not allowed to access this medicine"
            );
        }
    }

    // Convert entity to DTO
    private RemedySuggestionDTO convertToDTO(
            RemedySuggestion suggestion) {

        RemedySuggestionDTO dto = new RemedySuggestionDTO();

        dto.setId(suggestion.getId());
        dto.setMedicineId(suggestion.getMedicine().getId());
        dto.setRemedyText(suggestion.getRemedyText());
        dto.setSource(suggestion.getSource());

        return dto;
    }

    // Create remedy suggestion
    public RemedySuggestionDTO createRemedySuggestion(
            RemedySuggestionDTO remedySuggestionDTO) {

        Medicine medicine = medicineRepository
                .findById(remedySuggestionDTO.getMedicineId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Medicine not found with ID: "
                                        + remedySuggestionDTO.getMedicineId()
                        )
                );

        // Check ownership before creating
        validateMedicineOwnership(medicine);

        if (remedySuggestionRepository.existsByMedicine_Id(
                medicine.getId())) {

            throw new RuntimeException(
                    "A remedy suggestion already exists for this medicine"
            );
        }

        RemedySuggestion suggestion = new RemedySuggestion();

        suggestion.setMedicine(medicine);
        suggestion.setRemedyText(
                remedySuggestionDTO.getRemedyText()
        );
        suggestion.setSource(
                remedySuggestionDTO.getSource()
        );

        RemedySuggestion savedSuggestion =
                remedySuggestionRepository.save(suggestion);

        return convertToDTO(savedSuggestion);
    }

    // Get remedy suggestion by ID
    public RemedySuggestionDTO getRemedySuggestionById(Long id) {

        RemedySuggestion suggestion =
                remedySuggestionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Remedy suggestion not found with ID: "
                                                + id
                                )
                        );

        // Check ownership through the associated medicine
        validateMedicineOwnership(suggestion.getMedicine());

        return convertToDTO(suggestion);
    }

    // Get remedy suggestion by medicine ID
    public RemedySuggestionDTO getRemedySuggestionByMedicineId(
            Long medicineId) {

        // Check that the medicine exists and belongs to this patient
        Medicine medicine = medicineRepository
                .findById(medicineId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Medicine not found with ID: " + medicineId
                        )
                );

        validateMedicineOwnership(medicine);

        RemedySuggestion suggestion =
                remedySuggestionRepository.findByMedicine_Id(medicineId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Remedy suggestion not found for medicine ID: "
                                                + medicineId
                                )
                        );

        return convertToDTO(suggestion);
    }
}
