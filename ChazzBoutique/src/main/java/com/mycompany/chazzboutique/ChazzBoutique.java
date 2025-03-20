/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.chazzboutique;

import com.mycompany.chazzboutiquenegocio.dtos.InicioSesionDTO;
import com.mycompany.chazzboutiquenegocio.dtos.UsuarioDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IProductoNegocio;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IUsuarioNegocio;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IVarianteProductoNegocio;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IVentaNegocio;
import com.mycompany.chazzboutiquenegocio.objetosNegocio.ProductoNegocio;
import com.mycompany.chazzboutiquenegocio.objetosNegocio.UsuarioNegocio;
import com.mycompany.chazzboutiquenegocio.objetosNegocio.VarianteProductoNegocio;
import com.mycompany.chazzboutiquenegocio.objetosNegocio.VentaNegocio;
import com.mycompany.chazzboutiquepersistencia.conexion.ConexionBD;
import com.mycompany.chazzboutiquepersistencia.conexion.IConexionBD;
import com.mycompany.chazzboutiquepersistencia.daos.ProductoDAO;
import com.mycompany.chazzboutiquepersistencia.daos.UsuarioDAO;
import com.mycompany.chazzboutiquepersistencia.daos.VarianteProductoDAO;
import com.mycompany.chazzboutiquepersistencia.daos.VentaDAO;
import com.mycompany.chazzboutiquepersistencia.dominio.VarianteProducto;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.IProductoDAO;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.IUsuarioDAO;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.IVarianteProductoDAO;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.IVentaDAO;
import java.util.logging.Level;
import java.util.logging.Logger;
import presentacion.FrmMain;

/**
 *
 * @author carli
 */
public class ChazzBoutique {

    public static void main(String[] args) {
        IConexionBD conexionBD = new ConexionBD();
        IUsuarioDAO usuarioDAO = new UsuarioDAO(conexionBD);
        IVentaDAO ventaDAO = new VentaDAO(conexionBD);
        IVarianteProductoDAO varianteProductoDAO = new VarianteProductoDAO(conexionBD);
        IProductoDAO productoDAO = new ProductoDAO(conexionBD);
        
        IUsuarioNegocio usuarioNegocio = new UsuarioNegocio(usuarioDAO);
        IVentaNegocio ventaNegocio = new VentaNegocio(ventaDAO);
        IVarianteProductoNegocio varianteProductoNegocio = new VarianteProductoNegocio(varianteProductoDAO);
        IProductoNegocio productoNegocio= new ProductoNegocio(productoDAO);

        UsuarioDTO usuarioRegistrado;
        try {
            usuarioRegistrado = usuarioNegocio.iniciarSesion(new InicioSesionDTO("Yalam", "12345"));
            FrmMain frmMain = new FrmMain(usuarioNegocio, ventaNegocio, varianteProductoNegocio,productoNegocio, usuarioRegistrado);
            frmMain.setVisible(true);
        } catch (NegocioException ex) {
            Logger.getLogger(ChazzBoutique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
