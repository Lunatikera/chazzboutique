/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chazzboutiquepersistencia.daos;

import com.mycompany.chazzboutiquepersistencia.conexion.IConexionBD;
import com.mycompany.chazzboutiquepersistencia.dtos.ReporteVentaResultadoDTO;
import com.mycompany.chazzboutiquepersistencia.dtos.ReporteVentasDTO;
import com.mycompany.chazzboutiquepersistencia.excepciones.PersistenciaException;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.IReporteDAO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author carli
 */
public class ReporteDAO implements IReporteDAO {

    IConexionBD conexionBD;

    public ReporteDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public List<ReporteVentaResultadoDTO> generarReporteVentas(ReporteVentasDTO filtro) throws PersistenciaException {
        EntityManager em = conexionBD.getEntityManager();
        try {
            // Construcción dinámica de JPQL
            StringBuilder jpql = new StringBuilder();
            // Selección según agrupamiento
            String groupBy = "";
            switch (filtro.getAgruparPor() != null ? filtro.getAgruparPor().toUpperCase() : "") {
                case "MES":
                    jpql.append("SELECT NEW com.mycompany.chazzboutiquepersistencia.daos.ReporteVentaResultadoDTO("
                            + "FUNCTION('YEAR', v.fecha), FUNCTION('MONTH', v.fecha), SUM(d.cantidad), SUM(d.precioUnitario * d.cantidad)) ");
                    groupBy = "GROUP BY FUNCTION('YEAR', v.fecha), FUNCTION('MONTH', v.fecha) ORDER BY FUNCTION('YEAR', v.fecha), FUNCTION('MONTH', v.fecha)";
                    break;
                case "AÑO":
                    jpql.append("SELECT NEW com.mycompany.chazzboutiquepersistencia.daos.ReporteVentaResultadoDTO("
                            + "FUNCTION('YEAR', v.fecha), null, SUM(d.cantidad), SUM(d.precioUnitario * d.cantidad)) ");
                    groupBy = "GROUP BY FUNCTION('YEAR', v.fecha) ORDER BY FUNCTION('YEAR', v.fecha)";
                    break;
                default: // DIA
                    jpql.append("SELECT NEW com.mycompany.chazzboutiquepersistencia.daos.ReporteVentaResultadoDTO("
                            + "v.fecha, null, SUM(d.cantidad), SUM(d.precioUnitario * d.cantidad)) ");
                    groupBy = "GROUP BY v.fecha ORDER BY v.fecha";
            }
            jpql.append("FROM VentaDetalle d JOIN d.venta v WHERE v.fecha BETWEEN :fi AND :ff ");
            if (filtro.getProductoId() != null) {
                jpql.append("AND d.producto.id = :pid ");
            }
            if (filtro.getCategoriaId() != null) {
                jpql.append("AND d.producto.categoria.id = :cid ");
            }
            if (filtro.getVarianteId() != null) {
                jpql.append("AND d.variante.id = :vid ");
            }
            jpql.append(groupBy);

            TypedQuery<ReporteVentaResultadoDTO> query = em.createQuery(jpql.toString(), ReporteVentaResultadoDTO.class);
            query.setParameter("fi", filtro.getFechaInicio());
            query.setParameter("ff", filtro.getFechaFin());
            if (filtro.getProductoId() != null) {
                query.setParameter("pid", filtro.getProductoId());
            }
            if (filtro.getCategoriaId() != null) {
                query.setParameter("cid", filtro.getCategoriaId());
            }
            if (filtro.getVarianteId() != null) {
                query.setParameter("vid", filtro.getVarianteId());
            }

            int pagina = Math.max(filtro.getPagina(), 1);
            int tamanio = Math.max(filtro.getTamanio(), 1);
            query.setFirstResult((pagina - 1) * tamanio);
            query.setMaxResults(tamanio);

            return query.getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("Error al generar reporte de ventas", e);
        } finally {
            em.close();
        }
    }

}
