/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chazzboutiquepersistencia.dtos;

import java.time.LocalDate;

/**
 *
 * @author carli
 */
public class ReporteVentasDTO {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    private Long productoId;
    private Long categoriaId;
    private Long varianteId;

    private String agruparPor;

    // Paginación
    private Integer pagina = 1;     // 1-based
    private Integer tamanio = 20;

    private boolean incluirDetalles = false;

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Long getVarianteId() {
        return varianteId;
    }

    public void setVarianteId(Long varianteId) {
        this.varianteId = varianteId;
    }

    public String getAgruparPor() {
        return agruparPor;
    }

    public void setAgruparPor(String agruparPor) {
        this.agruparPor = agruparPor;
    }

    public Integer getPagina() {
        return pagina;
    }

    public void setPagina(Integer pagina) {
        this.pagina = pagina;
    }

    public Integer getTamanio() {
        return tamanio;
    }

    public void setTamanio(Integer tamanio) {
        this.tamanio = tamanio;
    }

    public boolean isIncluirDetalles() {
        return incluirDetalles;
    }

    public void setIncluirDetalles(boolean incluirDetalles) {
        this.incluirDetalles = incluirDetalles;
    }

    @Override
    public String toString() {
        return "ReporteVentaDTO{"
                + "fechaInicio=" + fechaInicio
                + ", fechaFin=" + fechaFin
                + ", productoId=" + productoId
                + ", categoriaId=" + categoriaId
                + ", varianteId=" + varianteId
                + ", agruparPor='" + agruparPor + '\''
                + ", pagina=" + pagina
                + ", tamanio=" + tamanio
                + ", incluirDetalles=" + incluirDetalles
                + '}';
    }
}


