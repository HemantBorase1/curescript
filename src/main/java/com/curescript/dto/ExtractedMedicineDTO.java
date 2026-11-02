
package com.curescript.dto;

import java.util.List;

public class ExtractedMedicineDTO {

    private String name;
    private String dosage;
    private String frequency;
    private String duration;
    private String instructions;
    private List<String> uncertainFields;

    public ExtractedMedicineDTO() {
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

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public List<String> getUncertainFields() {
        return uncertainFields;
    }

    public void setUncertainFields(List<String> uncertainFields) {
        this.uncertainFields = uncertainFields;
    }
}
