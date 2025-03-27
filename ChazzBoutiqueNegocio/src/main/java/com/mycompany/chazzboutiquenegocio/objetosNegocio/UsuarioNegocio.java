/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chazzboutiquenegocio.objetosNegocio;

import com.mycompany.chazzboutiquenegocio.dtos.InicioSesionDTO;
import com.mycompany.chazzboutiquenegocio.dtos.UsuarioDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IUsuarioNegocio;
import com.mycompany.chazzboutiquepersistencia.dominio.Usuario;
import com.mycompany.chazzboutiquepersistencia.excepciones.PersistenciaException;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.IUsuarioDAO;

/**
 *
 * @author carli
 */
public class UsuarioNegocio implements IUsuarioNegocio{
    
    IUsuarioDAO usuarioDAO;

    public UsuarioNegocio(IUsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public UsuarioDTO iniciarSesion(InicioSesionDTO inicioSesionDTO) throws NegocioException {
       if (inicioSesionDTO == null || inicioSesionDTO.getNombreUsuario() == null || inicioSesionDTO.getNombreUsuario().isEmpty() ||
            inicioSesionDTO.getContrasena() == null || inicioSesionDTO.getContrasena().isEmpty()) {
            throw new NegocioException("El nombre de usuario y la contraseña no pueden estar vacíos.");
        }

        try {
            Usuario usuario = usuarioDAO.iniciarSesion(inicioSesionDTO.getNombreUsuario(), inicioSesionDTO.getContrasena());

            if (usuario == null) {
                throw new NegocioException("Credenciales incorrectas.");
            }

            return convertirAUsuarioDTO(usuario);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al iniciar sesión en la base de datos.", e);
        }
    }


    @Override
    public UsuarioDTO buscarPorId(Long id) throws NegocioException {
      if (id == null || id <= 0) {
            throw new NegocioException("El ID debe ser un número válido.");
        }

        try {
            Usuario usuario = usuarioDAO.buscarPorId(id);

            if (usuario == null) {
                throw new NegocioException("Usuario no encontrado.");
            }

            return convertirAUsuarioDTO(usuario);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar usuario en la base de datos.", e);
        }
    }

    private UsuarioDTO convertirAUsuarioDTO(Usuario usuario) {
        return new UsuarioDTO(
            usuario.getId(),
            usuario.getNombreUsuario(),
            usuario.getRol(),
            usuario.getActivo(),
            usuario.getFechaCreacion()
        );
    }
    
}
