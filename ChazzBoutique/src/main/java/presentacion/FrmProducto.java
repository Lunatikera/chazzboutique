package presentacion;

import com.mycompany.chazzboutiquenegocio.dtos.CategoriaDTO;
import com.mycompany.chazzboutiquenegocio.dtos.ProductoDTO;
import com.mycompany.chazzboutiquenegocio.dtos.ProveedorDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.ICategoriaNegocio;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IProductoNegocio;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IProveedorNegocio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class FrmProducto extends JPanel {

    private FrmMain frmMain;
    private IProductoNegocio productoNegocio;
    private ICategoriaNegocio categoriaNegocio;
    private IProveedorNegocio proveedorNegocio;

    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtFechaCreacion;
    private JComboBox<CategoriaDTO> cmbCategoria;
    private JComboBox<ProveedorDTO> cmbProveedor;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnCancelar;
    private JTable tblProductos;
    private DefaultTableModel tableModel;

    public FrmProducto(FrmMain frmMain, IProductoNegocio productoNegocio,
                       ICategoriaNegocio categoriaNegocio, IProveedorNegocio proveedorNegocio) {
        this.frmMain = frmMain;
        this.productoNegocio = productoNegocio;
        this.categoriaNegocio = categoriaNegocio;
        this.proveedorNegocio = proveedorNegocio;
        initComponents();
        cargarCategorias();
        cargarProveedores();
        cargarProductos();
    }

    private void initComponents() {
        setLayout(null);
        setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Gestión de Productos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setBounds(50, 20, 400, 40);
        add(lblTitulo);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(50, 80, 100, 30);
        add(lblNombre);
        txtNombre = new JTextField();
        txtNombre.setBounds(150, 80, 300, 30);
        add(txtNombre);

        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setBounds(50, 120, 100, 30);
        add(lblDescripcion);
        txtDescripcion = new JTextField();
        txtDescripcion.setBounds(150, 120, 300, 30);
        add(txtDescripcion);

        JLabel lblFechaCreacion = new JLabel("Fecha de Creación:");
        lblFechaCreacion.setBounds(50, 160, 120, 30);
        add(lblFechaCreacion);
        txtFechaCreacion = new JTextField(LocalDate.now().toString());
        txtFechaCreacion.setBounds(180, 160, 270, 30);
        add(txtFechaCreacion);

        JLabel lblCategoria = new JLabel("Categoría:");
        lblCategoria.setBounds(50, 200, 100, 30);
        add(lblCategoria);
        cmbCategoria = new JComboBox<>();
        cmbCategoria.setBounds(150, 200, 300, 30);
        add(cmbCategoria);

        JLabel lblProveedor = new JLabel("Proveedor:");
        lblProveedor.setBounds(50, 240, 100, 30);
        add(lblProveedor);
        cmbProveedor = new JComboBox<>();
        cmbProveedor.setBounds(150, 240, 300, 30);
        add(cmbProveedor);

        btnAgregar = new JButton("Agregar");
        btnAgregar.setBounds(50, 280, 100, 30);
        add(btnAgregar);

        btnEditar = new JButton("Editar");
        btnEditar.setBounds(160, 280, 100, 30);
        add(btnEditar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(270, 280, 100, 30);
        add(btnEliminar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(380, 280, 100, 30);
        add(btnCancelar);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Descripción", "Fecha", "Categoría", "Proveedor"}, 0);
        tblProductos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblProductos);
        scrollPane.setBounds(50, 320, 700, 200);
        add(scrollPane);

        // Agregar listeners a los botones (implementación omitida por brevedad)
    }

    private void cargarCategorias() {
        try {
            List<CategoriaDTO> categorias = categoriaNegocio.obtenerCategorias();
            for (CategoriaDTO categoria : categorias) {
                cmbCategoria.addItem(categoria);
            }
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar categorías: " + e.getMessage());
        }
    }

    private void cargarProveedores() {
        try {
            List<ProveedorDTO> proveedores = proveedorNegocio.obtenerProveedores();
            for (ProveedorDTO proveedor : proveedores) {
                cmbProveedor.addItem(proveedor);
            }
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar proveedores: " + e.getMessage());
        }
    }

    private void cargarProductos() {
        try {
            List<ProductoDTO> productos = productoNegocio.obtenerTodosProductos();
            tableModel.setRowCount(0);
            for (ProductoDTO producto : productos) {
                tableModel.addRow(new Object[]{
                        producto.getId(),
                        producto.getNombreProducto(),
                        producto.getDescripcionProducto(),
                        producto.getFechaCreacion(),
                        producto.getCategoriaId(),
                        producto.getProveedorId()
                });
            }
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage());
        }
    }
}

