
package com.curescript.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class MedicineDTO {

    private Long id;

    @NotNull(message = "Prescription ID is required")
    @Positive(message = "Prescription ID must be greater than zero")
    private Long prescriptionId;

    private String name;
    private String dosage;
    private String frequency;
    private String duration;
    private String plainExplanation;
    private String uncertainFields;
    private boolean verified;

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
    public String getUncertainFields() {
        return uncertainFields;
    }

    public void setUncertainFields(String uncertainFields) {
        this.uncertainFields = uncertainFields;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
