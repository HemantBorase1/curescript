package com.curescript.entity;

import jakarta.persistence.*;

@Entity
@Table(name="medicines")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String dosage;
    private String frequency;
    private String duration;
    @Column(columnDefinition = "TEXT")
    private String plainExplanation;

    @OneToOne(
            mappedBy = "medicine",
            fetch = FetchType.LAZY
    )
    private RemedySuggestion remedySuggestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id",nullable = false)
    private Prescription prescription;

    public RemedySuggestion getRemedySuggestion() {
        return remedySuggestion;
    }

    public void setRemedySuggestion(RemedySuggestion remedySuggestion) {
        this.remedySuggestion = remedySuggestion;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dosage='" + dosage + '\'' +
                ", frequency='" + frequency + '\'' +
                ", duration='" + duration + '\'' +
                ", plainExplanation='" + plainExplanation + '\'' +
                '}';
    }

    public Medicine(){
    }

    public Medicine(Long id, String name, String dosage, String frequency, String plainExplanation, String duration) {
        this.id = id;
        this.name = name;
        this.dosage = dosage;
        this.frequency = frequency;
        this.plainExplanation = plainExplanation;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
