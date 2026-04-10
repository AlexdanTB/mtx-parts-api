package com.mtxparts.api.service;

import com.mtxparts.api.entity.DetallePedido;
import com.mtxparts.api.entity.Pedido;
import com.mtxparts.api.entity.Producto;
import com.mtxparts.api.entity.Usuario;
import com.mtxparts.api.repository.DetallePedidoRepository;
import com.mtxparts.api.repository.PedidoRepository;
import com.mtxparts.api.type.StatusPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private ProductoService productoService;

    public List<Pedido> leerPedidos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPedidoPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> obtenerPedidosPorUsuario(Usuario usuario) {
        return pedidoRepository.findByUsuarioOrderByFechaCreacionDesc(usuario);
    }

    public List<Pedido> obtenerPedidosPorStatus(StatusPedido status) {
        return pedidoRepository.findByStatusPedido(status);
    }

    @Transactional
    public Pedido crearPedido(Usuario usuario, List<DetallePedido> detalles, String direccionEnvio) {
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setStatusPedido(StatusPedido.PENDIENTE);
        pedido.setFechaCreacion(LocalDate.now());
        pedido.setDireccionEnvio(direccionEnvio);

        BigDecimal total = BigDecimal.ZERO;

        for (DetallePedido detalle : detalles) {
            Producto producto = productoService.buscarProductoPorId(detalle.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalle.getProducto().getId()));

            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            detalle.setPedido(pedido);
            detalle.setPrecio_unitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(detalle.getCantidad())));
            total = total.add(detalle.getSubtotal());

            productoService.actualizarStock(producto.getId(), detalle.getCantidad());
        }

        pedido.setTotal(total);
        pedido.setDetalles(detalles);

        return pedidoRepository.save(pedido);
    }

    public Pedido actualizarPedido(Long id, Pedido pedido) {
        Pedido pedidoEncontrado = buscarPedidoPorId(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedidoEncontrado.setStatusPedido(pedido.getStatusPedido());
        pedidoEncontrado.setDireccionEnvio(pedido.getDireccionEnvio());

        return pedidoRepository.save(pedidoEncontrado);
    }

    public Pedido actualizarStatus(Long id, StatusPedido status) {
        Pedido pedido = buscarPedidoPorId(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedido.setStatusPedido(status);
        return pedidoRepository.save(pedido);
    }

    public void eliminarPedido(Long id) {
        Pedido pedido = buscarPedidoPorId(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedidoRepository.delete(pedido);
    }
}
