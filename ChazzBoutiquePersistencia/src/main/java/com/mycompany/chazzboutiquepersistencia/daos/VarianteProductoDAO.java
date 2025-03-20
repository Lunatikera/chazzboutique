/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chazzboutiquepersistencia.daos;

import com.mycompany.chazzboutiquepersistencia.conexion.IConexionBD;
import com.mycompany.chazzboutiquepersistencia.dominio.VarianteProducto;
import com.mycompany.chazzboutiquepersistencia.excepciones.PersistenciaException;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.IVarianteProductoDAO;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

/**
 *
 * @author carli
 */
public class VarianteProductoDAO implements IVarianteProductoDAO {
     IConexionBD conexionBD;

    public VarianteProductoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public VarianteProducto obtenerPorCodigoBarra(String codigoBarra) throws PersistenciaException {
              EntityManager entityManager = conexionBD.getEntityManager();
        try {
            TypedQuery<VarianteProducto> query = entityManager.createQuery(
                "SELECT v FROM VarianteProducto v WHERE v.codigoBarra = :codigoBarra", VarianteProducto.class);
            query.setParameter("codigoBarra", codigoBarra);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // No se encontró la variante
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener la variante de producto por código de barra: " + codigoBarra, e);
        }
    }
}
