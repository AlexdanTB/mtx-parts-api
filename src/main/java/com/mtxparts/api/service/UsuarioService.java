package com.mtxparts.api.service;

import com.mtxparts.api.entity.Usuario;
import com.mtxparts.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> leerUsuarios(){
        return usuarioRepository.findAll();
    }
}
