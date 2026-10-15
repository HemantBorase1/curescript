package com.curescript.dto;

public class LoginResponseDTO {

    private Long patientId;
    private String name;
    private String email;
    private String token;
    private String tokenType;

    public LoginResponseDTO(){

    }

    public LoginResponseDTO(Long patientId, String name, String email, String token, String tokenType) {
        this.patientId = patientId;
        this.name = name;
        this.email = email;
        this.token = token;
        this.tokenType = tokenType;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
