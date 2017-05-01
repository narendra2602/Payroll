package com.payroll.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import com.payroll.dao.ConnectionFactory;
import com.payroll.dao.LoginDAO;
import com.payroll.dto.BankDto;
import com.payroll.dto.LoginDto;
import com.payroll.dto.MonthDto;

public class BaseClass extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static  LoginDto loginDt;
	public static JLabel lblAccountingYear;
	public static JLabel lblFinancialAccountingYear;
	public static Icon arisLogo;
	public static Icon promLogo; 
	public static JLabel stock_status;
	protected NumberFormat formatter;
	protected double dbval=0.00;  
	protected static Map map=new HashMap();
	LoginDAO ldao;
	public static int userID = 0;
	protected static String packageName=null;
	
	public static String ipaddress=null;
	protected static String infoName=null;
	protected SimpleDateFormat simpleDateFormat;
	public JLabel clockLab; 
	public JLabel lblVersion;
	public final String version;
	public FocusListener myFocusListener;
	public static int add;  // for new contractor
	
	public BaseClass() 
	{
		  formatter = new DecimalFormat("0.00");
		  simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");  // Date Format
		  ipaddress =getIpAdress();
		  packageName="Labour Contractor PayRoll";
		  version="Version : 22.04.2017-V1";
		  
       myFocusListener = new FocusListener() {
       public void focusGained(FocusEvent focusEvent) {
           try {
               JTextField src = (JTextField)focusEvent.getSource();
               src.setBackground(new Color(170, 181, 157));
               src.selectAll();
           } catch (ClassCastException ignored) {
               /* I only listen to JTextFields */
               try {
					JComboBox src = (JComboBox)focusEvent.getSource();
					src.setBackground(new Color(139, 153, 122));
				} catch (ClassCastException ignored1) {
					// TODO Auto-generated catch block
					
				}
           }
       }

       public void focusLost(FocusEvent focusEvent) {
           try {
               JTextField src = (JTextField)focusEvent.getSource();
               src.setBackground(null);
           } catch (ClassCastException ignored) {
               /* I only listen to JTextFields */
               try {
					JComboBox src = (JComboBox)focusEvent.getSource();
					src.setBackground(null);
				} catch (ClassCastException ignored1) {
					// TODO Auto-generated catch block
					
				}
           }
       }
   };
		  
	}
	
	 

	public int setIntNumber(String s){   
		int i = 0;
		try{
			i= Integer.parseInt(s);

		}
		catch(Exception e){
			i=0;
		}

		return i;
	}


	public double setDoubleNumber(String s)
	{   
		double d = 0.00;
		try
		{
			d= Double.parseDouble(s);
			
		}
		catch(Exception e)
		{
			d=0.00;
		}

		return d;
	}
	
	public long setLongNumber(String s){   
		
		
		long i = 0;
		try{
			i= Long.parseLong(s.trim());

		}
		catch(Exception e){
			i=0;
		}

		return i;
	}
	
	public Date formatDate(String responseDate)
	{
		Date returnDate=null;
		
		try
		{
			returnDate = simpleDateFormat.parse(responseDate);
		}
		catch(Exception e)
		{
			returnDate =null;
		}
		
		return returnDate;
	}
	
	public String formatDate(Date responseDate)
	{
		String returnDate=null;
		
		try
		{
			returnDate = simpleDateFormat.format(responseDate);
		}
		catch(Exception e)
		{
			returnDate ="__/__/____";
		}
		
		return returnDate;
	}
	public String formatDateScreen(Date responseDate,JFormattedTextField txtField)
	{
		String returnDate=null;
		simpleDateFormat.setLenient(true);
		
		try
		{
			returnDate = simpleDateFormat.format(responseDate);
		}
		catch(Exception e)
		{
			returnDate ="__/__/____";
		}
		
		checkDate(txtField);
		
		
		return returnDate;
	}
	
	
	
	
	
	public String setString(String s){   
		try{
			if(s==null)
				s="";

		}
		catch(Exception e){
			 s="";
		}

		return s;
	}
	
	public void checkDate(JFormattedTextField tf)
	{
		try 
        {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            DefaultFormatterFactory formatterFactory = new DefaultFormatterFactory(dateMask);
            tf.setFormatterFactory(formatterFactory);
            dateMask.install(tf);
        } 
        catch (ParseException ex) 
        {
            ex.printStackTrace();
        }
	}


	public void checkNumber(JFormattedTextField tf)
	{
		try 
        {
            MaskFormatter numMask = new MaskFormatter("#########0.00");
            DefaultFormatterFactory formatterFactory = new DefaultFormatterFactory(numMask);
            tf.setFormatterFactory(formatterFactory);
            numMask.install(tf);
        } 
        catch (ParseException ex) 
        {
            Logger.getLogger(BaseClass.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	public void checkInteger(JFormattedTextField tf)
	{
		try 
        {
            MaskFormatter numMask = new MaskFormatter("##########");
            DefaultFormatterFactory formatterFactory = new DefaultFormatterFactory(numMask);
            tf.setFormatterFactory(formatterFactory);
            numMask.install(tf);
        } 
        catch (ParseException ex) 
        {
            Logger.getLogger(BaseClass.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	
	
	public void checkIntegerWithLength(JFormattedTextField tf,int len)
	{
		try 
        {
			StringBuffer sbf = new StringBuffer();
			for(int i=0;i<len;i++)
			{
				sbf.append("#");
			}
			 
            MaskFormatter numMask = new MaskFormatter(sbf.toString());
            DefaultFormatterFactory formatterFactory = new DefaultFormatterFactory(numMask);
            tf.setFormatterFactory(formatterFactory);
            numMask.install(tf);
        } 
        catch (ParseException ex) 
        {
            Logger.getLogger(BaseClass.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	/**
	 * @param tf 
	 * @param len is not using
	 * @param characters 
	 */
	public void checkYN(JFormattedTextField tf,int len,String characters)
	{
		try 
        {
			//StringBuffer sbf = new StringBuffer();
            MaskFormatter numMask = new MaskFormatter("A");
            numMask.setValidCharacters(characters);
            DefaultFormatterFactory formatterFactory = new DefaultFormatterFactory(numMask);
            tf.setFormatterFactory(formatterFactory);
            numMask.install(tf);
        } 
        catch (ParseException ex) 
        {
            Logger.getLogger(BaseClass.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	
	
	public boolean isValidDate(String inDate) 
	{

		if (inDate == null)
			return false;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		 
		try {
			dateFormat.parse(inDate.trim());
		}
		catch (ParseException pe) {
			return false;
		}
		return true;
	} 

	public boolean isValidBlankDate(String inDate) 
	{

		if (inDate.equals("__/__/____"))
			return true;

		if (inDate == null)
			return false;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		 
		try {
			dateFormat.parse(inDate.trim());
		}
		catch (ParseException pe) {
			return false;
		}
		return true;
	} 

	
	public  boolean isValidRange(String dt,Date sdate,Date edate)
	{
	     
//	    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	     
	    // declare and initialize testDate variable, this is what will hold
	    // our converted string
	    
	    Date inDate1 = null;
	    Date Date1 = null;
	    Date Date2 = null;
		 
	    // we will now try to parse the string into date form
	    try
	    {
	      inDate1 = sdf.parse(dt);
	      Date1 = sdf.parse(sdf.format(sdate));
	      Date2 = sdf.parse(sdf.format(edate));
	      
	    }
	 
	    // if the format of the string provided doesn't match the format we 
	    // declared in SimpleDateFormat() we will get an exception
	 
	    catch (ParseException e)
	    {
	      System.out.println("the date you provided is in an invalid date format.");
	      return false;
	    }
	    
	      
	    		
	    if (inDate1.before(Date1)) 
	    {
	      System.out.println("The enter date is less then Starting date.");
	      return false;
	    }
	 
	    if (inDate1.after(Date2)) 
	    {
	      System.out.println("The enter date is greater then ending date.");
	      return false;
	    }
	  
	     
	    // if we make it to here without getting an error it is assumed that
	    // the date was a valid one and that it's in the proper format
	 
	    return true;
	 
	} // end isValidRange

	
	 
	public double roundTwoDecimals(double d) {
	    DecimalFormat twoDForm = new DecimalFormat("#.##");
	    return Double.valueOf(twoDForm.format(d));
	}	
	
	public boolean checkingMissingDate(Date gtDate,Date maxDate,Date minDate)
	{

		if(gtDate.after(maxDate) && gtDate.before(minDate))
		{
			return true;
		}
		else if(gtDate.compareTo(maxDate)==0)
		{
			return true;
		}
		else if(gtDate.compareTo(minDate)==0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public String checkNull(String value)
	{
		System.out.println(value);
		try
		{
			if(!value.equals(null))
			{
				return value;
			}
			else
			{
				return value="";
			}
		}
		catch(Exception e)
		{
			return value="";
		}

	}
	
	public int getSerialNo(int div,int depo,int year,int invno,int doctp)
	{
		PreparedStatement ps1 = null;
		ResultSet rst=null;
		Connection con=null;
		int i=0;
		int sno=0;
		try {
			con=ConnectionFactory.getConnection();
			String query1="select max(serialno)  from invsnd where fin_year=? and div_code=? and sdepo_code=? and sdoc_type=? and sinv_no=?  " ;
			con.setAutoCommit(false);

			ps1 =con.prepareStatement(query1);
			ps1.setInt(1, year);
			ps1.setInt(2, div);
			ps1.setInt(3, depo);
			ps1.setInt(4, doctp);
			ps1.setInt(5, invno);
			rst =ps1.executeQuery();

			if(rst.next()){
					sno=rst.getInt(1);
			}

			con.commit();
			con.setAutoCommit(true);
			ps1.close();

		} catch (SQLException ex) {
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in PurchaseDAO - GetSerial no   " + ex);
			i=-1;
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : "+i);

				if(ps1 != null){ps1.close();}

				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in SQLPurchaseDAO.Connection.close "+e);
			}
		}

		return sno;
	}	 	
	

	
	public int confirmationDialongHO()
	{
		Object[] options = {"Single", "Multiple"};
		return JOptionPane.showOptionDialog(this, "Are you sure?","Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null, options, options[0]);		
	}

	
	public int confirmationDialong()
	{
		Object[] options = {"No", "Yes"};
		return JOptionPane.showOptionDialog(this, "Are you sure?","Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null, options, options[0]);		
	}

	public int confirmationDialongYes(String text)
	{
		Object[] options = {"No", "Yes"};
		return JOptionPane.showOptionDialog(this, "Do you want to Re-generate Again?",""+text+" is Already Generated!!!", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null, options, options[0]);		
	}

	
	public void alertMessage(Component classObject,String messageBody)
	{
		JOptionPane.showMessageDialog(classObject, messageBody);
	}
	
	
	public static boolean checkInternetConnectivity(String site) {
	    Socket sock = new Socket();
	    InetSocketAddress addr = new InetSocketAddress(site,80);
	    try {
	        sock.connect(addr,12000);
	        return true;
	    } catch (IOException e) {
	        return false;
	    } finally {
	        try {sock.close();}
	        catch (IOException e) {}
	    }
	}
	
	/**
	 * 
	 * @param container : reference of classed the method called from
	 * @param buttonIndex : index of button where to set requestFocus
	 */
	public void setButtonBackgroundColor(JFrame container,int buttonIndex,KeyEvent keyEvent)
	{
		Component component[] = container.getContentPane().getComponents();
		List<JButton> buttonList=new ArrayList<JButton>();
		int buttonListSize=0;
		int currentButtonIndex=buttonIndex;
	    for(int i=0; i<component.length; i++)
	    {
	        if (component[i] instanceof JButton)
	        {
	            JButton button = (JButton)component[i];
	        	buttonList.add(button);
	        }
	    }
	    
	    buttonListSize=buttonList.size();
	    
	    if(keyEvent.getKeyCode()==KeyEvent.VK_LEFT){
	    	buttonIndex= buttonIndex==1 ? buttonIndex : --buttonIndex;
	    }
	    if(keyEvent.getKeyCode()==KeyEvent.VK_RIGHT){
	    	
	    	buttonIndex= buttonListSize==buttonIndex ? buttonIndex : ++buttonIndex;
	    }
	    
	   
	    
	    for(int i=0;i<buttonListSize;i++)
	    {
	    	JButton button = buttonList.get(i);
	    	
	    	if(currentButtonIndex==i+1)
	    	{
	    		button.setBackground(null);
	    	}
	    	
            if(keyEvent.getKeyCode()==KeyEvent.VK_LEFT)
            {
            	if(buttonIndex==i+1)
            	{
            		button.requestFocus();
            		button.setBackground(Color.blue);
            	}
            }

            if(keyEvent.getKeyCode()==KeyEvent.VK_RIGHT)
            {
            	if(buttonIndex==i+1)
            	{
            		button.requestFocus();
            		button.setBackground(Color.blue);
            	}
            }
            
	    }
	    
	    
	    
	}
	

  
	 
	 

	public String getMonthName(int moncode)
	{
		Vector monthlist=loginDt.getFmonth();
		int s=monthlist.size();
		String monthname="";
		MonthDto mdt=null;
		
		for (int i=0;i<s;i++)
		{
			mdt = (MonthDto) monthlist.get(i);
			if (mdt.getMnthcode()==moncode)
			{
				monthname=mdt.getMnthname();
				break;
			}
				
		}
		return monthname;
	}


	public int getMonthIndex(int moncode)
	{
		Vector monthlist=loginDt.getFmonth();
		int s=monthlist.size();
		int index=0;
		MonthDto mdt=null;
		
		for (int i=0;i<s;i++)
		{
			mdt = (MonthDto) monthlist.get(i);
			if (mdt.getMnthcode()==moncode)
			{
				index=i;
				break;
			}
				
		}
		return index;
	}


	public int getIndex(int bankcode)
	{
		Vector banklist=loginDt.getBankList();
		int s=banklist.size();
		int index=0;
		BankDto bdt=null;
		
		for (int i=0;i<s;i++)
		{
			bdt = (BankDto) banklist.get(i);
			if (bdt.getBank_code()==bankcode)
			{
				index=i;
				break;
			}
				
		}
		return index;
	}

	 

	public String getIpAdress()
	{

		InetAddress ip;
		String ipadd="";
	  	  try {
	   
	  		ip = InetAddress.getLocalHost();
	  		ipadd=ip.getHostAddress();
	   
	  	  } catch (UnknownHostException e) {
	   
	  		e.printStackTrace();
	   
	  	  }		
	  	  return ipadd;
	}
	

	/**
	 * 
	 * @param container : reference of classed the method called from
	 * @param buttonIndex : index of button where to set requestFocus
	 */
	public void setCaretPositionTextField(JFrame container)
	{
		Component component[] = container.getContentPane().getComponents();
		 
	    for(int i=0; i<component.length; i++)
	    {
	        if (component[i] instanceof JTextField)
	        {
	        	JTextField textfield = (JTextField)component[i];
	        	textfield.setCaretPosition(0);
	        }
	    }
	    
	}	    	
	
}
