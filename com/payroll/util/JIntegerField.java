package com.payroll.util;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * A JTextField that accepts only integers with lenght.
 *
 * @author Akhilesh Patil
 */
public class JIntegerField extends JTextField {

    public JIntegerField() 
    {
        super();
    }

    public JIntegerField( int cols ) 
    {
        super(new UpperCaseDocument(cols),"", cols );
    }

    static class UpperCaseDocument extends PlainDocument {

    	private int maxlength;
        
        UpperCaseDocument(int length)
        {
        	this.maxlength=length;
        }
        
        public void insertString( int offs, String str, AttributeSet a ) throws BadLocationException 
        {

            if ( str == null ) 
            {
                return;
            }

            char[] chars = str.toCharArray();
            boolean ok = true;

            for ( int i = 0; i < chars.length; i++ ) 
            {
                try 
                {
                    Integer.parseInt( String.valueOf( chars[i] ) );
                } 
                catch ( NumberFormatException exc ) 
                {
                    ok = false;
                    break;
                }
            }

            
            if (ok)
            {
            	if (!((getLength() + str.length()) > maxlength)) 
        		{   
            		super.insertString( offs, new String( chars ), a );   
        		}
            	
            }
        }
    }

}