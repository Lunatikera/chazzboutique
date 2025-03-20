/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.Color;
import java.awt.Dimension;

/**
 *
 * @author carli
 */
public class ScrollBar extends javax.swing.JScrollBar {

    public ScrollBar() {
        setUI(new ModernScrollBarUI());
        setPreferredSize(new Dimension(20,20));
        setBackground(new Color(48,48,48));
        setUnitIncrement(10);
    }
    
    
    
    
}
