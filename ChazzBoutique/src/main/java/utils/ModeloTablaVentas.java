/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author carli
 */
public class ModeloTablaVentas extends DefaultTableModel {
       public ModeloTablaVentas(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // Solo la columna de CANTIDAD (columna 1) es editable
        return column == 1;
    }
}

