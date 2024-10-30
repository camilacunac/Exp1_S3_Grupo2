package com.example.recetas.controller;

import com.example.recetas.model.LoginDTO;
import com.example.recetas.model.Response;
import com.example.recetas.model.Usuario;
import com.example.recetas.service.UsuarioService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Obtener todos los usuarios
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    // Crear un nuevo usuario
    // @PostMapping("/registro")
    // public ResponseEntity<Response> crearUsuario(@ModelAttribute Usuario usuario)
    // {
    // try {
    // return usuarioService.crearUsuario(usuario);
    // } catch (Exception e) {
    // return ResponseEntity.status(500).body(new Response("error", null, "Error
    // interno al crear el usuario"));
    // }
    // }

    @PostMapping("/registro")
    public ResponseEntity<Response> crearUsuario(@RequestBody Usuario usuario) {
        try {
            return usuarioService.crearUsuario(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response("error", null, "Error interno al crear el usuario"));
        }
    }

    // Inicio de sesión (Login)
    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginDTO loginRequest, HttpServletResponse response) {
        try {
            return usuarioService.login(loginRequest.getCorreo(), loginRequest.getContrasena(), response);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new Response("error", null, "Error interno durante el inicio de sesión"));
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        usuarioService.logout(response);
        return ResponseEntity.status(HttpStatus.OK).body("Logout exitoso");
    }

    // Actualizar el rol de un usuario
    @PutMapping("/{id}/rol")
    public ResponseEntity<Response> actualizarRol(@PathVariable Long id, @RequestParam String nuevoRol) {
        try {
            return usuarioService.actualizarRol(id, nuevoRol);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response("error", null, "Error interno al actualizar el rol"));
        }
    }

    // Eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> eliminarUsuario(@PathVariable Long id) {
        try {
            return usuarioService.eliminarUsuario(id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response("error", null, "Error interno al eliminar el usuario"));
        }
    }
}
