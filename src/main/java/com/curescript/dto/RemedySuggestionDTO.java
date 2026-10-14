package com.curescript.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class RemedySuggestionDTO {

    private Long id;

    @NotNull(message = "Medicine ID is required")
    @Positive(message = "Medicine ID must be greater than Zero")
    private Long medicineId;

    @NotBlank(message = "Remedy text is required")
    private String remedyText;

    @NotBlank(message = "Source is Required")
    private String source;

    public RemedySuggestionDTO(){

    }

    public RemedySuggestionDTO(Long id, Long medicineId, String remedyText, String source) {
        this.id = id;
        this.medicineId = medicineId;
        this.remedyText = remedyText;
        this.source = source;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
    }

    public String getRemedyText() {
        return remedyText;
    }

    public void setRemedyText(String remedyText) {
        this.remedyText = remedyText;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
