package com.mtxparts.api.dto.response;

import com.mtxparts.api.entity.Producto;

import java.math.BigDecimal;

public record ProductoResponse(
    Long id,
    String sku,
    String nombre,
    String descripcion,
    BigDecimal precio,
    Integer stock,
    String imagen,
    Long categoriaId
) {
    public static ProductoResponse fromEntity(Producto producto) {
        return new ProductoResponse(
            producto.getId(),
            producto.getSku(),
            producto.getNombre(),
            producto.getDescripcion(),
            producto.getPrecio(),
            producto.getStock(),
            producto.getImagen(),
            producto.getCategoriaId()
        );
    }
}
