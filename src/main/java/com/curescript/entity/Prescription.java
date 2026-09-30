package com.curescript.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime uploadDate;
    private String rawImageUrl;
    private String doctorName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable=false)
    private Patient patient;

    @OneToMany(
            mappedBy = "prescription",
            fetch = FetchType.LAZY
    )
    private List<Medicine> medicines;

    public List<Medicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "id=" + id +
                ", uploadDate=" + uploadDate +
                ", rawImageUrl='" + rawImageUrl + '\'' +
                ", doctorName='" + doctorName + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getRawImageUrl() {
        return rawImageUrl;
    }

    public void setRawImageUrl(String rawImageUrl) {
        this.rawImageUrl = rawImageUrl;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
