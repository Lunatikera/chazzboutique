package com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio;

import com.mycompany.chazzboutiquenegocio.dtos.VarianteProductoDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;

import java.util.List;

public interface IVarianteProductoNegocio {

    VarianteProductoDTO obtenerVariantePorCodigoBarra(String codigoBarra) throws NegocioException;

    List<VarianteProductoDTO> obtenerVariantesPorProducto(Long productoId) throws NegocioException;

    VarianteProductoDTO buscarPorId(Long id) throws NegocioException;

    void crearVariante(VarianteProductoDTO dto) throws NegocioException;

    void actualizarVariante(VarianteProductoDTO dto) throws NegocioException;

    void eliminarVariante(Long id) throws NegocioException;

    public List<VarianteProductoDTO> buscarVariantesPorNombreProducto(String terminoBusqueda, int pagina, int tamanoPagina) throws NegocioException;

    public long contarVariantesPorNombreProducto(String terminoBusqueda) throws NegocioException;

}
