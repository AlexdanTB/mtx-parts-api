package com.mtxparts.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mtxparts.api.role.Rol;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@Table(name="usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max=20,min=3)
    private String name;

    @Column(unique=true)
    private String email;

    private String phone;

    private String password;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String imagen_url;
}
