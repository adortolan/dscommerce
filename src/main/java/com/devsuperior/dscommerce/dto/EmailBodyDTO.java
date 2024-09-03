package com.devsuperior.dscommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailBodyDTO {
    @NotBlank(message = "O email deve ser informado")
    @Email(message = "O email deve ser válido")
    private String email;

    public EmailBodyDTO(String email) {
        this.email = email;
    }

    public EmailBodyDTO() { }

    public String getEmail() {
        return email;
    }
}
