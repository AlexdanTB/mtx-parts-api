package com.mtxparts.api.service;

import com.mtxparts.api.dto.request.CreateProductoRequest;
import com.mtxparts.api.dto.request.UpdateProductoRequest;
import com.mtxparts.api.dto.response.ProductoResponse;
import com.mtxparts.api.entity.Producto;
import com.mtxparts.api.exception.ConflictException;
import com.mtxparts.api.exception.ResourceNotFoundException;
import com.mtxparts.api.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public List<ProductoResponse> listarTodos() {
        return productoRepository.findAll().stream()
            .map(ProductoResponse::fromEntity)
            .toList();
    }

    public ProductoResponse obtenerPorId(Long id) {
        return productoRepository.findById(id)
            .map(ProductoResponse::fromEntity)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    public List<ProductoResponse> buscar(String busqueda) {
        return productoRepository.buscarPorNombreODescripcion(busqueda).stream()
            .map(ProductoResponse::fromEntity)
            .toList();
    }

    public List<ProductoResponse> listarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId).stream()
            .map(ProductoResponse::fromEntity)
            .toList();
    }

    @Transactional
    public ProductoResponse crear(CreateProductoRequest request) {
        if (productoRepository.existsBySku(request.sku())) {
            throw new ConflictException("El SKU ya existe");
        }

        Producto producto = Producto.builder()
            .sku(request.sku())
            .nombre(request.nombre())
            .descripcion(request.descripcion())
            .precio(request.precio())
            .stock(request.stock() != null ? request.stock() : 0)
            .imagen(request.imagen())
            .categoriaId(request.categoriaId())
            .build();

        producto = productoRepository.save(producto);
        return ProductoResponse.fromEntity(producto);
    }

    @Transactional
    public ProductoResponse actualizar(Long id, UpdateProductoRequest request) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        if (request.nombre() != null) {
            producto.setNombre(request.nombre());
        }
        if (request.descripcion() != null) {
            producto.setDescripcion(request.descripcion());
        }
        if (request.precio() != null) {
            producto.setPrecio(request.precio());
        }
        if (request.stock() != null) {
            producto.setStock(request.stock());
        }
        if (request.imagen() != null) {
            producto.setImagen(request.imagen());
        }
        if (request.categoriaId() != null) {
            producto.setCategoriaId(request.categoriaId());
        }

        producto = productoRepository.save(producto);
        return ProductoResponse.fromEntity(producto);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }
}
