package com.devsuperior.dscommerce.dto;

import com.devsuperior.dscommerce.services.validation.UserInsertValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

@UserInsertValid
public class UserInsertDTO extends UserDTO {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Campo Obrigatório")
    @Size(min=8, max=20, message = "O tamanho deve ser entre 8 e 20 caracteres")
    private String password;

    public UserInsertDTO () {
    }

    public UserInsertDTO(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
