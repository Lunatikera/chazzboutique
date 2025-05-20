/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.chazzboutiquepersistencia.interfacesDAO;

import com.mycompany.chazzboutiquepersistencia.dominio.Producto;
import com.mycompany.chazzboutiquepersistencia.dtos.FiltroProductoDTO;
import com.mycompany.chazzboutiquepersistencia.excepciones.PersistenciaException;
import java.util.List;

/**
 *
 * @author carli
 */
public interface IProductoDAO {

    public Producto buscarPorId(Long id) throws PersistenciaException;

    public List<Producto> buscarPorNombre(String nombre) throws PersistenciaException;

    public List<Producto> obtenerTodosProductos() throws PersistenciaException;

    public void crearProducto(Producto producto) throws PersistenciaException;

    public void actualizarProducto(Producto producto) throws PersistenciaException;

    public void eliminarProducto(Long id) throws PersistenciaException;

    public List<Producto> buscarPorFiltro(FiltroProductoDTO filtro) throws PersistenciaException;

    public List<Producto> obtenerPorCategoria(Long categoriaId) throws PersistenciaException;

}
