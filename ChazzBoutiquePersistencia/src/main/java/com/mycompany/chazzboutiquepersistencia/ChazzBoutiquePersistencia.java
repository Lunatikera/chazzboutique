/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.chazzboutiquepersistencia;

import com.mycompany.chazzboutiquepersistencia.conexion.ConexionBD;
import com.mycompany.chazzboutiquepersistencia.conexion.IConexionBD;
import com.mycompany.chazzboutiquepersistencia.daos.CategoriaDAO;
import com.mycompany.chazzboutiquepersistencia.daos.ProductoDAO;
import com.mycompany.chazzboutiquepersistencia.dominio.Categoria;
import com.mycompany.chazzboutiquepersistencia.dominio.Producto;
import com.mycompany.chazzboutiquepersistencia.dominio.Proveedor;
import com.mycompany.chazzboutiquepersistencia.dominio.Usuario;
import com.mycompany.chazzboutiquepersistencia.excepciones.PersistenciaException;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.ICategoriaDAO;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.IProductoDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author carli
 */
public class ChazzBoutiquePersistencia {

    public static void main(String[] args) throws PersistenciaException {
        // Crear una fábrica de administradores de entidades
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ChazzBoutique");
        EntityManager em = emf.createEntityManager();
        IConexionBD conexionBD = new ConexionBD();
        ICategoriaDAO categoriaDAO = new CategoriaDAO(conexionBD);
        

        Categoria categoria = new Categoria();
        categoria.setNombreCategoria("prueba");
        categoria.setDescripcionCategoria("plek");
        categoria.setImagenCategoria("sis");
        categoriaDAO.crearCategoria(categoria);
        

    }
}
