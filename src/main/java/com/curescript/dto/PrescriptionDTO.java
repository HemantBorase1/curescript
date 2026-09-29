package com.curescript.dto;

public class PrescriptionDTO {

    private Long id;
    private Long patientId;
    private String uploadDate;
    private String rawImageUrl;
    private String doctorName;

    public PrescriptionDTO(){

    }

    public PrescriptionDTO(Long id, Long patientId, String uploadDate, String rawImageUrl, String doctorName) {
        this.id = id;
        this.patientId = patientId;
        this.uploadDate = uploadDate;
        this.rawImageUrl = rawImageUrl;
        this.doctorName = doctorName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
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
