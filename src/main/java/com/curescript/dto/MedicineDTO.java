package com.curescript.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class MedicineDTO {

    private Long id;

    @NotNull(message = "Prescription ID is Required")
    @Positive(message = "Prescription ID must be greater than Zero")
    private Long prescriptionId;

    @NotBlank(message = "Medicine name is Required")
    private String name;

    @NotBlank(message = "Dosage is Required")
    private String dosage;

    @NotBlank(message = "Frequency is required")
    private String frequency;

    @NotBlank(message = "Duration is required")
    private String duration;
    private String plainExplanation;

    public MedicineDTO() {

    }

    public MedicineDTO(
            Long id,
            Long prescriptionId,
            String name,
            String dosage,
            String frequency,
            String duration,
            String plainExplanation) {

        this.id = id;
        this.prescriptionId = prescriptionId;
        this.name = name;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
        this.plainExplanation = plainExplanation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPlainExplanation() {
        return plainExplanation;
    }

    public void setPlainExplanation(String plainExplanation) {
        this.plainExplanation = plainExplanation;
    }
}
