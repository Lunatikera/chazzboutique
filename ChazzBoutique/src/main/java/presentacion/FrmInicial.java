package presentacion;

import utils.ProductoComboItem;
import com.mycompany.chazzboutiquenegocio.dtos.ProductoDTO;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.*;

import javax.swing.*;
import java.awt.*;

public class FrmInicial extends JPanel {

    private FrmMain frmMain;
    private ICategoriaNegocio categoriaNegocio;
    private IProductoNegocio productoNegocio;
    private IProveedorNegocio proveedorNegocio;
    private IVarianteProductoNegocio varianteProductoNegocio;

    public FrmInicial(FrmMain frmMain,
            ICategoriaNegocio categoriaNegocio,
            IProductoNegocio productoNegocio,
            IProveedorNegocio proveedorNegocio,
            IVarianteProductoNegocio varianteProductoNegocio) {
        this.frmMain = frmMain;
        this.categoriaNegocio = categoriaNegocio;
        this.productoNegocio = productoNegocio;
        this.proveedorNegocio = proveedorNegocio;
        this.varianteProductoNegocio = varianteProductoNegocio;
        initComponents();
    }

    private void initComponents() {
        setLayout(null);
        setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Inicio");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setBounds(350, 30, 300, 40);
        add(lblTitulo);

        JButton btnVenta = new JButton("Punto de Venta");
        btnVenta.setBounds(300, 100, 200, 40);
        btnVenta.setFocusPainted(false);
        btnVenta.addActionListener(e -> {
            frmMain.pintarPanelPrincipal(new PanelVenta(frmMain));
        });
        add(btnVenta);

        JButton btnRegistrarProducto = new JButton("Registrar Producto");
        btnRegistrarProducto.setBounds(300, 160, 200, 40);
        btnRegistrarProducto.setFocusPainted(false);
        btnRegistrarProducto.addActionListener(e -> {
            frmMain.pintarPanelPrincipal(new FrmProducto(frmMain, productoNegocio, categoriaNegocio, proveedorNegocio));
        });
        add(btnRegistrarProducto);

        JButton btnRegistrarCategoria = new JButton("Registrar Categoría");
        btnRegistrarCategoria.setBounds(300, 220, 200, 40);
        btnRegistrarCategoria.setFocusPainted(false);
        btnRegistrarCategoria.addActionListener(e -> {
            frmMain.pintarPanelPrincipal(new FrmCategoria(frmMain, categoriaNegocio, productoNegocio, proveedorNegocio));
        });
        add(btnRegistrarCategoria);

        // ComboBox para seleccionar producto
        JComboBox<ProductoComboItem> comboProductos = new JComboBox<>();
        comboProductos.setBounds(300, 280, 200, 30);
        add(comboProductos);

        // Cargar productos en el combo
        try {
            for (ProductoDTO producto : productoNegocio.obtenerTodosProductos()) {
                comboProductos.addItem(new ProductoComboItem(producto.getId(), producto.getNombreProducto()));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage());
        }

        // Botón para abrir gestión de variantes
        JButton btnVarianteProducto = new JButton("Gestionar Variantes");
        btnVarianteProducto.setBounds(300, 320, 200, 40);
        btnVarianteProducto.setFocusPainted(false);
        btnVarianteProducto.addActionListener(e -> {
            ProductoComboItem itemSeleccionado = (ProductoComboItem) comboProductos.getSelectedItem();
            if (itemSeleccionado != null) {
                Long productoId = itemSeleccionado.getId();
                FrmVarianteProducto frm = new FrmVarianteProducto(frmMain, varianteProductoNegocio, productoId);
                frmMain.pintarPanelPrincipal(frm);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un producto.");
            }
        });
        add(btnVarianteProducto);
    }
}
