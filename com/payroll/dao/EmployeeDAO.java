package com.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import com.payroll.dto.EmployeeMastDto;

public class EmployeeDAO
{

    public Vector getEmployeeList(int depo_code,int cmp_code)
  	{
  		PreparedStatement ps = null;
  		ResultSet rs=null;
  		Connection con=null;
  		Vector v =null;
  		Vector col =null;
  		EmployeeMastDto emp=null;
  		 
  		try 
  		{
  			con=ConnectionFactory.getConnection();
  			con.setAutoCommit(false);

  			String query="select EMP_CODE, EMP_NAME, DESIGNATION, father_name, surname, department, MADD1, madd2, madd3, MCITY, MSTATE, MPIN, MPHONE, MOBILE, "+ 
			"MEMAIL, esic_no, pf_no, pan_no, DOBirth, DOjoin, DOresign, gross, basic, da, hra, add_hra, incentive, spl_incentive, lta, medical, bonus, ot_rate, "+ 
			"stair_alw, label_print, emp_status, paymentmode, ifnull(bank,'Direct Cheque'), bank_add1, ifsc_code,bank_accno,bank_code,uan_no,bonus_per,bonus_check  "+
			"from employeemast where depo_code=? and cmp_code=?  ";


  			 
  			ps = con.prepareStatement(query);
  			ps.setInt(1, depo_code);
  			ps.setInt(2, cmp_code);
  			rs =ps.executeQuery();
  			
  			v = new Vector();

  			while (rs.next())
  			{
  				emp = new EmployeeMastDto();
  				emp.setEmp_code(rs.getInt(1));
  				emp.setEmp_name(rs.getString(2)); 
  				emp.setDesignation(rs.getString(3));
  				emp.setFather_name(rs.getString(4)); 
  				emp.setSurname(rs.getString(5));
  				emp.setDepartment(rs.getString(6));
  				emp.setMadd1(rs.getString(7));
  				emp.setMadd2(rs.getString(8));
  				emp.setMadd3(rs.getString(9));
  				emp.setMcity(rs.getString(10));
  				emp.setMstate(rs.getString(11));
  				emp.setMpin(rs.getString(12));
  				emp.setMphone(rs.getString(13));
  				emp.setMobile(rs.getString(14));
  				emp.setMemail(rs.getString(15));
  				emp.setEsic_no(rs.getLong(16));
  				emp.setPf_no(rs.getInt(17));
  				emp.setPan_no(rs.getString(18));
  				emp.setDobirth(rs.getDate(19));
  				emp.setDojoin(rs.getDate(20));
  				emp.setDoresign(rs.getDate(21));
  				emp.setGross(rs.getDouble(22));
  				emp.setBasic(rs.getDouble(23));
  				emp.setDa(rs.getDouble(24));
  				emp.setHra(rs.getDouble(25));
  				emp.setAdd_hra(rs.getDouble(26));
  				emp.setIncentive(rs.getDouble(27));
  				emp.setSpl_incentive(rs.getDouble(28));
  				emp.setLta(rs.getDouble(29));
  				emp.setMedical(rs.getDouble(30));
  				emp.setBonus(0.00); // used in bonus_limit
  				emp.setOt_rate(rs.getDouble(32));
  				emp.setStair_alw(rs.getDouble(33));
  				emp.setLabel_print(rs.getString(34));
  				emp.setEmp_status(rs.getString(35));
  				
  				// setting bank details
  				emp.setBank(rs.getString(37).length()>1?rs.getString(37):"Direct Cheque");
  				emp.setBank_add1(rs.getString(38));
  				emp.setIfsc_code(rs.getString(39));
  				emp.setBank_accno(rs.getString(40));
  				emp.setBank_code(rs.getInt(41));
  				emp.setUan_no(rs.getLong(42));
  				emp.setBonus_per(rs.getDouble(43));
  				emp.setBonus_limit(rs.getDouble(31)); // same bonus varaible will be used
  				emp.setBonus_check(rs.getDouble(44));

  				col= new Vector();
				col.addElement(rs.getString(1));//emp_code
				col.addElement(rs.getString(2));//emp name
				col.addElement(emp);
				
  				v.add(col);
  			}



  			con.commit();
  			con.setAutoCommit(true);
  			rs.close();
  			ps.close();

  		} catch (Exception ex) {
  			try {
  				con.rollback();
  			} catch (SQLException e) {
  				e.printStackTrace();
  			}
  			System.out.println("-------------Exception in EmployeeDAO.getEmployeeList " + ex);

  		}
  		finally {
  			try {
  				System.out.println("No. of Records Update/Insert : " );

  				if(rs != null){rs.close();}
  				if(ps != null){ps.close();}
  				if(con != null){con.close();}
  			} catch (SQLException e) {
  				System.out.println("-------------Exception in EmployeeDAO.Connection.close --- getEmployeeList "+e);
  			}
  		}
  		return v;
  	}    
          
    
    
    public int addEmployee(EmployeeMastDto edto)
    {
  	  
  	PreparedStatement ps2 = null;
		 
		Connection con=null;
		
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();

			String query2="insert into employeemast (DEPO_CODE, cmp_code, EMP_CODE, EMP_NAME, father_name, designation, "+ //6
					"MADD1, madd2, madd3, MCITY, MSTATE, MPIN, MPHONE, MOBILE, MEMAIL, esic_no, pf_no, "+ //17
					"pan_no, DOBirth, DOjoin, DOresign, gross, basic, da, hra, add_hra, incentive, "+ //27
					"spl_incentive, lta, medical, bonus, ot_rate, stair_alw, label_print, emp_status,bank,bank_add1,ifsc_code,bank_accno,bank_code,uan_no,bonus_per,bonus_check) "+//40
					"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			con.setAutoCommit(false);
			
			ps2 = con.prepareStatement(query2);
			ps2.setInt(1,edto.getDepo_code());
  			ps2.setInt(2,edto.getCmp_code());
  			ps2.setInt(3,edto.getEmp_code());
  			ps2.setString(4,edto.getEmp_name());
  			ps2.setString(5,edto.getFather_name());
  			ps2.setString(6,edto.getDepartment());
  			ps2.setString(7,edto.getMadd1());
  			ps2.setString(8,edto.getMadd2());
  			ps2.setString(9,edto.getMadd3());
  			ps2.setString(10,edto.getMcity());
  			ps2.setString(11,edto.getMstate());
  			ps2.setString(12,edto.getMpin());
  			ps2.setString(13,edto.getMphone());
  			ps2.setString(14,edto.getMobile());
  			ps2.setString(15,edto.getMemail());
  			ps2.setLong(16,edto.getEsic_no());
  			ps2.setInt(17,edto.getPf_no());
  			ps2.setString(18,edto.getPan_no());
  			ps2.setDate(19,setSqlDate(edto.getDobirth()));
  			ps2.setDate(20,setSqlDate(edto.getDojoin()));
  			ps2.setDate(21,setSqlDate(edto.getDoresign()));
  			ps2.setDouble(22,edto.getGross());
  			ps2.setDouble(23,edto.getBasic());
  			ps2.setDouble(24,edto.getDa());
  			ps2.setDouble(25,edto.getHra());
  			ps2.setDouble(26,edto.getAdd_hra());
  			ps2.setDouble(27,edto.getIncentive());
  			ps2.setDouble(28,edto.getSpl_incentive());
  			ps2.setDouble(29,edto.getLta());
  			ps2.setDouble(30,edto.getMedical());
  			ps2.setDouble(31,edto.getBonus_limit());
  			ps2.setDouble(32,edto.getOt_rate());
  			ps2.setDouble(33,edto.getStair_alw());
  			ps2.setString(34, edto.getLabel_print());
  			ps2.setString(35, "Y");  // emp_status
  			ps2.setString(36, edto.getBank());  
  			ps2.setString(37, edto.getBank_add1());  
  			ps2.setString(38, edto.getIfsc_code());  
  			ps2.setString(39, edto.getBank_accno());  
  			ps2.setInt(40,edto.getBank_code());
  			ps2.setLong(41,edto.getUan_no());
  			ps2.setDouble(42,edto.getBonus_per());
  			ps2.setDouble(43,edto.getBonus_check());

  			i=ps2.executeUpdate();
			 

			con.commit();
			con.setAutoCommit(true);
			ps2.close();

		} catch (SQLException ex) {
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in SQLEmployeeDAO.addEmployee " + ex);
			i=-1;
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : "+i);

				if(ps2 != null){ps2.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in SQLEmployeeDAO.Connection.close "+e);
			}
		}
		return i;
	}

    
    
    public int updateEmployee(EmployeeMastDto edto,int repno)
    {
  	  
  	PreparedStatement ps2 = null;
		 
		Connection con=null;
		
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();

			String query1="update employeemast set bank=?,bank_add1=?,ifsc_code=?,bank_accno=?,bank_code=?,bonus=?,bonus_per=?,bonus_check=?  "+
			" where depo_code=? and cmp_code=? and emp_code=? ";

			String query2="update employeemast set emp_name=?,father_name=?,designation=?,madd1=?,madd2=?,madd3=?,mcity=?,mstate=?,mpin=?,mphone=?,mobile=?,memail=?," +
			"esic_no=?,pf_no=?,pan_no=?,dobirth=?,dojoin=?,doresign=?,gross=?,basic=?,da=?,hra=?,add_hra=?,incentive=?," +
			"spl_incentive=?,lta=?,medical=?,bonus=?,ot_rate=?,stair_alw=?,label_print=?,emp_status=?,uan_no=?   "+
			" where depo_code=? and cmp_code=? and emp_code=? ";
			
			
			
			con.setAutoCommit(false);
			
			if(repno==1)
			{
				ps2 = con.prepareStatement(query2);
				ps2.setString(1,edto.getEmp_name());
				ps2.setString(2,edto.getFather_name());
				ps2.setString(3,edto.getDepartment());
				ps2.setString(4,edto.getMadd1());
				ps2.setString(5,edto.getMadd2());
				ps2.setString(6,edto.getMadd3());
				ps2.setString(7,edto.getMcity());
				ps2.setString(8,edto.getMstate());
				ps2.setString(9,edto.getMpin());

				ps2.setString(10,edto.getMphone());
				ps2.setString(11,edto.getMobile());
				ps2.setString(12,edto.getMemail());

				ps2.setLong(13,edto.getEsic_no());
				ps2.setInt(14,edto.getPf_no());
				ps2.setString(15,edto.getPan_no());

				ps2.setDate(16,setSqlDate(edto.getDobirth()));
				ps2.setDate(17,setSqlDate(edto.getDojoin()));
				ps2.setDate(18,setSqlDate(edto.getDoresign()));

				ps2.setDouble(19,edto.getGross());
				ps2.setDouble(20,edto.getBasic()); 
				ps2.setDouble(21,edto.getDa()); 
				ps2.setDouble(22,edto.getHra()); 
				ps2.setDouble(23,edto.getAdd_hra()); 
				ps2.setDouble(24,edto.getIncentive()); 
				ps2.setDouble(25,edto.getSpl_incentive()); 
				ps2.setDouble(26,edto.getLta()); 
				ps2.setDouble(27,edto.getMedical()); 
				ps2.setDouble(28,edto.getBonus_limit()); 
				ps2.setDouble(29,edto.getOt_rate()); 
				ps2.setDouble(30,edto.getStair_alw()); 
				ps2.setString(31, edto.getLabel_print());
				ps2.setString(32, edto.getEmp_status());  // emp_status
				ps2.setLong(33,edto.getUan_no());


				// where clause 
				ps2.setInt(34,edto.getDepo_code());
				ps2.setInt(35,edto.getCmp_code());
				ps2.setInt(36,edto.getEmp_code());

				i=ps2.executeUpdate();
			}
			else
			{
				ps2 = con.prepareStatement(query1);
				ps2.setString(1, edto.getBank());  
				ps2.setString(2, edto.getBank_add1());  
				ps2.setString(3, edto.getIfsc_code());  
				ps2.setString(4, edto.getBank_accno());  
				ps2.setInt(5,edto.getBank_code());
				ps2.setDouble(6,edto.getBonus_limit()); 
				ps2.setDouble(7,edto.getBonus_per()); 
				ps2.setDouble(8,edto.getBonus_check()); 
				// where clause 
				ps2.setInt(9,edto.getDepo_code());
				ps2.setInt(10,edto.getCmp_code());
				ps2.setInt(11,edto.getEmp_code());

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
			System.out.println("-------------Exception in SQLEmployeeDAO.updateEmployee " + ex);
			i=-1;
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : "+i);

				if(ps2 != null){ps2.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in SQLEmployeeDAO.Connection.close "+e);
			}
		}
		return i;
	}

    
    public int updateSalaryStructure(EmployeeMastDto edto)
    {
  	  
  	PreparedStatement ps2 = null;
		 
		Connection con=null;
		
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();

			String query2="update employeemast set	gross=?,basic=?,da=?,hra=?,add_hra=?,incentive=?," +
			"spl_incentive=?,lta=?,medical=?,bonus=?,ot_rate=?,stair_alw=?  "+
			" where depo_code=? and cmp_code=? ";
			
			con.setAutoCommit(false);
			
			ps2 = con.prepareStatement(query2);
			ps2.setDouble(1,edto.getGross());
			ps2.setDouble(2,edto.getBasic()); 
			ps2.setDouble(3,edto.getDa()); 
			ps2.setDouble(4,edto.getHra()); 
			ps2.setDouble(5,edto.getAdd_hra()); 
			ps2.setDouble(6,edto.getIncentive()); 
			ps2.setDouble(7,edto.getSpl_incentive()); 
			ps2.setDouble(8,edto.getLta()); 
			ps2.setDouble(9,edto.getMedical()); 
			ps2.setDouble(10,edto.getBonus()); 
			ps2.setDouble(11,edto.getOt_rate()); 
			ps2.setDouble(12,edto.getStair_alw()); 

  			// where clause 
			ps2.setInt(13,edto.getDepo_code());
			ps2.setInt(14,edto.getCmp_code());
			
	  		i=ps2.executeUpdate();

			con.commit();
			con.setAutoCommit(true);
			ps2.close();

		} catch (SQLException ex) {
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in SQLEmployeeDAO.updateSalaryStructure " + ex);
			i=-1;
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : "+i);

				if(ps2 != null){ps2.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in SQLEmployeeDAO.Connection.close "+e);
			}
		}
		return i;
	}

    

    public EmployeeMastDto getSalaryStructure(int depo,int cmpcode)
    {
  	  
    	PreparedStatement ps2 = null;
    	ResultSet rs2=null;
    	EmployeeMastDto edto=null; 
		Connection con=null;
		
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();

			String query2="select gross, basic, da, hra, add_hra, incentive, spl_incentive, lta, medical, bonus, ot_rate, stair_alw" +
			" from  employeesalary  where depo_code=? and cmp_code=? ";
			
			con.setAutoCommit(false);
			
			ps2 = con.prepareStatement(query2);
			ps2.setInt(1,depo);
			ps2.setInt(2,cmpcode);
			rs2=ps2.executeQuery();
			
			if(rs2.next())
			{
				edto= new EmployeeMastDto();
				edto.setGross(rs2.getDouble(1));
				edto.setBasic(rs2.getDouble(2));
				edto.setDa(rs2.getDouble(3));
				edto.setHra(rs2.getDouble(4));
				edto.setAdd_hra(rs2.getDouble(5));
				edto.setIncentive(rs2.getDouble(6));
				edto.setSpl_incentive(rs2.getDouble(7));
				edto.setLta(rs2.getDouble(8));
				edto.setMedical(rs2.getDouble(9));
				edto.setBonus(rs2.getDouble(10));
				edto.setOt_rate(rs2.getDouble(11));
				edto.setStair_alw(rs2.getDouble(12));
			}
			

			con.commit();
			con.setAutoCommit(true);
			rs2.close();
			ps2.close();

		} catch (SQLException ex) {
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in SQLEmployeeDAO.getSalaryStructure " + ex);
			i=-1;
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : "+i);

				if(rs2 != null){rs2.close();}
				if(ps2 != null){ps2.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in SQLEmployeeDAO.Connection.close "+e);
			}
		}
		return edto;
	}


  	public boolean checkEmployeeExists(int depo_code,int cmpcode,int empcode)
  	{
  		PreparedStatement ps2 = null;
  		ResultSet rs=null;
  		Connection con=null;
  		boolean check=false;
  		try {

  			con=ConnectionFactory.getConnection();
  			con.setAutoCommit(false);

  			String query2 = "select emp_code from employeemast where  depo_code=? and " +
  			"cmp_code=? and emp_code=? and ifnull(del_tag,'')<>'D' " ;
  			
  			
  			ps2 = con.prepareStatement(query2);
  			ps2.setInt(1,depo_code);
  			ps2.setInt(2, cmpcode);
  			ps2.setInt(3, empcode);
  			
  			rs= ps2.executeQuery();
  			if (rs.next())
  			{
  				 check=true;

  			}

  			con.commit();
  			con.setAutoCommit(true);
  			rs.close();
  			ps2.close();

  		} 
  		catch (SQLException ex) 
  		{
  			try 
  			{
  				con.rollback();
  			} 
  			catch (SQLException e) 
  			{
  				e.printStackTrace();
  			}
  			System.out.println("-------------Exception in SQLEmployeeDAO.checkEmployeeExists " + ex);

  		}
  		finally {
  			try {
  				//				System.out.println("No. of Records Update/Insert : "+i);

  				if(rs != null){rs.close();}
  				if(ps2 != null){ps2.close();}
  				if(con != null){con.close();}
  			} catch (SQLException e) {
  				System.out.println("-------------Exception in SQLEmployeeDAO.Connection.close "+e);
  			}
  		}

  		return check;
  	}
      

    
    public java.sql.Date setSqlDate(Date javadate)
	{
		      java.sql.Date sqlDate = null;
		      try {
				sqlDate = new java.sql.Date(javadate.getTime());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				sqlDate=null;
			}
		      return sqlDate;  
	}

	

    
}
