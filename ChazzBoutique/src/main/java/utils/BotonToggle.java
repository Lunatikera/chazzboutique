/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 *
 * @author carli
 */
public class BotonToggle extends JButton {
    private Icon normalIcon;
    private Icon clickedIcon;
    private boolean isClicked = false; // Estado del botón

    public BotonToggle() {
        setContentAreaFilled(false);
        setBorderPainted(false);
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Cambiar el estado y actualizar la imagen al hacer clic
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleState();
            }
        });
    }

    /**
     * Cambia el estado del botón y actualiza el ícono.
     */
    private void toggleState() {
        isClicked = !isClicked; // Alterna el estado
        setIcon(isClicked ? clickedIcon : normalIcon); // Cambia el ícono
    }

    // Getters y Setters
    public Icon getNormalIcon() {
        return normalIcon;
    }

    public void setNormalIcon(Icon normalIcon) {
        this.normalIcon = normalIcon;
        if (!isClicked) { // Inicializa el ícono solo si no está en el estado clickeado
            setIcon(normalIcon);
        }
    }

    public Icon getClickedIcon() {
        return clickedIcon;
    }

    public void setClickedIcon(Icon clickedIcon) {
        this.clickedIcon = clickedIcon;
        if (isClicked) { // Actualiza el ícono si está en el estado clickeado
            setIcon(clickedIcon);
        }
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        this.isClicked = clicked;
        setIcon(isClicked ? clickedIcon : normalIcon);
    }
}

