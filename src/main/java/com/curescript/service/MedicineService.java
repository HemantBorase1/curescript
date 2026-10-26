
package com.curescript.service;

import com.curescript.dto.MedicineDTO;
import com.curescript.entity.Medicine;
import com.curescript.entity.Prescription;
import com.curescript.exception.ResourceNotFoundException;
import com.curescript.repository.MedicineRepository;
import com.curescript.repository.PrescriptionRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final PrescriptionRepository prescriptionRepository;

    public MedicineService(
            MedicineRepository medicineRepository,
            PrescriptionRepository prescriptionRepository) {

        this.medicineRepository = medicineRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    // Create medicine with prescription ownership validation
    public MedicineDTO createMedicine(MedicineDTO medicineDTO) {

        // Step 1: Find the prescription
        Prescription prescription = prescriptionRepository
                .findById(medicineDTO.getPrescriptionId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Prescription not found with ID: "
                                        + medicineDTO.getPrescriptionId()
                        )
                );

        // Step 2: Get the logged-in patient's email
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String loggedInEmail = authentication.getName();

        // Step 3: Check prescription ownership
        String prescriptionOwnerEmail =
                prescription.getPatient().getEmail();

        if (!loggedInEmail.equalsIgnoreCase(prescriptionOwnerEmail)) {
            throw new AccessDeniedException(
                    "You are not allowed to add medicine to this prescription"
            );
        }

        // Step 4: Create the medicine
        Medicine medicine = new Medicine();

        medicine.setPrescription(prescription);
        medicine.setName(medicineDTO.getName());
        medicine.setDosage(medicineDTO.getDosage());
        medicine.setFrequency(medicineDTO.getFrequency());
        medicine.setDuration(medicineDTO.getDuration());
        medicine.setPlainExplanation(
                medicineDTO.getPlainExplanation()
        );

        // Step 5: Save the medicine
        Medicine savedMedicine = medicineRepository.save(medicine);

        // Step 6: Convert the saved medicine into a DTO
        MedicineDTO responseDTO = new MedicineDTO();

        responseDTO.setId(savedMedicine.getId());
        responseDTO.setPrescriptionId(
                savedMedicine.getPrescription().getId()
        );
        responseDTO.setName(savedMedicine.getName());
        responseDTO.setDosage(savedMedicine.getDosage());
        responseDTO.setFrequency(savedMedicine.getFrequency());
        responseDTO.setDuration(savedMedicine.getDuration());
        responseDTO.setPlainExplanation(
                savedMedicine.getPlainExplanation()
        );

        return responseDTO;
    }

    // Get medicine by ID
    public MedicineDTO getMedicineById(Long id) {

        // Step 1: Find the medicine
        Medicine medicine = medicineRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Medicine not found with ID: " + id
                        )
                );

        // Step 2: Get the logged-in patient's email
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String loggedInEmail = authentication.getName();

        // Step 3: Find the prescription owner
        String prescriptionOwnerEmail =
                medicine.getPrescription()
                        .getPatient()
                        .getEmail();

        // Step 4: Check ownership
        if (!loggedInEmail.equalsIgnoreCase(prescriptionOwnerEmail)) {
            throw new AccessDeniedException(
                    "You are not allowed to access this medicine"
            );
        }

        // Step 5: Convert the entity into a DTO
        MedicineDTO dto = new MedicineDTO();

        dto.setId(medicine.getId());
        dto.setPrescriptionId(
                medicine.getPrescription().getId()
        );
        dto.setName(medicine.getName());
        dto.setDosage(medicine.getDosage());
        dto.setFrequency(medicine.getFrequency());
        dto.setDuration(medicine.getDuration());
        dto.setPlainExplanation(medicine.getPlainExplanation());

        return dto;
    }

    // Get all medicines for a prescription
    public List<MedicineDTO> getMedicinesByPrescriptionId(
            Long prescriptionId) {

        // Step 1: Find the prescription
        Prescription prescription = prescriptionRepository
                .findById(prescriptionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Prescription not found with ID: "
                                        + prescriptionId
                        )
                );

        // Step 2: Get the logged-in patient's email
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String loggedInEmail = authentication.getName();

        // Step 3: Check prescription ownership
        String prescriptionOwnerEmail =
                prescription.getPatient().getEmail();

        if (!loggedInEmail.equalsIgnoreCase(prescriptionOwnerEmail)) {
            throw new AccessDeniedException(
                    "You are not allowed to access these medicines"
            );
        }

        // Step 4: Retrieve medicines for this prescription
        List<Medicine> medicines =
                medicineRepository.findByPrescription_Id(prescriptionId);

        // Step 5: Convert medicines to DTOs
        List<MedicineDTO> medicineDTOs = new ArrayList<>();

        for (Medicine medicine : medicines) {

            MedicineDTO dto = new MedicineDTO();

            dto.setId(medicine.getId());
            dto.setPrescriptionId(
                    medicine.getPrescription().getId()
            );
            dto.setName(medicine.getName());
            dto.setDosage(medicine.getDosage());
            dto.setFrequency(medicine.getFrequency());
            dto.setDuration(medicine.getDuration());
            dto.setPlainExplanation(medicine.getPlainExplanation());

            medicineDTOs.add(dto);
        }

        return medicineDTOs;
    }
}
