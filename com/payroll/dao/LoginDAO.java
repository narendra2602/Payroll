package com.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.payroll.dto.BankDto;
import com.payroll.dto.LoginDto;
import com.payroll.dto.MonthDto;
import com.payroll.dto.YearDto;

public class LoginDAO
{
 
 
	 
	private int fyear=0;
	
	
	public LoginDto getLoginInfo(String lname,String pass,int division,int depo_code,int uid)
	{
		PreparedStatement monps=null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement areaps = null;

		PreparedStatement bankps = null;
		ResultSet bankrs=null;
		PreparedStatement fst1 = null;
		ResultSet frs1=null;

		
		Statement st3=null;

		ResultSet rs=null;
		 
		ResultSet rs3=null;

		ResultSet rst=null;
		ResultSet monrs=null;
		
		 
		int fyear=0;
		
		Connection con=null;
		String drvnm=null;
		String printernm=null;
		String btnnm=null;
		
		 
		LoginDto ldo=null;
		 
		UserDAO udao=null;
		EmployeeDAO emp=null;
		try 
		{
			drvnm=ConnectionFactory.getDrvnm();
			printernm=ConnectionFactory.getPrinternm();
			btnnm=ConnectionFactory.getBtnnm();
			con=ConnectionFactory.getConnection();
			con.setAutoCommit(false);
			ldo = new LoginDto();
			
			/// nmot required
			 
			udao=new UserDAO();
			emp = new EmployeeDAO();
			/////////////
			
			
			
			  
				
			ldo.setDrvnm(drvnm);
			ldo.setPrinternm(printernm);
			ldo.setBtnnm(btnnm);
			
			
			String loginQ ="select login_id,login_mnth,login_year from login where login_name=? and login_pass=md5(?) and status=? ";

			String yearQ ="select p.mnth_code, p.frdate, p.todate, f.frdate, f.todate,p.fin_year from perdmast as p, yearfil as f " +
					"where p.fin_ord=? and p.fin_year=? and p.fin_year=f.year and f.typ=? ";



			String mnthfin ="select mnth_code,month_nm,frdate,todate,fin_ord,yy,day(todate) from perdmast where fin_year=? order by fin_ord";

			String finyear ="select year,description,frdate,todate from yearfil where typ='F' order by year desc ";

			String banklst ="select bank_code,bank_name from bankmast order by bank_code ";
 
			String yearMax="select frdate,todate,year  from yearfil where  year= (select max(year) from yearfil where typ='F')";
			
			////////////////////////////////////////////////
			ps2 = con.prepareStatement(loginQ);
			ps2.setString(1, lname);
			ps2.setString(2, pass);
			ps2.setString(3, "Y");
			rs= ps2.executeQuery();
			if (rs.next())
			{
				ldo.setLogin_id(rs.getInt(1));
				ldo.setLogin_mnth(rs.getInt(2));
				ldo.setLogin_year(rs.getInt(3));
				ldo.setDepo_code(depo_code);
				ldo.setDiv_code(division);
				ldo.setLogin_pass(pass);
			}

			//////////////////////////////////////////////////
			ps3 = con.prepareStatement(yearQ);
			ps3.setInt(1, rs.getInt(2));
			ps3.setInt(2, rs.getInt(3));
			ps3.setString(3, "F");
			rst= ps3.executeQuery();
			if (rst.next())
			{
				ldo.setMnth_code(rst.getInt(1));
				ldo.setSdate(rst.getDate(2));
				ldo.setEdate(rst.getDate(3));
				ldo.setFr_date(rst.getDate(4));
				ldo.setTo_date(rst.getDate(5));
				ldo.setFin_year(rst.getInt(6));

			}			


			/////////////////////////////////////////
			fst1=null;
			frs1=null;
			fst1=con.prepareStatement(yearMax);
			frs1=fst1.executeQuery();
			
			if (frs1.next())
			{
					ldo.setFin_year(frs1.getInt(3));

					System.out.println("Loading fin year is s  Narendra"+ldo.getFin_year());

			}
			

			
			
			
			/// setting fin year in dto
			HashMap hm=null;
			monps=con.prepareStatement(mnthfin);
			st3 = con.createStatement();
			rs3 =st3.executeQuery(finyear);
			Vector sf = new Vector();
			Vector nsf = new Vector();
			hm=new HashMap();
			YearDto fyd=null;
			MonthDto md=null;
			Vector mf=null;
			while (rs3.next())
			{
				fyd=new YearDto();
				fyd.setYearcode(rs3.getInt(1));
				fyd.setYeardesc(rs3.getString(2));
				fyd.setMsdate(rs3.getDate(3));
				fyd.setMedate(rs3.getDate(4));
				sf.add(fyd);
				nsf.add(fyd);
				if (fyear==0)
					fyear=rs3.getInt(1);
				
				this.fyear=fyear;
				
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
						md.setMnthyear(monrs.getInt(5));
						md.setMnthdays(monrs.getInt(7));
						mf.add(md);
	
					}
					  hm.put(rs3.getInt(1), mf);
						
				//////////////////////////////////
			}
			ldo.setFyear(sf);
			ldo.setNewyear(nsf);
			ldo.setFmon(hm);
			////////////// setting  fin year /////////////


			Vector v = emp.getEmployeeList(division,depo_code) ;
			int size=v.size();
			Vector emplist = new Vector();
			for (int i=0;i<size;i++)
			{
				emplist.add(((Vector) v.get(i)).get(2));
				
			}
					
			ldo.setEmpList(emplist);
			ldo.setEmpmap(emp.getEmployeeMap(division,depo_code)) ;
			ldo.setBranchList(udao.getBranch(ldo.getLogin_id()));
			ldo=getSalesPackage(division, depo_code, con, ldo);
					 
			
			
			//// Create bank List same varaible and dto of region is used 
			bankps = con.prepareStatement(banklst);
			bankrs= bankps.executeQuery();
			Vector bank=new Vector();
			BankDto bndt=null;
			while (bankrs.next())
			{
				bndt = new BankDto();
				bndt.setBank_code(bankrs.getInt(1));
				bndt.setBank_name(bankrs.getString(2));
				bank.add(bndt);

			}			
			ldo.setBankList(bank);
			/// End of bank List   			

			
			con.commit();
			con.setAutoCommit(true);
			rs.close();
			ps2.close();
			bankps.close();
			bankrs.close();
			rs3.close();
			st3.close();
			rst.close();
			ps3.close();
			frs1.close();
			fst1.close();
			

		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in LoginDAO.getLoginInfo " );
			ex.printStackTrace();
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : " );

				if(rs != null){rs.close();}
			 
				if(rs3!= null){rs3.close();}
				if(rst != null){rst.close();}
				 
				if(st3 != null){st3.close();}
				if(ps2 != null){ps2.close();}
				if(ps3 != null){ps3.close();}
				if(areaps != null){areaps.close();}
				if(bankps != null){bankps.close();}
				if(bankrs != null){bankrs.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in LoginDAO.Connection.close "+e);
			}
		}
		return ldo;
	}


	
	
	
	public boolean authenticateUser(String lname,String pass)
	{
		PreparedStatement ps2 = null;
		ResultSet rs=null;
		Connection con=null;
		boolean login=false;
		try 
		{
			con=ConnectionFactory.getConnection();
			con.setAutoCommit(false);

			
			String loginQ ="select login_id,login_mnth,login_year from login where login_name=? and login_pass=md5(?) and status=? ";

			ps2 = con.prepareStatement(loginQ);
			ps2.setString(1, lname);
			ps2.setString(2, pass);
			ps2.setString(3, "Y");
			rs= ps2.executeQuery();
			if (rs.next())
			{
				login=true;
				
			}

			con.commit();
			con.setAutoCommit(true);
			rs.close();
			ps2.close();

		} catch (Exception ex) {
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in LoginDAO.authenticateUser " + ex);

		}
		finally {
			try {

				if(rs != null){rs.close();}
				if(ps2 != null){ps2.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in LoginDAO.authenticateUser.Connection.close "+e);
			}
		}
		return login;
	}

	public boolean checkVersion()
	{
		PreparedStatement ps1 = null;
		ResultSet rs1=null;
		Connection con=null;
		boolean login=false;
		try 
		{
			con=ConnectionFactory.getConnection();
			con.setAutoCommit(false);

		  	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		 	Date sdate=null;
			try {
				// TODO VERSION CHANGE

				sdate = sdf.parse("01/06/2016");
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			
			String verdate= "select br_ver_date from versiondate";

			ps1 = con.prepareStatement(verdate);
			rs1= ps1.executeQuery();

			if (rs1.next())
			{
				
				if (rs1.getDate(1).equals(sdate))
					login=true;
				else if (sdate.after(rs1.getDate(1)))
					login=true;

				
			}
			rs1.close();
			ps1.close();
			
			con.commit();
			con.setAutoCommit(true);

		} catch (Exception ex) {
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in LoginDAO.checkVersion " + ex);

		}
		finally {
			try {

				if(rs1 != null){rs1.close();}
				if(ps1 != null){ps1.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in LoginDAO.authenticateUser.Connection.close "+e);
			}
		}
		return login;
	}

	
	

public LoginDto getSalesPackage(int division,int depo_code,Connection con,LoginDto ldto)
{
	 
	  
	PreparedStatement curr=null;
	PreparedStatement fst1 = null;
	ResultSet frs1=null;
	ResultSet rcurr=null;

	PreparedStatement ncurr=null;
	ResultSet nrcurr=null;

	
	int fyear=this.fyear;
	
	String currmonth =null;
	
	LoginDto ldo=ldto;
	 
	 
	try 
	{
		//prtdao=  new PartyDAO();
	 
		 
		  
		String yearMax="select frdate,todate,year  from yearfil where  year= (select max(year) from yearfil where typ='F')";
		String monfin ="select month_nm,mnth_code,frdate,todate,mth,yy,fin_ord,day(todate) from perdmast where fin_year=? order by fin_ord ";

		
		
		
		//currmonth = "select mth,fin_year,fin_desc,mnth_code,frdate,todate,fin_ord,yy from perdmast where fin_year=(select max(fin_year) from emptran where depo_code=? and cmp_code=? and doc_type=40 and ifnull(del_tag,'')<>'D' )  "+
	//	" and mnth_code=(select max(mnth_code) from emptran where  depo_code=? and cmp_code=? and doc_type=40 and ifnull(del_tag,'')<>'D' ) " ;

		currmonth = "select mth,fin_year,fin_desc,mnth_code,frdate,todate,fin_ord,yy from perdmast where fin_year=(select max(fin_year) from emptran where depo_code=? and cmp_code=? and doc_type=40 and ifnull(del_tag,'')<>'D' )  "+
		" and curdate() between frdate and todate " ;

		String newmonth = "select mth,fin_year,fin_desc,mnth_code,frdate,todate,fin_ord,yy from perdmast where fin_year=? and curdate() between frdate and todate " ;
		
		
		
		
        ////  CURRENT BILLING MONTH AND FINANCIAL YEAR AT LOGIN TIME //////////////
		curr = con.prepareStatement(currmonth) ;
        curr.setInt(1,division );  // fixed depo_code
        curr.setInt(2,depo_code );
       // curr.setInt(3,division );
      //  curr.setInt(4,depo_code );
        
		rcurr =curr.executeQuery();

		if (rcurr.next())
		{
			ldo.setMessage("Accounting Year: "+rcurr.getString(3)+"    Month: "+rcurr.getString(1)+"-"+rcurr.getInt(8));
			ldo.setFooter("Financial Accounting Year: "+rcurr.getString(3)+"    Processing Month: "+rcurr.getString(1)+"-"+rcurr.getInt(8));
			ldo.setFin_year(rcurr.getInt(2));
			ldo.setMnth_code(rcurr.getInt(4));
			ldo.setSdate(rcurr.getDate(5));
			ldo.setEdate(rcurr.getDate(6));
			ldo.setMnthName(rcurr.getString(1));
			ldo.setMno(rcurr.getInt(7)-1);
 
			fyear=rcurr.getInt(2);
   
		}
		else
		{
			ncurr = con.prepareStatement(newmonth) ;
			ncurr.setInt(1,ldo.getFin_year() );
			nrcurr =ncurr.executeQuery();

			if (nrcurr.next())
			{
				ldo.setMessage("Accounting Year: "+nrcurr.getString(3)+"    Month: "+nrcurr.getString(1)+"-"+nrcurr.getInt(8));
				ldo.setFooter("Financial Accounting Year: "+nrcurr.getString(3)+"    Processing Month: "+nrcurr.getString(1)+"-"+nrcurr.getInt(8));
				ldo.setFin_year(nrcurr.getInt(2));
				ldo.setMnth_code(nrcurr.getInt(4));
				ldo.setSdate(nrcurr.getDate(5));
				ldo.setEdate(nrcurr.getDate(6));
				ldo.setMnthName(nrcurr.getString(1));
				ldo.setMno(nrcurr.getInt(7)-1);
			}
			ncurr.close();
			nrcurr.close();
			
		}

		curr.close();
		rcurr.close();
		curr=null;
		rcurr=null;
        ////  END CURRENT BILLING MONTH AND FINANCIA YEAR AT LOGIN TIME //////////////
		
		
	 
           
		
		Vector mf = null;
		MonthDto md=null;
		/////////// fin year month///////////
		fst1=con.prepareStatement(monfin);
		fst1.setInt(1,fyear);
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
			md.setMkt_ord(frs1.getInt(7));
			md.setMnthdays(frs1.getInt(8));
		 
			mf.add(md);
		}
		ldo.setFmonth(mf);
 
 
		
		/////////////////////////////////////////
		fst1=null;
		frs1=null;
		fst1=con.prepareStatement(yearMax);
		frs1=fst1.executeQuery();
		
		if (frs1.next())
		{
			if (frs1.getInt(3)<= fyear)
			{
				ldo.setFr_date(frs1.getDate(1));
				ldo.setTo_date(frs1.getDate(2));
			}
			else
			{
				ldo.setFin_year(frs1.getInt(3));
				System.out.println("Loading fin year is "+ldo.getFin_year());
			}
			
		}
		
		
		
		// set Product List for Sales Package
		
  	//	ldo.setPrtList(prtdao.getPartyNm(depo_code,division,fyear,myear,45,sales_div));
	//	ldo.setPrtmap(prtdao.getPartyNmMap(depo_code,division,fyear,myear,45,sales_div));
		 

  
		fst1.close();
		frs1.close();
		 

	} catch (Exception ex) { ex.printStackTrace();
		try {
			con.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("-------------Exception in LoginDAO.getLoginInfo " );
		ex.printStackTrace();
	}
	finally {
		try {
			System.out.println("No. of Records Update/Insert : " );

		 
			if(rcurr != null){rcurr.close();}
			if(curr != null){curr.close();}
		 
			 
 
			if(fst1 != null){fst1.close();}
			if(frs1 != null){frs1.close();}

		} catch (SQLException e) {
			System.out.println("-------------Exception in LoginDAO.Connection.close "+e);
		}
	}
	return ldo;
}





}
