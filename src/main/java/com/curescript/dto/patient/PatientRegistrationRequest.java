package com.curescript.dto.patient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PatientRegistrationRequest {
    @NotBlank(message = "Message is Required")
    private String name;

    @NotBlank(message = "Email is Required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is Required")
    @Size(min=8,message = "Password Must Contain at least 8 Characters")
    private String password;

    public PatientRegistrationRequest(){

    }

    public PatientRegistrationRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
