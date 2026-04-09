package com.mtxparts.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 10, max = 45, message = "El nombre debe tener entre 10 y 45 caracteres")
    String nombreCompleto,

    @NotBlank(message = "El correo es requerido")
    @Email(message = "Formato de correo inválido")
    String email,

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    String password
) {}
