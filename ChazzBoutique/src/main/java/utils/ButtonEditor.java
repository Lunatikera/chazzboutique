package utils;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class ButtonEditor extends DefaultCellEditor {

    protected JButton button;
    private String label;
    private boolean isPushed;
    private Consumer<Integer> onClick;

    public ButtonEditor(JCheckBox checkBox, String label, Consumer<Integer> onClick) {
        super(checkBox);
        this.button = new JButton();
        this.button.setOpaque(true);
        this.label = label;
        this.onClick = onClick;

        button.addActionListener((ActionEvent e) -> {
            fireEditingStopped();
            onClick.accept(table.getSelectedRow());
        });
    }

    JTable table;

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        button.setText(label);
        isPushed = true;
        return button;
    }

    public Object getCellEditorValue() {
        isPushed = false;
        return label;
    }

    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
