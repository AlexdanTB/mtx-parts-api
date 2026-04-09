package com.mtxparts.api.service;

import com.mtxparts.api.dto.request.CreatePedidoRequest;
import com.mtxparts.api.dto.request.ItemPedidoRequest;
import com.mtxparts.api.dto.request.UpdatePedidoRequest;
import com.mtxparts.api.dto.response.PedidoResponse;
import com.mtxparts.api.entity.DetallePedido;
import com.mtxparts.api.entity.Pedidos;
import com.mtxparts.api.entity.Producto;
import com.mtxparts.api.entity.Usuario;
import com.mtxparts.api.exception.ResourceNotFoundException;
import com.mtxparts.api.repository.DetallePedidoRepository;
import com.mtxparts.api.repository.PedidoRepository;
import com.mtxparts.api.repository.ProductoRepository;
import com.mtxparts.api.type.StatusPedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioService usuarioService;

    public List<PedidoResponse> listarPedidosPorUsuario(String correo) {
        Usuario usuario = usuarioService.obtenerPorCorreo(correo);
        return pedidoRepository.findByUsuarioId(usuario.getId()).stream()
            .map(PedidoResponse::fromEntity)
            .toList();
    }

    public List<PedidoResponse> listarTodosLosPedidos() {
        return pedidoRepository.findAll().stream()
            .map(PedidoResponse::fromEntity)
            .toList();
    }

    public PedidoResponse obtenerPorId(Long id) {
        return pedidoRepository.findById(id)
            .map(PedidoResponse::fromEntity)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
    }

    @Transactional
    public PedidoResponse crearPedido(String correo, CreatePedidoRequest request) {
        Usuario usuario = usuarioService.obtenerPorCorreo(correo);

        Pedidos pedido = Pedidos.builder()
            .usuario(usuario)
            .statusPedido(StatusPedido.PENDIENTE)
            .direccionEnvio(request.direccionEnvio())
            .telefonoContacto(request.telefonoContacto())
            .build();

        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoRequest item : request.items()) {
            Producto producto = productoRepository.findById(item.productoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + item.productoId()));

            if (producto.getStock() < item.cantidad()) {
                throw new IllegalStateException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - item.cantidad());
            productoRepository.save(producto);

            BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(item.cantidad()));
            total = total.add(subtotal);

            DetallePedido detalle = DetallePedido.builder()
                .pedido(pedido)
                .producto(producto)
                .cantidad(item.cantidad())
                .precioUnitario(producto.getPrecio())
                .subtotal(subtotal)
                .build();

            pedido.getDetalles().add(detalle);
        }

        pedido.setTotal(total);
        pedido = pedidoRepository.save(pedido);

        return PedidoResponse.fromEntity(pedido);
    }

    @Transactional
    public PedidoResponse actualizarStatus(Long id, UpdatePedidoRequest request) {
        Pedidos pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));

        pedido.setStatusPedido(request.statusPedido());
        pedido = pedidoRepository.save(pedido);

        return PedidoResponse.fromEntity(pedido);
    }
}
