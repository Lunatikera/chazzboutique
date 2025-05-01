/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio;

import com.mycompany.chazzboutiquenegocio.dtos.VarianteProductoDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;
import java.util.List;

/**
 *
 * @author carli
 */
public interface IVarianteProductoNegocio {

    public VarianteProductoDTO obtenerVariantePorCodigoBarra(String codigoBarra) throws NegocioException;

    public List<VarianteProductoDTO> obtenerVariantesPorProducto(Long productoId) throws NegocioException;

}
