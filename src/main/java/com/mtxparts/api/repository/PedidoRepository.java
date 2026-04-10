package com.mtxparts.api.repository;

import com.mtxparts.api.entity.Pedido;
import com.mtxparts.api.entity.Usuario;
import com.mtxparts.api.type.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuario(Usuario usuario);
    List<Pedido> findByStatusPedido(StatusPedido statusPedido);
    List<Pedido> findByUsuarioOrderByFechaCreacionDesc(Usuario usuario);
}
