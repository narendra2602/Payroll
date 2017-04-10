package com.payroll.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.payroll.dto.ContractMastDto;

public class CmpMsflDAO
{
	public ContractMastDto getCompInfo(int depo_code,int cmp_code)
	{
		PreparedStatement ps = null;
		ResultSet rs=null;
		Connection con=null;
		ContractMastDto cdo=null;
		Blob b=null;
		StringBuffer remark=new StringBuffer();
		String rem="";
		BufferedReader br;

		try 
		{
			con=ConnectionFactory.getConnection();
			con.setAutoCommit(false);
			cdo = new ContractMastDto();

			String compQ ="select ifnull(cmp_name,''),ifnull(cmp_add1,''),ifnull(cmp_add2,''),ifnull(cmp_add3,''),ifnull(cmp_city,''),ifnull(cmp_pin,'')," +
			"ifnull(cmp_phone,''),ifnull(cmp_mobile,''),ifnull(cmp_fax,''),ifnull(cmp_email,'')," +
			"ifnull(employer_name,''),ifnull(employer_type,''),ifnull(employer_contribution,0)," +
			"ifnull(employee_contribution,0),banker,ifnull(license_no,''),ifnull(cmp_panno,''), " +
			"ifnull(postal_add,''),ifnull(residential_add,''),ifnull(director_add,''),ifnull(manager_add,''),ifnull(sertax_no,'') " +
			" from contractormast where depo_code=? and cmp_code=? ";

			////////////////////////////////////////////////
			ps = con.prepareStatement(compQ);
			ps.setInt(1, depo_code);
			ps.setInt(2, cmp_code);
			rs= ps.executeQuery();
			if (rs.next())
			{
				cdo.setCmp_name(rs.getString(1));
				cdo.setCmp_add1(rs.getString(2));
				cdo.setCmp_add2(rs.getString(3));
				cdo.setCmp_add3(rs.getString(4));
				cdo.setCmp_city(rs.getString(5));
				cdo.setCmp_pin(rs.getInt(6));
				cdo.setCmp_phone(rs.getString(7));
				cdo.setCmp_mobile(rs.getString(8));
				cdo.setCmp_fax(rs.getString(9));
				cdo.setCmp_email(rs.getString(10));
				cdo.setEmployer_name(rs.getString(11));
				cdo.setEmployer_type(rs.getString(12));
				cdo.setEmployer_cont(rs.getDouble(13));
				cdo.setEmployee_cont(rs.getDouble(14));
				cdo.setBanker(rs.getString(15));
				cdo.setLicense_no(rs.getString(16));
				cdo.setCmp_panno(rs.getString(17));
				b=rs.getBlob(18);  // postal addrsss
				remark=new StringBuffer();
				rem="";
				br=new BufferedReader(new InputStreamReader(b.getBinaryStream()));
				try {
					while ((rem=br.readLine())!=null)
					{
						remark.append(rem+"\n");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cdo.setPostal_add(remark.toString());

				b=rs.getBlob(19);  // residential
				remark=new StringBuffer();
				rem="";
				br=new BufferedReader(new InputStreamReader(b.getBinaryStream()));
				try {
					while ((rem=br.readLine())!=null)
					{
						remark.append(rem+"\n");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cdo.setResidential_add(remark.toString());

				
				
				b=rs.getBlob(20);  // director addrsss
				remark=new StringBuffer();
				rem="";
				br=new BufferedReader(new InputStreamReader(b.getBinaryStream()));
				try {
					while ((rem=br.readLine())!=null)
					{
						remark.append(rem+"\n");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cdo.setDirector_add(remark.toString());

				b=rs.getBlob(21);  // manager addrsss
				remark=new StringBuffer();
				rem="";
				br=new BufferedReader(new InputStreamReader(b.getBinaryStream()));
				try {
					while ((rem=br.readLine())!=null)
					{
						remark.append(rem+"\n");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cdo.setManager_add(remark.toString());
				
				cdo.setSertax_no(rs.getString(22));
			}

	 
			
			con.commit();
			con.setAutoCommit(true);
			rs.close();
			 
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in CmpMsflDAO.getCompInfo " + ex);

		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : " );

				if(rs != null){rs.close();}
				if(ps != null){rs.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in CmpMsflDAO.Connection.close "+e);
			}
		}
		return cdo;
	}

	
	
    public int updateCompanyInfo(ContractMastDto cmp)
    {
  	  
  	PreparedStatement ps2 = null;
		 
		Connection con=null;
		
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();

			String query2="update contractormast set cmp_add1=?,cmp_add2=?,cmp_add3=?,cmp_city=?,cmp_pin=?,cmp_phone=?,cmp_fax=?,cmp_email=?," +
			"cmp_panno=?,license_no=?,employer_name=?,employer_type=?,employer_contribution=?,employee_contribution=?,banker=?,sertax_no=? where depo_code=? and cmp_code=?  ";
			
			con.setAutoCommit(false);
			ps2 = con.prepareStatement(query2);
			ps2.setString(1,cmp.getCmp_add1());
			ps2.setString(2,cmp.getCmp_add2());
			ps2.setString(3,cmp.getCmp_add3());
			ps2.setString(4,cmp.getCmp_city());
			ps2.setInt(5,cmp.getCmp_pin());
			ps2.setString(6,cmp.getCmp_phone());
			ps2.setString(7,cmp.getCmp_fax());
			ps2.setString(8,cmp.getCmp_email());
			ps2.setString(9,cmp.getCmp_panno());
			ps2.setString(10,cmp.getLicense_no());
			ps2.setString(11,cmp.getEmployer_name());
			ps2.setString(12,cmp.getEmployer_type());
			ps2.setDouble(13,cmp.getEmployer_cont());
			ps2.setDouble(14,cmp.getEmployee_cont());
			ps2.setString(15,cmp.getBanker());
			ps2.setString(16,cmp.getSertax_no());
			ps2.setInt(17,cmp.getDepo_code());
			ps2.setInt(18,cmp.getCmp_code());
			
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
			System.out.println("-------------Exception in SQLCmpmsflDAO.updateCompany " + ex);
			i=-1;
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : "+i);

				if(ps2 != null){ps2.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in SQLCmpmsflDAO.Connection.close "+e);
			}
		}
		return i;
	}
	
    public int addCompanyInfo(ContractMastDto cmp)
    {
  	  
    	PreparedStatement ps2 = null;
		 
		Connection con=null;
		
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();

			String query2="insert into contractormast values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			con.setAutoCommit(false);
			ps2 = con.prepareStatement(query2);
			ps2.setInt(1,cmp.getDepo_code());
			ps2.setInt(2,cmp.getCmp_code());
			ps2.setString(3,cmp.getCmp_name());
			try {
				ps2.setString(4,cmp.getCmp_name().substring(0, 4));
			} catch (Exception e) {
				ps2.setString(4,cmp.getCmp_abvr());
			}
			ps2.setString(5,cmp.getCmp_add1());
			ps2.setString(6,cmp.getCmp_add2());
			ps2.setString(7,cmp.getCmp_add3());
			ps2.setString(8,cmp.getCmp_city());
			ps2.setInt(9,cmp.getCmp_pin());
			ps2.setString(10,cmp.getCmp_phone());
			ps2.setString(11,cmp.getCmp_mobile());
			ps2.setString(12,cmp.getCmp_fax());
			ps2.setString(13,cmp.getCmp_email());
			ps2.setString(14,cmp.getEmployer_name());
			ps2.setString(15,cmp.getEmployer_type());
			ps2.setDouble(16,cmp.getEmployer_cont());
			ps2.setDouble(17,cmp.getEmployee_cont());
			ps2.setString(18,cmp.getBanker());
			ps2.setString(19,cmp.getLicense_no());
			ps2.setString(20,cmp.getCmp_panno());
			ps2.setString(21,cmp.getPostal_add());
			ps2.setString(22,cmp.getResidential_add());
			ps2.setString(23,cmp.getDirector_add());
			ps2.setString(24,cmp.getManager_add());
			ps2.setString(25,cmp.getSertax_no());
			
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
			System.out.println("-------------Exception in SQLCmpmsflDAO.addCompanyInfo " + ex);
			i=-1;
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : "+i);

				if(ps2 != null){ps2.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in SQLCmpmsflDAO.Connection.close "+e);
			}
		}
		return i;
	}
	
}
