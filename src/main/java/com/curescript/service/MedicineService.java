
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
import org.springframework.transaction.annotation.Transactional;

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
    public List<MedicineDTO> getMedicinesByPrescriptionId(Long prescriptionId) {

        Prescription prescription = prescriptionRepository
                .findById(prescriptionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Prescription not found"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !prescription.getPatient().getEmail()
                .equalsIgnoreCase(authentication.getName())) {

            throw new AccessDeniedException(
                    "You cannot access this prescription");
        }

        List<Medicine> medicines =
                medicineRepository.findByPrescription_Id(prescriptionId);

        return medicines.stream()
                .map(medicine -> {
                    MedicineDTO dto = new MedicineDTO();
                    dto.setId(medicine.getId());
                    dto.setPrescriptionId(prescriptionId);
                    dto.setName(medicine.getName());
                    dto.setDosage(medicine.getDosage());
                    dto.setFrequency(medicine.getFrequency());
                    dto.setDuration(medicine.getDuration());
                    dto.setPlainExplanation(medicine.getPlainExplanation());
                    dto.setUncertainFields(medicine.getUncertainFields());
                    dto.setVerified(medicine.isVerified());
                    return dto;
                })
                .toList();
    }

    @Transactional
    public MedicineDTO verifyMedicine(Long medicineId) {

        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medicine not found"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !medicine.getPrescription().getPatient().getEmail()
                .equalsIgnoreCase(authentication.getName())) {

            throw new AccessDeniedException(
                    "You cannot verify this medicine");
        }

        medicine.setVerified(true);

        medicineRepository.save(medicine);

        MedicineDTO dto = new MedicineDTO();
        dto.setId(medicine.getId());
        dto.setPrescriptionId(medicine.getPrescription().getId());
        dto.setName(medicine.getName());
        dto.setDosage(medicine.getDosage());
        dto.setFrequency(medicine.getFrequency());
        dto.setDuration(medicine.getDuration());
        dto.setPlainExplanation(medicine.getPlainExplanation());
        dto.setUncertainFields(medicine.getUncertainFields());
        dto.setVerified(medicine.isVerified());

        return dto;
    }

    @Transactional
    public MedicineDTO updateMedicine(Long medicineId, MedicineDTO medicineDTO) {

        // Step 1: Find medicine
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medicine not found"));

        // Step 2: Get logged-in patient
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // Step 3: Check ownership
        if (authentication == null
                || !authentication.isAuthenticated()
                || !medicine.getPrescription().getPatient().getEmail()
                .equalsIgnoreCase(authentication.getName())) {

            throw new AccessDeniedException(
                    "You cannot update this medicine");
        }

        // Step 4: Update medicine details
        if (medicineDTO.getName() != null) {
            medicine.setName(medicineDTO.getName());
        }

        if (medicineDTO.getDosage() != null) {
            medicine.setDosage(medicineDTO.getDosage());
        }

        if (medicineDTO.getFrequency() != null) {
            medicine.setFrequency(medicineDTO.getFrequency());
        }

        if (medicineDTO.getDuration() != null) {
            medicine.setDuration(medicineDTO.getDuration());
        }

        // Step 5: Mark as not reviewed after editing
        medicine.setVerified(false);

        // Step 6: Save medicine
        Medicine updatedMedicine = medicineRepository.save(medicine);

        // Step 7: Convert to DTO
        MedicineDTO dto = new MedicineDTO();

        dto.setId(updatedMedicine.getId());
        dto.setPrescriptionId(updatedMedicine.getPrescription().getId());
        dto.setName(updatedMedicine.getName());
        dto.setDosage(updatedMedicine.getDosage());
        dto.setFrequency(updatedMedicine.getFrequency());
        dto.setDuration(updatedMedicine.getDuration());
        dto.setPlainExplanation(updatedMedicine.getPlainExplanation());
        dto.setUncertainFields(updatedMedicine.getUncertainFields());
        dto.setVerified(updatedMedicine.isVerified());

        return dto;
    }
}
