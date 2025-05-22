package presentacion;

import com.mycompany.chazzboutiquenegocio.dtos.CategoriaDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.ICategoriaNegocio;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IProductoNegocio;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IProveedorNegocio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmCategoria extends JPanel {

    private FrmMain frmMain;
    private ICategoriaNegocio categoriaNegocio;
    private IProductoNegocio productoNegocio;
    private IProveedorNegocio proveedorNegocio;

    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtImagen;
    private JButton btnConfirmar;
    private JButton btnCancelar;
    private JTable tblCategorias;
    private DefaultTableModel tableModel;

    private CategoriaDTO categoriaSeleccionada;

    public FrmCategoria(FrmMain frmMain, ICategoriaNegocio categoriaNegocio,
                        IProductoNegocio productoNegocio, IProveedorNegocio proveedorNegocio) {
        this.frmMain = frmMain;
        this.categoriaNegocio = categoriaNegocio;
        this.productoNegocio = productoNegocio;
        this.proveedorNegocio = proveedorNegocio;
        initComponents();
        cargarCategorias();
    }

    private void initComponents() {
        setLayout(null);
        setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Gestión de Categorías");
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

        JLabel lblImagen = new JLabel("Imagen:");
        lblImagen.setBounds(50, 160, 100, 30);
        add(lblImagen);
        txtImagen = new JTextField();
        txtImagen.setBounds(150, 160, 300, 30);
        add(txtImagen);

        btnConfirmar = new JButton("Guardar");
        btnConfirmar.setBounds(150, 210, 140, 35);
        btnConfirmar.setBackground(Color.BLACK);
        btnConfirmar.setForeground(Color.WHITE);
        add(btnConfirmar);

        btnCancelar = new JButton("Regresar");
        btnCancelar.setBounds(310, 210, 140, 35);
        btnCancelar.addActionListener(e -> {
            frmMain.pintarPanelPrincipal(new FrmInicial(frmMain, categoriaNegocio, productoNegocio, proveedorNegocio));
        });
        add(btnCancelar);

        btnConfirmar.addActionListener(e -> guardarCategoria());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Descripción", "Imagen", "Editar", "Eliminar"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5;
            }
        };

        tblCategorias = new JTable(tableModel);
        tblCategorias.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tblCategorias);
        scrollPane.setBounds(50, 270, 800, 300);
        add(scrollPane);

        tblCategorias.getColumn("Editar").setCellRenderer(new ButtonRenderer("Editar"));
        tblCategorias.getColumn("Eliminar").setCellRenderer(new ButtonRenderer("Eliminar"));
        tblCategorias.getColumn("Editar").setCellEditor(new ButtonEditor(new JCheckBox(), "Editar", this::editarCategoria));
        tblCategorias.getColumn("Eliminar").setCellEditor(new ButtonEditor(new JCheckBox(), "Eliminar", this::eliminarCategoria));
    }

    private void guardarCategoria() {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String imagen = txtImagen.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre es obligatorio.");
            return;
        }

        try {
            if (categoriaSeleccionada == null) {
                CategoriaDTO dto = new CategoriaDTO(null, nombre, descripcion, imagen);
                categoriaNegocio.crearCategoria(dto);
                JOptionPane.showMessageDialog(this, "Categoría registrada.");
            } else {
                categoriaSeleccionada.setNombreCategoria(nombre);
                categoriaSeleccionada.setDescripcionCategoria(descripcion);
                categoriaSeleccionada.setImagenCategoria(imagen);
                categoriaNegocio.actualizarCategoria(categoriaSeleccionada);
                JOptionPane.showMessageDialog(this, "Categoría actualizada.");
                categoriaSeleccionada = null;
            }

            limpiarFormulario();
            cargarCategorias();

        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void cargarCategorias() {
        tableModel.setRowCount(0);
        try {
            List<CategoriaDTO> categorias = categoriaNegocio.obtenerCategorias();
            for (CategoriaDTO cat : categorias) {
                tableModel.addRow(new Object[]{
                        cat.getId(),
                        cat.getNombreCategoria(),
                        cat.getDescripcionCategoria(),
                        cat.getImagenCategoria(),
                        "Editar",
                        "Eliminar"
                });
            }
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar categorías.");
        }
    }

    private void editarCategoria(int row) {
        Long id = (Long) tableModel.getValueAt(row, 0);
        try {
            for (CategoriaDTO cat : categoriaNegocio.obtenerCategorias()) {
                if (cat.getId().equals(id)) {
                    this.categoriaSeleccionada = cat;
                    txtNombre.setText(cat.getNombreCategoria());
                    txtDescripcion.setText(cat.getDescripcionCategoria());
                    txtImagen.setText(cat.getImagenCategoria());
                    break;
                }
            }
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar categoría.");
        }
    }

    private void eliminarCategoria(int row) {
        Long id = (Long) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar esta categoría?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                categoriaNegocio.eliminarCategoria(id);
                cargarCategorias();
            } catch (NegocioException e) {
                JOptionPane.showMessageDialog(this, "No se puede eliminar la categoría: elimine primero los productos relacionados.");
            }
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtImagen.setText("");
    }
}
