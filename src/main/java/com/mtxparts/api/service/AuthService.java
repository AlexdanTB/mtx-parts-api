package com.mtxparts.api.service;

import com.mtxparts.api.entity.Usuario;
import com.mtxparts.api.repository.UsuarioRepository;
import com.mtxparts.api.role.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario registrar(String nombreCompleto, String email, String password, String telefono, String direccion) {
        if (usuarioService.existeEmail(email)) {
            throw new RuntimeException("El correo ya está registrado");
        }

        if (password.length() < 6) {
            throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");
        }

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(nombreCompleto);
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setTelefono(telefono);
        usuario.setDireccion(direccion);
        usuario.setRol(Rol.ROLE_CLIENTE);

        return usuarioService.guardarUsuario(usuario);
    }

    public Usuario login(String email, String password) {
        return usuarioService.validarLogin(email, password);
    }
}
