package presentacion;

import com.mycompany.chazzboutiquenegocio.dtos.VarianteProductoDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IVarianteProductoNegocio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class FrmVarianteProducto extends JPanel {

    private FrmMain frmMain;
    private IVarianteProductoNegocio varianteProductoNegocio;
    private Long productoId;

    private JTable tblVariantes;
    private DefaultTableModel modeloTabla;

    private JTextField txtCodigoBarra, txtColor, txtTalla, txtStock, txtPrecioCompra, txtPrecioVenta;
    private JButton btnGuardar, btnEliminar, btnActualizar, btnRegresar;

    private VarianteProductoDTO varianteSeleccionada = null;

    public FrmVarianteProducto(FrmMain frmMain, IVarianteProductoNegocio varianteProductoNegocio, Long productoId) {
        this.frmMain = frmMain;
        this.varianteProductoNegocio = varianteProductoNegocio;
        this.productoId = productoId;
        initComponents();
        cargarVariantes();
    }

    private void initComponents() {
        setLayout(null);
        setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Gestión de Variantes de Producto");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setBounds(50, 20, 500, 40);
        add(lblTitulo);

        JLabel lblCodigo = new JLabel("Código de Barra:");
        lblCodigo.setBounds(50, 80, 150, 25);
        add(lblCodigo);
        txtCodigoBarra = new JTextField();
        txtCodigoBarra.setBounds(200, 80, 200, 25);
        add(txtCodigoBarra);

        JLabel lblColor = new JLabel("Color:");
        lblColor.setBounds(50, 115, 150, 25);
        add(lblColor);
        txtColor = new JTextField();
        txtColor.setBounds(200, 115, 200, 25);
        add(txtColor);

        JLabel lblTalla = new JLabel("Talla:");
        lblTalla.setBounds(50, 150, 150, 25);
        add(lblTalla);
        txtTalla = new JTextField();
        txtTalla.setBounds(200, 150, 200, 25);
        add(txtTalla);

        JLabel lblStock = new JLabel("Stock:");
        lblStock.setBounds(50, 185, 150, 25);
        add(lblStock);
        txtStock = new JTextField();
        txtStock.setBounds(200, 185, 200, 25);
        add(txtStock);

        JLabel lblCompra = new JLabel("Precio Compra:");
        lblCompra.setBounds(50, 220, 150, 25);
        add(lblCompra);
        txtPrecioCompra = new JTextField();
        txtPrecioCompra.setBounds(200, 220, 200, 25);
        add(txtPrecioCompra);

        JLabel lblVenta = new JLabel("Precio Venta:");
        lblVenta.setBounds(50, 255, 150, 25);
        add(lblVenta);
        txtPrecioVenta = new JTextField();
        txtPrecioVenta.setBounds(200, 255, 200, 25);
        add(txtPrecioVenta);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(420, 80, 120, 30);
        btnGuardar.addActionListener(e -> guardarVariante());
        add(btnGuardar);

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBounds(420, 120, 120, 30);
        btnActualizar.addActionListener(e -> actualizarVariante());
        add(btnActualizar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(420, 160, 120, 30);
        btnEliminar.addActionListener(e -> eliminarVariante());
        add(btnEliminar);

        btnRegresar = new JButton("Regresar");
        btnRegresar.setBounds(420, 200, 120, 30);
        btnRegresar.addActionListener(e -> frmMain.pintarPanelPrincipal(new FrmInicial(frmMain,
                frmMain.categoriaNegocio, frmMain.productoNegocio, frmMain.proveedorNegocio, varianteProductoNegocio)));
        add(btnRegresar);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Código", "Color", "Talla", "Stock", "Compra", "Venta"}, 0);
        tblVariantes = new JTable(modeloTabla);
        tblVariantes.setRowHeight(25);
        tblVariantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblVariantes.getSelectionModel().addListSelectionListener(e -> cargarSeleccionado());

        JScrollPane scrollPane = new JScrollPane(tblVariantes);
        scrollPane.setBounds(50, 320, 800, 300);
        add(scrollPane);
    }

    private void cargarVariantes() {
        try {
            modeloTabla.setRowCount(0);
            List<VarianteProductoDTO> variantes = varianteProductoNegocio.obtenerVariantesPorProducto(productoId);
            for (VarianteProductoDTO v : variantes) {
                modeloTabla.addRow(new Object[]{
                        v.getId(), v.getCodigoBarra(), v.getColor(), v.getTalla(),
                        v.getStock(), v.getPrecioCompra(), v.getPrecioVenta()
                });
            }
        } catch (NegocioException e) {
            mostrarError("Error al cargar variantes: " + e.getMessage());
        }
    }

    private void cargarSeleccionado() {
        int fila = tblVariantes.getSelectedRow();
        if (fila >= 0) {
            Long id = (Long) modeloTabla.getValueAt(fila, 0);
            try {
                varianteSeleccionada = varianteProductoNegocio.buscarPorId(id);
                txtCodigoBarra.setText(varianteSeleccionada.getCodigoBarra());
                txtColor.setText(varianteSeleccionada.getColor());
                txtTalla.setText(varianteSeleccionada.getTalla());
                txtStock.setText(String.valueOf(varianteSeleccionada.getStock()));
                txtPrecioCompra.setText(varianteSeleccionada.getPrecioCompra().toString());
                txtPrecioVenta.setText(varianteSeleccionada.getPrecioVenta().toString());
            } catch (NegocioException e) {
                mostrarError("Error al cargar variante seleccionada");
            }
        }
    }

    private void guardarVariante() {
        try {
            VarianteProductoDTO dto = new VarianteProductoDTO(
                    txtCodigoBarra.getText(),
                    Integer.parseInt(txtStock.getText()),
                    new BigDecimal(txtPrecioCompra.getText()),
                    txtTalla.getText(),
                    txtColor.getText(),
                    new BigDecimal(txtPrecioVenta.getText()),
                    productoId
            );
            varianteProductoNegocio.crearVariante(dto);
            limpiarCampos();
            cargarVariantes();
        } catch (Exception ex) {
            mostrarError("Error al guardar: " + ex.getMessage());
        }
    }

    private void actualizarVariante() {
        if (varianteSeleccionada == null) {
            mostrarError("Debe seleccionar una variante");
            return;
        }

        try {
            varianteSeleccionada.setCodigoBarra(txtCodigoBarra.getText());
            varianteSeleccionada.setColor(txtColor.getText());
            varianteSeleccionada.setTalla(txtTalla.getText());
            varianteSeleccionada.setStock(Integer.parseInt(txtStock.getText()));
            varianteSeleccionada.setPrecioCompra(new BigDecimal(txtPrecioCompra.getText()));
            varianteSeleccionada.setPrecioVenta(new BigDecimal(txtPrecioVenta.getText()));

            varianteProductoNegocio.actualizarVariante(varianteSeleccionada);
            limpiarCampos();
            cargarVariantes();
        } catch (Exception ex) {
            mostrarError("Error al actualizar: " + ex.getMessage());
        }
    }

    private void eliminarVariante() {
        if (varianteSeleccionada == null) {
            mostrarError("Debe seleccionar una variante");
            return;
        }

        try {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar variante?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                varianteProductoNegocio.eliminarVariante(varianteSeleccionada.getId());
                limpiarCampos();
                cargarVariantes();
            }
        } catch (NegocioException e) {
            mostrarError("Error al eliminar: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        txtCodigoBarra.setText("");
        txtColor.setText("");
        txtTalla.setText("");
        txtStock.setText("");
        txtPrecioCompra.setText("");
        txtPrecioVenta.setText("");
        varianteSeleccionada = null;
        tblVariantes.clearSelection();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
