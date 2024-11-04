package com.example.recetas.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.recetas.model.LoginDTO;
import com.example.recetas.model.Receta;
import com.example.recetas.model.Usuario;
import org.springframework.ui.Model;

@Controller
public class RouterController {
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @GetMapping("/login")
    public String mostrarFormularioLogin(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    @GetMapping("inicio")
    public String mostrarInicio(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated()
                && !auth.getPrincipal().equals("anonymousUser");

        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("recetasPopulares", RecetaController.recetasPopulares);
        return "inicio";
    }

    @GetMapping("/")
    public String mostrarIndex(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated()
                && !auth.getPrincipal().equals("anonymousUser");

        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("recetasPopulares", RecetaController.recetasPopulares);
        return "inicio";
    }

    @GetMapping("/receta/{id}")
    public String verReceta(@PathVariable Long id, Model model) {
        Receta receta = RecetaController.recetasPopulares.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null); // Esto devuelve null si no encuentra la receta

        if (receta == null) {
            return "error"; // O redirige a una página de error si la receta no se encuentra
        }

        model.addAttribute("receta", receta);
        return "receta"; // El nombre del template Thymeleaf
    }

    @GetMapping("/buscar")
    public String mostrarBusqueda(Model model) {
        // Añadimos los atributos del modelo si necesitas que tengan valores por defecto
        model.addAttribute("nombre", "");
        model.addAttribute("categoria", "");
        model.addAttribute("valoracionMinima", "");
        model.addAttribute("recetas", List.of()); // Lista vacía para el inicio
        return "busqueda"; // Renderiza 'busqueda.html'
    }
}
