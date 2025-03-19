/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 *
 * @author carli
 */
public class BotonMenu extends JButton{
    
     private Icon simpleIcon;
    private Icon selectedIcon;
    
    

    public Icon getSimpleIcon() {
        return simpleIcon;
    }

    public void setSimpleIcon(Icon iconoSimple) {
        this.simpleIcon = iconoSimple;
        setIcon(iconoSimple); // Set the initial icon
    }

    public Icon getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(Icon iconoSeleccionado) {
        this.selectedIcon = iconoSeleccionado;
    }

    public BotonMenu() {
        setContentAreaFilled(false);
        setBorderPainted(false);
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Add mouse listener for hover effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setIcon(selectedIcon); // Change to selected icon on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setIcon(simpleIcon); // Change back to simple icon when not hovering
            }
        });
    }

    @Override
    public void setSelected(boolean b) {
        super.setSelected(b);
        if (b) {
            setIcon(selectedIcon);
        } else {
            setIcon(simpleIcon);
        }
    }
    
}


