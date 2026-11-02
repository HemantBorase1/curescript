
package com.curescript.service;

import com.curescript.dto.ExtractedMedicineDTO;
import com.curescript.dto.PrescriptionAiResponseDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class PrescriptionAiResponseValidator {

    public PrescriptionAiResponseDTO validate(
            PrescriptionAiResponseDTO response) {

        if (response == null) {
            throw new IllegalArgumentException(
                    "AI returned an empty prescription response");
        }

        List<ExtractedMedicineDTO> medicines = response.getMedicines();

        if (medicines == null || medicines.isEmpty()) {
            response.setNeedsProfessionalVerification(true);
            return response;
        }

        boolean needsVerification = false;

        for (ExtractedMedicineDTO medicine : medicines) {

            if (medicine == null) {
                needsVerification = true;
                continue;
            }

            Set<String> uncertainFields = new LinkedHashSet<>();

            if (medicine.getUncertainFields() != null) {
                uncertainFields.addAll(medicine.getUncertainFields());
            }

            addIfMissing(uncertainFields, "name", medicine.getName());
            addIfMissing(uncertainFields, "dosage", medicine.getDosage());
            addIfMissing(uncertainFields, "frequency", medicine.getFrequency());
            addIfMissing(uncertainFields, "duration", medicine.getDuration());
            addIfMissing(
                    uncertainFields,
                    "instructions",
                    medicine.getInstructions());

            medicine.setUncertainFields(
                    new ArrayList<>(uncertainFields));

            if (!uncertainFields.isEmpty()) {
                needsVerification = true;
            }
        }

        response.setNeedsProfessionalVerification(
                response.isNeedsProfessionalVerification()
                        || needsVerification);

        return response;
    }

    private void addIfMissing(
            Set<String> uncertainFields,
            String fieldName,
            String value) {

        if (value == null || value.isBlank()) {
            uncertainFields.add(fieldName);
        }
    }
}
