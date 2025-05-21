package presentacion;

import java.awt.*;
import javax.swing.*;

public class FrmAñadirProducto extends JPanel {

    public FrmAñadirProducto() {
        initComponents();
    }

    private void initComponents() {
        this.setLayout(null);
        this.setBackground(Color.WHITE);

        JLabel titulo = new JLabel("Añadir Producto");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setBounds(350, 20, 250, 30);
        this.add(titulo);

      

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(350, 100, 100, 30);
        this.add(lblNombre);
        JTextField txtNombre = new JTextField();
        txtNombre.setBounds(450, 100, 200, 30);
        this.add(txtNombre);

        // Color
        JLabel lblColor = new JLabel("Color:");
        lblColor.setBounds(350, 150, 100, 30);
        this.add(lblColor);
        JButton btnAgregarColor = new JButton("+");
        btnAgregarColor.setBounds(450, 150, 45, 30);
        this.add(btnAgregarColor);

        // Tallas
        JLabel lblTallas = new JLabel("Tallas:");
        lblTallas.setBounds(350, 200, 100, 30);
        this.add(lblTallas);
        JButton btnTallaS = new JButton("S");
        JButton btnTallaM = new JButton("M");
        JButton btnTallaL = new JButton("L");
        JButton btnTallaPlus = new JButton("+");
        btnTallaS.setBounds(450, 200, 45, 30);
        btnTallaM.setBounds(500, 200, 45, 30);
        btnTallaL.setBounds(550, 200, 45, 30);
        btnTallaPlus.setBounds(600, 200, 45, 30);
        this.add(btnTallaS);
        this.add(btnTallaM);
        this.add(btnTallaL);
        this.add(btnTallaPlus);

        // Cantidad
        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(350, 250, 100, 30);
        this.add(lblCantidad);
        JButton btnMenos = new JButton("-");
        JButton btnMas = new JButton("+");
        JTextField txtCantidad = new JTextField("1");
        txtCantidad.setHorizontalAlignment(JTextField.CENTER);
        txtCantidad.setBounds(450, 250, 45, 30);
        btnMenos.setBounds(400, 250, 45, 30);
        btnMas.setBounds(500, 250, 45, 30);
        this.add(btnMenos);
        this.add(txtCantidad);
        this.add(btnMas);

        // Precio
        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setBounds(350, 300, 100, 30);
        this.add(lblPrecio);
        JLabel lblPrecioValor = new JLabel("$119.00");
        lblPrecioValor.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPrecioValor.setBounds(450, 300, 100, 30);
        this.add(lblPrecioValor);

        // Botón Confirmar
        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setBounds(350, 360, 200, 35);
        this.add(btnConfirmar);
    }
}

