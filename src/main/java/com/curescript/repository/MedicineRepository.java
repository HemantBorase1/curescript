package com.curescript.repository;

import com.curescript.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine,Long> {

}
