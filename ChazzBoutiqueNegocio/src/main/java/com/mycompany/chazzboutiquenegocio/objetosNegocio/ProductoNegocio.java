package com.mycompany.chazzboutiquenegocio.objetosNegocio;

import com.mycompany.chazzboutiquenegocio.dtos.ProductoDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IProductoNegocio;
import com.mycompany.chazzboutiquepersistencia.dominio.Categoria;
import com.mycompany.chazzboutiquepersistencia.dominio.Producto;
import com.mycompany.chazzboutiquepersistencia.dominio.Proveedor;
import com.mycompany.chazzboutiquepersistencia.excepciones.PersistenciaException;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.IProductoDAO;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.ICategoriaDAO;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.IProveedorDAO;

import java.util.List;
import java.util.stream.Collectors;

public class ProductoNegocio implements IProductoNegocio {

    private final IProductoDAO productoDAO;
    private final ICategoriaDAO categoriaDAO;
    private final IProveedorDAO proveedorDAO;

    public ProductoNegocio(IProductoDAO productoDAO, ICategoriaDAO categoriaDAO, IProveedorDAO proveedorDAO) {
        this.productoDAO = productoDAO;
        this.categoriaDAO = categoriaDAO;
        this.proveedorDAO = proveedorDAO;
    }

    @Override
    public ProductoDTO buscarPorId(Long id) throws NegocioException {
        if (id == null || id <= 0) {
            throw new NegocioException("El ID debe ser un número válido.");
        }

        try {
            Producto producto = productoDAO.buscarPorId(id);

            if (producto == null) {
                throw new NegocioException("Producto no encontrado.");
            }

            return convertirAProductoDTO(producto);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar producto en la base de datos.", e);
        }
    }

   private ProductoDTO convertirAProductoDTO(Producto producto) {
    Long categoriaId = (producto.getCategoria() != null) ? producto.getCategoria().getId() : null;
    Long proveedorId = (producto.getProveedor() != null) ? producto.getProveedor().getId() : null;

    return new ProductoDTO(
            producto.getId(),
            producto.getNombre(),
            producto.getDescripcion(),
            producto.getFechaCreacion(),
            categoriaId,
            proveedorId
    );
}


    @Override
    public List<ProductoDTO> buscarPorNombre(String nombre) throws NegocioException {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new NegocioException("El nombre de búsqueda no puede estar vacío");
            }

            List<Producto> productosEntidad = productoDAO.buscarPorNombre(nombre);
            List<ProductoDTO> productosDTO = convertirEntidadesADTOs(productosEntidad);

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

    @Override
    public void crearProducto(ProductoDTO productoDTO) throws NegocioException {
        try {
            Categoria categoria = categoriaDAO.buscarPorId(productoDTO.getCategoriaId());
            Proveedor proveedor = proveedorDAO.buscarPorId(productoDTO.getProveedorId());

            Producto producto = new Producto(
                    null,
                    productoDTO.getNombreProducto(),
                    productoDTO.getDescripcionProducto(),
                    productoDTO.getFechaCreacion(),
                    proveedor,
                    categoria
            );

            productoDAO.crearProducto(producto);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al crear producto.", e);
        }
    }

    @Override
    public void actualizarProducto(ProductoDTO productoDTO) throws NegocioException {
        try {
            Categoria categoria = categoriaDAO.buscarPorId(productoDTO.getCategoriaId());
            Proveedor proveedor = proveedorDAO.buscarPorId(productoDTO.getProveedorId());

            Producto producto = new Producto(
                    productoDTO.getId(),
                    productoDTO.getNombreProducto(),
                    productoDTO.getDescripcionProducto(),
                    productoDTO.getFechaCreacion(),
                    proveedor,
                    categoria
            );

            productoDAO.actualizarProducto(producto);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al actualizar producto.", e);
        }
    }

    @Override
    public void eliminarProducto(Long id) throws NegocioException {
        try {
            productoDAO.eliminarProducto(id);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al eliminar producto.", e);
        }
    }

    public List<ProductoDTO> convertirEntidadesADTOs(List<Producto> productos) {
        return productos.stream()
                .map(this::convertirAProductoDTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO convertirEntidadADTO(Producto entidad) {
        return convertirAProductoDTO(entidad);
    }
} 
