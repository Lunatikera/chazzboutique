/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.chazzboutiquepersistencia.interfacesDAO;

import com.mycompany.chazzboutiquepersistencia.dominio.VarianteProducto;
import com.mycompany.chazzboutiquepersistencia.excepciones.PersistenciaException;
import java.util.List;

/**
 *
 * @author carli
 */
public interface IVarianteProductoDAO {

    public VarianteProducto obtenerPorCodigoBarra(String codigoBarra) throws PersistenciaException;

    public VarianteProducto actualizarVarianteProducto(VarianteProducto variante) throws PersistenciaException;

    public VarianteProducto buscarPorId(Long id) throws PersistenciaException;

    public List<VarianteProducto> obtenerVariantesPorProducto(Long productoId) throws PersistenciaException;

    public VarianteProducto crearVariante(VarianteProducto variante) throws PersistenciaException;

    public void eliminarVarianteProducto(Long id) throws PersistenciaException;

    public List<VarianteProducto> buscarVariantesPorNombreProducto(String terminoBusqueda, int pagina, int tamañoPagina) throws PersistenciaException;

    public long contarVariantesPorNombreProducto(String terminoBusqueda) throws PersistenciaException;

}
