package com.curescript.service;

import com.curescript.dto.PrescriptionDTO;
import com.curescript.entity.Patient;
import com.curescript.entity.Prescription;
import com.curescript.exception.ResourceNotFoundException;
import com.curescript.repository.PatientRepository;
import com.curescript.repository.PrescriptionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

 @Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;

    public PrescriptionService(
            PrescriptionRepository prescriptionRepository,
            PatientRepository patientRepository) {

        this.prescriptionRepository = prescriptionRepository;
        this.patientRepository = patientRepository;
    }


     public PrescriptionDTO createPrescription(
             PrescriptionDTO prescriptionDTO) {

         // Step 1: Get the logged-in user's email from JWT authentication
         Authentication authentication =
                 SecurityContextHolder.getContext().getAuthentication();

         String loggedInEmail = authentication.getName();

         // Step 2: Find the logged-in patient using their email
         Patient patient = patientRepository
                 .findByEmail(loggedInEmail)
                 .orElseThrow(() ->
                         new ResourceNotFoundException(
                                 "Logged-in patient not found"
                         )
                 );

         // Step 3: Create a new prescription
         Prescription prescription = new Prescription();

         prescription.setPatient(patient);
         prescription.setUploadDate(LocalDateTime.now());
         prescription.setRawImageUrl(
                 prescriptionDTO.getRawImageUrl()
         );
         prescription.setDoctorName(
                 prescriptionDTO.getDoctorName()
         );

         // Step 4: Save the prescription
         Prescription savedPrescription =
                 prescriptionRepository.save(prescription);

         // Step 5: Convert the saved entity into a DTO
         PrescriptionDTO responseDTO = new PrescriptionDTO();

         responseDTO.setId(savedPrescription.getId());
         responseDTO.setPatientId(
                 savedPrescription.getPatient().getId()
         );
         responseDTO.setUploadDate(
                 savedPrescription.getUploadDate().toString()
         );
         responseDTO.setRawImageUrl(
                 savedPrescription.getRawImageUrl()
         );
         responseDTO.setDoctorName(
                 savedPrescription.getDoctorName()
         );

         return responseDTO;
     }

     public PrescriptionDTO getPrescriptionById(Long id) {

         // Step 1: Fetch the prescription from the database
         Prescription prescription = prescriptionRepository
                 .findById(id)
                 .orElseThrow(() ->
                         new ResourceNotFoundException(
                                 "Prescription not found with ID: " + id
                         )
                 );

         // Step 2: Get the logged-in user's email
         Authentication authentication =
                 SecurityContextHolder.getContext().getAuthentication();

         String loggedInEmail = authentication.getName();

         // Step 3: Get the prescription owner's email
         String prescriptionOwnerEmail =
                 prescription.getPatient().getEmail();

         // Step 4: Check ownership
         if (!loggedInEmail.equalsIgnoreCase(prescriptionOwnerEmail)) {
             throw new AccessDeniedException(
                     "You are not allowed to access this prescription"
             );
         }

         // Step 5: Convert the entity into a DTO
         PrescriptionDTO prescriptionDTO = new PrescriptionDTO();

         prescriptionDTO.setId(prescription.getId());
         prescriptionDTO.setPatientId(
                 prescription.getPatient().getId()
         );
         prescriptionDTO.setUploadDate(
                 prescription.getUploadDate().toString()
         );
         prescriptionDTO.setRawImageUrl(
                 prescription.getRawImageUrl()
         );
         prescriptionDTO.setDoctorName(
                 prescription.getDoctorName()
         );

         return prescriptionDTO;
     }

     public List<PrescriptionDTO> getPrescriptionsByPatientId(
             Long patientId) {


         Authentication authentication =
                 SecurityContextHolder.getContext().getAuthentication();

         String loggedInEmail = authentication.getName();

         Patient patient = patientRepository
                 .findById(patientId)
                 .orElseThrow(() ->
                         new ResourceNotFoundException(
                                 "Patient not found with ID: " + patientId
                         )
                 );

         if (!loggedInEmail.equalsIgnoreCase(patient.getEmail())) {
             throw new AccessDeniedException(
                     "You are not allowed to access these prescriptions"
             );
         }
         // Check whether the patient exists
         patientRepository.findById(patientId)
                 .orElseThrow(() ->
                         new ResourceNotFoundException(
                                 "Patient not found with ID: "
                                         + patientId
                         )
                 );

         // Fetch only this patient's prescriptions
         List<Prescription> prescriptions =
                 prescriptionRepository.findByPatient_Id(patientId);

         List<PrescriptionDTO> prescriptionDTOs =
                 new ArrayList<>();

         for (Prescription prescription : prescriptions) {

             PrescriptionDTO dto = new PrescriptionDTO();

             dto.setId(prescription.getId());
             dto.setPatientId(patientId);
             dto.setUploadDate(
                     prescription.getUploadDate().toString()
             );
             dto.setRawImageUrl(prescription.getRawImageUrl());
             dto.setDoctorName(prescription.getDoctorName());

             prescriptionDTOs.add(dto);
         }

         return prescriptionDTOs;
     }

}
