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

    private Long id;
    private String codigoBarra;
    private int stock;
    private BigDecimal precioCompra;
    private String talla;
    private String color;
    private BigDecimal precioVenta;
    private Long productoId;
    private String nombreProducto;
    private String urlImagen;

    public VarianteProductoDTO() {
    }

    // Constructor
    public VarianteProductoDTO(String codigoBarra, int stock, BigDecimal precioCompra, String talla, BigDecimal precioVenta, Long productoId) {
        this.codigoBarra = codigoBarra;
        this.stock = stock;
        this.precioCompra = precioCompra;
        this.talla = talla;
        this.precioVenta = precioVenta;
        this.productoId = productoId;
    }

    public VarianteProductoDTO() {
    }

    public VarianteProductoDTO(String codigoBarra, int stock, BigDecimal precioCompra, String talla, String color, BigDecimal precioVenta, Long productoId) {
        this.codigoBarra = codigoBarra;
        this.stock = stock;
        this.precioCompra = precioCompra;
        this.talla = talla;
        this.color = color;
        this.precioVenta = precioVenta;
        this.productoId = productoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
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

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
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
