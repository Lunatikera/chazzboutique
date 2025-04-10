/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.chazzboutiquepersistencia.interfacesDAO;

import com.mycompany.chazzboutiquepersistencia.dominio.Producto;
import com.mycompany.chazzboutiquepersistencia.excepciones.PersistenciaException;
import java.util.List;

/**
 *
 * @author carli
 */
public interface IProductoDAO {

    public Producto buscarPorId(Long id) throws PersistenciaException;
    public List<Producto> buscarPorNombre(String nombre) throws PersistenciaException ;
    public List<Producto> obtenerTodosProductos() throws PersistenciaException ;

}
