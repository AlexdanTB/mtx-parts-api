package com.mtxparts.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateProductoRequest(
    @Size(max = 100)
    String nombre,

    String descripcion,

    BigDecimal precio,

    @Min(value = 0, message = "El stock no puede ser negativo")
    Integer stock,

    String imagen,

    Long categoriaId
) {}
