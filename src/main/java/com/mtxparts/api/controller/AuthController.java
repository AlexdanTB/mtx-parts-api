package com.mtxparts.api.controller;

import com.mtxparts.api.entity.Usuario;
import com.mtxparts.api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Map<String, String> datos) {
        try {
            String nombreCompleto = datos.get("nombreCompleto");
            String email = datos.get("email");
            String password = datos.get("password");
            String telefono = datos.get("telefono");
            String direccion = datos.get("direccion");

            if (nombreCompleto == null || email == null || password == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Faltan campos requeridos"));
            }

            Usuario usuario = authService.registrar(nombreCompleto, email, password, telefono, direccion);
            usuario.setPassword(null);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("success", true, "data", usuario, "message", "Usuario registrado exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        try {
            String email = credenciales.get("email");
            String password = credenciales.get("password");

            if (email == null || password == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Email y password son requeridos"));
            }

            Usuario usuario = authService.login(email, password);
            usuario.setPassword(null);

            return ResponseEntity.ok()
                    .body(Map.of("success", true, "data", usuario, "message", "Login exitoso"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Credenciales inválidas"));
        }
    }
}
