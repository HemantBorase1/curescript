
package com.curescript.dto;

import java.util.List;

public class PrescriptionAiResponseDTO {

    private String doctorName;
    private List<ExtractedMedicineDTO> medicines;
    private boolean needsProfessionalVerification;

    public PrescriptionAiResponseDTO() {
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public List<ExtractedMedicineDTO> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<ExtractedMedicineDTO> medicines) {
        this.medicines = medicines;
    }

    public boolean isNeedsProfessionalVerification() {
        return needsProfessionalVerification;
    }

    public void setNeedsProfessionalVerification(
            boolean needsProfessionalVerification) {
        this.needsProfessionalVerification =
                needsProfessionalVerification;
    }
}
