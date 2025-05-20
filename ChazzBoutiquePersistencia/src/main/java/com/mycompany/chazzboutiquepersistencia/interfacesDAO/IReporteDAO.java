/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.chazzboutiquepersistencia.interfacesDAO;

import com.mycompany.chazzboutiquepersistencia.dtos.ReporteVentaResultadoDTO;
import com.mycompany.chazzboutiquepersistencia.dtos.ReporteVentasDTO;
import com.mycompany.chazzboutiquepersistencia.excepciones.PersistenciaException;
import java.util.List;

/**
 *
 * @author carli
 */
public interface IReporteDAO {

    public List<ReporteVentaResultadoDTO> generarReporteVentas(ReporteVentasDTO filtro) throws PersistenciaException;

}
