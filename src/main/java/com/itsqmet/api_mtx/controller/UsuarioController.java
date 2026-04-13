package com.itsqmet.api_mtx.controller;

import com.itsqmet.api_mtx.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.itsqmet.api_mtx.service.UsuarioService;

import java.util.List;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {


    @Autowired
    private UsuarioService usuarioService;

    //LEER - GET
    @GetMapping
    public List<Usuario> getUsuario(){
        return usuarioService.leerUsuario();
    }

    //GUARDAR - POST
    @PostMapping
    public Usuario postUsuario(@RequestBody Usuario usuario){
        return usuarioService.guardarUsuario(usuario);
    }

    //ACTUALIZAR - PUT
    @PutMapping("/{id}")
    public Usuario putUsuario(@PathVariable Long id, @RequestBody Usuario usuario){
        return usuarioService.actualizarUsuario(id, usuario);
    }

    //ELIMINAR - DELETE
    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable Long id){
        usuarioService.eliminarUsuario(id);
    }
}
