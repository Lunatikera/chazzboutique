/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chazzboutiquepersistencia.daos;

import com.mycompany.chazzboutiquepersistencia.conexion.IConexionBD;
import com.mycompany.chazzboutiquepersistencia.dominio.Producto;
import com.mycompany.chazzboutiquepersistencia.excepciones.PersistenciaException;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.IProductoDAO;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author carli
 */
public class ProductoDAO implements IProductoDAO {

    IConexionBD conexionBD;

    public ProductoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public Producto buscarPorId(Long id) throws PersistenciaException {
       EntityManager em = conexionBD.getEntityManager();
        try {
            return em.find(Producto.class, id);
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar producto por ID", e);
        } finally {
            em.close();
        }
    }

}
