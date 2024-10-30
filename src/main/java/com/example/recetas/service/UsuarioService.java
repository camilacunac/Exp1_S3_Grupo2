package com.example.recetas.service;

import com.example.recetas.model.Response;
import com.example.recetas.model.Usuario;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface UsuarioService {
    ResponseEntity<Response> crearUsuario(Usuario usuario) throws Exception;

    ResponseEntity<Response> login(String correo, String contrasena, HttpServletResponse httpResponse) throws Exception;

    ResponseEntity<Response> actualizarRol(Long idUsuario, String nuevoRol) throws Exception;

    ResponseEntity<Response> eliminarUsuario(Long idUsuario) throws Exception;

    void logout(HttpServletResponse response);

    List<Usuario> getAllUsuarios();
}
