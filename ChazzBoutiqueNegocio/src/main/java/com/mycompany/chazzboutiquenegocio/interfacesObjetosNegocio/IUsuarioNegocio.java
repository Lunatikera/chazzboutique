/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio;

import com.mycompany.chazzboutiquenegocio.dtos.InicioSesionDTO;
import com.mycompany.chazzboutiquenegocio.dtos.UsuarioDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;

/**
 *
 * @author carli
 */
public interface IUsuarioNegocio {
    public UsuarioDTO iniciarSesion(InicioSesionDTO inicioSesionDTO) throws NegocioException;

    public UsuarioDTO buscarPorId(Long id) throws NegocioException;
}
