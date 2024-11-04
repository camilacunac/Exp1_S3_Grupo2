package com.example.recetas.controller;

import com.example.recetas.model.LoginDTO;
import com.example.recetas.model.Response;
import com.example.recetas.model.Usuario;
import com.example.recetas.service.UsuarioService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
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
    public String crearUsuario(@ModelAttribute("usuario") Usuario usuario, Model model) {
        try {
            ResponseEntity<Response> response = usuarioService.crearUsuario(usuario);
            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/login"; // Redirige a la página de login si el registro es exitoso
            } else {
                model.addAttribute("error", true); // Añade el error al modelo para mostrarlo en la vista
                return "registro"; // Devuelve la vista de registro con mensaje de error
            }
        } catch (Exception e) {
            model.addAttribute("error", true);
            return "registro";
        }
    }

    // Inicio de sesión (Login)
    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTO loginRequest, Model model, HttpServletResponse response) {
        try {
            ResponseEntity<Response> loginResponse = usuarioService.login(loginRequest.getCorreo(),
                    loginRequest.getContrasena(), response);
            if (loginResponse.getStatusCode() == HttpStatus.OK) {
                return "redirect:/inicio"; // Redirige a la página de inicio en caso de éxito
            } else {
                model.addAttribute("error", true); // Agrega un atributo para mostrar el mensaje de error
                return "login"; // Vuelve a mostrar el formulario de inicio de sesión con el mensaje de error
            }
        } catch (Exception e) {
            model.addAttribute("error", true);
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        usuarioService.logout(response);
        return "redirect:/inicio";
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
