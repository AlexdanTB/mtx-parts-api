package com.itsqmet.api_mtx.repository;

import com.itsqmet.api_mtx.entity.DetallePedido;
import com.itsqmet.api_mtx.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    List<DetallePedido> findByPedido(Pedido pedido);
}
