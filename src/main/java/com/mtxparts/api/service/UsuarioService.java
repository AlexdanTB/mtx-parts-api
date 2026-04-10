package com.mtxparts.api.service;

import com.mtxparts.api.entity.Usuario;
import com.mtxparts.api.repository.UsuarioRepository;
import com.mtxparts.api.role.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> leerUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario guardarUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }
        String passwordEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passwordEncriptada);
        usuario.setRol(Rol.ROLE_CLIENTE);
        return usuarioRepository.save(usuario);
    }

    public Usuario guardarAdmin(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }
        String passwordEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passwordEncriptada);
        usuario.setRol(Rol.ROLE_ADMIN);
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(Long id, Usuario usuario) {
        Usuario usuarioEncontrado = buscarUsuarioPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuarioEncontrado.setNombreCompleto(usuario.getNombreCompleto());
        usuarioEncontrado.setEmail(usuario.getEmail());
        usuarioEncontrado.setTelefono(usuario.getTelefono());
        usuarioEncontrado.setDireccion(usuario.getDireccion());

        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            usuarioEncontrado.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        return usuarioRepository.save(usuarioEncontrado);
    }

    public void eliminarUsuario(Long id) {
        Usuario usuario = buscarUsuarioPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuarioRepository.delete(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities("ROLE_" + usuario.getRol().name())
                .build();
    }

    public Usuario validarLogin(String email, String password) {
        UserDetails userDetails = loadUserByUsername(email);
        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            return usuarioRepository.findByEmail(email).get();
        }
        throw new RuntimeException("Credenciales inválidas");
    }

    public boolean existeEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }
}
