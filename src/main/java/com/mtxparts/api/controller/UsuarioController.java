package com.mtxparts.api.controller;

import com.mtxparts.api.dto.request.UpdateUsuarioRequest;
import com.mtxparts.api.dto.response.ApiResponse;
import com.mtxparts.api.dto.response.UsuarioResponse;
import com.mtxparts.api.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/mi-perfil")
    public ResponseEntity<ApiResponse<UsuarioResponse>> obtenerMiPerfil(
            @AuthenticationPrincipal UserDetails user) {
        var usuario = usuarioService.obtenerPorCorreo(user.getUsername());
        UsuarioResponse response = UsuarioResponse.fromEntity(usuario);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/mi-perfil")
    public ResponseEntity<ApiResponse<UsuarioResponse>> actualizarMiPerfil(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody UpdateUsuarioRequest request) {
        UsuarioResponse response = usuarioService.actualizarPerfil(user.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success(response, "Perfil actualizado"));
    }
}
