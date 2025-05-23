package com.mycompany.chazzboutiquenegocio.dtos;

public class CategoriaDTO {
    private Long id;
    private String nombreCategoria;
    private String descripcionCategoria;
    private String imagenCategoria;

    public CategoriaDTO() {
    }

    public CategoriaDTO(Long id, String nombreCategoria, String descripcionCategoria, String imagenCategoria) {
        this.id = id;
        this.nombreCategoria = nombreCategoria;
        this.descripcionCategoria = descripcionCategoria;
        this.imagenCategoria = imagenCategoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getDescripcionCategoria() {
        return descripcionCategoria;
    }

    public void setDescripcionCategoria(String descripcionCategoria) {
        this.descripcionCategoria = descripcionCategoria;
    }

    public String getImagenCategoria() {
        return imagenCategoria;
    }

    public void setImagenCategoria(String imagenCategoria) {
        this.imagenCategoria = imagenCategoria;
    }

    @Override
    public String toString() {
        return nombreCategoria;
    }
}
