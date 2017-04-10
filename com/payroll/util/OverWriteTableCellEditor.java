package com.payroll.util;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

public class OverWriteTableCellEditor extends javax.swing.AbstractCellEditor implements
TableCellEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextField component = new JTextField();

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		if (value==null)
		     value="";
		component.setText(value.toString());
		component.setSelectionStart(0);
		component.setSelectionEnd(value.toString().length());
		component.setBackground(Color.YELLOW);
		component.setForeground(Color.RED);
		return component;
	}

	@Override
	public Object getCellEditorValue() {
		return component.getText();
	}
}