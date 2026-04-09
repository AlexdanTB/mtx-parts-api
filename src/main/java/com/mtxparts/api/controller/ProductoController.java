package com.mtxparts.api.controller;

import com.mtxparts.api.dto.request.CreateProductoRequest;
import com.mtxparts.api.dto.request.UpdateProductoRequest;
import com.mtxparts.api.dto.response.ApiResponse;
import com.mtxparts.api.dto.response.ProductoResponse;
import com.mtxparts.api.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductoResponse>>> listarTodos(
            @RequestParam(required = false) String buscar,
            @RequestParam(required = false) Long categoria) {
        List<ProductoResponse> productos;
        if (buscar != null && !buscar.isBlank()) {
            productos = productoService.buscar(buscar);
        } else if (categoria != null) {
            productos = productoService.listarPorCategoria(categoria);
        } else {
            productos = productoService.listarTodos();
        }
        return ResponseEntity.ok(ApiResponse.success(productos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoResponse>> obtenerPorId(@PathVariable Long id) {
        ProductoResponse producto = productoService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success(producto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductoResponse>> crear(
            @Valid @RequestBody CreateProductoRequest request) {
        ProductoResponse producto = productoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(producto, "Producto creado exitosamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductoRequest request) {
        ProductoResponse producto = productoService.actualizar(id, request);
        return ResponseEntity.ok(ApiResponse.success(producto, "Producto actualizado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
