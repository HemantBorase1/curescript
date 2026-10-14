package com.curescript.repository;

import com.curescript.entity.RemedySuggestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RemedySuggestionRepository extends JpaRepository<RemedySuggestion,Long> {

    boolean existsByMedicine_Id(Long medicineId);
    Optional<RemedySuggestion> findByMedicine_Id(Long medicineId);
}
