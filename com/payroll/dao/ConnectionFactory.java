package com.payroll.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

public class ConnectionFactory {

	public static Connection getConnection(){
		Connection con = null;
		ResourceBundle rb=null;
		String host=null;
		String port=null;
		String dbname=null;
		String user=null;
		String password=null;
		try {
			rb = ResourceBundle.getBundle("Payroll");
			Class.forName("com.mysql.jdbc.Driver");
			host=rb.getString("host");
			port=rb.getString("port");
			dbname=rb.getString("dbname");
			user=rb.getString("user");
			password=rb.getString("password");
			
			con = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+dbname+"",""+user+"",""+password+"");			  
			//con = DriverManager.getConnection("jdbc:mysql://localhost/aristo","root","spk67890");
			//con = DriverManager.getConnection("jdbc:mysql://localhost:3406/aristo","root","654321");
			
			  
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
        return con;
	}

	public static String getDrvnm(){
		ResourceBundle rb=null;
		String drvnm=null;
		try {
			rb = ResourceBundle.getBundle("Payroll");
			drvnm=rb.getString("netnm");
			  
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
        return drvnm;
	}

	public static String getPrinternm(){
		ResourceBundle rb=null;
		String drvnm=null;
		try {
			rb = ResourceBundle.getBundle("Payroll");
			drvnm=rb.getString("printernm");
			  
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
        return drvnm;
	}
	
	public static String getBtnnm(){
		ResourceBundle rb=null;
		String drvnm=null;
		try {
			rb = ResourceBundle.getBundle("Payroll");
			drvnm=rb.getString("btnnm");
			  
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
        return drvnm;
	}
	
	
	public static String[] getUINamePassword()
	{
		ResourceBundle rb=null;
		String UINamePassword[] = new String[2];
		try 
		{
			rb = ResourceBundle.getBundle("Payroll");
			UINamePassword[0]=rb.getString("UIname");
			UINamePassword[1]=rb.getString("UIpass");
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
			UINamePassword[0]=null;
		}
        return UINamePassword;
	}
	
	
	
}
 