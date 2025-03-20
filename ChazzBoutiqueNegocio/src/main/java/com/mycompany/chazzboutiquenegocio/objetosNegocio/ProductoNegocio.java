/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chazzboutiquenegocio.objetosNegocio;

import com.mycompany.chazzboutiquenegocio.dtos.ProductoDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IProductoNegocio;
import com.mycompany.chazzboutiquepersistencia.dominio.Producto;
import com.mycompany.chazzboutiquepersistencia.excepciones.PersistenciaException;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.IProductoDAO;

/**
 *
 * @author carli
 */
public class ProductoNegocio implements IProductoNegocio {
    IProductoDAO productoDAO;

    public ProductoNegocio(IProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }

    @Override
    public ProductoDTO buscarPorId(Long id) throws NegocioException {
       if (id == null || id <= 0) {
            throw new NegocioException("El ID debe ser un número válido.");
        }

        try {
            Producto producto = productoDAO.buscarPorId(id);

            if (producto == null) {
                throw new NegocioException("Usuario no encontrado.");
            }

            return convertirAProductoDTO(producto);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar usuario en la base de datos.", e);
        }
    }
    
     private ProductoDTO convertirAProductoDTO(Producto producto) {
        return new ProductoDTO(
            producto.getId(),
            producto.getNombre(),
            producto.getDescripcion(),
            producto.getFechaCreacion()
        );
    }
    
}
