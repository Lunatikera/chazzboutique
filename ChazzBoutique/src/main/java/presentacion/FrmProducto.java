package presentacion;

import com.mycompany.chazzboutiquenegocio.dtos.CategoriaDTO;
import com.mycompany.chazzboutiquenegocio.dtos.ProductoDTO;
import com.mycompany.chazzboutiquenegocio.dtos.ProveedorDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.ICategoriaNegocio;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IProductoNegocio;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IProveedorNegocio;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IVarianteProductoNegocio;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
private IVarianteProductoNegocio varianteProductoNegocio;
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
        lblFechaCreacion.setBounds(50, 160, 130, 30);
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

        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.setBounds(490, 280, 100, 30);
        add(btnRegresar);

        btnRegresar.addActionListener(e -> regresarAlInicio());

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Descripción", "Fecha", "Categoría", "Proveedor"}, 0
        );
        tblProductos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblProductos);
        scrollPane.setBounds(50, 330, 900, 300);
        add(scrollPane);

        // Eventos
        btnAgregar.addActionListener(e -> agregarProducto());
        btnEditar.addActionListener(e -> editarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnCancelar.addActionListener(e -> limpiarCampos());

        // Rellenar campos al seleccionar una fila
        tblProductos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                int row = tblProductos.getSelectedRow();
                if (row != -1) {
                    txtNombre.setText(tableModel.getValueAt(row, 1).toString());
                    txtDescripcion.setText(tableModel.getValueAt(row, 2).toString());
                    txtFechaCreacion.setText(tableModel.getValueAt(row, 3).toString());
                }
            }
        });
    }

    private void regresarAlInicio() {
        frmMain.pintarPanelPrincipal(
                new FrmInicial(frmMain, categoriaNegocio, productoNegocio, proveedorNegocio,varianteProductoNegocio)
        );

    }

    private void cargarCategorias() {
        try {
            List<CategoriaDTO> categorias = categoriaNegocio.obtenerCategorias();
            cmbCategoria.removeAllItems();
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
            cmbProveedor.removeAllItems();
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

    private void agregarProducto() {
        try {
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            LocalDate fecha = LocalDate.parse(txtFechaCreacion.getText().trim());
            CategoriaDTO categoria = (CategoriaDTO) cmbCategoria.getSelectedItem();
            ProveedorDTO proveedor = (ProveedorDTO) cmbProveedor.getSelectedItem();

            if (nombre.isEmpty() || categoria == null || proveedor == null) {
                JOptionPane.showMessageDialog(this, "Por favor, llena todos los campos obligatorios.");
                return;
            }

            ProductoDTO nuevoProducto = new ProductoDTO(
                    null, nombre, descripcion, fecha, categoria.getId(), proveedor.getId()
            );

            productoNegocio.crearProducto(nuevoProducto);
            JOptionPane.showMessageDialog(this, "Producto agregado correctamente.");
            limpiarCampos();
            cargarProductos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar producto: " + e.getMessage());
        }
    }

    private void editarProducto() {
        int filaSeleccionada = tblProductos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para editar.");
            return;
        }

        try {
            Long id = (Long) tableModel.getValueAt(filaSeleccionada, 0);
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            LocalDate fecha = LocalDate.parse(txtFechaCreacion.getText().trim());
            CategoriaDTO categoria = (CategoriaDTO) cmbCategoria.getSelectedItem();
            ProveedorDTO proveedor = (ProveedorDTO) cmbProveedor.getSelectedItem();

            if (nombre.isEmpty() || categoria == null || proveedor == null) {
                JOptionPane.showMessageDialog(this, "Completa todos los campos antes de editar.");
                return;
            }

            ProductoDTO productoEditado = new ProductoDTO(
                    id, nombre, descripcion, fecha, categoria.getId(), proveedor.getId()
            );

            productoNegocio.actualizarProducto(productoEditado);
            JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");
            limpiarCampos();
            cargarProductos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al editar producto: " + e.getMessage());
        }
    }

    private void eliminarProducto() {
        int filaSeleccionada = tblProductos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Long id = (Long) tableModel.getValueAt(filaSeleccionada, 0);
            productoNegocio.eliminarProducto(id);
            JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.");
            limpiarCampos();
            cargarProductos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar producto: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtFechaCreacion.setText(LocalDate.now().toString());
        cmbCategoria.setSelectedIndex(0);
        cmbProveedor.setSelectedIndex(0);
        tblProductos.clearSelection();
    }
}
