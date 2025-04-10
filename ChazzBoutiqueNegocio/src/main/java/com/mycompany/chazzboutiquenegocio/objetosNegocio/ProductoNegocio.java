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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<ProductoDTO> buscarPorNombre(String nombre) throws NegocioException {
        try {
            // Validar parámetro de entrada
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new NegocioException("El nombre de búsqueda no puede estar vacío");
            }

            // Obtener entidades desde el DAO
            List<Producto> productosEntidad = productoDAO.buscarPorNombre(nombre);

            // Convertir entidades a DTOs
            List<ProductoDTO> productosDTO = new ArrayList<>();
            productosDTO = convertirEntidadesADTOs(productosEntidad);
            // Validar resultados
            if (productosDTO.isEmpty()) {
                throw new NegocioException("No se encontraron productos con ese nombre");
            }

            return productosDTO;

        } catch (PersistenciaException e) {
            throw new NegocioException("Error al acceder a los datos: " + e.getMessage(), e);
        }
    }
    
     @Override
    public List<ProductoDTO> obtenerTodosProductos() throws NegocioException {
        try {
            List<Producto> entidades = productoDAO.obtenerTodosProductos();
            return convertirEntidadesADTOs(entidades);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al recuperar productos: " + e.getMessage());
        }
    }

    public ProductoDTO convertirEntidadADTO(Producto entidad) {
        if (entidad == null) {
            return null;
        }

        return new ProductoDTO(
                entidad.getId(),
                entidad.getNombre(),
                entidad.getDescripcion(),
                entidad.getFechaCreacion()
        );
    }

    public List<ProductoDTO> convertirEntidadesADTOs(List<Producto> productos) {
        return productos.stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }
}
