/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chazzboutiquenegocio.dtos;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author carli
 */
public class DetalleVentaDTO {
    private Long id;
    private Long varianteProductoId;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private String nombreProducto; // Opcional: para mostrar en interfaces
    private String codigoVariante; // Opcional: para referencia

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVarianteProductoId() {
        return varianteProductoId;
    }

    public void setVarianteProductoId(Long varianteProductoId) {
        this.varianteProductoId = varianteProductoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getCodigoVariante() {
        return codigoVariante;
    }

    public void setCodigoVariante(String codigoVariante) {
        this.codigoVariante = codigoVariante;
    }

    @Override
    public String toString() {
        return "DetalleVentaDTO{" + "id=" + id + ", varianteProductoId=" + varianteProductoId + ", cantidad=" + cantidad + ", precioUnitario=" + precioUnitario + ", nombreProducto=" + nombreProducto + ", codigoVariante=" + codigoVariante + '}';
    }

    
}
