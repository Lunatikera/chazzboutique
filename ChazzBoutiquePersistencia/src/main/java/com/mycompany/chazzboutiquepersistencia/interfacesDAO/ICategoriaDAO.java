/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.chazzboutiquepersistencia.interfacesDAO;

import com.mycompany.chazzboutiquepersistencia.dominio.Categoria;
import com.mycompany.chazzboutiquepersistencia.excepciones.PersistenciaException;
import java.util.List;

/**
 *
 * @author carli
 */
public interface ICategoriaDAO {

    public List<Categoria> obtenerTodasCategorias() throws PersistenciaException;

    public Categoria actualizarCategoria(Categoria categoria) throws PersistenciaException;

    public void eliminarCategoria(Long id) throws PersistenciaException;

    public void crearCategoria(Categoria categoria) throws PersistenciaException;

    public Categoria buscarPorId(Long id) throws PersistenciaException;

}
