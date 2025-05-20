package com.mycompany.chazzboutiquepersistencia.daos;

import com.mycompany.chazzboutiquepersistencia.conexion.IConexionBD;
import com.mycompany.chazzboutiquepersistencia.dominio.VarianteProducto;
import com.mycompany.chazzboutiquepersistencia.excepciones.PersistenciaException;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.IVarianteProductoDAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class VarianteProductoDAO implements IVarianteProductoDAO {

    private final IConexionBD conexionBD;

    public VarianteProductoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public VarianteProducto crearVariante(VarianteProducto variante) throws PersistenciaException {
        EntityManager em = conexionBD.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(variante);
            tx.commit();
            return variante;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new PersistenciaException("Error al crear la variante de producto", e);
        } finally {
            em.close();
        }
    }

    @Override
    public VarianteProducto buscarPorId(Long id) throws PersistenciaException {
        EntityManager em = conexionBD.getEntityManager();
        try {
            VarianteProducto v = em.find(VarianteProducto.class, id);
            return v;
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar variante por ID", e);
        } finally {
            em.close();
        }
    }

    @Override
    public VarianteProducto obtenerPorCodigoBarra(String codigoBarra) throws PersistenciaException {
        EntityManager em = conexionBD.getEntityManager();
        try {
            TypedQuery<VarianteProducto> query = em.createQuery(
                "SELECT v FROM VarianteProducto v WHERE v.codigoBarra = :cb AND v.eliminado = false",
                VarianteProducto.class
            );
            query.setParameter("cb", codigoBarra);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener variante por código de barras", e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<VarianteProducto> obtenerVariantesPorProducto(Long productoId) throws PersistenciaException {
        EntityManager em = conexionBD.getEntityManager();
        try {
            TypedQuery<VarianteProducto> query = em.createQuery(
                "SELECT v FROM VarianteProducto v " +
                "WHERE v.producto.id = :pid AND v.eliminado = false",
                VarianteProducto.class
            );
            query.setParameter("pid", productoId);
            return query.getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener variantes por producto", e);
        } finally {
            em.close();
        }
    }

    @Override
    public VarianteProducto actualizarVarianteProducto(VarianteProducto variante) throws PersistenciaException {
        EntityManager em = conexionBD.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            VarianteProducto updated = em.merge(variante);
            tx.commit();
            return updated;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new PersistenciaException("Error al actualizar la variante de producto", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminarVarianteProducto(Long id) throws PersistenciaException {
        EntityManager em = conexionBD.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            VarianteProducto v = em.find(VarianteProducto.class, id);
            if (v != null) {
                v.setEliminado(true);
                em.merge(v);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new PersistenciaException("Error al hacer soft delete de la variante", e);
        } finally {
            em.close();
        }
    }
}
