package com.itsqmet.api_mtx.repository;

import com.itsqmet.api_mtx.entity.Pedido;
import com.itsqmet.api_mtx.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.itsqmet.api_mtx.type.StatusPedido;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuario(Usuario usuario);
    List<Pedido> findByStatusPedido(StatusPedido statusPedido);
    List<Pedido> findByUsuarioOrderByFechaCreacionDesc(Usuario usuario);
}
