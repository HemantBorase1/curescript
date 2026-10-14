package com.curescript.service;

import com.curescript.dto.RemedySuggestionDTO;
import com.curescript.entity.Medicine;
import com.curescript.entity.RemedySuggestion;
import com.curescript.repository.MedicineRepository;
import com.curescript.repository.RemedySuggestionRepository;

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

    public RemedySuggestionDTO createRemedySuggestion(
            RemedySuggestionDTO remedySuggestionDTO) {

        Medicine medicine = medicineRepository
                .findById(remedySuggestionDTO.getMedicineId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Medicine not found with ID: "
                                        + remedySuggestionDTO.getMedicineId()
                        )
                );

        if (remedySuggestionRepository.existsByMedicine_Id(
                medicine.getId())) {
            throw new RuntimeException(
                    "A remedy suggestion already exists for this medicine"
            );
        }

        RemedySuggestion remedySuggestion = new RemedySuggestion();

        remedySuggestion.setMedicine(medicine);
        remedySuggestion.setRemedyText(
                remedySuggestionDTO.getRemedyText()
        );
        remedySuggestion.setSource(
                remedySuggestionDTO.getSource()
        );

        RemedySuggestion savedSuggestion =
                remedySuggestionRepository.save(remedySuggestion);

        RemedySuggestionDTO responseDTO =
                new RemedySuggestionDTO();

        responseDTO.setId(savedSuggestion.getId());
        responseDTO.setMedicineId(
                savedSuggestion.getMedicine().getId()
        );
        responseDTO.setRemedyText(savedSuggestion.getRemedyText());
        responseDTO.setSource(savedSuggestion.getSource());

        return responseDTO;
    }
    // Get remedy suggestion by ID
    public RemedySuggestionDTO getRemedySuggestionById(Long id) {

        RemedySuggestion suggestion =
                remedySuggestionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Remedy suggestion not found with ID: "
                                                + id
                                )
                        );

        RemedySuggestionDTO responseDTO =
                new RemedySuggestionDTO();

        responseDTO.setId(suggestion.getId());
        responseDTO.setMedicineId(
                suggestion.getMedicine().getId()
        );
        responseDTO.setRemedyText(suggestion.getRemedyText());
        responseDTO.setSource(suggestion.getSource());

        return responseDTO;
    }
    // Get remedy suggestion by medicine ID
    public RemedySuggestionDTO getRemedySuggestionByMedicineId(
            Long medicineId) {

        RemedySuggestion suggestion =
                remedySuggestionRepository.findByMedicine_Id(medicineId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Remedy suggestion not found for medicine ID: "
                                                + medicineId
                                )
                        );

        RemedySuggestionDTO responseDTO =
                new RemedySuggestionDTO();

        responseDTO.setId(suggestion.getId());
        responseDTO.setMedicineId(
                suggestion.getMedicine().getId()
        );
        responseDTO.setRemedyText(suggestion.getRemedyText());
        responseDTO.setSource(suggestion.getSource());

        return responseDTO;
    }
}