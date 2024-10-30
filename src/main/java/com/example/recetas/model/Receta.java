package com.example.recetas.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "recetas")
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_receta")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion", nullable = false, length = 1000)
    private String descripcion;

    @Column(name = "ingredientes", nullable = false, length = 2000)
    private String ingredientes;

    @Column(name = "instrucciones", nullable = false, length = 3000)
    private String instrucciones;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "valoracion")
    private Double valoracion;

    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @Column(name = "imagen_url")
    private String imagenUrl;

    // Constructor vacío para JPA
    public Receta() {
    }

    // Constructor con parámetros
    public Receta(String nombre, String descripcion, String ingredientes, String instrucciones,
            String categoria, Double valoracion, Date fechaCreacion, String imagenUrl) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ingredientes = ingredientes;
        this.instrucciones = instrucciones;
        this.categoria = categoria;
        this.valoracion = valoracion;
        this.fechaCreacion = fechaCreacion;
        this.imagenUrl = imagenUrl;
    }

    public Receta(Long id, String nombre, String descripcion, String ingredientes, String instrucciones,
            String categoria, Double valoracion, Date fechaCreacion, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ingredientes = ingredientes;
        this.instrucciones = instrucciones;
        this.categoria = categoria;
        this.valoracion = valoracion;
        this.fechaCreacion = fechaCreacion;
        this.imagenUrl = imagenUrl;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Double getValoracion() {
        return valoracion;
    }

    public void setValoracion(Double valoracion) {
        this.valoracion = valoracion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
