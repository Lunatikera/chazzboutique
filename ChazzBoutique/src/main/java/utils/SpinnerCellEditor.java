/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author carli
 */
public class SpinnerCellEditor extends AbstractCellEditor implements TableCellEditor {

    private final JSpinner spinner;
    private final JSpinner.NumberEditor editor;

    public SpinnerCellEditor() {
        spinner = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
        editor = new JSpinner.NumberEditor(spinner, "#");
        spinner.setEditor(editor);

        // Configurar fuente Segoe UI 20
        Font segoe20 = new Font("Segoe UI", Font.BOLD, 20);
        spinner.setFont(segoe20);
        editor.getTextField().setFont(segoe20);

        // Bloquear entrada manual de texto no numérico
        editor.getTextField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
                    e.consume();
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        spinner.setValue(value != null ? value : 1);
        return spinner;
    }

    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }

}
