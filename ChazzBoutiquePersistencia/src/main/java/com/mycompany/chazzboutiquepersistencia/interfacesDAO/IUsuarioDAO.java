/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.chazzboutiquepersistencia.interfacesDAO;

import com.mycompany.chazzboutiquepersistencia.dominio.Usuario;
import com.mycompany.chazzboutiquepersistencia.excepciones.PersistenciaException;

/**
 *
 * @author carli
 */
public interface IUsuarioDAO {

    public Usuario iniciarSesion(String nombreUsuario, String contrasena) throws PersistenciaException;

    public Usuario buscarPorId(Long id) throws PersistenciaException;

}
