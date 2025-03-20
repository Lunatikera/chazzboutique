/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chazzboutiquenegocio.objetosNegocio;

import com.mycompany.chazzboutiquenegocio.dtos.VarianteProductoDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IVarianteProductoNegocio;
import com.mycompany.chazzboutiquepersistencia.dominio.VarianteProducto;
import com.mycompany.chazzboutiquepersistencia.excepciones.PersistenciaException;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.IVarianteProductoDAO;


/**
 *
 * @author carli
 */
public class VarianteProductoNegocio implements IVarianteProductoNegocio{
    IVarianteProductoDAO varianteProductoDAO;

    public VarianteProductoNegocio(IVarianteProductoDAO varianteProductoDAO) {
        this.varianteProductoDAO = varianteProductoDAO;
    }
    
    @Override
    public VarianteProductoDTO obtenerVariantePorCodigoBarra(String codigoBarra) throws NegocioException {
        try {
            VarianteProducto variante = varianteProductoDAO.obtenerPorCodigoBarra(codigoBarra);
            if (variante != null) {
                // Convertir VarianteProducto a VarianteProductoDTO
                return new VarianteProductoDTO(
                    variante.getCodigoBarra(),
                    variante.getStock(),
                    variante.getPrecioCompra(),
                    variante.getTalla(),
                    variante.getPrecioVenta(),
                    variante.getProducto().getId()
                );
            } else {
                return null; // O lanzar una excepción si prefieres
            }
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener la variante de producto por código de barra: " + codigoBarra, e);
        }
    }
}
