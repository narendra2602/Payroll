package com.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.payroll.dto.ChildMenuDto;
import com.payroll.dto.MenuDto;
import com.payroll.dto.SubMenuDto;

 

public class MenuDAO
{

	public ArrayList getMainMenu(int pack_code,int uid)
	{
		PreparedStatement ps = null;
		ResultSet rs=null;
		
		PreparedStatement ps1 = null;
		ResultSet rs1=null;

		PreparedStatement ps2 = null;
		ResultSet rs2=null;
		
		Connection con=null;
		ArrayList l=null;
		ArrayList m=null;
		ArrayList s=null;
		MenuDto md=null;
		SubMenuDto sm=null;
		ChildMenuDto cm=null;
		boolean notadd=true;
		boolean snotadd=true;
		
		try 
		{
			con=ConnectionFactory.getConnection();
			con.setAutoCommit(false);

			String menuQ ="select tab_code,tab_name from tabmaster  where tab_code in "+
						" (select distinct(tab_code) from userrights where user_id=?  and pack_code=? and status='Y') order by tab_code";
			
			
			String subQ="select menu_code,menu_name,class_name,sub_class from menumaster where pack_code=? and menu_code in" +
					"(select distinct(menu_code) from userrights where user_id=? and pack_code=? and tab_code=?  and status='Y') order by pack_code,tab_code,serialno ";
			
			String submQ="select smenu_code,smenu_name,class_name,sub_class from submenumaster  where smenu_code in "+
			" (select distinct(smenu_code) from userrights where user_id=? and pack_code=?  and tab_code=? and menu_code =?  and status='Y' and smenu_code<>0) order by smenu_code ";
			
			////////////////////////////////////////////////
			ps = con.prepareStatement(menuQ);
			ps.setInt(1, uid);
			ps.setInt(2, pack_code);
			rs= ps.executeQuery();
			
			ps1 = con.prepareStatement(subQ);
			ps2 = con.prepareStatement(submQ);

			l= new ArrayList();
			while (rs.next())
			{

				ps1.setInt(1, pack_code); // package code
				ps1.setInt(2, uid); // user id
				ps1.setInt(3, pack_code); // package code
				ps1.setInt(4, rs.getInt(1));
				
				rs1=ps1.executeQuery();
				

				md = new MenuDto();
				md.setTab_code(rs.getInt(1));
				md.setTab_name(rs.getString(2));
				m= new ArrayList();
				while(rs1.next())
				{
					notadd=true;
					ps2.setInt(1, uid); // user id
					ps2.setInt(2, pack_code); // package code
					ps2.setInt(3,rs.getInt(1));//tab code
					ps2.setInt(4, rs1.getInt(1)); //menu code
					
					rs2=ps2.executeQuery();
					sm=new SubMenuDto();
					sm.setMenu_code(rs.getString(1)+rs1.getString(1));
					sm.setMenu_name(rs1.getString(2));
					sm.setClass_name(rs1.getString(3));
					sm.setSubclass_name(rs1.getString(4));
					s = new ArrayList();
					while(rs2.next())
					{
						snotadd=true;
						cm=new ChildMenuDto();
						cm.setSmenu_code(rs.getString(1)+rs1.getString(1)+rs2.getString(1));
						cm.setSmenu_name(rs2.getString(2));
						cm.setClass_name(rs2.getString(3));
						cm.setSubclass_name(rs2.getString(4));
						if (rs.getInt(1)==4 && rs2.getString(3).trim().equals(""))
						{
							snotadd=false;
						}
						if (snotadd)
						s.add(cm);
//						s.put(rs.getString(1)+rs1.getString(1)+rs2.getString(1),rs2.getString(2));
					}
					 
					   sm.setSmenu_name(s);
						if (rs.getInt(1)==5 && rs1.getString(3).trim().equals(""))
						{
							notadd=false;
						}
						if (rs.getInt(1)==6 && rs1.getString(3).trim().equals(""))
						{
							notadd=false;
						}
						if (notadd)
							m.add(sm);
				}
				md.setMenu_name(m);
				
				l.add(md);
			}

		
			con.commit();
			con.setAutoCommit(true);
			rs.close();
			 
		} catch (SQLException ex) {
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in MenuDAO.getMenu " + ex);

		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : " );

				if(rs != null){rs.close();}
				if(ps != null){ps.close();}
				if(rs1 != null){rs1.close();}
				if(ps1 != null){ps1.close();}
				if(rs2 != null){rs2.close();}
				if(ps2 != null){ps2.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in MenuDAO.Connection.close "+e);
			}
		}
		return l;
	}

	
	
}
