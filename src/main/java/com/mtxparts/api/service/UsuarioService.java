package com.mtxparts.api.service;

import com.mtxparts.api.dto.request.LoginRequest;
import com.mtxparts.api.dto.request.RegisterRequest;
import com.mtxparts.api.dto.request.UpdateUsuarioRequest;
import com.mtxparts.api.dto.response.AuthResponse;
import com.mtxparts.api.dto.response.UsuarioResponse;
import com.mtxparts.api.entity.Usuario;
import com.mtxparts.api.exception.ConflictException;
import com.mtxparts.api.exception.ResourceNotFoundException;
import com.mtxparts.api.repository.UsuarioRepository;
import com.mtxparts.api.role.Rol;
import com.mtxparts.api.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.email())
            .orElseThrow(() -> new ResourceNotFoundException("Credenciales inválidas"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        String token = jwtTokenProvider.generateToken(userDetails, usuario.getId(), usuario.getRol().name());

        return AuthResponse.of(token, UsuarioResponse.fromEntity(usuario));
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new ConflictException("El correo ya está registrado");
        }

        Usuario usuario = Usuario.builder()
            .nombreCompleto(request.nombreCompleto())
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .rol(Rol.ROLE_CLIENTE)
            .build();

        usuario = usuarioRepository.save(usuario);

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        String token = jwtTokenProvider.generateToken(userDetails, usuario.getId(), usuario.getRol().name());

        return AuthResponse.of(token, UsuarioResponse.fromEntity(usuario));
    }

    public Usuario obtenerPorCorreo(String correo) {
        return usuarioRepository.findByEmail(correo)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    @Transactional
    public UsuarioResponse actualizarPerfil(String correo, UpdateUsuarioRequest request) {
        Usuario usuario = obtenerPorCorreo(correo);
        usuario.setNombreCompleto(request.nombreCompleto());
        usuario.setFoto(request.foto());
        usuario = usuarioRepository.save(usuario);
        return UsuarioResponse.fromEntity(usuario);
    }
}
