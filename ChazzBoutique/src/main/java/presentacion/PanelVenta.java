/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package presentacion;

import com.mycompany.chazzboutiquenegocio.dtos.DetalleVentaDTO;
import com.mycompany.chazzboutiquenegocio.dtos.ProductoDTO;
import com.mycompany.chazzboutiquenegocio.dtos.VarianteProductoDTO;
import com.mycompany.chazzboutiquenegocio.dtos.VentaDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;
import com.mycompany.chazzboutiquepersistencia.dominio.Venta;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import utils.ModeloTablaVentas;
import utils.SpinnerCellEditor;
import utils.SpinnerRenderer;

/**
 *
 * @author carli
 */
public class PanelVenta extends javax.swing.JPanel {

    FrmMain frmPrincipal;
    DefaultTableModel modelo;
    private boolean descuentoAplicado = false;
    private BigDecimal descuentoActual = BigDecimal.ZERO;
    private BigDecimal totalSinDescuento;
    private Map<Integer, String> filaCodigoMap = new HashMap<>(); // Mapea índice de fila a código de barras

    public PanelVenta(FrmMain frmPrincipal) {
        initComponents();
        this.frmPrincipal = frmPrincipal;
        this.modelo = (DefaultTableModel) jTable.getModel();
        this.habilitarCampos(false);
        this.configurarFormatoMoneda(txtMontoPago);
        this.configurarFormatoMoneda(txtDescuento);

        // En el constructor, después de inicializar el spinner:
        spnCantidad.setModel(new SpinnerNumberModel(1, 1, 100, 1)); // Valores de 1 a 100, incremento de 1
        // En el constructor, después de inicializar el spinner:
        spnCantidad.addChangeListener(e -> {
            if (!txtNombreProducto.getText().isEmpty()) {
                btnAgregar.setEnabled(true);
            }
        });

// Configurar el editor del spinner para capturar Enter
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spnCantidad.getEditor();
        editor.getTextField().addActionListener(e -> {
            if (btnAgregar.isEnabled()) {
                agregarProductoATabla();
            }
        });
        String[] columnNames = {"NOMBRE PRODUCTO", "CANTIDAD", "PRECIO UNITARIO", "SUBTOTAL"};
        modelo = new ModeloTablaVentas(columnNames, 0);
        jTable.setModel(modelo);

        // Configurar spinner para columna de cantidad (columna 1)
        jTable.getColumnModel().getColumn(1).setCellEditor(new SpinnerCellEditor());
        jTable.getColumnModel().getColumn(1).setCellRenderer(new SpinnerRenderer());
        jTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Ajustar altura de filas para el spinner
        jTable.setRowHeight(40);

        // Configurar header
        JTableHeader header = jTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setForeground(Color.WHITE);
        header.setBackground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        txtCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (txtNombreProducto.getText().isEmpty()) {
                    // Si no hay producto cargado, buscar
                    buscarProductoPorCodigo();
                } else {
                    // Si ya hay producto cargado, agregar a la tabla
                    agregarProductoATabla();
                }
            }
        });

        // Agregar ActionListener al botón Agregar
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoATabla();
            }
        });

        jTable.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 1) { // Si cambió la columna CANTIDAD
                int row = e.getFirstRow();
                actualizarSubtotal(row);
                actualizarTotales();
            }
        });

        jTable.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {
            @Override
            public boolean stopCellEditing() {
                try {
                    int cantidad = Integer.parseInt(getCellEditorValue().toString());
                    if (cantidad <= 0) {
                        throw new NumberFormatException();
                    }
                    return super.stopCellEditing();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Ingrese un número válido > 0", "Error", JOptionPane.ERROR_MESSAGE);
                    return false; // Cancela la edición
                }
            }
        });

        btnEliminar.addActionListener(e -> eliminarFilasSeleccionadas());
        btnBorrarTabla.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro que desea vaciar toda la tabla?",
                    "Confirmar limpieza",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                modelo.setRowCount(0); 
                filaCodigoMap.clear();
                actualizarTotales();
            }
        });

        // Listener para el botón de aplicar descuento
        btnAplicarDescuento.addActionListener(e -> aplicarDescuento());

// Listener para el campo de monto de pago
        txtMontoPago.addActionListener(e -> calcularCambio());
        txtMontoPago.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                calcularCambio();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                calcularCambio();
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                calcularCambio();
            }
        });

// Listener para el campo de descuento
        txtDescuento.addActionListener(e -> aplicarDescuento());
        botonMenu3.addActionListener(e -> registrarVenta());

// Listener para el campo de descuento
    }

    private void registrarVenta() {
        try {
            // 1. Validar que haya productos en la venta
            if (filaCodigoMap.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay productos en la venta",
                        "Venta vacía",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Obtener totales y validar pago
            BigDecimal total = obtenerTotalDeLabel(lblTotalResult);
            BigDecimal montoPago = obtenerTotalDeTextField(txtMontoPago);

            if (montoPago.compareTo(total) < 0) {
                JOptionPane.showMessageDialog(this,
                        String.format("Pago insuficiente. Falta: %s",
                                formatoMoneda(total.subtract(montoPago))),
                        "Error en pago",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. Crear DTO de venta
            VentaDTO ventaDTO = new VentaDTO();
            ventaDTO.setUsuarioId(frmPrincipal.getUsuarioRegistrado().getId());
            ventaDTO.setFecha(LocalDate.now());
            ventaDTO.setTotal(total);
            ventaDTO.setEstado("COMPLETADA");
            ventaDTO.setDescuento(descuentoAplicado ? descuentoActual : BigDecimal.ZERO);

            // 4. Crear lista de detalles usando el mapa
            List<DetalleVentaDTO> detalles = new ArrayList<>();

            for (int i = 0; i < modelo.getRowCount(); i++) {
                String codigoBarras = filaCodigoMap.get(i);
                if (codigoBarras == null) {
                    continue;
                }

                VarianteProductoDTO variante = frmPrincipal.varianteProductoNegocio.obtenerVariantePorCodigoBarra(codigoBarras);
                if (variante == null) {
                    throw new NegocioException("Producto con código " + codigoBarras + " ya no disponible");
                }

                DetalleVentaDTO detalle = new DetalleVentaDTO();
                detalle.setVarianteProductoId(variante.getId());
                detalle.setCodigoVariante(codigoBarras);
                detalle.setCantidad((Integer) jTable.getValueAt(i, 1)); // Columna CANTIDAD
                detalle.setPrecioUnitario(obtenerValorMonedaDeTabla(jTable.getValueAt(i, 2))); // Columna PRECIO UNITARIO

                detalles.add(detalle);
            }

            ventaDTO.setDetalles(detalles);
            System.out.println(ventaDTO.toString());
            // 5. Registrar la venta
            VentaDTO ventaregistrada = frmPrincipal.ventaNegocio.registrarVenta(ventaDTO);

            // 6. Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(this,
                    "Venta registrada exitosamente\nNúmero de venta: " + ventaregistrada.getId(),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            // 7. Resetear la venta
            resetearVenta();

        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al registrar venta: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error inesperado: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(PanelVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void resetearVenta() {
        // Limpiar tabla y mapa
        modelo.setRowCount(0);
        filaCodigoMap.clear();

        // Resetear campos
        txtCodigo.setText("");
        limpiarCampos();
        txtMontoPago.setText("$0.00");
        txtDescuento.setText("$0.00");
        lblTotalResult.setText("$0.00");
        lblTotalResult2.setText("$0.00");
        jLabel13.setText("$0.00");

        // Resetear estado de descuento
        descuentoAplicado = false;
        descuentoActual = BigDecimal.ZERO;
        btnAplicarDescuento.setSelected(false);

        // Poner foco en código de barras
        txtCodigo.requestFocus();
    }

    private BigDecimal obtenerTotalDeTextField(JTextField textField) {
        try {
            String texto = textField.getText()
                    .replace("$", "")
                    .replace(",", "")
                    .trim();
            return texto.isEmpty() ? BigDecimal.ZERO : new BigDecimal(texto);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    private void eliminarFilasSeleccionadas() {
        int[] filasSeleccionadas = jTable.getSelectedRows();

        if (filasSeleccionadas.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione al menos un producto para eliminar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar los productos seleccionados?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            // Ordenar de mayor a menor para evitar problemas con índices
            Arrays.sort(filasSeleccionadas);

            for (int i = filasSeleccionadas.length - 1; i >= 0; i--) {
                int fila = filasSeleccionadas[i];
                if (fila >= 0 && fila < modelo.getRowCount()) {
                    // Eliminar del mapa
                    filaCodigoMap.remove(fila);
                    // Eliminar fila de la tabla
                    modelo.removeRow(fila);
                }
            }

            // Reindexar el mapa después de eliminar filas
            reindexarMapa();
            actualizarTotales();
        }
    }

    private void reindexarMapa() {
        Map<Integer, String> nuevoMapa = new HashMap<>();
        int nuevaFila = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (filaCodigoMap.containsKey(i)) {
                nuevoMapa.put(nuevaFila, filaCodigoMap.get(i));
                nuevaFila++;
            }
        }

        filaCodigoMap = nuevoMapa;
    }

    private void agregarProductoATabla() {
        try {
            String codigo = txtCodigo.getText().trim();

            if (codigo.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Primero escanee un producto",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Verificar si el producto ya está en la tabla
            if (filaCodigoMap.containsValue(codigo)) {
                JOptionPane.showMessageDialog(this,
                        "Este producto ya está en la venta",
                        "Producto duplicado",
                        JOptionPane.WARNING_MESSAGE);
                txtCodigo.setText("");
                txtCodigo.requestFocus();
                return;
            }

            int cantidad = (int) spnCantidad.getValue();
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this,
                        "La cantidad debe ser mayor a 0",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            VarianteProductoDTO variante = frmPrincipal.varianteProductoNegocio.obtenerVariantePorCodigoBarra(codigo);
            if (variante == null) {
                JOptionPane.showMessageDialog(this,
                        "El producto ya no está disponible",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                limpiarCampos();
                return;
            }

            ProductoDTO producto = frmPrincipal.productoNegocio.buscarPorId(variante.getProductoId());
            if (producto == null) {
                JOptionPane.showMessageDialog(this,
                        "El producto ya no está disponible",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                limpiarCampos();
                return;
            }

            if (variante.getStock() < cantidad) {
                JOptionPane.showMessageDialog(this,
                        String.format("Stock insuficiente para %s (Disponible: %d, Solicitado: %d)",
                                producto.getNombreProducto(),
                                variante.getStock(),
                                cantidad),
                        "Stock insuficiente",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            BigDecimal precio = variante.getPrecioVenta();
            BigDecimal subtotal = precio.multiply(new BigDecimal(cantidad));

            // Agregar fila sin columna oculta
            int filaIndex = modelo.getRowCount();
            modelo.addRow(new Object[]{
                producto.getNombreProducto(), // Columna 0: Nombre
                cantidad, // Columna 1: Cantidad
                formatoMoneda(precio), // Columna 2: Precio Unitario
                formatoMoneda(subtotal) // Columna 3: Subtotal
            });

            // Mapear fila a código de barras
            filaCodigoMap.put(filaIndex, codigo);

            actualizarTotales();

            txtCodigo.setText("");
            limpiarCampos();
            txtCodigo.requestFocus();
            habilitarCampos(false);

        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al agregar producto: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(PanelVenta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error inesperado al agregar producto",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(PanelVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
// Método para actualizar los totales

    private BigDecimal obtenerValorMoneda(JTextField textField) {
        try {
            String text = textField.getText().replace("$", "").replace(",", "");
            return new BigDecimal(text);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

   

    private void aplicarDescuento() {
        try {
            if (!descuentoAplicado) {
                // Obtener el valor del descuento del campo txtDescuento
                BigDecimal descuento = obtenerValorMoneda(txtDescuento);

                // Validar el descuento
                if (descuento.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "Ingrese un descuento válido",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (descuento.compareTo(totalSinDescuento) > 0) {
                    JOptionPane.showMessageDialog(this,
                            "El descuento no puede ser mayor al total",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Aplicar descuento
                this.descuentoActual = descuento;
                BigDecimal totalConDescuento = totalSinDescuento.subtract(descuento);
                lblTotalResult.setText(formatoMoneda(totalConDescuento));

                // Cambiar estado
                descuentoAplicado = true;
                btnAplicarDescuento.setSelected(true); // Cambiar apariencia del botón

            } else {
                // Quitar descuento
                lblTotalResult.setText(formatoMoneda(totalSinDescuento));

                // Cambiar estado
                descuentoAplicado = false;
                btnAplicarDescuento.setSelected(false);
            }

            // Recalcular cambio si hay monto de pago
            calcularCambio();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calcularCambio() {
        try {
            // Obtener el total (con descuento si aplica)
            String totalStr = lblTotalResult.getText().replace("$", "").replace(",", "");
            BigDecimal total = new BigDecimal(totalStr);

            // Obtener monto de pago
            BigDecimal montoPago = obtenerValorMoneda(txtMontoPago);

            // Calcular cambio
            BigDecimal cambio = montoPago.subtract(total);

            // Mostrar solo si es positivo
            if (cambio.compareTo(BigDecimal.ZERO) > 0) {
                jLabel13.setText(formatoMoneda(cambio));
            } else {
                jLabel13.setText("$0.00"); // Ocultar valores negativos
            }

        } catch (NumberFormatException ex) {
            jLabel13.setText("$0.00");
        }
    }

    private void actualizarTotales() {
        // Calcular total sin descuentos
        totalSinDescuento = calcularTotalTabla();

        // Mostrar valores
        lblTotalResult2.setText(formatoMoneda(totalSinDescuento));

        if (descuentoAplicado) {
            // Si hay descuento aplicado, mantenerlo
            BigDecimal totalConDescuento = totalSinDescuento.subtract(descuentoActual);
            lblTotalResult.setText(formatoMoneda(totalConDescuento));
        } else {
            // Sin descuento
            lblTotalResult.setText(formatoMoneda(totalSinDescuento));
        }

        calcularCambio();
    }

    private BigDecimal calcularTotalTabla() {
        BigDecimal total = BigDecimal.ZERO;
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-MX"));

        for (int i = 0; i < jTable.getRowCount(); i++) {
            try {
                String subtotalStr = jTable.getValueAt(i, 3).toString(); // Columna SUBTOTAL
                Number subtotalNumber = nf.parse(subtotalStr);
                total = total.add(new BigDecimal(subtotalNumber.toString()));
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this,
                        "Error al calcular total: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        return total;
    }

    private void buscarProductoPorCodigo() {
        String codigo = txtCodigo.getText().trim();

        if (codigo.isEmpty()) {
            return; // No hacer nada si el campo está vacío
        }

        try {
            // 1. Verificar si el producto ya está en la venta
            if (filaCodigoMap.containsValue(codigo)) {
                JOptionPane.showMessageDialog(this,
                        "Este producto ya está en la venta",
                        "Producto duplicado",
                        JOptionPane.WARNING_MESSAGE);
                txtCodigo.setText("");
                txtCodigo.requestFocus();
                return;
            }

            // 2. Buscar la variante del producto
            VarianteProductoDTO variante = frmPrincipal.varianteProductoNegocio.obtenerVariantePorCodigoBarra(codigo);

            if (variante == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró ningún producto con ese código",
                        "Producto no encontrado",
                        JOptionPane.WARNING_MESSAGE);
                limpiarCampos();
                habilitarCampos(false);
                return;
            }

            // 3. Buscar información completa del producto
            ProductoDTO producto = frmPrincipal.productoNegocio.buscarPorId(variante.getProductoId());

            if (producto == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró información del producto",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                limpiarCampos();
                habilitarCampos(false);
                return;
            }

            // 4. Actualizar la interfaz con los datos del producto
            txtNombreProducto.setText(producto.getNombreProducto());
            txtPrecio.setText(formatoMoneda(variante.getPrecioVenta()));
            spnCantidad.setValue(1); // Resetear a cantidad inicial

            // 5. Configurar el color del producto si está disponible
            if (variante.getColor() != null && !variante.getColor().isEmpty()) {
                try {
                    Color color = Color.decode(variante.getColor());
                    btnColor.setBackground(color);
                } catch (NumberFormatException e) {
                    // Intentar convertir nombres de colores (RED, BLUE, etc.)
                    try {
                        Color color = (Color) Color.class.getField(variante.getColor().toUpperCase()).get(null);
                        btnColor.setBackground(color);
                    } catch (Exception ex) {
                        btnColor.setBackground(Color.WHITE);
                    }
                }
            } else {
                btnColor.setBackground(Color.WHITE);
            }

            // 6. Habilitar campos para editar cantidad y agregar
            habilitarCampos(true);

        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar producto: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            limpiarCampos();
            habilitarCampos(false);
            Logger.getLogger(PanelVenta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error inesperado al buscar producto",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            limpiarCampos();
            habilitarCampos(false);
            Logger.getLogger(PanelVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void actualizarSubtotal(int row) {
        try {
            // Obtener cantidad (nuevo valor editado)
            int cantidad = Integer.parseInt(jTable.getValueAt(row, 1).toString());

            // Obtener precio unitario (sin formato de moneda)
            String precioStr = jTable.getValueAt(row, 2).toString().replace("$", "").replace(",", "");
            BigDecimal precio = new BigDecimal(precioStr);

            // Calcular nuevo subtotal
            BigDecimal subtotal = precio.multiply(new BigDecimal(cantidad));

            // Actualizar celda de SUBTOTAL
            jTable.setValueAt(formatoMoneda(subtotal), row, 3);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        lblTotal = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblTotalResult = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtMontoPago = new javax.swing.JTextField();
        txtDescuento = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        btnAplicarDescuento = new utils.BotonToggle();
        botonMenu3 = new utils.BotonMenu();
        btnBorrarTabla = new utils.BotonMenu();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        txtCodigo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        txtNombreProducto = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        txtPrecio = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btnColor = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        btnAgregar = new utils.BotonMenu();
        spnCantidad = new javax.swing.JSpinner();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        lblTotal2 = new javax.swing.JLabel();
        lblTotalResult2 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        btnImprimir = new utils.BotonMenu();
        btnEliminar = new utils.BotonMenu();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(560, 680));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setPreferredSize(new java.awt.Dimension(305, 175));

        lblTotal.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        lblTotal.setText("TOTAL");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(156, 156, 156));
        jLabel8.setText("DESCUENTO");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(156, 156, 156));
        jLabel9.setText("MONTO PAGO");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(156, 156, 156));
        jLabel10.setText("CAMBIO");

        lblTotalResult.setBackground(new java.awt.Color(176, 50, 53));
        lblTotalResult.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        lblTotalResult.setForeground(new java.awt.Color(176, 50, 53));
        lblTotalResult.setText("$0.00");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel13.setText("$0.00");

        txtMontoPago.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        txtMontoPago.setPreferredSize(new java.awt.Dimension(92, 31));
        txtMontoPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMontoPagoActionPerformed(evt);
            }
        });

        txtDescuento.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        txtDescuento.setPreferredSize(new java.awt.Dimension(92, 31));
        txtDescuento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescuentoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addComponent(lblTotal)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addGap(38, 38, 38)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotalResult)
                    .addComponent(jLabel13)
                    .addComponent(txtMontoPago, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotal)
                    .addComponent(lblTotalResult))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtMontoPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel13))
                .addGap(14, 14, 14))
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        btnAplicarDescuento.setClickedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/removerDescuento.png"))); // NOI18N
        btnAplicarDescuento.setNormalIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btnDescuento.png"))); // NOI18N
        btnAplicarDescuento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAplicarDescuentoActionPerformed(evt);
            }
        });

        botonMenu3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btnPagar.png"))); // NOI18N
        botonMenu3.setSimpleIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btnPagar.png"))); // NOI18N

        btnBorrarTabla.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btnBorrarProductos.png"))); // NOI18N
        btnBorrarTabla.setSimpleIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btnBorrarProductos.png"))); // NOI18N
        btnBorrarTabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarTablaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(169, 169, 169)
                .addComponent(btnAplicarDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnBorrarTabla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(botonMenu3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(btnAplicarDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(botonMenu3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBorrarTabla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(316, 316, 316))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(112, 112, 112)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(112, 112, 112)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(160, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.LINE_END);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(1701, 400));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 64)); // NOI18N
        jLabel1.setText("Punto de Venta");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setPreferredSize(new java.awt.Dimension(330, 128));

        txtCodigo.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        txtCodigo.setPreferredSize(new java.awt.Dimension(311, 60));
        txtCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel2.setText("Codigo");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        jPanel3.add(jPanel4);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setPreferredSize(new java.awt.Dimension(330, 128));

        txtNombreProducto.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        txtNombreProducto.setPreferredSize(new java.awt.Dimension(311, 60));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel3.setText("Nombre");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        jPanel3.add(jPanel6);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setPreferredSize(new java.awt.Dimension(330, 128));

        txtPrecio.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        txtPrecio.setPreferredSize(new java.awt.Dimension(311, 60));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel7.setText("Precio");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        jPanel3.add(jPanel9);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setPreferredSize(new java.awt.Dimension(510, 128));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel5.setText("Color");

        btnColor.setBackground(new java.awt.Color(249, 249, 249));
        btnColor.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnColor.setBorderPainted(false);
        btnColor.setPreferredSize(new java.awt.Dimension(60, 60));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel6.setText("Cantidad");

        btnAgregar.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btnAgregar.png"))); // NOI18N
        btnAgregar.setSimpleIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btnAgregar.png"))); // NOI18N

        spnCantidad.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(btnColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(spnCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spnCantidad))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16))
        );

        jPanel3.add(jPanel8);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(611, 611, 611)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 1703, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );

        add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jTable.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jTable.setForeground(new java.awt.Color(176, 50, 53));
        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NOMBRE PRODUCTO", "CANTIDAD", "PRECIO UNITARIO", "SUBTOTAL"
            }
        ));
        jTable.setGridColor(new java.awt.Color(204, 204, 204));
        jTable.setRowHeight(40);
        jScrollPane1.setViewportView(jTable);

        lblTotal2.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblTotal2.setText("TOTAL:");

        lblTotalResult2.setBackground(new java.awt.Color(176, 50, 53));
        lblTotalResult2.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblTotalResult2.setForeground(new java.awt.Color(176, 50, 53));
        lblTotalResult2.setText("$0.00");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 968, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTotal2)
                .addGap(38, 38, 38)
                .addComponent(lblTotalResult2)
                .addGap(90, 90, 90))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotal2)
                    .addComponent(lblTotalResult2))
                .addContainerGap())
        );

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        btnImprimir.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btnImprimir.png"))); // NOI18N
        btnImprimir.setSimpleIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btnImprimir.png"))); // NOI18N

        btnEliminar.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btnBasura.png"))); // NOI18N
        btnEliminar.setSimpleIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btnBasura.png"))); // NOI18N

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(58, Short.MAX_VALUE)
                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(119, 119, 119))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(123, 123, 123)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        add(jPanel5, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private BigDecimal obtenerValorMonedaDeTabla(Object valorCelda) {
        if (valorCelda == null) {
            return BigDecimal.ZERO;
        }

        try {
            // Convertir a String y limpiar formato
            String valorStr = valorCelda.toString()
                    .replace("$", "") // Quitar símbolo de peso
                    .replace(",", "") // Quitar separadores de miles
                    .trim();

            // Validar que no esté vacío
            if (valorStr.isEmpty()) {
                return BigDecimal.ZERO;
            }

            // Convertir a BigDecimal con precisión de 2 decimales
            return new BigDecimal(valorStr).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Valor monetario inválido: " + valorCelda.toString(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return BigDecimal.ZERO;
        }
    }
    private void txtMontoPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMontoPagoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMontoPagoActionPerformed

    private void txtCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoActionPerformed

    private void txtDescuentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescuentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescuentoActionPerformed

    private void btnAplicarDescuentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAplicarDescuentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAplicarDescuentoActionPerformed

    private void btnBorrarTablaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarTablaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBorrarTablaActionPerformed
    private BigDecimal obtenerTotalDeLabel(JLabel label) {
        try {
            // Eliminar símbolo de moneda y comas
            String texto = label.getText()
                    .replace("$", "")
                    .replace(",", "")
                    .trim();

            // Convertir a BigDecimal
            return new BigDecimal(texto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al convertir el total: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return BigDecimal.ZERO; // Retornar 0 en caso de error
        }
    }

    private String formatoMoneda(BigDecimal cantidad) {
        // 1. Create a Mexican peso locale (modern approach)
        Locale mexico = Locale.forLanguageTag("es-MX");

        // 2. Get currency formatter
        NumberFormat formatter = NumberFormat.getCurrencyInstance(mexico);

        // 3. (Optional) Force Mexican peso symbol if needed
        formatter.setCurrency(Currency.getInstance("MXN"));

        return formatter.format(cantidad);
    }

    private void configurarFormatoMoneda(JTextField textField) {
        textField.setText("$0.00");
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals("$0.00")) {
                    textField.setText("");
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setText("$0.00");
                } else {
                    try {
                        // Formatear el texto como moneda
                        String text = textField.getText().replace("$", "").replace(",", "");
                        BigDecimal amount = new BigDecimal(text);
                        textField.setText(formatoMoneda(amount));
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(PanelVenta.this,
                                "Ingrese un valor numérico válido",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        textField.setText("$0.00");
                    }
                }
            }
        });
    }

    private void habilitarCampos(boolean habilitar) {
        txtNombreProducto.setEnabled(false);  // Siempre de solo lectura
        txtPrecio.setEnabled(false);         // Siempre de solo lectura
        spnCantidad.setEnabled(habilitar);   // Solo habilitado cuando hay producto
        btnAgregar.setEnabled(habilitar);    // Solo habilitado cuando hay producto
        btnColor.setEnabled(false);          // Siempre visual

        if (habilitar) {
            spnCantidad.requestFocus(); // Poner foco en el spinner cuando se habilita
        }
    }

    private void limpiarCampos() {
        txtNombreProducto.setText("");
        txtPrecio.setText("");
        spnCantidad.setValue(1);
        btnColor.setBackground(Color.WHITE);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private utils.BotonMenu botonMenu3;
    private utils.BotonMenu btnAgregar;
    private utils.BotonToggle btnAplicarDescuento;
    private utils.BotonMenu btnBorrarTabla;
    private javax.swing.JButton btnColor;
    private utils.BotonMenu btnEliminar;
    private utils.BotonMenu btnImprimir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotal2;
    private javax.swing.JLabel lblTotalResult;
    private javax.swing.JLabel lblTotalResult2;
    private javax.swing.JSpinner spnCantidad;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtDescuento;
    private javax.swing.JTextField txtMontoPago;
    private javax.swing.JTextField txtNombreProducto;
    private javax.swing.JTextField txtPrecio;
    // End of variables declaration//GEN-END:variables
}
