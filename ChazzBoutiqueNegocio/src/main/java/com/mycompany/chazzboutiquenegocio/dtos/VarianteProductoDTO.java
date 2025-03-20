/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chazzboutiquenegocio.dtos;

import java.math.BigDecimal;

/**
 *
 * @author carli
 */
public class VarianteProductoDTO {
   private String codigoBarra;
    private int stock;
    private BigDecimal precioCompra;
    private String talla;
    private BigDecimal precioVenta;
    private Long productoId;

    // Constructor
    public VarianteProductoDTO(String codigoBarra, int stock, BigDecimal precioCompra, String talla, BigDecimal precioVenta, Long productoId) {
        this.codigoBarra = codigoBarra;
        this.stock = stock;
        this.precioCompra = precioCompra;
        this.talla = talla;
        this.precioVenta = precioVenta;
        this.productoId = productoId;
    }

    // Getters y Setters
    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }
 
}
