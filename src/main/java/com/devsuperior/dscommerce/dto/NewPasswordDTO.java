package com.devsuperior.dscommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NewPasswordDTO {
    @NotBlank
    private String token;

    @NotBlank
    @Size(min = 8, message = "O tamanho deve ter no minimo 8 caracteres")
    private String password;


    public NewPasswordDTO() {
    }

    public NewPasswordDTO(String token, String password) {
        this.token = token;
        this.password = password;
    }

    public @NotBlank String getToken() {
        return token;
    }

    public void setToken(@NotBlank String token) {
        this.token = token;
    }

    public @NotBlank @Size(min = 8, message = "O tamanho deve ter no minimo 8 caracteres") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 8, message = "O tamanho deve ter no minimo 8 caracteres") String password) {
        this.password = password;
    }
}
