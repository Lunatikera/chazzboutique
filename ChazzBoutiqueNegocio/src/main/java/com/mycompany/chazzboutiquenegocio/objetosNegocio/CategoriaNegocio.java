package com.mycompany.chazzboutiquenegocio.objetosNegocio;

import com.mycompany.chazzboutiquenegocio.dtos.CategoriaDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.ICategoriaNegocio;
import com.mycompany.chazzboutiquepersistencia.dominio.Categoria;
import com.mycompany.chazzboutiquepersistencia.excepciones.PersistenciaException;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.ICategoriaDAO;

import java.util.List;
import java.util.stream.Collectors;

public class CategoriaNegocio implements ICategoriaNegocio {

    private final ICategoriaDAO categoriaDAO;

    public CategoriaNegocio(ICategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    @Override
    public void crearCategoria(CategoriaDTO dto) throws NegocioException {
        Categoria categoria = new Categoria();
        categoria.setNombreCategoria(dto.getNombreCategoria());
        categoria.setDescripcionCategoria(dto.getDescripcionCategoria());
        categoria.setImagenCategoria(dto.getImagenCategoria());
        categoria.setEliminado(false);

        try {
            categoriaDAO.crearCategoria(categoria);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al crear categoría", e);
        }
    }

    @Override
    public void actualizarCategoria(CategoriaDTO dto) throws NegocioException {
        try {
            Categoria categoria = categoriaDAO.buscarPorId(dto.getId());
            categoria.setNombreCategoria(dto.getNombreCategoria());
            categoria.setDescripcionCategoria(dto.getDescripcionCategoria());
            categoria.setImagenCategoria(dto.getImagenCategoria());

            categoriaDAO.actualizarCategoria(categoria);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al actualizar categoría", e);
        }
    }

    @Override
    public void eliminarCategoria(Long id) throws NegocioException {
        try {
            Categoria categoria = categoriaDAO.buscarPorId(id);

            if (categoria.getProductos() != null && !categoria.getProductos().isEmpty()) {
                throw new NegocioException("No se puede eliminar la categoría porque tiene productos asociados. Elimine primero los productos.");
            }

            categoriaDAO.eliminarCategoria(id);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al eliminar la categoría", e);
        }
    }

    @Override
    public List<CategoriaDTO> obtenerCategorias() throws NegocioException {
        try {
            List<Categoria> categorias = categoriaDAO.obtenerTodasCategorias();
            return categorias.stream()
                    .map(cat -> new CategoriaDTO(cat.getId(), cat.getNombreCategoria(), cat.getDescripcionCategoria(), cat.getImagenCategoria()))
                    .collect(Collectors.toList());
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener categorías", e);
        }
    }
}
