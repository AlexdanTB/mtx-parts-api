package com.mtxparts.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mtxparts.api.entity.Usuario;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.itsqmet.api_mtx.type.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido statusPedido;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(nullable = false)
    private LocalDate fechaCreacion;

    @Column(length = 500)
    private String direccionEnvio;

    @JsonIgnore
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<com.itsqmet.api_mtx.entity.DetallePedido> detalles = new ArrayList<>();
}
