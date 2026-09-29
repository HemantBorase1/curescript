package com.curescript.dto;

public class MedicineDTO {

    private Long id;
    private Long prescriptionId;
    private String name;
    private String dosage;
    private String frequency;
    private String duration;
    private String plainExplaination;

    public MedicineDTO(){

    }

    public MedicineDTO(Long id, Long prescriptionId, String name, String dosage, String frequency, String duration, String plainExplaination) {
        this.id = id;
        this.prescriptionId = prescriptionId;
        this.name = name;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
        this.plainExplaination = plainExplaination;
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

    public String getPlainExplaination() {
        return plainExplaination;
    }

    public void setPlainExplaination(String plainExplaination) {
        this.plainExplaination = plainExplaination;
    }
}
