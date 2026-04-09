package com.mtxparts.api.dto.response;

import com.mtxparts.api.entity.Usuario;

public record UsuarioResponse(
    Long id,
    String nombreCompleto,
    String email,
    String foto,
    String rol
) {
    public static UsuarioResponse fromEntity(Usuario usuario) {
        return new UsuarioResponse(
            usuario.getId(),
            usuario.getNombreCompleto(),
            usuario.getEmail(),
            usuario.getFoto(),
            usuario.getRol().name()
        );
    }
}
