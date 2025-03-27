/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.chazzboutiquepersistencia.interfacesDAO;

import com.mycompany.chazzboutiquepersistencia.dominio.VarianteProducto;
import com.mycompany.chazzboutiquepersistencia.excepciones.PersistenciaException;
import javax.persistence.PersistenceException;

/**
 *
 * @author carli
 */
public interface IVarianteProductoDAO {

    public VarianteProducto obtenerPorCodigoBarra(String codigoBarra) throws PersistenciaException;

    public VarianteProducto actualizar(VarianteProducto variante) throws PersistenciaException;

    public VarianteProducto buscarPorId(Long id) throws PersistenciaException;

}
