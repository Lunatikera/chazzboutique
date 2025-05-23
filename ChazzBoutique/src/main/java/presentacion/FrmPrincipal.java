/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package presentacion;

import com.mycompany.chazzboutiquenegocio.dtos.UsuarioDTO;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.ICategoriaNegocio;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IProductoNegocio;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IProveedorNegocio;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IUsuarioNegocio;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IVarianteProductoNegocio;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IVentaNegocio;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author carli
 */
public class FrmPrincipal extends javax.swing.JFrame {

    public IUsuarioNegocio usuarioNegocio;
    public IVentaNegocio ventaNegocio;
    public IVarianteProductoNegocio varianteProductoNegocio;
    public IProductoNegocio productoNegocio;
    public ICategoriaNegocio categoriaNegocio;
    public IProveedorNegocio proveedorNegocio;
    private UsuarioDTO usuarioRegistrado;

    public FrmPrincipal(IUsuarioNegocio usuarioNegocio, IVentaNegocio ventaNegocio,
            IVarianteProductoNegocio varianteProductoNegocio,
            IProductoNegocio productoNegocio, ICategoriaNegocio categoriaNegocio,
            IProveedorNegocio proveedorNegocio, UsuarioDTO usuarioRegistrado) {
        initComponents();
        this.setTitle("ChazzBoutique");
        this.setLocationRelativeTo(null);

        this.usuarioNegocio = usuarioNegocio;
        this.ventaNegocio = ventaNegocio;
        this.varianteProductoNegocio = varianteProductoNegocio;
        this.productoNegocio = productoNegocio;
        this.categoriaNegocio = categoriaNegocio;
        this.proveedorNegocio = proveedorNegocio;
        this.usuarioRegistrado = usuarioRegistrado;

        this.pintarPanelPrincipal(new PanelHome(this));
    }

    public void pintarPanelPrincipal(JPanel panel) {
        jScrollPane1.setViewportView(panel);
        panel.setPreferredSize(new Dimension(0, panel.getPreferredSize().height));
        jScrollPane1.revalidate();
        jScrollPane1.repaint();
    }

    public JScrollPane getPanelPrincipal() {
        return jScrollPane1;
    }

    public IVentaNegocio getVentaNegocio() {
        return ventaNegocio;
    }

    public UsuarioDTO getUsuarioRegistrado() {
        return usuarioRegistrado;
    }

    public IUsuarioNegocio getUsuarioNegocio() {
        return usuarioNegocio;
    }

    public IVarianteProductoNegocio getVarianteProductoNegocio() {
        return varianteProductoNegocio;
    }

    public IProductoNegocio getProductoNegocio() {
        return productoNegocio;
    }

    public IProveedorNegocio getProveedorNegocio() {
        return proveedorNegocio;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        botonMenuHome = new utils.BotonMenu();
        jPanel5 = new javax.swing.JPanel();
        botonVenta = new utils.BotonMenu();
        jPanel6 = new javax.swing.JPanel();
        btnCategorias = new utils.BotonMenu();
        jPanel7 = new javax.swing.JPanel();
        botonMenuProducto = new utils.BotonMenu();
        jPanel8 = new javax.swing.JPanel();
        botonReportes = new utils.BotonMenu();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(176, 50, 53));
        jPanel1.setPreferredSize(new java.awt.Dimension(277, 1080));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/chazzLogoBlack.png"))); // NOI18N

        jPanel3.setBackground(new java.awt.Color(176, 50, 53));
        jPanel3.setPreferredSize(new java.awt.Dimension(277, 782));

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setPreferredSize(new java.awt.Dimension(300, 84));

        botonMenuHome.setForeground(new java.awt.Color(255, 255, 255));
        botonMenuHome.setText("Home");
        botonMenuHome.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        botonMenuHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonMenuHomeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(botonMenuHome, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(botonMenuHome, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel4);

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));
        jPanel5.setPreferredSize(new java.awt.Dimension(300, 84));

        botonVenta.setForeground(new java.awt.Color(255, 255, 255));
        botonVenta.setText("Venta");
        botonVenta.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        botonVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVentaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(botonVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(botonVenta, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel5);

        jPanel6.setBackground(new java.awt.Color(0, 0, 0));
        jPanel6.setPreferredSize(new java.awt.Dimension(300, 84));

        btnCategorias.setForeground(new java.awt.Color(255, 255, 255));
        btnCategorias.setText("Categorias");
        btnCategorias.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnCategorias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCategoriasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(btnCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCategorias, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel6);

        jPanel7.setBackground(new java.awt.Color(0, 0, 0));
        jPanel7.setPreferredSize(new java.awt.Dimension(300, 84));

        botonMenuProducto.setForeground(new java.awt.Color(255, 255, 255));
        botonMenuProducto.setText("Productos");
        botonMenuProducto.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        botonMenuProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonMenuProductoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(botonMenuProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(botonMenuProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel7);

        jPanel8.setBackground(new java.awt.Color(0, 0, 0));
        jPanel8.setPreferredSize(new java.awt.Dimension(300, 84));

        botonReportes.setForeground(new java.awt.Color(255, 255, 255));
        botonReportes.setText("Reportes");
        botonReportes.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        botonReportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonReportesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(botonReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(botonReportes, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel8);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(26, 26, 26)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 648, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(238, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.LINE_START);
        jPanel1.getAccessibleContext().setAccessibleName("2");

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(1701, 1078));
        jPanel2.add(jScrollPane1);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVentaActionPerformed
        this.pintarPanelPrincipal(new PanelVenta(this));
    }//GEN-LAST:event_botonVentaActionPerformed

    private void botonMenuProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonMenuProductoActionPerformed
        PanelEscogerAnadir panel = new PanelEscogerAnadir(this);
        this.pintarPanelPrincipal(panel);
    }//GEN-LAST:event_botonMenuProductoActionPerformed

    private void btnCategoriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCategoriasActionPerformed
        PnlAnadirCategoria panel = new PnlAnadirCategoria(this);
        this.pintarPanelPrincipal(panel);
       
    }//GEN-LAST:event_btnCategoriasActionPerformed

    private void botonMenuHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonMenuHomeActionPerformed
        this.pintarPanelPrincipal(new PanelHome(this));
        
        
    }//GEN-LAST:event_botonMenuHomeActionPerformed

    private void botonReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonReportesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonReportesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private utils.BotonMenu botonMenuHome;
    private utils.BotonMenu botonMenuProducto;
    private utils.BotonMenu botonReportes;
    private utils.BotonMenu botonVenta;
    private utils.BotonMenu btnCategorias;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
