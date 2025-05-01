/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chazzboutiquepersistencia.daos;

import com.mycompany.chazzboutiquepersistencia.conexion.IConexionBD;
import com.mycompany.chazzboutiquepersistencia.dominio.VarianteProducto;
import com.mycompany.chazzboutiquepersistencia.excepciones.PersistenciaException;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.IVarianteProductoDAO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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
    public List<VarianteProducto> obtenerVariantesPorProducto(Long productoId) throws PersistenciaException {
        EntityManager entityManager = conexionBD.getEntityManager();
        try {
            // Consulta JPQL para obtener todas las variantes de un producto
            TypedQuery<VarianteProducto> query = entityManager.createQuery(
                    "SELECT v FROM VarianteProducto v WHERE v.producto.id = :productoId",
                    VarianteProducto.class
            );
            query.setParameter("productoId", productoId);
            return query.getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener variantes para el producto ID: " + productoId, e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
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

    @Override
    public VarianteProducto actualizar(VarianteProducto variante) throws PersistenciaException {
        EntityManager entityManager = conexionBD.getEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            // Actualiza la variante en la base de datos
            VarianteProducto varianteActualizada = entityManager.merge(variante);

            transaction.commit();
            return varianteActualizada;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new PersistenciaException("Error al actualizar la variante de producto con ID: " + variante.getId(), e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    @Override
    public VarianteProducto buscarPorId(Long id) throws PersistenciaException {
        EntityManager entityManager = conexionBD.getEntityManager();
        try {
            // Busca la variante por su ID
            VarianteProducto variante = entityManager.find(VarianteProducto.class, id);

            if (variante == null) {
                throw new PersistenciaException("No se encontró la variante de producto con ID: " + id);
            }

            return variante;
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar variante de producto por ID: " + id, e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}
