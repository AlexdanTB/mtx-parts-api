package com.mtxparts.api.dto.response;

import com.mtxparts.api.entity.DetallePedido;

import java.math.BigDecimal;

public record DetallePedidoResponse(
    Long id,
    Long productoId,
    String productoNombre,
    Integer cantidad,
    BigDecimal precioUnitario,
    BigDecimal subtotal
) {
    public static DetallePedidoResponse fromEntity(DetallePedido detalle) {
        return new DetallePedidoResponse(
            detalle.getId(),
            detalle.getProducto().getId(),
            detalle.getProducto().getNombre(),
            detalle.getCantidad(),
            detalle.getPrecioUnitario(),
            detalle.getSubtotal()
        );
    }
}
