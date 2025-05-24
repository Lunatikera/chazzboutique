package presentacion;

import com.mycompany.chazzboutiquenegocio.dtos.CategoriaDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import utils.ButtonEditor;
import utils.ButtonRenderer;

/**
 *
 * @author
 */
public class PnlAnadirCategoria extends javax.swing.JPanel {

    private FrmPrincipal frmPrincipal;
    private DefaultTableModel tableModel;
    private CategoriaDTO categoriaSeleccionada;
    private String imagen;

    public PnlAnadirCategoria(FrmPrincipal frmPrincipal) {
        initComponents();
        this.frmPrincipal = frmPrincipal;
        this.setSize(new Dimension(1701, 1080));

        lblImagen.setPreferredSize(new Dimension(480, 600));

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Descripción", "Imagen", "Editar", "Eliminar"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5;
            }
        };
        tblCategorias.setModel(tableModel);

        tblCategorias.getColumn("Editar").setCellRenderer(new ButtonRenderer("Editar"));
        tblCategorias.getColumn("Eliminar").setCellRenderer(new ButtonRenderer("Eliminar"));
        tblCategorias.getColumn("Editar").setCellEditor(new ButtonEditor(new JCheckBox(), "Editar", this::editarCategoria));
        tblCategorias.getColumn("Eliminar").setCellEditor(new ButtonEditor(new JCheckBox(), "Eliminar", this::eliminarCategoria));
        cargarCategorias();
    }

    private void guardarCategoria() {
        String nombre = txtNombreCategoria.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String imagen = this.imagen != null ? this.imagen : " ";

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre es obligatorio.");
            return;
        }

        try {
            if (categoriaSeleccionada == null) {
                CategoriaDTO dto = new CategoriaDTO(null, nombre, descripcion, imagen);
                frmPrincipal.categoriaNegocio.crearCategoria(dto);
                JOptionPane.showMessageDialog(this, "Categoría registrada.");
            } else {
                categoriaSeleccionada.setNombreCategoria(nombre);
                categoriaSeleccionada.setDescripcionCategoria(descripcion);
                categoriaSeleccionada.setImagenCategoria(imagen);
                frmPrincipal.categoriaNegocio.actualizarCategoria(categoriaSeleccionada);
                JOptionPane.showMessageDialog(this, "Categoría actualizada.");
                categoriaSeleccionada = null;
            }

            limpiarFormulario();
            cargarCategorias();

        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

  private void seleccionarImagen() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Seleccionar Imagen");
    fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png", "gif"));

    int resultado = fileChooser.showOpenDialog(this);
    if (resultado == JFileChooser.APPROVE_OPTION) {
        File imagenSeleccionada = fileChooser.getSelectedFile();

        try {
            String nombreArchivo = imagenSeleccionada.getName();
            File carpetaDestino = new File("src/main/resources/images/");
            if (!carpetaDestino.exists()) {
                carpetaDestino.mkdirs();
            }

            File destino = new File(carpetaDestino, nombreArchivo);
            Files.copy(imagenSeleccionada.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

            imagen = "/images/" + nombreArchivo; // Ruta relativa para guardar en el DTO
            ImageIcon icon = new ImageIcon(destino.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(lblImagen.getWidth(), lblImagen.getHeight(), Image.SCALE_SMOOTH);
            lblImagen.setIcon(new ImageIcon(img));
            lblImagen.setText("");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al copiar la imagen: " + e.getMessage());
        }
    }
}


    private void cargarCategorias() {
        tableModel.setRowCount(0);
        try {
            List<CategoriaDTO> categorias = frmPrincipal.categoriaNegocio.obtenerCategorias();
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
            for (CategoriaDTO cat : frmPrincipal.categoriaNegocio.obtenerCategorias()) {
                if (cat.getId().equals(id)) {
                    this.categoriaSeleccionada = cat;
                    txtNombreCategoria.setText(cat.getNombreCategoria());
                    txtDescripcion.setText(cat.getDescripcionCategoria());

                    String ruta = cat.getImagenCategoria();
                    if (ruta != null && !ruta.isEmpty()) {
                        this.imagen = ruta;

                        if (lblImagen.getWidth() > 0 && lblImagen.getHeight() > 0) {
                            cargarImagenEnLabel(ruta);
                        } else {
                            lblImagen.addComponentListener(new ComponentAdapter() {
                                @Override
                                public void componentResized(ComponentEvent e) {
                                    cargarImagenEnLabel(ruta);
                                    lblImagen.removeComponentListener(this);
                                }
                            });
                        }
                    } else {
                        lblImagen.setIcon(null);
                        lblImagen.setText("");
                        this.imagen = null;
                    }
                    break;
                }
            }
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar categoría.");
        }
    }

    private void cargarImagenEnLabel(String rutaRelativa) {
        File file = new File("src/main/resources" + rutaRelativa);
        if (file.exists()) {
            ImageIcon icon = new ImageIcon(file.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(480, 600, Image.SCALE_SMOOTH);
            lblImagen.setIcon(new ImageIcon(img));
            lblImagen.setText("");
        } else {
            lblImagen.setIcon(null);
            lblImagen.setText("Imagen no encontrada");
        }
    }

    private void eliminarCategoria(int row) {
        Long id = (Long) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar esta categoría?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                frmPrincipal.categoriaNegocio.eliminarCategoria(id);
                cargarCategorias();
            } catch (NegocioException e) {
                JOptionPane.showMessageDialog(this, "No se puede eliminar la categoría: elimine primero los productos relacionados.");
            }
        }
    }

    private void limpiarFormulario() {
        txtNombreCategoria.setText("");
        txtDescripcion.setText("");
        lblImagen.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCategorias = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        txtNombreCategoria = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnConfirmar = new javax.swing.JButton();
        lblImagen = new javax.swing.JLabel();
        btnAgregarImagen = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1701, 1080));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 64)); // NOI18N
        jLabel1.setText("Añadir categoría");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(599, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(591, 591, 591))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(23, 23, 23))
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        tblCategorias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Descripción", "Imagen", "Editar", "Eliminar"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblCategorias);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel4.setText("Descripción:");

        txtDescripcion.setColumns(20);
        txtDescripcion.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        txtDescripcion.setRows(5);
        jScrollPane1.setViewportView(txtDescripcion);

        txtNombreCategoria.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel3.setText("Nombre:");

        btnConfirmar.setBackground(new java.awt.Color(0, 0, 0));
        btnConfirmar.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        btnConfirmar.setForeground(new java.awt.Color(255, 255, 255));
        btnConfirmar.setText("Confirmar");
        btnConfirmar.setBorder(null);
        btnConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 911, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addGap(80, 80, 80)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNombreCategoria)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)))
                            .addComponent(btnConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(122, 122, 122))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(btnConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        lblImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1.png"))); // NOI18N

        btnAgregarImagen.setBackground(new java.awt.Color(0, 0, 0));
        btnAgregarImagen.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        btnAgregarImagen.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarImagen.setText("Agregar Imagen");
        btnAgregarImagen.setBorder(null);
        btnAgregarImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarImagenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(177, 177, 177)
                        .addComponent(lblImagen)
                        .addGap(73, 73, 73))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnAgregarImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(100, 100, 100)))
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(116, 116, 116))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(lblImagen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAgregarImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
        guardarCategoria();
    }//GEN-LAST:event_btnConfirmarActionPerformed

    private void btnAgregarImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarImagenActionPerformed
        seleccionarImagen();
    }//GEN-LAST:event_btnAgregarImagenActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarImagen;
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblImagen;
    private javax.swing.JTable tblCategorias;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtNombreCategoria;
    // End of variables declaration//GEN-END:variables
}
