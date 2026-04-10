package com.mtxparts.api.controller;

import com.mtxparts.api.entity.Usuario;
import com.mtxparts.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<?> getUsuarios() {
        List<Usuario> usuarios = usuarioService.leerUsuarios();
        usuarios.forEach(u -> u.setPassword(null));
        return ResponseEntity.ok(Map.of("success", true, "data", usuarios));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.buscarUsuarioPorId(id);
        if (usuario.isPresent()) {
            usuario.get().setPassword(null);
            return ResponseEntity.ok(Map.of("success", true, "data", usuario.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("success", false, "message", "Usuario no encontrado"));
    }

    @GetMapping("/mi-perfil")
    public ResponseEntity<?> getMiPerfil(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "No autenticado"));
        }
        Optional<Usuario> usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        if (usuario.isPresent()) {
            usuario.get().setPassword(null);
            return ResponseEntity.ok(Map.of("success", true, "data", usuario.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("success", false, "message", "Usuario no encontrado"));
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.guardarUsuario(usuario);
            nuevoUsuario.setPassword(null);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("success", true, "data", nuevoUsuario, "message", "Usuario creado exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Usuario actualizado = usuarioService.actualizarUsuario(id, usuario);
            actualizado.setPassword(null);
            return ResponseEntity.ok(Map.of("success", true, "data", actualizado, "message", "Usuario actualizado"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.ok(Map.of("success", true, "message", "Usuario eliminado"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
