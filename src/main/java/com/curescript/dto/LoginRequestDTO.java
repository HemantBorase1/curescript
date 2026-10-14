package com.curescript.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {

    @NotBlank(message = "Email is Required")
    @Email(message = "Enter a Valid email address")
    private String email;

    @NotBlank(message = "Password is Required")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
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