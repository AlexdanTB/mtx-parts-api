package com.mtxparts.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateProductoRequest(
    @NotBlank(message = "El SKU es requerido")
    String sku,

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100)
    String nombre,

    String descripcion,

    @NotNull(message = "El precio es requerido")
    BigDecimal precio,

    @Min(value = 0, message = "El stock no puede ser negativo")
    Integer stock,

    String imagen,

    Long categoriaId
) {}
