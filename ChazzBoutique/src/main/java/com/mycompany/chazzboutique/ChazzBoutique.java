package com.mycompany.chazzboutique;

import com.mycompany.chazzboutiquenegocio.dtos.InicioSesionDTO;
import com.mycompany.chazzboutiquenegocio.dtos.UsuarioDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.*;
import com.mycompany.chazzboutiquenegocio.objetosNegocio.*;
import com.mycompany.chazzboutiquepersistencia.conexion.ConexionBD;
import com.mycompany.chazzboutiquepersistencia.conexion.IConexionBD;
import com.mycompany.chazzboutiquepersistencia.daos.*;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.*;
import presentacion.FrmMain;

import java.util.logging.Level;
import java.util.logging.Logger;
import presentacion.FrmPrincipal;

public class ChazzBoutique {

    public static void main(String[] args) {
        IConexionBD conexionBD = new ConexionBD();

        // DAOs
        IUsuarioDAO usuarioDAO = new UsuarioDAO(conexionBD);
        IVentaDAO ventaDAO = new VentaDAO(conexionBD);
        IVarianteProductoDAO varianteProductoDAO = new VarianteProductoDAO(conexionBD);
        IProductoDAO productoDAO = new ProductoDAO(conexionBD);
        IDetalleVentaDAO detalleVentaDAO = new DetalleVentaDAO(conexionBD);
        ICategoriaDAO categoriaDAO = new CategoriaDAO(conexionBD);
        IProveedorDAO proveedorDAO = new ProveedorDAO(conexionBD); 

        // Negocios
        IUsuarioNegocio usuarioNegocio = new UsuarioNegocio(usuarioDAO);
        IVentaNegocio ventaNegocio = new VentaNegocio(ventaDAO, detalleVentaDAO, varianteProductoDAO, usuarioDAO);
        IVarianteProductoNegocio varianteProductoNegocio = new VarianteProductoNegocio(varianteProductoDAO, productoDAO);
        IProductoNegocio productoNegocio = new ProductoNegocio(productoDAO, categoriaDAO, proveedorDAO);
        ICategoriaNegocio categoriaNegocio = new CategoriaNegocio(categoriaDAO);
        IProveedorNegocio proveedorNegocio = new ProveedorNegocio(proveedorDAO); 

        try {
           
            UsuarioDTO usuarioRegistrado = usuarioNegocio.iniciarSesion(new InicioSesionDTO("Yalam", "12345"));

            // Creación de ventana principal
            FrmPrincipal frmPrincipal = new FrmPrincipal(
                    usuarioNegocio, 
                    ventaNegocio, 
                    varianteProductoNegocio, 
                    productoNegocio, 
                    categoriaNegocio, 

                    proveedorNegocio, 
                    usuarioRegistrado
            );

            frmPrincipal.setVisible(true);
        } catch (NegocioException ex) {
            Logger.getLogger(ChazzBoutique.class.getName()).log(Level.SEVERE, "Error al iniciar sesión", ex);
            System.out.println("Error al iniciar sesión: " + ex.getMessage());
        }
    }
}
