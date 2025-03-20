/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chazzboutiquepersistencia.daos;

import com.mycompany.chazzboutiquepersistencia.conexion.IConexionBD;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.IVentaDAO;

/**
 *
 * @author carli
 */
public class VentaDAO implements IVentaDAO {
     IConexionBD conexionBD;

    public VentaDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }
}
