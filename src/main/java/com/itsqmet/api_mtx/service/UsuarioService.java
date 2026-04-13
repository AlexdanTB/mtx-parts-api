package com.itsqmet.api_mtx.service;

import com.itsqmet.api_mtx.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.itsqmet.api_mtx.repository.UsuarioRepository;
import com.itsqmet.api_mtx.role.Rol;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    //INYECCION DE DEPENDENCIA POR CAMPO
    @Autowired
    private UsuarioRepository usuarioRepository;

    //ENCRIPTAR LA CONTRASEÑA
    @Autowired
    private PasswordEncoder passwordEncoder;

    //LEER
    public List<Usuario> leerUsuario(){
        return usuarioRepository.findAll();
    }

    //BUSCAR POR ID
    public Optional<Usuario> buscarUsuarioPorId(Long id){
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    //GUARDAR
    public Usuario guardarUsuario(Usuario usuario){
        //ENCRIPTAR LA CONTRASEÑA QUE EL USUARIO INGRESE AL REGISTRARSe
        String passwordEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passwordEncriptada);
        //ASIGNAR UN ROL POR DEFAULT
        usuario.setRol(Rol.ROLE_USUARIO);
        return usuarioRepository.save(usuario);
    }

    //ACTUALIZAR
    public Usuario actualizarUsuario(Long id, Usuario usuario){
        Usuario usuarioEncontrado = buscarUsuarioPorId(id)
                .orElseThrow(()-> new RuntimeException("Usuario No Existe"));
        if (usuario.getName() != null) {
            usuarioEncontrado.setName(usuario.getName());
        }
        if (usuario.getEmail() != null) {
            usuarioEncontrado.setEmail(usuario.getEmail());
        }
        if (usuario.getPhone() != null) {
            usuarioEncontrado.setPhone(usuario.getPhone());
        }
        if (usuario.getRol() != null) {
            usuarioEncontrado.setRol(usuario.getRol());
        }
        if (usuario.getImagen_url ()!= null){
            usuarioEncontrado.setImagen_url(usuario.getImagen_url());
        }
        if (usuario.getAddress ()!= null){
            usuarioEncontrado.setAddress(usuario.getAddress());
        }
        // La contraseña ya la teníamos bien validada
        if(usuario.getPassword() != null && !usuario.getPassword().isEmpty()){
            usuarioEncontrado.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        return usuarioRepository.save(usuarioEncontrado);
    }

    //ELIMINAR
    public void eliminarUsuario(Long id){
        Usuario usuarioEncontrado = buscarUsuarioPorId(id)
                .orElseThrow(()-> new RuntimeException(("Usuario no encontrado")));
        usuarioRepository.delete(usuarioEncontrado);
    }

    //AUTENTICACION
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontreado"));
        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities(usuario.getRol().name())
                .build();
    }

    //VALIDAR EL LOGIN
    public Usuario validadLogin(String email, String password){
        //TRAER LOS DATOS REALES DEL USUARIO
        UserDetails userDetails = loadUserByUsername(email);
        //COMPARA EL TEXTO PLANO DELA CONTRASEÑA CON EL HASH DE LA BASE DE DATOS
        if(passwordEncoder.matches(password, userDetails.getPassword())){
            return usuarioRepository.findByEmail(email).get();
        }
        throw new RuntimeException("Error de autenticación");
    }

}
