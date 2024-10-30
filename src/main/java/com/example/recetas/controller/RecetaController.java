package com.example.recetas.controller;

import com.example.recetas.model.Receta;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recetas")
public class RecetaController {

        // Lista estática de recetas
        private static final List<Receta> recetas = new ArrayList<>();
        public static final List<Receta> recetasPopulares = List.of(
                        new Receta(Long.parseLong("1"), "Tarta de Manzana", "Deliciosa tarta de manzana",
                                        "Manzanas, Harina, Azúcar, Canela",
                                        "Mezclar y hornear", "Postre", 4.5, new Date(), "url1"),
                        new Receta(Long.parseLong("2"), "Sopa de Tomate", "Sopa clásica de tomate",
                                        "Tomates, Cebolla, Ajo, Aceite de oliva",
                                        "Cocinar y triturar", "Sopa", 4.0, new Date(), "url2"),
                        new Receta(Long.parseLong("3"), "Pollo al Horno", "Pollo jugoso al horno",
                                        "Pollo, Ajo, Hierbas, Aceite", "Sazonar y hornear",
                                        "Principal", 4.8, new Date(), "url3"));

        // Inicializamos las recetas estáticas
        static {
                recetas.add(new Receta(Long.parseLong("1"), "Tarta de Manzana", "Deliciosa tarta de manzana",
                                "Manzanas, Harina, Azúcar, Canela",
                                "Mezclar y hornear", "Postre", 4.5, new Date(), "url1"));
                recetas.add(new Receta(Long.parseLong("2"), "Sopa de Tomate", "Sopa clásica de tomate",
                                "Tomates, Cebolla, Ajo, Aceite de oliva",
                                "Cocinar y triturar", "Sopa", 4.0, new Date(), "url2"));
                recetas.add(new Receta(Long.parseLong("3"), "Pollo al Horno", "Pollo jugoso al horno",
                                "Pollo, Ajo, Hierbas, Aceite",
                                "Sazonar y hornear", "Principal", 4.8, new Date(), "url3"));
                // Añade más recetas según sea necesario
        }

        // Endpoint para obtener todas las recetas
        @GetMapping
        public List<Receta> obtenerTodasLasRecetas() {
                return recetas;
        }

        // Endpoint para buscar recetas por propiedades
        @GetMapping("/buscar")
        public List<Receta> buscarRecetas(
                        @RequestParam(required = false) String nombre,
                        @RequestParam(required = false) String categoria,
                        @RequestParam(required = false) Double valoracionMinima) {
                return recetas.stream()
                                .filter(receta -> (nombre == null
                                                || receta.getNombre().toLowerCase().contains(nombre.toLowerCase())) &&
                                                (categoria == null || receta.getCategoria().equalsIgnoreCase(categoria))
                                                &&
                                                (valoracionMinima == null
                                                                || receta.getValoracion() >= valoracionMinima))
                                .collect(Collectors.toList());
        }
}
