package com.mtxparts.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUsuarioRequest(
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 10, max = 45, message = "El nombre debe tener entre 10 y 45 caracteres")
    String nombreCompleto,

    String foto
) {}
