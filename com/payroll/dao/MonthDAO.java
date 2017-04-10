package com.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;

import com.payroll.dto.LoginDto;
import com.payroll.dto.MonthDto;
import com.payroll.dto.YearDto;

public class MonthDAO 
{
	public int setMonth(int mon,int year,LoginDto ldo)
	{
		PreparedStatement ps = null;
		ResultSet rs=null;

		Connection con=null;
		 
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();
			con.setAutoCommit(false);

			String monQ ="select mth,fin_year,fin_desc,mnth_code,frdate,todate,fin_ord,yy from perdmast  where fin_year=? and mnth_code=?";

	 
			////////////////////////////////////////////////
			ps = con.prepareStatement(monQ);
			ps.setInt(1, year);
			ps.setInt(2, mon);
			rs= ps.executeQuery();
			if (rs.next())
			{
				ldo.setMessage("Accounting Year: "+rs.getString(3)+"    Month: "+rs.getString(1)+"-"+rs.getInt(8));
				ldo.setFooter("Financial Accounting Year: "+rs.getString(3)+"    Processing Month: "+rs.getString(1)+"-"+rs.getInt(8));
				ldo.setFin_year(rs.getInt(2));
				ldo.setMnth_code(rs.getInt(4));
				ldo.setSdate(rs.getDate(5));
				ldo.setEdate(rs.getDate(6));
				ldo.setMnthName(rs.getString(1));
				ldo.setMno(rs.getInt(7)-1);
				i=1;
			}
			
			
			rs.close();
			ps.close();
			////////////////////////////////////////////////
			
			
		
			
			con.commit();
			con.setAutoCommit(true);
			rs.close();
			ps.close();
			 

		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in MonthDAO.setMonth " + ex);

		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : " );

				if(rs != null){rs.close();}
				if(ps != null){ps.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in MonthDAO.Connection.close "+e);
			}
		}
		return i;
	}

	public int setYear(int year,LoginDto ldo)
	{
		PreparedStatement ps = null;
		ResultSet rs=null;
		PreparedStatement ps4 = null;
		ResultSet rs4=null;
		PreparedStatement fst1 = null;
		ResultSet frs1=null;
		
		PreparedStatement ps1 = null;
		ResultSet rs1=null;
		
		PreparedStatement monps=null;
		
		Statement st3=null;

		ResultSet monrs=null;
		ResultSet rs3=null;
		HashMap hm=null;

		int fyear=0;

		
		Connection con=null;
		int i=0;
		int mno=0;
		 
		try 
		{
			con=ConnectionFactory.getConnection();
			con.setAutoCommit(false);

			 
			
			String monQ ="select mth,fin_year,fin_desc,mnth_code,frdate,todate,fin_ord,yy from perdmast  where fin_year=? and mnth_code=?";

		
			String monfin ="select month_nm,mnth_code,frdate,todate,mth,yy,fin_ord,day(todate)  from perdmast where fin_year=? order by fin_ord ";

			String yearMax="select frdate,todate from yearfil where  year=? and  typ='F'";

			String finyear ="select year,description,frdate,todate from yearfil where typ='F' order by year desc ";
			String mnthfin ="select mnth_code,month_nm,frdate,todate,mth,yy,fin_ord,day(todate) from perdmast where fin_year=? order by fin_ord";

			 
  
		 
			ps = con.prepareStatement(monQ);
			ps.setInt(1, year);
			ps.setInt(2, mno);
			rs= ps.executeQuery();
			if (rs.next())
			{
				 

				ldo.setMessage("Accounting Year: "+rs.getString(3)+"    Month: "+rs.getString(1)+"-"+rs.getInt(8));
				ldo.setFooter("Financial Accounting Year: "+rs.getString(3)+"    Processing Month: "+rs.getString(1)+"-"+rs.getInt(8));
				ldo.setFin_year(rs.getInt(2));
				ldo.setMnth_code(rs.getInt(4));
				
				ldo.setSdate(rs.getDate(5));
				ldo.setEdate(rs.getDate(6));
				ldo.setMnthName(rs.getString(1));
				ldo.setMno(rs.getInt(7)-1);
				i=1;
			}
			////////////////////////////////////////////////
			
			Vector mf=null;
			MonthDto md=null;
			/////////// fin year month///////////
			fst1=con.prepareStatement(monfin);
			fst1.setInt(1,year);
			frs1=fst1.executeQuery();
			
			mf = new Vector();
			while (frs1.next())
			{
				md=new MonthDto();
				md.setMnthname(frs1.getString(1)+"-"+frs1.getString(6));
				md.setMnthcode(frs1.getInt(2));
				md.setSdate(frs1.getDate(3));
				md.setEdate(frs1.getDate(4));
				md.setMnthabbr(frs1.getString(5));
				md.setMnthno(frs1.getInt(7));
				md.setMnthdays(frs1.getInt(8));
				mf.add(md);

			}
			ldo.setFmonth(mf);
	
			fst1=null;
			frs1=null;
			fst1=con.prepareStatement(yearMax);
			fst1.setInt(1,year);
			frs1=fst1.executeQuery();
			
			if (frs1.next())
			{
				ldo.setFr_date(frs1.getDate(1));
				ldo.setTo_date(frs1.getDate(2));
				
			}
			
			
			
			/// setting fin year in dto
			hm=null;
			monps=con.prepareStatement(mnthfin);
			st3 = con.createStatement();
			rs3 =st3.executeQuery(finyear);
			Vector sf = new Vector();
			Vector nsf = new Vector();
			hm=new HashMap();
			YearDto fyd=null;
			while (rs3.next())
			{
				fyd=new YearDto();
				fyd.setYearcode(rs3.getInt(1));
				fyd.setYeardesc(rs3.getString(2));
				fyd.setMsdate(rs3.getDate(3));
				fyd.setMedate(rs3.getDate(4));
				if (fyear==0)
				{
					fyear=rs3.getInt(1);
				}
				sf.add(fyd);
				nsf.add(fyd);

				//// setting month accourdint to year 
					monps.setInt(1, rs3.getInt(1));
					monrs =monps.executeQuery();
					mf = new Vector();
					while (monrs.next())
					{
						md=new MonthDto();
						md.setMnthcode(monrs.getInt(1));
						md.setMnthname(monrs.getString(2)+"-"+ monrs.getString(6));
						md.setSdate(monrs.getDate(3));
						md.setEdate(monrs.getDate(4));
						md.setMnthabbr(monrs.getString(5));
						md.setMnthno(monrs.getInt(7));
						md.setMkt_ord(monrs.getInt(7));
						md.setMnthdays(monrs.getInt(8));
						mf.add(md);
	
					}
					  hm.put(rs3.getInt(1), mf);
						
				//////////////////////////////////
			}
			ldo.setFyear(sf);
			ldo.setNewyear(nsf);
			ldo.setFmon(hm);
			
			
			//////  this is only for sales package /////
			if (year < fyear)
			{
				ldo.getFyear().remove(0);
				YearDto ydr = (YearDto) ldo.getFyear().get(0);
				ldo.setFr_date(ydr.getMsdate());
				ldo.setTo_date(ydr.getMedate());

			}

			////////////// setting  fin year /////////////


			
			con.commit();
			con.setAutoCommit(true);
			rs.close();
			ps.close();
			frs1.close();
			fst1.close();
			
		} catch (Exception ex) {System.out.println("exception in year change "+ex);
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in MonthDAO.setYear " + ex);

		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : " );

				if(rs != null){rs.close();}
				if(ps != null){ps.close();}
				if(rs1 != null){rs1.close();}
				if(ps1 != null){ps1.close();}
				if(rs4 != null){rs4.close();}
				if(ps4 != null){ps4.close();}
				if(con != null){con.close();}
				if(frs1 != null){frs1.close();}
				if(fst1 != null){fst1.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in MonthDAO.Connection.close "+e);
			}
		}
		return i;
	}

	
	 
}
