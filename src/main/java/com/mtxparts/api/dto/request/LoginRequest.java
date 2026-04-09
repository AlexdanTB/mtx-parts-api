package com.mtxparts.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank(message = "El correo es requerido")
    @Email(message = "Formato de correo inválido")
    String email,

    @NotBlank(message = "La contraseña es requerida")
    String password
) {}
