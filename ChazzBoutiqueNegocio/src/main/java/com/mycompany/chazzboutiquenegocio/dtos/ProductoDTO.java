package com.mycompany.chazzboutiquenegocio.dtos;

import java.time.LocalDate;

public class ProductoDTO {

    private Long id;
    private String nombreProducto;
    private String descripcionProducto;
    private LocalDate fechaCreacion;

    private Long categoriaId;     
    private Long proveedorId;    
    public ProductoDTO() {
    }

    public ProductoDTO(Long id, String nombreProducto, String descripcionProducto, LocalDate fechaCreacion) {
        this.id = id;
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.fechaCreacion = fechaCreacion;
    }

    public ProductoDTO(Long id, String nombreProducto, String descripcionProducto, LocalDate fechaCreacion, Long categoriaId, Long proveedorId) {
        this.id = id;
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.fechaCreacion = fechaCreacion;
        this.categoriaId = categoriaId;
        this.proveedorId = proveedorId;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Long getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Long proveedorId) {
        this.proveedorId = proveedorId;
    }
}
