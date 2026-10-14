 package com.curescript.service;

import com.curescript.dto.PrescriptionDTO;
import com.curescript.entity.Patient;
import com.curescript.entity.Prescription;
import com.curescript.exception.ResourceNotFoundException;
import com.curescript.repository.PatientRepository;
import com.curescript.repository.PrescriptionRepository;

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

        Patient patient = patientRepository
                .findById(prescriptionDTO.getPatientId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient not found with ID: "
                                        + prescriptionDTO.getPatientId()
                        )
                );

        Prescription prescription = new Prescription();

        prescription.setPatient(patient);
        prescription.setUploadDate(LocalDateTime.now());
        prescription.setRawImageUrl(
                prescriptionDTO.getRawImageUrl()
        );
        prescription.setDoctorName(
                prescriptionDTO.getDoctorName()
        );

        Prescription savedPrescription =
                prescriptionRepository.save(prescription);

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

        Prescription prescription = prescriptionRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Prescription not found with ID: " + id
                        )
                );

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
