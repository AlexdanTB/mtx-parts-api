package com.itsqmet.api_mtx.service;

import com.itsqmet.api_mtx.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itsqmet.api_mtx.repository.ProductoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> leerProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Optional<Producto> buscarPorSku(String sku) {
        return productoRepository.findBySku(sku);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Producto> buscarProductosDisponibles() {
        return productoRepository.findByStockGreaterThan(0);
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, Producto producto) {
        Producto productoEncontrado = buscarProductoPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        productoEncontrado.setSku(producto.getSku());
        productoEncontrado.setNombre(producto.getNombre());
        productoEncontrado.setDescripcion(producto.getDescripcion());
        productoEncontrado.setPrecio(producto.getPrecio());
        productoEncontrado.setStock(producto.getStock());

        return productoRepository.save(productoEncontrado);
    }

    public void eliminarProducto(Long id) {
        Producto productoEncontrado = buscarProductoPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        productoRepository.delete(productoEncontrado);
    }

    public boolean existeSku(String sku) {
        return productoRepository.findBySku(sku).isPresent();
    }

    public void actualizarStock(Long id, int cantidad) {
        Producto producto = buscarProductoPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
    }

}
