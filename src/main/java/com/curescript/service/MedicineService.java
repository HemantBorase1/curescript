package com.curescript.service;

import com.curescript.dto.MedicineDTO;
import com.curescript.entity.Medicine;
import com.curescript.entity.Prescription;
import com.curescript.repository.MedicineRepository;
import com.curescript.repository.PrescriptionRepository;

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

    public MedicineDTO createMedicine(MedicineDTO medicineDTO) {

        Prescription prescription = prescriptionRepository
                .findById(medicineDTO.getPrescriptionId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Prescription not found with ID: "
                                        + medicineDTO.getPrescriptionId()
                        )
                );

        Medicine medicine = new Medicine();

        medicine.setPrescription(prescription);
        medicine.setName(medicineDTO.getName());
        medicine.setDosage(medicineDTO.getDosage());
        medicine.setFrequency(medicineDTO.getFrequency());
        medicine.setDuration(medicineDTO.getDuration());
        medicine.setPlainExplanation(
                medicineDTO.getPlainExplanation()
        );

        Medicine savedMedicine = medicineRepository.save(medicine);

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

        Medicine medicine = medicineRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Medicine not found with ID: " + id
                        )
                );

        MedicineDTO medicineDTO = new MedicineDTO();

        medicineDTO.setId(medicine.getId());
        medicineDTO.setPrescriptionId(
                medicine.getPrescription().getId()
        );
        medicineDTO.setName(medicine.getName());
        medicineDTO.setDosage(medicine.getDosage());
        medicineDTO.setFrequency(medicine.getFrequency());
        medicineDTO.setDuration(medicine.getDuration());
        medicineDTO.setPlainExplanation(
                medicine.getPlainExplanation()
        );

        return medicineDTO;
    }
    // Get all medicines for a prescription
    public List<MedicineDTO> getMedicinesByPrescriptionId(
            Long prescriptionId) {

        prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Prescription not found with ID: "
                                        + prescriptionId
                        )
                );

        List<Medicine> medicines =
                medicineRepository.findByPrescription_Id(prescriptionId);

        List<MedicineDTO> medicineDTOs = new ArrayList<>();

        for (Medicine medicine : medicines) {

            MedicineDTO dto = new MedicineDTO();

            dto.setId(medicine.getId());
            dto.setPrescriptionId(prescriptionId);
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
