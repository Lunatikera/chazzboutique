/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chazzboutiquenegocio.dtos;

import java.time.LocalDate;

/**
 *
 * @author carli
 */
public class UsuarioDTO {
  private Long id;
    private String nombreUsuario;
    private String rol;
    private Boolean activo;
    private LocalDate fechaCreacion;

    public UsuarioDTO() {}

    public UsuarioDTO(Long id, String nombreUsuario, String rol, Boolean activo, LocalDate fechaCreacion) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.rol = rol;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    } 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    
}
