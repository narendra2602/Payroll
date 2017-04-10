package com.payroll.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class TableDataSorter 
{
	public static DocumentListener getTableSorter(final JTextField textField,final TableRowSorter<TableModel> sorter ,final JTable table,final int colIndex,final boolean wholeSearch)
	{
		DocumentListener Listener = new DocumentListener() 
		{
			private void searchFieldChangedUpdate(DocumentEvent evt) 
            {
                String text = textField.getText();
                if (text.length() == 0) 
                {
                    sorter.setRowFilter(null);
                    table.clearSelection();
                } 
                else 
                {
                	try 
                	{
                		//for searching whole test in row
                		if(wholeSearch)
                		{
                			sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text+"(i?)", colIndex));
                		}
                		else //searching text with start with 'text'
                		{


                			RowFilter<TableModel, Object> firstFiler = null;
                			RowFilter<TableModel, Object> secondFilter = null;
                			List<RowFilter<TableModel,Object>> filters = new ArrayList<RowFilter<TableModel,Object>>();
                			RowFilter<TableModel, Object> compoundRowFilter = null;
                			try {

                				firstFiler = RowFilter.regexFilter("^"+text, 0);
                				secondFilter = RowFilter.regexFilter("^"+text, colIndex);
                				filters.add(firstFiler);
                				filters.add(secondFilter);
                				compoundRowFilter = RowFilter.orFilter(filters); // you may also choose the OR filter
                			} catch (java.util.regex.PatternSyntaxException e) {
                				e.printStackTrace();

                			}
                			sorter.setRowFilter(compoundRowFilter);


                		}
 
                        table.clearSelection();
                    } 
                    catch (PatternSyntaxException pse) 
                    {
                        JOptionPane.showMessageDialog(null, "Bad regex pattern","Bad regex pattern", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

           
            public void insertUpdate(DocumentEvent evt) {
                searchFieldChangedUpdate(evt);
            }

           
            public void removeUpdate(DocumentEvent evt) {
                searchFieldChangedUpdate(evt);
            }

          
            public void changedUpdate(DocumentEvent evt) {
                searchFieldChangedUpdate(evt);
            }
		};
		 
		return Listener; 
		
	}
	
	
}
