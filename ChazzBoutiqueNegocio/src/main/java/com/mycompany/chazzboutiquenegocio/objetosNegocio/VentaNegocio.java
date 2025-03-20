/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chazzboutiquenegocio.objetosNegocio;

import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IVentaNegocio;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.IVentaDAO;

/**
 *
 * @author carli
 */
public class VentaNegocio implements IVentaNegocio {

    private IVentaDAO ventaDAO;

    public VentaNegocio(IVentaDAO ventaDAO) {
        this.ventaDAO = ventaDAO;
    }

}
