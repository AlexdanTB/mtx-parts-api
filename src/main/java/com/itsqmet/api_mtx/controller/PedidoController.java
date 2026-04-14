package com.itsqmet.api_mtx.controller;

import com.itsqmet.api_mtx.entity.DetallePedido;
import com.itsqmet.api_mtx.entity.Pedido;
import com.itsqmet.api_mtx.entity.Producto;
import com.itsqmet.api_mtx.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.itsqmet.api_mtx.service.PedidoService;
import com.itsqmet.api_mtx.service.UsuarioService;
import com.itsqmet.api_mtx.type.StatusPedido;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPedidoPorId(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "No autenticado"));
        }

        Optional<Pedido> pedidoOpt = pedidoService.buscarPedidoPorId(id);
        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "Pedido no encontrado"));
        }

        Pedido pedido = pedidoOpt.get();
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(userDetails.getUsername());

        boolean esAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean esDueno = usuarioOpt.isPresent() && pedido.getUsuario().getId().equals(usuarioOpt.get().getId());

        if (!esAdmin && !esDueno) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("success", false, "message", "No tienes permisos para ver este pedido"));
        }

        return ResponseEntity.ok(Map.of("success", true, "data", pedido));
    }

    @PostMapping
    public ResponseEntity<?> crearPedido(
            @RequestBody Map<String, Object> datos,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {

            Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(userDetails.getUsername());
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "message", "Usuario no encontrado"));
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> detallesData = (List<Map<String, Object>>) datos.get("detalles");
            if (detallesData == null || detallesData.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Debe incluir al menos un producto"));
            }

            List<DetallePedido> detalles = detallesData.stream().map(d -> {
                DetallePedido detalle = new DetallePedido();
                Producto producto = new Producto();
                producto.setId(Long.valueOf(d.get("productoId").toString()));
                detalle.setProducto(producto);
                detalle.setCantidad(Integer.parseInt(d.get("cantidad").toString()));
                return detalle;
            }).collect(Collectors.toList());

            String direccionEnvio = (String) datos.get("direccionEnvio");

            Pedido pedido = pedidoService.crearPedido(usuarioOpt.get(), detalles, direccionEnvio);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("success", true, "data", pedido, "message", "Pedido creado exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> actualizarStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> datos,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "message", "No autenticado"));
            }

            boolean esAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (!esAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("success", false, "message", "Solo administradores pueden cambiar el estado"));
            }

            String statusStr = datos.get("status");
            StatusPedido status = StatusPedido.valueOf(statusStr);

            Pedido pedido = pedidoService.actualizarStatus(id, status);
            return ResponseEntity.ok(Map.of("success", true, "data", pedido, "message", "Estado actualizado"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getPedidosPorStatus(@PathVariable String status) {
        try {
            StatusPedido statusPedido = StatusPedido.valueOf(status);
            List<Pedido> pedidos = pedidoService.obtenerPedidosPorStatus(statusPedido);
            return ResponseEntity.ok(Map.of("success", true, "data", pedidos));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Estado inválido"));
        }
    }
}
