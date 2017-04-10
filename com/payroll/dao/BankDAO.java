package com.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import com.payroll.dto.BankDto;



public class BankDAO 
{
      public Vector<?> getBankList(int div, int depo,int year)
      {
    	  
    	PreparedStatement ps2 = null;
  		ResultSet rs=null;
  		Connection con=null;
  		Vector  v=null;
  		int i=0; 
  		try 
  		{
  			con=ConnectionFactory.getConnection();


			String query2 ="select bank_code,bank_name from bankmast order by bank_code ";
			
  			ps2 = con.prepareStatement(query2);

  			rs= ps2.executeQuery();
  			con.setAutoCommit(false);
  			v = new Vector ();
  			Vector  col=null;
  			while (rs.next())
  			{
  				col = new Vector();
				col.add(new Boolean(false));
				col.add(rs.getInt(1));
				col.add(rs.getString(2));
				col.add("");
				v.add(col);
  				

  			}
  			con.commit();
  			con.setAutoCommit(true);
  			rs.close();
  			ps2.close();

  		} catch (SQLException ex) {
  			try {
  				con.rollback();
  			} catch (SQLException e) {
  				e.printStackTrace();
  			}
  			System.out.println("-------------Exception in SQLBankDAO.Banklist " + ex);
  			i=-1;
  		}
  		finally {
  			try {
  				System.out.println("No. of Records Update/Insert : "+i);

  				if(rs != null){rs.close();}
  				if(ps2 != null){ps2.close();}
  				if(con != null){con.close();}
  			} catch (SQLException e) {
  				System.out.println("-------------Exception in SQLBankDAO.Connection.close "+e);
  			}
  		}
  		return v;
  	}

	
	
      
      public int addBank(ArrayList<?> banklist)
      {
    	  
    	PreparedStatement ps2 = null;
  		 
  		Connection con=null;
  		BankDto bdto=null;
  		
  		int i=0;
  		try 
  		{
  			con=ConnectionFactory.getConnection();

  			String query2="insert into bankmast values (?,?)";

  			con.setAutoCommit(false);
			
  			ps2 = con.prepareStatement(query2);
  			int s=banklist.size();
  			 
  			for (int j=0;j<s;j++)
  			{
  				bdto= (BankDto) banklist.get(j);
	  			ps2.setInt(1,bdto.getBank_code());
	  			ps2.setString(2,bdto.getBank_name());
	  			i=ps2.executeUpdate();
  			}

  			con.commit();
  			con.setAutoCommit(true);
  			ps2.close();

  		} catch (SQLException ex) {
  			try {
  				con.rollback();
  			} catch (SQLException e) {
  				e.printStackTrace();
  			}
  			System.out.println("-------------Exception in SQLBankDAO.addBank " + ex);
  			i=-1;
  		}
  		finally {
  			try {
  				System.out.println("No. of Records Update/Insert : "+i);

  				if(ps2 != null){ps2.close();}
  				if(con != null){con.close();}
  			} catch (SQLException e) {
  				System.out.println("-------------Exception in SQLBankDAO.Connection.close "+e);
  			}
  		}
  		return i;
  	}


      public int updateBank(ArrayList<?> banklist)
      {
    	  
    	PreparedStatement ps2 = null;
  		 
  		Connection con=null;
  		BankDto bdto=null;
  		
  		int i=0;
  		try 
  		{
  			con=ConnectionFactory.getConnection();

  			String query2="update bankmast set bank_name=? where bank_code=? ";

  			con.setAutoCommit(false);
			
  			ps2 = con.prepareStatement(query2);
  			int s=banklist.size();
  			 
  			for (int j=0;j<s;j++)
  			{
  				bdto= (BankDto) banklist.get(j);
	  			ps2.setString(1,bdto.getBank_name());
	  			ps2.setInt(2,bdto.getBank_code());
	  			i=ps2.executeUpdate();
  			}

  			con.commit();
  			con.setAutoCommit(true);
  			ps2.close();

  		} catch (SQLException ex) {
  			try {
  				con.rollback();
  			} catch (SQLException e) {
  				e.printStackTrace();
  			}
  			System.out.println("-------------Exception in SQLBankDAO.updateBank " + ex);
  			i=-1;
  		}
  		finally {
  			try {
  				System.out.println("No. of Records Update/Insert : "+i);

  				if(ps2 != null){ps2.close();}
  				if(con != null){con.close();}
  			} catch (SQLException e) {
  				System.out.println("-------------Exception in SQLBankDAO.Connection.close "+e);
  			}
  		}
  		return i;
  	}

      public int deleteBank(ArrayList<?> banklist)
      {
    	  
    	PreparedStatement ps2 = null;
  		 
  		Connection con=null;
  		BankDto sdto=null;
  		
  		int i=0;
  		try 
  		{
  			con=ConnectionFactory.getConnection();

  			String query2="update bankmast set del_tag=? where bank_code=? ";

  			con.setAutoCommit(false);
			
  			ps2 = con.prepareStatement(query2);
  			int s=banklist.size();
  			for (int j=0;j<s;j++)
  			{
  				sdto= (BankDto) banklist.get(j);
	  			ps2.setString(1,"D");
	  			ps2.setInt(2,sdto.getBank_code());
	  			i=ps2.executeUpdate();
  			}

  			con.commit();
  			con.setAutoCommit(true);
  			ps2.close();

  		} catch (SQLException ex) {
  			try {
  				con.rollback();
  			} catch (SQLException e) {
  				e.printStackTrace();
  			}
  			System.out.println("-------------Exception in SQLBankDAO.deleteBank " + ex);
  			i=-1;
  		}
  		finally {
  			try {
  				System.out.println("No. of Records Update/Insert : "+i);

  				if(ps2 != null){ps2.close();}
  				if(con != null){con.close();}
  			} catch (SQLException e) {
  				System.out.println("-------------Exception in SQLBankDAO.Connection.close "+e);
  			}
  		}
  		return i;
  	}
	            
      
}
