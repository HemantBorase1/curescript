package com.curescript.repository;

import com.curescript.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineRepository
        extends JpaRepository<Medicine, Long> {

    List<Medicine> findByPrescription_Id(Long prescriptionId);
}