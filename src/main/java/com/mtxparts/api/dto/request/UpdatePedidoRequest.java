package com.mtxparts.api.dto.request;

import com.mtxparts.api.type.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record UpdatePedidoRequest(
    @NotNull(message = "El status es requerido")
    StatusPedido statusPedido
) {}
