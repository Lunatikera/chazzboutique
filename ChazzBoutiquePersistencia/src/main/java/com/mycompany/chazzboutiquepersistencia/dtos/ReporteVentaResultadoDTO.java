/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chazzboutiquepersistencia.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author carli
 */
public class ReporteVentaResultadoDTO {
   private LocalDate fecha;
    private Integer mes;
    private Integer totalCantidad;
    private BigDecimal totalVentas;

    // Constructor para JPQL
    public ReporteVentaResultadoDTO(LocalDate fecha, Integer mes, Long cantidad, BigDecimal total) {
        this.fecha = fecha;
        this.mes = mes;
        this.totalCantidad = cantidad != null ? cantidad.intValue() : 0;
        this.totalVentas = total;
    }

    // Getters
    public LocalDate getFecha() {
        return fecha;
    }
    public Integer getMes() {
        return mes;
    }
    public Integer getTotalCantidad() {
        return totalCantidad;
    }
    public BigDecimal getTotalVentas() {
        return totalVentas;
    } 
}
