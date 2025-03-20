/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chazzboutiquepersistencia.dominio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author carli
 */
@Entity
@Table(name = "tblVenta")
public class Venta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venta_id", nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.PERSIST)
    private List<DetalleVenta> detallesVentas;
    
    @Column(name = "fechaVenta", nullable = false)
    private LocalDate fechaVenta;

    @Column(name = "totalCompra", nullable = false)
    private BigDecimal totalCompra;

    @Column(name = "estadoVenta", nullable = false)
    private String estadoVenta;

    @OneToMany(mappedBy = "venta")
    private List<DetalleVenta> detalles;

    public Venta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getFecha() {
        return fechaVenta;
    }

    public void setFecha(LocalDate fecha) {
        this.fechaVenta = fecha;
    }

    public BigDecimal getTotal() {
        return totalCompra;
    }

    public void setTotal(BigDecimal total) {
        this.totalCompra = totalCompra;
    }

    public String getEstado() {
        return estadoVenta;
    }

    public void setEstado(String estadoVenta) {
        this.estadoVenta = estadoVenta;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

}
