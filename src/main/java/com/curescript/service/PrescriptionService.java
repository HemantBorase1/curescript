
package com.curescript.service;

import com.curescript.dto.PrescriptionDTO;
import com.curescript.entity.Patient;
import com.curescript.entity.Prescription;
import com.curescript.exception.ResourceNotFoundException;
import com.curescript.repository.PatientRepository;
import com.curescript.repository.PrescriptionRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final CloudinaryService cloudinaryService;

    public PrescriptionService(
            PrescriptionRepository prescriptionRepository,
            PatientRepository patientRepository,
            CloudinaryService cloudinaryService) {

        this.prescriptionRepository = prescriptionRepository;
        this.patientRepository = patientRepository;
        this.cloudinaryService = cloudinaryService;
    }

    // Create a prescription using an existing image URL
    public PrescriptionDTO createPrescription(
            PrescriptionDTO prescriptionDTO) {

        Patient patient = getLoggedInPatient();

        Prescription prescription = new Prescription();
        prescription.setPatient(patient);
        prescription.setUploadDate(LocalDateTime.now());
        prescription.setRawImageUrl(prescriptionDTO.getRawImageUrl());
        prescription.setDoctorName(prescriptionDTO.getDoctorName());

        Prescription savedPrescription =
                prescriptionRepository.save(prescription);

        return convertToDTO(savedPrescription);
    }

    // Upload image to Cloudinary and save its URL in MySQL
    @Transactional
    public PrescriptionDTO uploadPrescriptionImage(
            MultipartFile file,
            String doctorName) throws IOException {

        Patient patient = getLoggedInPatient();

        // Upload the image first
        String imageUrl =
                cloudinaryService.uploadPrescriptionImage(file);

        // Save the returned URL in the prescription record
        Prescription prescription = new Prescription();
        prescription.setPatient(patient);
        prescription.setUploadDate(LocalDateTime.now());
        prescription.setRawImageUrl(imageUrl);
        prescription.setDoctorName(doctorName);

        Prescription savedPrescription =
                prescriptionRepository.save(prescription);

        return convertToDTO(savedPrescription);
    }

    // Get the authenticated patient from the JWT identity
    private Patient getLoggedInPatient() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null) {
            throw new AccessDeniedException(
                    "Authentication is required"
            );
        }

        String loggedInEmail = authentication.getName();

        return patientRepository.findByEmail(loggedInEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Logged-in patient not found"
                        )
                );
    }

    // Convert prescription entity to DTO
    private PrescriptionDTO convertToDTO(Prescription prescription) {

        PrescriptionDTO dto = new PrescriptionDTO();

        dto.setId(prescription.getId());
        dto.setPatientId(prescription.getPatient().getId());
        dto.setUploadDate(prescription.getUploadDate().toString());
        dto.setRawImageUrl(prescription.getRawImageUrl());
        dto.setDoctorName(prescription.getDoctorName());

        return dto;
    }

    // Get prescription by ID
    public PrescriptionDTO getPrescriptionById(Long id) {

        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Prescription not found with ID: " + id
                        )
                );

        String loggedInEmail = getLoggedInPatient().getEmail();
        String ownerEmail = prescription.getPatient().getEmail();

        if (!loggedInEmail.equalsIgnoreCase(ownerEmail)) {
            throw new AccessDeniedException(
                    "You are not allowed to access this prescription"
            );
        }

        return convertToDTO(prescription);
    }

    // Get prescriptions belonging to the logged-in patient
    public List<PrescriptionDTO> getPrescriptionsByPatientId(
            Long patientId) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with ID: " + patientId
                        )
                );

        String loggedInEmail = getLoggedInPatient().getEmail();

        if (!loggedInEmail.equalsIgnoreCase(patient.getEmail())) {
            throw new AccessDeniedException(
                    "You are not allowed to access these prescriptions"
            );
        }

        List<Prescription> prescriptions =
                prescriptionRepository.findByPatient_Id(patientId);

        List<PrescriptionDTO> dtos = new ArrayList<>();

        for (Prescription prescription : prescriptions) {
            dtos.add(convertToDTO(prescription));
        }

        return dtos;
    }
}
