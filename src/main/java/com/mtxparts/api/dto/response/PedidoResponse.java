package com.mtxparts.api.dto.response;

import com.mtxparts.api.entity.Pedidos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
    Long id,
    Long usuarioId,
    String usuarioNombre,
    String statusPedido,
    BigDecimal total,
    LocalDateTime fechaCreacion,
    String direccionEnvio,
    String telefonoContacto,
    List<DetallePedidoResponse> detalles
) {
    public static PedidoResponse fromEntity(Pedidos pedido) {
        List<DetallePedidoResponse> detalleResponses = pedido.getDetalles() != null
            ? pedido.getDetalles().stream()
                .map(DetallePedidoResponse::fromEntity)
                .toList()
            : List.of();

        return new PedidoResponse(
            pedido.getId(),
            pedido.getUsuario().getId(),
            pedido.getUsuario().getNombreCompleto(),
            pedido.getStatusPedido().name(),
            pedido.getTotal(),
            pedido.getFechaCreacion(),
            pedido.getDireccionEnvio(),
            pedido.getTelefonoContacto(),
            detalleResponses
        );
    }
}
