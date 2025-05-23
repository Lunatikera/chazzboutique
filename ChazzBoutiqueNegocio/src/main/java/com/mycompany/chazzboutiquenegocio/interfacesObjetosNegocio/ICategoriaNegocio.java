package com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio;

import com.mycompany.chazzboutiquenegocio.dtos.CategoriaDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;

import java.util.List;

public interface ICategoriaNegocio {
    void crearCategoria(CategoriaDTO categoria) throws NegocioException;
    void actualizarCategoria(CategoriaDTO categoria) throws NegocioException;
    void eliminarCategoria(Long id) throws NegocioException;
    List<CategoriaDTO> obtenerCategorias() throws NegocioException;
}
