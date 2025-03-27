/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author carli
 */
public class SpinnerRenderer extends DefaultTableCellRenderer {

      public SpinnerRenderer() {
        setHorizontalAlignment(JLabel.RIGHT);
        setFont(new Font("Segoe UI", Font.BOLD, 20));
    }

    @Override
    protected void setValue(Object value) {
        setText(value == null ? "" : value.toString());
    }
}
