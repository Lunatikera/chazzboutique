/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.juvenr.mqc.chazzboutiqueapi.categorias.dto;

/**
 *
 * @author carli
 */
public class CategoriaResponse {
    private Long id;
    private String nombre;
    private String imagenUrl;

    public CategoriaResponse() {
    }

    public CategoriaResponse(Long id, String nombre, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.imagenUrl = imagenUrl;
    }

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

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
    
    
}
