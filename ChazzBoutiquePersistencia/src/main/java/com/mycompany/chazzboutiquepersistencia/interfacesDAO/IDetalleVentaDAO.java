/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.chazzboutiquepersistencia.interfacesDAO;

import com.mycompany.chazzboutiquepersistencia.dominio.DetalleVenta;
import com.mycompany.chazzboutiquepersistencia.excepciones.PersistenciaException;

/**
 *
 * @author carli
 */
public interface IDetalleVentaDAO {
        public DetalleVenta registrarDetalle(DetalleVenta detalle) throws PersistenciaException ;

}
