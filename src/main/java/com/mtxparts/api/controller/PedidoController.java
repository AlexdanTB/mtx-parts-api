package com.mtxparts.api.controller;

import com.mtxparts.api.dto.request.CreatePedidoRequest;
import com.mtxparts.api.dto.request.UpdatePedidoRequest;
import com.mtxparts.api.dto.response.ApiResponse;
import com.mtxparts.api.dto.response.PedidoResponse;
import com.mtxparts.api.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PedidoResponse>>> listarMisPedidos(
            @AuthenticationPrincipal UserDetails user) {
        List<PedidoResponse> pedidos = pedidoService.listarPedidosPorUsuario(user.getUsername());
        return ResponseEntity.ok(ApiResponse.success(pedidos));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<PedidoResponse>>> listarTodosLosPedidos() {
        List<PedidoResponse> pedidos = pedidoService.listarTodosLosPedidos();
        return ResponseEntity.ok(ApiResponse.success(pedidos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PedidoResponse>> obtenerPorId(@PathVariable Long id) {
        PedidoResponse pedido = pedidoService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success(pedido));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PedidoResponse>> crear(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody CreatePedidoRequest request) {
        PedidoResponse pedido = pedidoService.crearPedido(user.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(pedido, "Pedido creado exitosamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PedidoResponse>> actualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePedidoRequest request) {
        PedidoResponse pedido = pedidoService.actualizarStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success(pedido, "Estado del pedido actualizado"));
    }
}
