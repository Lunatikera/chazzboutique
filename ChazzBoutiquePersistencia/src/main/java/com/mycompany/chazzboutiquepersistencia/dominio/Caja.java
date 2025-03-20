/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chazzboutiquepersistencia.dominio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author carli
 */
@Entity
@Table(name = "tblCaja")
public class Caja implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id", nullable = false, unique = true)
    private Long id;

    @Column(name = "horaApertura", nullable = false)
    private LocalDateTime horaApertura;
    
    @Column(name = "horaCierre", nullable = false)
    private LocalDateTime horaCierre;
    
    @Column(name = "montoInicial", nullable = false)
    private LocalDateTime montoInicial;

    @Column(name = "estado", nullable = false)
    private String estado;

    public Caja() {
    }


}
