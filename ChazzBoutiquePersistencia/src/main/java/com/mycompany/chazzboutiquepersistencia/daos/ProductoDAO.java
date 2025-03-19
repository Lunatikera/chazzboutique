/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chazzboutiquepersistencia.daos;

import com.mycompany.chazzboutiquepersistencia.conexion.IConexionBD;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author carli
 */
public class ProductoDAO  {

    IConexionBD conexionBD;

    public ProductoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

}
