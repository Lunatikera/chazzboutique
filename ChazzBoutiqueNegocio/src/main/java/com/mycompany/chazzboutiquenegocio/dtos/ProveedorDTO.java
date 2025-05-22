package com.mycompany.chazzboutiquenegocio.dtos;

public class ProveedorDTO {
    private Long id;
    private String nombre;

    public ProveedorDTO() {
    }

    public ProveedorDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    @Override
    public String toString() {
        return nombre; // para que se vea bien en el JComboBox
    }
}
