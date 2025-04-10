/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio;

import com.mycompany.chazzboutiquenegocio.dtos.ProductoDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;
import java.util.List;

/**
 *
 * @author carli
 */
public interface IProductoNegocio {

    public ProductoDTO buscarPorId(Long id) throws NegocioException;

    public List<ProductoDTO> buscarPorNombre(String nombre) throws NegocioException;
    public List<ProductoDTO> obtenerTodosProductos() throws NegocioException ;

}
