package com.mtxparts.api.controller;

import com.mtxparts.api.entity.Producto;
import com.mtxparts.api.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<?> getProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Boolean disponibles) {
        
        List<Producto> productos;
        
        if (nombre != null && !nombre.isEmpty()) {
            productos = productoService.buscarPorNombre(nombre);
        } else if (Boolean.TRUE.equals(disponibles)) {
            productos = productoService.buscarProductosDisponibles();
        } else {
            productos = productoService.leerProductos();
        }
        
        return ResponseEntity.ok(Map.of("success", true, "data", productos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductoPorId(@PathVariable Long id) {
        Optional<Producto> producto = productoService.buscarProductoPorId(id);
        if (producto.isPresent()) {
            return ResponseEntity.ok(Map.of("success", true, "data", producto.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("success", false, "message", "Producto no encontrado"));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<?> getProductoPorSku(@PathVariable String sku) {
        Optional<Producto> producto = productoService.buscarPorSku(sku);
        if (producto.isPresent()) {
            return ResponseEntity.ok(Map.of("success", true, "data", producto.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("success", false, "message", "Producto no encontrado"));
    }

    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        try {
            if (productoService.existeSku(producto.getSku())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("success", false, "message", "El SKU ya existe"));
            }
            Producto nuevoProducto = productoService.guardarProducto(producto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("success", true, "data", nuevoProducto, "message", "Producto creado exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            Producto actualizado = productoService.actualizarProducto(id, producto);
            return ResponseEntity.ok(Map.of("success", true, "data", actualizado, "message", "Producto actualizado"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.ok(Map.of("success", true, "message", "Producto eliminado"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
