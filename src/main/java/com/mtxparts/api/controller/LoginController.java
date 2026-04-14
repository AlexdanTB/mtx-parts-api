package com.mtxparts.api.controller;


import com.mtxparts.api.entity.Usuario;
import com.mtxparts.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Usuario login(@RequestBody Usuario loginUsuario){
        return  usuarioService.validadLogin(loginUsuario.getEmail(),loginUsuario.getPassword());
    }

}
