package com.mtxparts.api.repository;

import com.mtxparts.api.entity.Pedidos;
import com.mtxparts.api.type.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedidos, Long> {
    List<Pedidos> findByUsuarioId(Long usuarioId);
    List<Pedidos> findByStatusPedido(StatusPedido statusPedido);
}
