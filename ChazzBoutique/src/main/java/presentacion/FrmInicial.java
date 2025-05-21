package presentacion;

import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.ICategoriaNegocio;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FrmInicial extends JPanel {

    private FrmMain frmMain;
    private ICategoriaNegocio categoriaNegocio;

    public FrmInicial(FrmMain frmMain, ICategoriaNegocio categoriaNegocio) {
        this.frmMain = frmMain;
        this.categoriaNegocio = categoriaNegocio;
        initComponents();
    }

    private void initComponents() {
        setLayout(null);
        setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Inicio");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setBounds(350, 30, 300, 40);
        add(lblTitulo);

        // Botón Punto de Venta
        JButton btnVenta = new JButton("Punto de Venta");
        btnVenta.setBounds(300, 100, 200, 40);
        btnVenta.setFocusPainted(false);
        btnVenta.addActionListener(e -> {
            frmMain.pintarPanelPrincipal(new PanelVenta(frmMain));
        });
        add(btnVenta);

        // Botón Registrar Producto
        JButton btnRegistrarProducto = new JButton("Registrar Producto");
        btnRegistrarProducto.setBounds(300, 160, 200, 40);
        btnRegistrarProducto.setFocusPainted(false);
        btnRegistrarProducto.addActionListener(e -> {
            frmMain.pintarPanelPrincipal(new FrmAñadirProducto());
        });
        add(btnRegistrarProducto);

        // Botón Registrar Categoría
        JButton btnRegistrarCategoria = new JButton("Registrar Categoría");
        btnRegistrarCategoria.setBounds(300, 220, 200, 40);
        btnRegistrarCategoria.setFocusPainted(false);
        btnRegistrarCategoria.addActionListener(e -> {
            frmMain.pintarPanelPrincipal(new FrmCategoria(frmMain, categoriaNegocio));
        });
        add(btnRegistrarCategoria);
    }
}
