package com.mtxparts.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreatePedidoRequest(
    @NotEmpty(message = "Debe incluir al menos un producto")
    @Valid
    List<ItemPedidoRequest> items,

    @NotNull(message = "La dirección de envío es requerida")
    String direccionEnvio,

    String telefonoContacto
) {}
