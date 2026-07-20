package com.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.apache.commons.lang3.SystemUtils;

//import com.itextpdf.text.log.SysoCounter;
import com.payroll.dto.EmployeeMastDto;
import com.payroll.dto.EmptranDto;
import com.payroll.view.BaseClass;

 

public class PayrollDAO 
{


	public Vector getAttendanceList(int depo_code,int cmp_code,int fyear,int mnth_code)
	{
		PreparedStatement ps = null;
		ResultSet rs=null;
		Connection con=null;
		Vector v =null;
		Vector col=null;
		EmptranDto emp=null;
		int sno=1;
		try 
		{
			con=ConnectionFactory.getConnection();
			con.setAutoCommit(false);

			String query="select e.emp_name,e.esic_no,e.pf_no,e.emp_code,ifnull(t.atten_days,0.00) atten,ifnull(t.extra_hrs,0) extrahrs,ifnull(t.arrear_days,0) arrear,ifnull(t.remark,'') remark," +
			" e.gross,e.basic,e.da,e.hra,e.add_hra,e.incentive,e.spl_incentive,e.lta,e.medical,e.bonus,e.ot_rate,e.stair_alw,t.atten_lock,e.food_allowance "+
			" from employeemast e left join emptran t  "+
			" on e.cmp_code=t.cmp_code and e.emp_code=t.emp_code and t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? " +
			" and ifnull(t.del_tag,'')<>'D' where e.depo_code=? and e.cmp_code=? and (e.doresign is null or CONCAT(YEAR(e.doresign),LPAD(MONTH(e.doresign),2,'0'))=?) ";

			ps = con.prepareStatement(query);
			ps.setInt(1, fyear);
			ps.setInt(2, depo_code);
			ps.setInt(3, cmp_code);
			ps.setInt(4, mnth_code);
			ps.setInt(5, depo_code);
			ps.setInt(6, cmp_code);
			ps.setInt(7, mnth_code);
			rs =ps.executeQuery();
			
			v = new Vector();
			while (rs.next())
			{
				emp = new EmptranDto();
				col= new Vector();
				col.add(sno++);  // sno 0
				col.add(rs.getString(1)); // emp_name //1
				col.add(rs.getString(2)); // esic no  2
				col.add(rs.getString(3)); // pf no  3
				col.add(rs.getInt(4)); // emp_code  4
				col.add(rs.getDouble(5)); // attendance  5
				col.add(rs.getDouble(6)); // extra hrs  6
				col.add(rs.getDouble(7)); // arrears days (+/-) 7
				col.add(rs.getString(8)); // Remark 8
				
				emp.setGross(rs.getDouble(9));
				emp.setBasic(rs.getDouble(10));
				emp.setDa(rs.getDouble(11));
				emp.setHra(rs.getDouble(12));
				emp.setAdd_hra(rs.getDouble(13));
				emp.setIncentive(rs.getDouble(14));
				emp.setSpl_incentive(rs.getDouble(15));
				emp.setLta(rs.getDouble(16));
				emp.setMedical(rs.getDouble(17));
				emp.setBonus_value(rs.getDouble(18));
				emp.setOt_rate(rs.getDouble(19));
				emp.setStair_alw(rs.getDouble(20));
				emp.setAtten_lock(rs.getInt(21));
				emp.setFood_alw(rs.getDouble(22)); // new field added food allowance on 12/11/2019
				emp.setEmp_code(rs.getInt(4));
				emp.setEmp_name(rs.getString(1));
				emp.setEsic_no(rs.getLong(2));
				emp.setPf_no(rs.getInt(3));
				
				col.add(emp); // hidden emptranDto 9
				
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
			System.out.println("-------------Exception in PayrollDAO.getAttendanceList " + ex);

		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : " );

				if(rs != null){rs.close();}
				if(ps != null){ps.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in PayrollDAO.Connection.close --- getAttendanceList "+e);
			}
		}
		return v;
	}
	
	

	
	
    public int addAttendance(ArrayList<?> attnlist)
    {
  	  
    	PreparedStatement ps1 = null;
    	ResultSet rs1=null;
    	PreparedStatement ps2 = null;
    	PreparedStatement ps3 = null;
    	
    	Connection con=null;
    	EmptranDto emp=null;

		
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();

			
			//String query1="delete from emptran where  fin_year=? and depo_code=? and cmp_code=? and  mnth_code=?  " ;

			String query1="select emp_code from emptran where  fin_year=? and depo_code=? and cmp_code=? and emp_code=? and  mnth_code=?  " ;
			
		
			String query2="insert into emptran (depo_code,cmp_code,doc_type,emp_code,doc_no,doc_date,atten_days," + //7
			" arrear_days,extra_hrs,gross,basic, da, hra, add_hra, incentive, spl_incentive, lta, medical, ot_rate, stair_alw, " + //20
			" created_by, created_date,serialno, fin_year, mnth_code,remark,absent_days,food_alw)"+ //28
			" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			
			String query3="update  emptran set atten_days=?,arrear_days=?,extra_hrs=?,remark=?, " + 
			" modified_by=?, modified_date=?,absent_days=? " +
			" where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? and emp_code=? ";
			
			String query4="SELECT BASIC,DA,HRA,INCENTIVE,MNTH_CODE FROM EMPTRAN WHERE depo_code=? and cmp_code=? and emp_code=? AND MNTH_CODE = "+
					" (select MAX(MNTH_CODE) from emptran where   depo_code=? and cmp_code=? and emp_code=? AND ATTEN_DAYS > 0 and  mnth_code<?) ";  
			
			con.setAutoCommit(false);
			
			ps1 = con.prepareStatement(query1);
			ps2 = con.prepareStatement(query2);
			ps3 = con.prepareStatement(query3);
		
			
			
			boolean first=true;
			boolean insert=true;
			int s=attnlist.size();
			for (int j=0;j<s;j++)
			{
				emp= (EmptranDto) attnlist.get(j);
				
				insert=true;
	  			// where clause 
	  			ps1.setInt(1,emp.getFin_year());
	  			ps1.setInt(2,emp.getDepo_code());
	  			ps1.setInt(3,emp.getCmp_code());
				ps1.setInt(4,emp.getEmp_code());
	  			ps1.setInt(5,emp.getMnth_code()); 	
	  			rs1 =ps1.executeQuery();
	  			if(rs1.next())
	  			{
	  				insert=false;
	  			}
	  			 rs1.close();
				
				if(insert)
				{
					ps2.setInt(1,emp.getDepo_code());
					ps2.setInt(2,emp.getCmp_code());
					ps2.setInt(3,emp.getDoc_type()); 	
					ps2.setInt(4,emp.getEmp_code());
					ps2.setInt(5, emp.getDoc_no());
					ps2.setDate(6, setSqlDate(emp.getDoc_date()));
					ps2.setDouble(7, emp.getAtten_days());
					ps2.setDouble(8, emp.getArrear_days());
					ps2.setDouble(9, emp.getExtra_hrs()); 
					ps2.setDouble(10, emp.getGross());
					ps2.setDouble(11, emp.getBasic());
					ps2.setDouble(12, emp.getDa());
					ps2.setDouble(13, emp.getHra());
					ps2.setDouble(14, emp.getAdd_hra());
					ps2.setDouble(15,emp.getIncentive());
					ps2.setDouble(16,emp.getSpl_incentive());
					ps2.setDouble(17,emp.getLta());
					ps2.setDouble(18,emp.getMedical());
					ps2.setDouble(19,emp.getOt_rate());
					ps2.setDouble(20,emp.getStair_alw());
					ps2.setInt(21,emp.getCreated_by());
					ps2.setDate(22,setSqlDate(emp.getCreated_date()));
					ps2.setInt(23,emp.getSerialno());
					ps2.setInt(24,emp.getFin_year());
					ps2.setInt(25,emp.getMnth_code());
					ps2.setString(26,emp.getRemark());
					ps2.setDouble(27,emp.getAbsent_days());
					ps2.setDouble(28,emp.getFood_alw());
					i=ps2.executeUpdate();
				}
				else
				{
					ps3.setDouble(1, emp.getAtten_days());
					ps3.setDouble(2, emp.getArrear_days());
					ps3.setDouble(3, emp.getExtra_hrs()); 
					ps3.setString(4,emp.getRemark());
					ps3.setInt(5,emp.getCreated_by());
					ps3.setDate(6,setSqlDate(emp.getCreated_date()));
					ps3.setDouble(7, emp.getAbsent_days());

					// where clause
					ps3.setInt(8,emp.getFin_year());
					ps3.setInt(9,emp.getDepo_code());
					ps3.setInt(10,emp.getCmp_code());
					ps3.setInt(11,emp.getMnth_code());
					ps3.setInt(12,emp.getEmp_code());
					i=ps3.executeUpdate();
					
				}
		  			
				
							
			}
			con.commit();
			con.setAutoCommit(true);
			ps1.close();
			ps2.close();
			ps3.close();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in PayrollDAO.addAttendance " + ex);
			i=-1;
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : "+i);

				if(ps1 != null){ps1.close();}
				if(rs1 != null){rs1.close();}
				if(ps2 != null){ps2.close();}
				if(ps3 != null){ps3.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in PayrollDAO.Connection.close "+e);
			}
		}
		return i;
	}
	
    public int addAttendancenNew(ArrayList<?> attnlist)
    {
  	  
    	PreparedStatement ps1 = null;
    	ResultSet rs1=null;
    	PreparedStatement ps2 = null;
    	PreparedStatement ps3 = null;
    	PreparedStatement ps4 = null;
    	ResultSet rs4=null;
    	Connection con=null;
    	EmptranDto emp=null;
    		System.out.println("ADD ATTENDANCE NEW mai aaya");
		
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();

			
			//String query1="delete from emptran where  fin_year=? and depo_code=? and cmp_code=? and  mnth_code=?  " ;

			String query1="select emp_code from emptran where  fin_year=? and depo_code=? and cmp_code=? and emp_code=? and  mnth_code=?  " ;
			
		
			String query2="insert into emptran (depo_code,cmp_code,doc_type,emp_code,doc_no,doc_date,atten_days," + //7
			" arrear_days,extra_hrs,gross,basic, da, hra, add_hra, incentive, spl_incentive, lta, medical, ot_rate, stair_alw, " + //20
			" created_by, created_date,serialno, fin_year, mnth_code,remark,absent_days,food_alw,"+ //28
			" arear_month,arear_basic,arear_da,arear_hra,arear_incentive,arear_medical)"+ //34
			" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			
			String query3="update  emptran set atten_days=?,arrear_days=?,extra_hrs=?,remark=?, " + 
			" modified_by=?, modified_date=?,absent_days=?," +
			" arear_month=?,arear_basic=?,arear_da=?,arear_hra=?,arear_incentive=?,arear_medical=?,medical=? " +
			" where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? and emp_code=? ";
			
			String query4="SELECT MNTH_CODE,BASIC,DA,HRA,INCENTIVE,MEDICAL FROM EMPTRAN WHERE depo_code=? and cmp_code=? and emp_code=? AND MNTH_CODE = "+
					" (select MAX(MNTH_CODE) from emptran where   depo_code=? and cmp_code=? and emp_code=? AND ATTEN_DAYS > 0 and  mnth_code<?) ";  
			
			con.setAutoCommit(false);
			
			ps1 = con.prepareStatement(query1);
			ps2 = con.prepareStatement(query2);
			ps3 = con.prepareStatement(query3);
			ps4 = con.prepareStatement(query4);
			
			boolean first=true;
			boolean insert=true;
			int s=attnlist.size();
			for (int j=0;j<s;j++)
			{
				emp= (EmptranDto) attnlist.get(j);
				insert=true;
	  			// where clause 
	  			ps1.setInt(1,emp.getFin_year());
	  			ps1.setInt(2,emp.getDepo_code());
	  			ps1.setInt(3,emp.getCmp_code());
				ps1.setInt(4,emp.getEmp_code());
	  			ps1.setInt(5,emp.getMnth_code()); 	
	  			rs1 =ps1.executeQuery();
	  			if(rs1.next())
	  			{
	  				insert=false;
	  			}
	  			 rs1.close();
				
				if(insert)
				{
					if(emp.getArrear_days()>0)
					{
			  			ps4.setInt(1,emp.getDepo_code());
			  			ps4.setInt(2,emp.getCmp_code());
						ps4.setInt(3,emp.getEmp_code());
			  			ps4.setInt(4,emp.getDepo_code());
			  			ps4.setInt(5,emp.getCmp_code());
						ps4.setInt(6,emp.getEmp_code());
			  			ps4.setInt(7,emp.getMnth_code()); 	
			  			rs4=ps4.executeQuery();
						if(rs4.next())
						{
							ps2.setInt(29,rs4.getInt(1));
							ps2.setDouble(30,rs4.getDouble(2));
							ps2.setDouble(31,rs4.getDouble(3));
							ps2.setDouble(32,rs4.getDouble(4));
							ps2.setDouble(33,rs4.getDouble(5));
							ps2.setDouble(34,rs4.getDouble(6));
						}
						rs4.close();
					}
					else
					{
						ps2.setInt(29,emp.getMnth_code());
						ps2.setDouble(30,emp.getBasic());
						ps2.setDouble(31,emp.getDa());
						ps2.setDouble(32,emp.getHra());
						ps2.setDouble(33,emp.getIncentive());
						ps2.setDouble(34,emp.getArear_medical());

					}
					
					ps2.setInt(1,emp.getDepo_code());
					ps2.setInt(2,emp.getCmp_code());
					ps2.setInt(3,emp.getDoc_type()); 	
					ps2.setInt(4,emp.getEmp_code());
					ps2.setInt(5, emp.getDoc_no());
					ps2.setDate(6, setSqlDate(emp.getDoc_date()));
					ps2.setDouble(7, emp.getAtten_days());
					ps2.setDouble(8, emp.getArrear_days());
					ps2.setDouble(9, emp.getExtra_hrs()); 
					ps2.setDouble(10, emp.getGross());
					ps2.setDouble(11, emp.getBasic());
					ps2.setDouble(12, emp.getDa());
					ps2.setDouble(13, emp.getHra());
					ps2.setDouble(14, emp.getAdd_hra());
					ps2.setDouble(15,emp.getIncentive());
					ps2.setDouble(16,emp.getSpl_incentive());
					ps2.setDouble(17,emp.getLta());
					ps2.setDouble(18,(emp.getMedical())/12);
					ps2.setDouble(19,emp.getOt_rate());
					ps2.setDouble(20,emp.getStair_alw());
					ps2.setInt(21,emp.getCreated_by());
					ps2.setDate(22,setSqlDate(emp.getCreated_date()));
					ps2.setInt(23,emp.getSerialno());
					ps2.setInt(24,emp.getFin_year());
					ps2.setInt(25,emp.getMnth_code());
					ps2.setString(26,emp.getRemark());
					ps2.setDouble(27,emp.getAbsent_days());
					ps2.setDouble(28,emp.getFood_alw());
					i=ps2.executeUpdate();
				}
				else
				{


					if(emp.getArrear_days()>0)
					{
						ps4.setInt(1,emp.getDepo_code());
						ps4.setInt(2,emp.getCmp_code());
						ps4.setInt(3,emp.getEmp_code());
						ps4.setInt(4,emp.getDepo_code());
						ps4.setInt(5,emp.getCmp_code());
						ps4.setInt(6,emp.getEmp_code());
						ps4.setInt(7,emp.getMnth_code()); 	
						rs4=ps4.executeQuery();
						if(rs4.next())
						{
							System.out.println(rs4.getInt(1)+" "+rs4.getDouble(2)+" "+emp.getEmp_code()+" "+emp.getArrear_days());
							ps3.setInt(8,rs4.getInt(1));
							ps3.setDouble(9,rs4.getDouble(2));
							ps3.setDouble(10,rs4.getDouble(3));
							ps3.setDouble(11,rs4.getDouble(4));
							ps3.setDouble(12,rs4.getDouble(5));
							ps3.setDouble(13,rs4.getDouble(6));
							rs4.close();
						}
					}
					else
					{
						ps3.setInt(8,emp.getMnth_code());
						ps3.setDouble(9,emp.getBasic());
						ps3.setDouble(10,emp.getDa());
						ps3.setDouble(11,emp.getHra());
						ps3.setDouble(12,emp.getIncentive());
						ps3.setDouble(13,emp.getArear_medical());
					}

					
					ps3.setDouble(1, emp.getAtten_days());
					ps3.setDouble(2, emp.getArrear_days());
					ps3.setDouble(3, emp.getExtra_hrs()); 
					ps3.setString(4,emp.getRemark());
					ps3.setInt(5,emp.getCreated_by());
					ps3.setDate(6,setSqlDate(emp.getCreated_date()));
					ps3.setDouble(7, emp.getAbsent_days());

					ps3.setDouble(14, emp.getMedical()/12);
					
					// where clause
					ps3.setInt(15,emp.getFin_year());
					ps3.setInt(16,emp.getDepo_code());
					ps3.setInt(17,emp.getCmp_code());
					ps3.setInt(18,emp.getMnth_code());
					ps3.setInt(19,emp.getEmp_code());
					i=ps3.executeUpdate();
					
				}
		  			
				
							
			}
			con.commit();
			con.setAutoCommit(true);
			ps1.close();
			ps2.close();
			ps3.close();
			ps4.close();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in PayrollDAO.addAttendance " + ex);
			i=-1;
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : "+i);

				if(ps1 != null){ps1.close();}
				if(rs1 != null){rs1.close();}
				if(ps2 != null){ps2.close();}
				if(ps3 != null){ps3.close();}
				if(ps4 != null){ps4.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in PayrollDAO.Connection.close "+e);
			}
		}
		return i;
	}
	
    
    public Vector getSalaryList(int depo_code,int cmp_code,int fyear,int mnth_code,int mnthdays)
	{
		PreparedStatement ps = null;
		ResultSet rs=null;
		Connection con=null;
		Vector v =null;
		Vector col=null;
		EmptranDto emp=null;
		int sno=1;
		double basic,da,hra,incentive,add_hra,spl_incentive,gross,pf,esic,empesic,emppf,epspf,food_allowance,basicForPf,basicProfTax,profTax;
		double basicpf,dapf,incentivepf;
		double basicArrear,daArrear,incentiveArrear;
		double presentdays;
		basicForPf=0.00;
		basicProfTax=0.00;
		profTax=0.00;
		try 
		{
			con=ConnectionFactory.getConnection();
			con.setAutoCommit(false);

/*			String query="select emp_code,emp_name,atten_days,basic,da,hra,incentive,ot,medical,lta,round(basic+da+hra+incentive+ot) gross,advance,round(((basic+da)*12)/100) pf,round(((basic+da+hra+incentive+ot)*1.75)/100) esic,round(((basic+da+hra+incentive+ot)*4.75)/100) empesic from "+ 
			"(select e.emp_code,e.emp_name,e.designation,e.pf_no,e.esic_no,round((t.basic/30)*t.atten_days) basic,round((t.da/30)*t.atten_days) da,round((t.hra/30)*t.atten_days) hra,round((t.incentive/30)*t.atten_days) incentive, "+
			" round(t.extra_hrs*t.ot_rate) ot,t.medical,t.lta,t.advance,t.atten_days "+
			" from emptran t,employeemast e  where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and ifnull(t.del_tag,'')<>'D' "+
			" and e.depo_code=? and e.cmp_code=? and e.emp_code=t.emp_code) a ";
*/
	
			
			String query="select emp_code,emp_name,atten_days,basic,da,hra,incentive,ot,medical_value,lta_value,round(basic+da+hra+incentive+ot) gross," +
			"advance,round(((basic+da)*12)/100) pf,round(((basic+da+hra+incentive+ot)*1.75)/100) esic,round(((basic+da+hra+incentive+ot)*4.75)/100) empesic," +
			"absent_days,arrear_days,add_hra,spl_incentive,misc_value,stair_value,machine1_value,machine2_value,food_alw,mmonth,mdays from "+ 
			"(select e.emp_code,e.emp_name,e.designation,e.pf_no,e.esic_no,t.basic,t.da,t.hra,t.incentive, "+
			" round(t.extra_hrs*t.ot_rate) ot,t.medical_value,t.lta_value,t.advance,t.atten_days,t.absent_days,t.arrear_days," +
			" t.add_hra,t.spl_incentive,t.misc_value,t.stair_value,t.machine1_value,t.machine2_value,t.food_alw,month(t.doc_date) mmonth," +
			" day(last_day(concat(left(t.mnth_code,4),'-',right(t.mnth_code,2),'-01'))) mdays "+
			" from emptran t,employeemast e  where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and t.atten_lock=1  and ifnull(t.del_tag,'')<>'D' "+
			" and e.depo_code=? and e.cmp_code=? and e.emp_code=t.emp_code) a ";
			
			/*if(mnth_code>202044)
			{
				query="select emp_code,emp_name,atten_days,basic,da,hra,incentive,ot,medical_value,lta_value,round(basic+da+hra+incentive+ot) gross," +
				"advance,round(((basic+da)*10)/100) pf,round(((basic+da+hra+incentive+ot)*1.75)/100) esic,round(((basic+da+hra+incentive+ot)*4.75)/100) empesic," +
				"absent_days,arrear_days,add_hra,spl_incentive,misc_value,stair_value,machine1_value,machine2_value,food_alw from "+ 
				"(select e.emp_code,e.emp_name,e.designation,e.pf_no,e.esic_no,t.basic,t.da,t.hra,t.incentive, "+
				" round(t.extra_hrs*t.ot_rate) ot,t.medical_value,t.lta_value,t.advance,t.atten_days,t.absent_days,t.arrear_days," +
				" t.add_hra,t.spl_incentive,t.misc_value,t.stair_value,t.machine1_value,t.machine2_value,t.food_alw "+
				" from emptran t,employeemast e  where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and t.atten_lock=1  and ifnull(t.del_tag,'')<>'D' "+
				" and e.depo_code=? and e.cmp_code=? and e.emp_code=t.emp_code) a ";
			}*/
				
			
			System.out.println("mnth code "+mnth_code); 
			
			ps = con.prepareStatement(query);
			ps.setInt(1, fyear);
			ps.setInt(2, depo_code);
			ps.setInt(3, cmp_code);
			ps.setInt(4, mnth_code);
			ps.setInt(5, depo_code);
			ps.setInt(6, cmp_code);
			rs =ps.executeQuery();
			
			v = new Vector();
			double extradays=0.00;
			while (rs.next())
			{
				
				extradays=rs.getDouble(17);
				if(rs.getDouble(17)>=rs.getDouble(26))
					extradays=30;
	
				
				if(rs.getInt(1)==220)
				{
					System.out.println("**** "+rs.getDouble(4)+" "+rs.getDouble(5)+" "+rs.getDouble(7)+" "+extradays);
				}

				
//				System.out.println("after "+extradays);

				//Salary calculation 
				
				if(rs.getInt(1)==220)
					System.out.println(rs.getInt(1)+" presend days "+rs.getDouble(3)+" absent days "+rs.getDouble(16));
				if(rs.getDouble(16)==0) // if absent days is 0 calculate full salary
				{
					basic = roundTwoDecimals(rs.getDouble(4)+((rs.getDouble(4)/30)*extradays));
					da = roundTwoDecimals(rs.getDouble(5)+((rs.getDouble(5)/30)*extradays));
					hra = roundTwoDecimals(rs.getDouble(6)+((rs.getDouble(6)/30)*extradays));
					incentive = roundTwoDecimals(rs.getDouble(7)+((rs.getDouble(7)/30)*extradays));
					add_hra = roundTwoDecimals(rs.getDouble(18)+((rs.getDouble(18)/30)*extradays));
					spl_incentive = roundTwoDecimals(rs.getDouble(19)+((rs.getDouble(19)/30)*extradays));
					food_allowance=roundTwoDecimals(rs.getDouble(3)*rs.getDouble(24)); // presentDays*Food_alw (perday)
					////

					
					basicpf = roundTwoDecimals(rs.getDouble(4));
					dapf = roundTwoDecimals(rs.getDouble(5));
					incentivepf = roundTwoDecimals(rs.getDouble(7));
					if(rs.getInt(1)==10)
						System.out.println(rs.getInt(1)+" 1--->  presend days "+rs.getDouble(3)+" absent days "+rs.getDouble(16));
					
				}
				else if(rs.getDouble(3)==0) // if present days is 0 do not calculate salary (salary is 0)
				{
					basic = 0.00;
					da = 0.00;
					hra = 0.00;
					incentive = 0.00;
					add_hra = 0.00;
					spl_incentive = 0.00;
					food_allowance=0.00;
					
					basicpf = 0.00;
					dapf = 0.00;
					incentivepf = 0.00;
					basicProfTax=0.00;
					profTax=0.00;

				}
				else if(rs.getDouble(3)<5) // if present days is < 5  calculate present days salary
				{
					basic = roundTwoDecimals((rs.getDouble(4)/30)*(extradays+rs.getDouble(3)));
					
					if(rs.getInt(1)==10)
						System.out.println("basic "+basic+" "+rs.getDouble(4)+" "+extradays+" "+rs.getDouble(3));

					
					da = roundTwoDecimals((rs.getDouble(5)/30)*(extradays+rs.getDouble(3)));
					hra = roundTwoDecimals((rs.getDouble(6)/30)*(extradays+rs.getDouble(3)));
					incentive = roundTwoDecimals((rs.getDouble(7)/30)*(extradays+rs.getDouble(3)));
					add_hra = roundTwoDecimals((rs.getDouble(18)/30)*(extradays+rs.getDouble(3)));
					spl_incentive = roundTwoDecimals((rs.getDouble(19)/30)*(extradays+rs.getDouble(3)));
					food_allowance=roundTwoDecimals(rs.getDouble(3)*rs.getDouble(24)); // presentDays*Food_alw (perday)
					
					basicpf = roundTwoDecimals((rs.getDouble(4)/30)*(rs.getDouble(3)));
					dapf = roundTwoDecimals((rs.getDouble(5)/30)*(rs.getDouble(3)));
					incentivepf = roundTwoDecimals((rs.getDouble(7)/30)*(rs.getDouble(3)));

					// CHANGE ON 09-11-2024 
//					basicpf = roundTwoDecimals(rs.getDouble(4));
//					dapf = roundTwoDecimals(rs.getDouble(5));
//					incentivepf = roundTwoDecimals(rs.getDouble(7));

					
					if(rs.getInt(1)==12028)
						System.out.println("basicapf is "+basicpf+" "+rs.getDouble(4)+"  "+rs.getDouble(3)+" dapf "+dapf+" "+rs.getDouble(5));

					if(rs.getInt(1)==12028)
						System.out.println(rs.getInt(1)+" 3--->  presend days "+rs.getDouble(3)+" absent days "+rs.getDouble(16));

				}
				else  // if absent days is not 0 calculate salary (monthdays-absent days)
				{
/*					presentdays=mnthdays-rs.getDouble(16);
					basic = roundTwoDecimals((rs.getDouble(4)/30)*(presentdays+rs.getInt(17)));
					da =    roundTwoDecimals((rs.getDouble(5)/30)*(presentdays+rs.getInt(17)));
					hra =   roundTwoDecimals((rs.getDouble(6)/30)*(presentdays+rs.getInt(17)));
					incentive = roundTwoDecimals((rs.getDouble(7)/30)*(presentdays+rs.getInt(17)));
					add_hra = roundTwoDecimals((rs.getDouble(18)/30)*(presentdays+rs.getInt(17)));
					spl_incentive = roundTwoDecimals((rs.getDouble(19)/30)*(presentdays+rs.getInt(17)));
*/					
					basic = roundTwoDecimals(rs.getDouble(4)+((rs.getDouble(4)/30)*(extradays-rs.getDouble(16))));
					da = roundTwoDecimals(rs.getDouble(5)+((rs.getDouble(5)/30)*(extradays-rs.getDouble(16))));
					hra = roundTwoDecimals(rs.getDouble(6)+((rs.getDouble(6)/30)*(extradays-rs.getDouble(16))));
					incentive = roundTwoDecimals(rs.getDouble(7)+((rs.getDouble(7)/30)*(extradays-rs.getDouble(16))));
					add_hra = roundTwoDecimals(rs.getDouble(18)+((rs.getDouble(18)/30)*(extradays-rs.getDouble(16))));
					spl_incentive = roundTwoDecimals(rs.getDouble(19)+((rs.getDouble(19)/30)*(extradays-rs.getDouble(16))));
					food_allowance=roundTwoDecimals(rs.getDouble(3)*rs.getDouble(24)); // presentDays*Food_alw (perday)
					
					basicpf = roundTwoDecimals(rs.getDouble(4)+((rs.getDouble(4)/30)*(rs.getDouble(16)*-1)));
					dapf = roundTwoDecimals(rs.getDouble(5)+((rs.getDouble(5)/30)*(rs.getDouble(16)*-1)));
					incentivepf = roundTwoDecimals(rs.getDouble(7)+((rs.getDouble(7)/30)*(rs.getDouble(16)*-1)));

/*					///  ADDED ON 27/05/2026
					basicpf = roundTwoDecimals((rs.getDouble(4)/30)*(rs.getDouble(3)));
					dapf = roundTwoDecimals((rs.getDouble(5)/30)*(rs.getDouble(3)));
					incentivepf = roundTwoDecimals((rs.getDouble(7)/30)*(rs.getDouble(3)));
*/
					
					
					if(rs.getInt(1)==12028)
						System.out.println(rs.getInt(1)+" 4--->  presend days "+rs.getDouble(3)+" absent days "+rs.getDouble(16));
					
					
					if(rs.getInt(1)==12028)
						System.out.println(rs.getInt(1)+" ####--->"+basic+" "+da+" "+incentive+" "+rs.getDouble(9)+" "+food_allowance);

					if(rs.getInt(1)==12028)
						System.out.println(rs.getInt(1)+" RENU ####--->"+basicpf+" "+dapf+" "+incentivepf+" "+rs.getDouble(9)+" "+food_allowance);

				}
				
				// professinal tax calculations
				if(rs.getDouble(3)>0 && mnth_code>202204) // if present days is >  0  calculate professional tax on salary may-2022 onward 
				{
					basicProfTax=rs.getDouble(4)+rs.getDouble(5)+rs.getDouble(6)+rs.getDouble(7)+rs.getDouble(18);
					profTax=calculateProfessionalTax(basicProfTax, rs.getInt(25));
				}
				
//				gross=roundTwoDecimals(basic+da+hra+add_hra+incentive+spl_incentive+rs.getDouble(8)+rs.getDouble(20)); // last field Misc value

				
				//// new calcluation on 29/01/2025
				basicArrear = roundTwoDecimals((rs.getDouble(4)/30)*extradays);
				daArrear = roundTwoDecimals((rs.getDouble(5)/30)*extradays);
				incentiveArrear = roundTwoDecimals((rs.getDouble(7)/30)*extradays);
				
				double arrearPFAmount=basicArrear+daArrear+incentiveArrear;
			
				if(rs.getInt(1)==12028)
				{
					System.out.println("&&&& "+basicArrear+" "+daArrear+" "+incentiveArrear+ " "+arrearPFAmount);
					System.out.println("***** "+rs.getDouble(4)+" "+rs.getDouble(5)+" "+rs.getDouble(7)+" "+extradays);
				}
				//// new calcluation end 
				
				gross=roundTwoDecimals(basic+da+hra+add_hra+incentive+spl_incentive+rs.getDouble(8)+rs.getDouble(20)+rs.getDouble(21)+rs.getDouble(9)+rs.getDouble(10)+rs.getDouble(22)+rs.getDouble(23));
				pf=roundTwoDecimals((basic+da)*12/100);
				if(mnth_code>202044)
					pf=roundTwoDecimals((basic+da)*10/100);
				
				emppf=roundTwoDecimals((basic+da)*3.67/100);
				epspf=roundTwoDecimals((basic+da)*8.33/100);
				
				if(mnth_code>=201911)// new condition for field food allowance on 12/11/2019 calculation of PF
				{
					gross=roundTwoDecimals(basic+da+hra+add_hra+incentive+spl_incentive+rs.getDouble(8)+rs.getDouble(20)+rs.getDouble(21)+rs.getDouble(9)+rs.getDouble(10)+rs.getDouble(22)+rs.getDouble(23)+food_allowance);
					basicForPf=basicpf+dapf+incentivepf+rs.getDouble(9)+food_allowance;
					
					if(rs.getInt(1)==12028)
						System.out.println(rs.getInt(1)+" **** --->"+basicpf+" "+dapf+" "+incentivepf+" "+rs.getDouble(9)+" "+food_allowance+"  "+basicForPf+" GROSS "+gross);


					
					if(basicForPf>15000 )
						pf=roundTwoDecimals(15000*12/100)+roundTwoDecimals(arrearPFAmount*12/100);
					else
						pf=roundTwoDecimals((basic+da+incentive+rs.getDouble(9)+food_allowance)*12/100);

					
					
					if(rs.getInt(1)==12028)
						System.out.println("new pf value is ### "+pf);


					
					//if(mnth_code>202044) ///Later implement similar to condition > 15000 
					//	pf=roundTwoDecimals((basic+da+incentive+rs.getDouble(9)+food_allowance)*10/100);

					
					if(basicForPf>15000)
					{
						emppf=roundTwoDecimals(15000*3.67/100);
						epspf=roundTwoDecimals(15000*8.33/100);
					}
					else
					{
						emppf=roundTwoDecimals((basic+da+incentive+rs.getDouble(9)+food_allowance)*3.67/100);
						epspf=roundTwoDecimals((basic+da+incentive+rs.getDouble(9)+food_allowance)*8.33/100);
					}
					
				}
				
				if(mnth_code>201906)
				{
//					esic=roundTwoDecimals((gross*0.75)/100);
//					empesic=roundTwoDecimals((gross*3.25)/100);
					
					
					esic=(int) Math.ceil((gross*0.75)/100);     // Changes made on 24/06/2024 as per Yashpal instructions
//					empesic=(int) Math.ceil((gross*3.25)/100);  // Changes made on 24/06/2024 as per Yashpal instructions
					empesic=roundTwoDecimals((gross*3.25)/100); // Changes made on 16/09/2024 as per Yashpal instructions
				}
				else
				{
					esic=roundTwoDecimals((gross*1.75)/100);
					empesic=roundTwoDecimals((gross*4.75)/100);
				}
				
				
				
				if(basicProfTax>21000 && mnth_code>202204 )
				{
					esic=0.00;
					empesic=0.00;
				}
				 
				
				emp = new EmptranDto();
				col= new Vector(); 
				col.add(rs.getInt(1));  // emp_code 0
				col.add(rs.getString(2)); // emp_name //1
				col.add(rs.getDouble(3)); // atten_days  2
				col.add(basic); // basic 3
				col.add(da); //da  4
				col.add(hra); // hra  5
				col.add(incentive); // incentive 6
				col.add(rs.getDouble(8)); //ot  7
				col.add(rs.getDouble(9)); // medical 8
				col.add(rs.getDouble(10)); // lta  9
				col.add(gross); //gross 10
				col.add(rs.getDouble(12)); //advance 11
				col.add(pf); //pf  12
				col.add(esic); //esic 13
				col.add(pf+esic+rs.getDouble(12)); // total deduction 14
				col.add(gross-(pf+esic+rs.getDouble(12))); // net salary 15
				
				emp.setBasic_value(basic);
				emp.setDa_value(da);
				emp.setHra_value(hra);
				emp.setAdd_hra_value(add_hra);
				emp.setIncentive_value(incentive);
				emp.setSpl_incen_value(spl_incentive);
				emp.setFood_value(food_allowance);
				emp.setOt_value(rs.getDouble(8));
				emp.setGross(gross);
				emp.setPf_value(pf);
				emp.setEmployer_pf(emppf);
				emp.setEps_pf(epspf);
				emp.setEsis_value(esic);
				emp.setEmployer_esis_value(empesic);
				emp.setEmp_code(rs.getInt(1));
				emp.setEmp_name(rs.getString(2));
				emp.setBasicpf_value(basicForPf);
				emp.setProf_tax(profTax);
				
				col.add(emp); // hidden emptranDto 16
				
				v.add(col);
			}

			System.out.println("SIZE OF V "+v.size());

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
			System.out.println("-------------Exception in PayrollDAO.getSalaryList " + ex);

		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : " );

				if(rs != null){rs.close();}
				if(ps != null){ps.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in PayrollDAO.Connection.close --- getSalaryList "+e);
			}
		}
		return v;
	}    
    
  
    public Vector getSalaryListNew(int depo_code,int cmp_code,int fyear,int mnth_code,int mnthdays)
 	{
 		PreparedStatement ps = null;
 		ResultSet rs=null;
 		PreparedStatement ps2 = null;
 		ResultSet rs2=null;
 		Connection con=null;
 		Vector v =null;
 		Vector col=null;
 		EmptranDto emp=null;
 		int sno=1;
 		int month_no=0;
 		double basic,da,hra,incentive,add_hra,spl_incentive,gross,pf,esic,empesic,emppf,epspf,food_allowance,basicForPf,basicProfTax,profTax,basicProfTaxNew;
 		double basicpf,dapf,incentivepf,medical;
 		double basicArrear,daArrear,incentiveArrear,medicalArrear,arrearpf,arearemppf,arearepspf,hraArrear,grossarear,empesicArear,esicArear;
 		double presentdays;
 		basicForPf=0.00;
 		basicProfTax=0.00;
 		basicProfTaxNew=0.00;
 		profTax=0.00;
 		try 
 		{
 			con=ConnectionFactory.getConnection();
 			con.setAutoCommit(false);
  			
 			String query="select emp_code,emp_name,atten_days,basic,da,hra,incentive,ot,medical,lta_value,round(basic+da+hra+incentive+ot) gross," +
 			"advance,round(((basic+da)*12)/100) pf,round(((basic+da+hra+incentive+ot)*1.75)/100) esic,round(((basic+da+hra+incentive+ot)*4.75)/100) empesic," +
 			"absent_days,arrear_days,add_hra,spl_incentive,misc_value,stair_value,machine1_value,machine2_value,food_alw,mmonth,mdays,arear_basic,arear_da," +
 			"arear_hra,arear_incentive,arear_medical,ifnull(arrear_month_mdays,0)  from "+ 
 			"(select e.emp_code,e.emp_name,e.designation,e.pf_no,e.esic_no,t.basic,t.da,t.hra,t.incentive, "+
 			" round(t.extra_hrs*t.ot_rate) ot,t.medical,t.lta_value,t.advance,t.atten_days,t.absent_days,t.arrear_days," +
 			" t.add_hra,t.spl_incentive,t.misc_value,t.stair_value,t.machine1_value,t.machine2_value,t.food_alw,month(t.doc_date) mmonth," +
 			" day(last_day(concat(left(t.mnth_code,4),'-',right(t.mnth_code,2),'-01'))) mdays,t.arear_basic,t.arear_da,t.arear_hra,t.arear_incentive,t.arear_medical,"+
 			" day(last_day(concat(left(t.arear_month,4),'-',right(t.arear_month,2),'-01'))) arrear_month_mdays "+
 			" from emptran t,employeemast e  where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and t.atten_lock=1  and ifnull(t.del_tag,'')<>'D' "+
 			" and e.depo_code=? and e.cmp_code=? and e.emp_code=t.emp_code) a ";
 	
			String query2="select sum(esis_value) esis_value"+  
					" from emptran where fin_year=? and mnth_code between 202510 and ? and emp_code=? group by emp_code" ;
 			 
			ps2=con.prepareStatement(query2);

			
 			System.out.println("mnth code "+mnth_code); 
 			
 			ps = con.prepareStatement(query);
 			ps.setInt(1, fyear);
 			ps.setInt(2, depo_code);
 			ps.setInt(3, cmp_code);
 			ps.setInt(4, mnth_code);
 			ps.setInt(5, depo_code);
 			ps.setInt(6, cmp_code);
 			rs =ps.executeQuery();
 			
 			v = new Vector();
 			double extradays=0.00;
 			while (rs.next())
 			{
 				
 				extradays=rs.getDouble(17);
 				month_no=rs.getInt(25);
 				if(rs.getDouble(17)>=rs.getDouble(26))
 					extradays=30;
 				
 				
 
 				//Salary calculation 
					basic = roundTwoDecimals(((rs.getDouble(4)/rs.getDouble(26))*rs.getDouble(3)));
					da = roundTwoDecimals(((rs.getDouble(5)/rs.getDouble(26))*rs.getDouble(3)));
					hra = roundTwoDecimals(((rs.getDouble(6)/rs.getDouble(26))*rs.getDouble(3)));
					incentive = roundTwoDecimals(((rs.getDouble(7)/rs.getDouble(26))*rs.getDouble(3)));
					add_hra = roundTwoDecimals(((rs.getDouble(18)/rs.getDouble(26))*rs.getDouble(3)));
					spl_incentive = roundTwoDecimals(((rs.getDouble(19)/rs.getDouble(26))*rs.getDouble(3)));
 					food_allowance=roundTwoDecimals(rs.getDouble(3)*rs.getDouble(24)); // presentDays*Food_alw (perday)
					medical = roundTwoDecimals(((rs.getDouble(9)/rs.getDouble(26))*rs.getDouble(3)));

 					basicpf = roundTwoDecimals(rs.getDouble(4));
 					dapf = roundTwoDecimals(rs.getDouble(5));
 					incentivepf = roundTwoDecimals(rs.getDouble(7));

 					
  				
// 				gross=roundTwoDecimals(basic+da+hra+add_hra+incentive+spl_incentive+rs.getDouble(8)+rs.getDouble(20)); // last field Misc value

 				//on 32 to arrear month days calulate all arrear on this days
 				// on 26 month days
 				//// new calcluation on 29/01/2025
 				if(rs.getDouble(32)>0.00)
 				{
 					if(rs.getInt(1)==146 || rs.getInt(1)==167 )
 					{
 						System.out.println("****** > "+rs.getDouble(27)+" "+rs.getDouble(32)+" "+rs.getInt(1)+" "+extradays);
 					}
 				basicArrear = roundTwoDecimals((rs.getDouble(27)/rs.getDouble(32))*extradays);
 				daArrear = roundTwoDecimals((rs.getDouble(28)/rs.getDouble(32))*extradays);
 				hraArrear = roundTwoDecimals((rs.getDouble(29)/rs.getDouble(32))*extradays);
 				incentiveArrear = roundTwoDecimals((rs.getDouble(30)/rs.getDouble(32))*extradays);
 				medicalArrear = roundTwoDecimals((rs.getDouble(9)/rs.getDouble(32))*extradays);
 				}
 				else
 				{
 	 				basicArrear = 0.00;
 	 				daArrear = 0.00;
 	 				hraArrear = 0.00;
 	 				incentiveArrear = 0.00;
 	 				medicalArrear = 0.00;
 					
 				}
 				double arrearPFAmount=basicArrear+daArrear+incentiveArrear+medicalArrear;
 
				// professinal tax calculations
 				if(rs.getDouble(3)>0 && mnth_code>202204) // if present days is >  0  calculate professional tax on salary may-2022 onward 
 				{
 					basicProfTaxNew=rs.getDouble(4)+rs.getDouble(5)+rs.getDouble(6)+rs.getDouble(7)+rs.getDouble(9);
 					basicProfTax=basic+da+hra+incentive+medical;
 					if(mnth_code>202507)
 						basicProfTax+=arrearPFAmount;
 					profTax=calculateProfessionalTax(basicProfTax, rs.getInt(25));
 				}

 				
 				
  				//// new calcluation end 
 				gross=0.00;
 				pf=0.00;
 				emppf=0.00;
 				epspf=0.00;
 				grossarear=0.00;
 				arrearpf=0.00;
 				arearemppf=0.00;
 				arearepspf=0.00;
 				
 				if(mnth_code>=201911)// new condition for field food allowance on 12/11/2019 calculation of PF
 				{
 					gross=roundTwoDecimals(basic+da+hra+add_hra+incentive+spl_incentive+rs.getDouble(8)+rs.getDouble(20)+rs.getDouble(21)+medical+rs.getDouble(10)+rs.getDouble(22)+rs.getDouble(23)+food_allowance);
 					grossarear=roundTwoDecimals(basicArrear+daArrear+hraArrear+incentiveArrear+medicalArrear);
// 					basicForPf=basicpf+dapf+incentivepf+rs.getDouble(9)+food_allowance;
// 					basicForPf=basicpf+dapf+incentivepf+rs.getDouble(9);
 					basicForPf=basic+da+incentive+medical;
 					

					arrearpf=roundTwoDecimalsnew(arrearPFAmount*12/100);
					arearemppf=roundTwoDecimalsnew(arrearPFAmount*3.67/100);
					arearepspf=roundTwoDecimalsnew(arrearPFAmount*8.33/100);
 					
 					if(basicForPf>15000 )
 					{
 						pf=roundTwoDecimalsnew(15000*12/100); //+roundTwoDecimals(arrearPFAmount*12/100);
 						emppf=roundTwoDecimalsnew(15000*3.67/100);
 						epspf=roundTwoDecimalsnew(15000*8.33/100);
 					}
 					else
 					{
 						pf=roundTwoDecimalsnew((basic+da+incentive+medical)*12/100);
 						emppf=roundTwoDecimalsnew((basic+da+incentive+medical)*3.67/100);
 						epspf=roundTwoDecimalsnew((basic+da+incentive+medical)*8.33/100);
 					}

					if(rs.getInt(1)==18)
					{
						System.out.println(basicForPf+" "+arrearpf+" "+basic+" "+da+" "+incentive+" "+medical+" PF "+pf);
					}


 					
 					
 				}
					esic=(int) Math.ceil((gross*0.75)/100);     // Changes made on 24/06/2024 as per Yashpal instructions
 					empesic=roundTwoDecimalsnew((gross*3.25)/100); // Changes made on 16/09/2024 as per Yashpal instructions
 				
					esicArear=(int) Math.ceil((grossarear*0.75)/100);     // Changes made on 24/06/2024 as per Yashpal instructions
 					empesicArear=roundTwoDecimalsnew((grossarear*3.25)/100); // Changes made on 16/09/2024 as per Yashpal instructions
 	 					
 					if(rs.getInt(1)==132)
					{
						System.out.println(arrearPFAmount+" "+arrearpf+" "+arearemppf+" "+arearepspf+" "+esicArear+" "+empesicArear);
						System.out.println(basicArrear+" "+daArrear+" "+incentiveArrear+" "+hraArrear+" "+medicalArrear+" 27 "+rs.getDouble(27));
					}

 					
 				// new criteria for calculating esic   26/02/2026  
 					
 					ps2.setInt(1, fyear);
 					ps2.setInt(2, (mnth_code-1));
 					ps2.setInt(3, rs.getInt(1));
 					rs2=ps2.executeQuery();
 					int esicamt=0;
 					if(rs2.next())
 					{
 						esicamt=rs2.getInt(1);
 					}
 					rs2.close();
 					

 					
 				if(basicProfTaxNew>21000 && mnth_code>202204 && esicamt==0)
 				{
 					esic=0.00;
 					empesic=0.00;
 					esicArear=0.00;
 					empesicArear=0.00;
 				}
 				 
 				
 				emp = new EmptranDto();
 				col= new Vector(); 
 				col.add(rs.getInt(1));  // emp_code 0
 				col.add(rs.getString(2)); // emp_name //1
 				col.add(rs.getDouble(3)); // atten_days  2
 				col.add(basic); // basic 3
 				col.add(da); //da  4
 				col.add(hra); // hra  5
 				col.add(incentive); // incentive 6
 				col.add(rs.getDouble(8)); //ot  7
 				col.add(rs.getDouble(9)); // medical 8
 				col.add(rs.getDouble(10)); // lta  9
 				col.add(gross); //gross 10
 				col.add(rs.getDouble(12)); //advance 11
 				col.add(pf); //pf  12
 				col.add(esic); //esic 13
 				col.add(pf+esic+rs.getDouble(12)); // total deduction 14
 				col.add(gross-(pf+esic+rs.getDouble(12))); // net salary 15
 				
 				emp.setBasic_value(basic);
 				emp.setDa_value(da);
 				emp.setHra_value(hra);
 				emp.setAdd_hra_value(add_hra);
 				emp.setIncentive_value(incentive);
 				emp.setSpl_incen_value(spl_incentive);
 				emp.setFood_value(food_allowance);
 				emp.setOt_value(rs.getDouble(8));
 				emp.setGross(gross);
 				emp.setPf_value(pf);
 				emp.setEmployer_pf(emppf);
 				emp.setEps_pf(epspf);
 				emp.setEsis_value(esic);
 				emp.setEmployer_esis_value(empesic);
 				emp.setEmp_code(rs.getInt(1));
 				emp.setEmp_name(rs.getString(2));
 				emp.setBasicpf_value(basicForPf);
 				emp.setProf_tax(profTax);
 				emp.setArear_basic_value(basicArrear);
 				emp.setArear_da_value(daArrear);
 				emp.setArear_hra_value(hraArrear);
 				emp.setArear_incentive_value(incentiveArrear);
 				emp.setArear_medical_value(medicalArrear);
 				emp.setArear1_pf_value(arrearpf);
 				emp.setArear1_esic_value(esicArear);
 				emp.setMedical_value(medical);
 				col.add(emp); // hidden emptranDto 16
 				
 				v.add(col);
 			}
 			System.out.println("SIZE OF V "+v.size());

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
 			System.out.println("-------------Exception in PayrollDAO.getSalaryList " + ex);

 		}
 		finally {
 			try {
 				System.out.println("No. of Records Update/Insert : " );

 				if(rs != null){rs.close();}
 				if(ps != null){ps.close();}
 				if(con != null){con.close();}
 			} catch (SQLException e) {
 				System.out.println("-------------Exception in PayrollDAO.Connection.close --- getSalaryList "+e);
 			}
 		}
 		return v;
 	}    
  
    // Calculate from 01/05/2022 onwards (basic+da+hra+add_hra+incentive)
    public int calculateProfessionalTax(double basicProfTax,int mnth_code)
    {
    	int proftax=0;
    	if(basicProfTax>18750 && basicProfTax<25000)
    		proftax=125;
    	else if(basicProfTax>=25000 && basicProfTax<33333)
    		proftax=mnth_code==3?174:166;
    	else if(basicProfTax>=33333)
    		proftax=mnth_code==3?212:208;

    	
    	return proftax;
    }
    
    public int updateSalaryList(ArrayList<?> attnlist)
    {
  	  
    	PreparedStatement ps1 = null;
    	Connection con=null;
    	EmptranDto emp=null;
		
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();

			
			String query1="update emptran set basic_value=?,da_value=?,hra_value=?,incentive_value=?,spl_incen_value=?, " +
			"ot_value=?,pf_value=?,esis_value=?,employer_esis_value=?,modified_by=?,modified_date=?,add_hra_value=?,employer_pf=?,eps_pf=?,food_value=?,basicpf_value=?,prof_tax=? " +
			" where  fin_year=? and depo_code=? and cmp_code=? and  mnth_code=? and emp_code=? " ;

		
			con.setAutoCommit(false);
			
			ps1 = con.prepareStatement(query1);
			
			int s=attnlist.size();
			for (int j=0;j<s;j++)
			{
				emp= (EmptranDto) attnlist.get(j);
				 
				ps1.setDouble(1, emp.getBasic_value());
				ps1.setDouble(2, emp.getDa_value());
				ps1.setDouble(3, emp.getHra_value());
				ps1.setDouble(4,emp.getIncentive_value());
				ps1.setDouble(5,emp.getSpl_incen_value());
				ps1.setDouble(6,emp.getOt_value());
				ps1.setDouble(7,emp.getPf_value());
				ps1.setDouble(8,emp.getEsis_value());
				ps1.setDouble(9,emp.getEmployer_esis_value());
				ps1.setInt(10,emp.getModified_by());
				ps1.setDate(11,setSqlDate(emp.getModified_date()));
				ps1.setDouble(12, emp.getAdd_hra_value());
				ps1.setDouble(13,emp.getEmployer_pf());
				ps1.setDouble(14,emp.getEps_pf());
				ps1.setDouble(15,emp.getFood_value());
				ps1.setDouble(16,emp.getBasicpf_value());
				ps1.setDouble(17,emp.getProf_tax());
				// where clause
				ps1.setInt(18,emp.getFin_year());
				ps1.setInt(19,emp.getDepo_code());
				ps1.setInt(20,emp.getCmp_code());
				ps1.setInt(21,emp.getMnth_code());
				ps1.setInt(22,emp.getEmp_code());
				i=ps1.executeUpdate();
		  			
			}
			con.commit();
			con.setAutoCommit(true);
			ps1.close();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in PayrollDAO.updateSalaryList " + ex);
			i=-1;
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : "+i);

				if(ps1 != null){ps1.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in PayrollDAO.Connection.close "+e);
			}
		}
		return i;
	}
	
    
    public int updateSalaryListNew(ArrayList<?> attnlist)
    {
  	  
    	PreparedStatement ps1 = null;
    	PreparedStatement ps3 = null;
    	PreparedStatement ps2 = null;
    	Connection con=null;
    	EmptranDto emp=null;
		
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();

			
			String query1="update emptran set basic_value=?,da_value=?,hra_value=?,incentive_value=?,spl_incen_value=?, " +
			"ot_value=?,pf_value=?,esis_value=?,employer_esis_value=?,modified_by=?,modified_date=?,add_hra_value=?,employer_pf=?," +
			"eps_pf=?,food_value=?,basicpf_value=?,prof_tax=?," +
			"arear_basic_value=?,arear_da_value=?,arear_hra_value=?,arear_incentive_value=?,arear_medical_value=?," +
			"arear1_pf_value=?,arear2_pf_value=?,arear1_esic_value=?,arear2_esic_value=?,medical_value=? "+
			" where  fin_year=? and depo_code=? and cmp_code=? and  mnth_code=? and emp_code=? " ;

			
			
			 String query2="UPDATE emptran e"+ 
			 " SET e.prof_tax = ( "+
			 "   select CASE "+ 
			 "       WHEN earnings < 18750 THEN 0 "+ 
			 "       WHEN earnings > 18750 AND earnings < 25000 THEN 125 "+ 
 		     "		 WHEN earnings >= 25000 AND earnings < 33333 THEN 166 "+ 
			 "       ELSE 208 "+ 
			 "   END "+ 
			 " FROM ( "+ 
			 "   SELECT emp_code, cmp_code, fin_year,mnth_code, "+
			 "   SUM(basic_value + da_value + hra_value + incentive_value+medical_value+ot_value+stair_value+machine1_value+machine2_value+ "+ 
			 "   arear_basic_value+arear_da_value+arear_hra_value+arear_incentive_value+arear_medical_value+food_value) AS earnings, "+
			 "   SUM(prof_tax) AS prof_tax "+ 
			 "   FROM ( "+ 
			 "   SELECT emp_code, cmp_code, fin_year,mnth_code,basic_value, da_value, hra_value, prof_tax,incentive_value,medical_value,0 ot_value, + "+
			 "    0 stair_value,0 machine1_value,0 machine2_value, "+
			 "    arear_basic_value,arear_da_value,arear_hra_value,arear_incentive_value,arear_medical_value,food_value "+
			 "    FROM emptran "+ 
			 "    UNION ALL "+ 
			 "    SELECT emp_code, cmp_code, fin_year, mnth_code,basic_value, da_value, hra_value, prof_tax,incen_value incentive_value,0 medical_value,0 ot_value, + "+
			 "    0 stair_value,0 machine1_value,0 machine2_value, "+
			 "    0 arear_basic_value,0 arear_da_value,0 arear_hra_value,0 arear_incentive_value,0 arear_medical_value,0 food_value "+
			 "    FROM arrear "+ 
			 "   ) sub "+ 
			 "   WHERE fin_year = ? and mnth_code=? "+ 
			 "    AND cmp_code = ? "+ 
			 "   GROUP BY emp_code, cmp_code, fin_year "+ 
			 " ) a "+ 
			 " WHERE e.emp_code = a.emp_code "+ 
			 "  AND e.cmp_code = a.cmp_code "+ 
			 " AND e.fin_year = a.fin_year "+ 
			 " and e.mnth_code=a.mnth_code "+	
		     "	) "+
			 " WHERE e.fin_year = ? "+
			 " AND e.mnth_code = ? "+
			 " AND e.cmp_code = ? ";
			
			
			
			// year end prof tax calculation

			 String query3="UPDATE emptran e"+ 
			 " SET e.prof_tax = ( "+
			 "   select CASE "+ 
			 "       WHEN earnings < 225000 THEN 0 "+ 
 			 "        WHEN earnings > 225000 AND earnings <= 300000 THEN (1500 - prof_tax) "+
             "		 WHEN earnings > 300000 AND earnings <= 400000 THEN (2000 - prof_tax) "+
			 "        ELSE (2500 - prof_tax) "+
			 "   END "+ 
			 " FROM ( "+ 
			 "   SELECT emp_code, cmp_code, fin_year, "+
			 "   SUM(basic_value + da_value + hra_value + incentive_value+medical_value+ot_value+stair_value+machine1_value+machine2_value+ "+ 
			 "   arear_basic_value+arear_da_value+arear_hra_value+arear_incentive_value+arear_medical_value) AS earnings, "+
			 "   SUM(prof_tax) AS prof_tax "+ 
			 "   FROM ( "+ 
			 "   SELECT emp_code, cmp_code, fin_year,basic_value, da_value, hra_value, prof_tax,incentive_value,medical_value,ot_value, + "+
			 "    stair_value,machine1_value,machine2_value, "+
			 "    arear_basic_value,arear_da_value,arear_hra_value,arear_incentive_value,arear_medical_value "+
			 "    FROM emptran "+ 
			 "    UNION ALL "+ 
			 "    SELECT emp_code, cmp_code, fin_year, basic_value, da_value, hra_value, prof_tax,incen_value incentive_value,0 medical_value,0 ot_value, + "+
			 "    0 stair_value,0 machine1_value,0 machine2_value, "+
			 "    0 arear_basic_value,0 arear_da_value,0 arear_hra_value,0 arear_incentive_value,0 arear_medical_value "+
			 "    FROM arrear "+ 
			 "   ) sub "+ 
			 "   WHERE fin_year = ?  "+ 
			 "    AND cmp_code = ? "+ 
			 "   GROUP BY emp_code, cmp_code, fin_year "+ 
			 " ) a "+ 
			 " WHERE e.emp_code = a.emp_code "+ 
			 "  AND e.cmp_code = a.cmp_code "+ 
			 " AND e.fin_year = a.fin_year "+ 
		     "	) "+
			 " WHERE e.fin_year = ? "+
			 " AND e.mnth_code = ? "+
			 " AND e.cmp_code = ? ";
			

			 
				

			
		
			con.setAutoCommit(false);
			
			ps1 = con.prepareStatement(query1);
			ps2 = con.prepareStatement(query2);
			ps3 = con.prepareStatement(query3);
			
			int s=attnlist.size();
			for (int j=0;j<s;j++)
			{
				emp= (EmptranDto) attnlist.get(j);
				 
				ps1.setDouble(1, emp.getBasic_value());
				ps1.setDouble(2, emp.getDa_value());
				ps1.setDouble(3, emp.getHra_value());
				ps1.setDouble(4,emp.getIncentive_value());
				ps1.setDouble(5,emp.getSpl_incen_value());
				ps1.setDouble(6,emp.getOt_value());
				ps1.setDouble(7,emp.getPf_value());
				ps1.setDouble(8,emp.getEsis_value());
				ps1.setDouble(9,emp.getEmployer_esis_value());
				ps1.setInt(10,emp.getModified_by());
				ps1.setDate(11,setSqlDate(emp.getModified_date()));
				ps1.setDouble(12, emp.getAdd_hra_value());
				ps1.setDouble(13,emp.getEmployer_pf());
				ps1.setDouble(14,emp.getEps_pf());
				ps1.setDouble(15,emp.getFood_value());
				ps1.setDouble(16,emp.getBasicpf_value());
//				ps1.setDouble(17,emp.getProf_tax());
				ps1.setDouble(17,0);
				ps1.setDouble(18,emp.getArear_basic_value());
				ps1.setDouble(19,emp.getArear_da_value());
				ps1.setDouble(20,emp.getArear_hra_value());
				ps1.setDouble(21,emp.getArear_incentive_value());
				ps1.setDouble(22,emp.getArear_medical_value());
				ps1.setDouble(23,emp.getArear1_pf_value());
				ps1.setDouble(24,emp.getArear2_pf_value());
				ps1.setDouble(25,emp.getArear1_esic_value());
				ps1.setDouble(26,emp.getArear2_esic_value());
				ps1.setDouble(27,emp.getMedical_value());
				// where clause
				ps1.setInt(28,emp.getFin_year());
				ps1.setInt(29,emp.getDepo_code());
				ps1.setInt(30,emp.getCmp_code());
				ps1.setInt(31,emp.getMnth_code());
				ps1.setInt(32,emp.getEmp_code());
				i=ps1.executeUpdate();
		  			
			}
			int month_no = Integer.parseInt(String.valueOf(emp.getMnth_code()).substring(4));
			System.out.println("month_no is "+month_no);
			
 			if(month_no==3)
 			{
 	 			ps3.setInt(1, emp.getFin_year());
 	 			ps3.setInt(2, emp.getCmp_code());
 	 			ps3.setInt(3, emp.getFin_year());
 	 			ps3.setInt(4, emp.getMnth_code());
 	 			ps3.setInt(5, emp.getCmp_code());
 				int k=ps3.executeUpdate();

 			}
 			else
 			{
 				System.out.println("monthly prof tax update....");
 	 			ps2.setInt(1, emp.getFin_year());
 	 			ps2.setInt(2, emp.getMnth_code());
 	 			ps2.setInt(3, emp.getCmp_code());
 	 			ps2.setInt(4, emp.getFin_year());
 	 			ps2.setInt(5, emp.getMnth_code());
 	 			ps2.setInt(6, emp.getCmp_code());
 				int j=ps2.executeUpdate();
 				
 			}

			
			con.commit();
			con.setAutoCommit(true);
			ps1.close();
			ps2.close();
			ps3.close();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in PayrollDAO.updateSalaryList " + ex);
			i=-1;
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : "+i);

				if(ps1 != null){ps1.close();}
				if(ps2 != null){ps2.close();}
 				if(ps3 != null){ps3.close();}

				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in PayrollDAO.Connection.close "+e);
			}
		}
		return i;
	}
	
    
    public ArrayList getEsicList(int depo_code,int cmp_code,int fyear,int mnth_code,int repno,int opt)
  	{
  		PreparedStatement ps = null;
  		ResultSet rs=null;
  		PreparedStatement ps1 = null;
  		ResultSet rs1=null;
  		Connection con=null;
  		ArrayList v =null;
  		 
  		EmptranDto emp=null;
  		int sno=1;
  		int bkcode=0;
  		double total=0.00;
  		boolean first=false;
  		long diff=0;
  		long diffDays=0;
  		double arrearbasic=0.00;
  		double prevarrearbasic=0.00;
  		double arrearnet=0.00;
  		String mncode = String.valueOf(mnth_code);
  		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
  		Date edate=null;
  		try {
			 edate = sdf.parse((mncode.substring(0,4)+"-"+mncode.substring(5)+"-01"));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
  		String orderby="";
  		if(repno==10)
  			orderby="  order by e.bank_code,e.emp_code ";
  		else if(repno==1)
  			orderby="  order by e.esic_no ";
  		 
  		int lmnth_code=0;
  		int totalmnthdays=0;
  		try 
  		{
  			con=ConnectionFactory.getConnection();
  			con.setAutoCommit(false);

  			
  			String lastmonthQuery="SELECT MNTH_CODE,day(todate) FROM PERDMAST WHERE MNTH_CODE<? ORDER BY MNTH_CODE DESC LIMIT 1";
  			ps1=con.prepareStatement(lastmonthQuery);
  			ps1.setInt(1, mnth_code);
  			rs1=ps1.executeQuery();
  			if(rs1.next())
  			{
  				lmnth_code=rs1.getInt(1);
  				totalmnthdays=rs1.getInt(2);
  			}
  			
  			rs1.close();
  			ps1.close();
  			
  			
  			String query="select e.esic_no,e.emp_name,t.atten_days,(t.basic+t.da+t.hra+t.add_hra+t.incentive+t.spl_incentive) totalwages, 0 reason, e.doresign lwdate," +
  			"e.emp_code,e.pf_no,t.esis_value,t.employer_esis_value,t.serialno,t.pf_value,t.advance,t.loan,t.stair_days,t.stair_alw,t.stair_value," +
  			"t.extra_hrs,t.arrear_days,t.ot_rate,t.ot_value,t.employer_pf,t.eps_pf,(t.basic_value+t.da_value+t.food_value+t.incentive_value+t.medical_value) grosspf,(t.basic+t.da) salarypf," +
  			"(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incentive_value+t.spl_incen_value+t.stair_value+t.misc_value+t.ot_value+t.lta_value+t.medical_value+t.food_value) netwages," +
  			"t.absent_days,t.misc_value,  " +
  			"ifnull(e.bank,'Direct Cheque'),e.bank_add1,e.ifsc_code,ifnull(e.bank_accno,''),e.bank_code,t.lta_value,t.medical_value,t.incentive_value,t.spl_incen_value,t.coupon_amt,e.uan_no," +
  			"t.machine1_rate,t.machine2_rate,t.machine1_days,t.machine2_days,t.machine1_value,t.machine2_value,e.adhar_no,ifnull(e.pan_no,''),t.basicpf_value,0 prevdays,0 prevgrosswages,0 prevesisvalue,0 prevemployeresisvalue,0 prevarreardays,0 prevnetwages "+
  			" from emptran t,employeemast e "+
			" where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and ifnull(t.del_tag,'')<>'D' and e.depo_Code=? and e.cmp_code=? "+
			" and e.emp_code=t.emp_code "+orderby;

  			if(repno==3)
  			{
  	  			query="select e.esic_no,e.emp_name,t.atten_days,(t.basic+t.da+t.hra+t.add_hra+t.incentive+t.spl_incentive) totalwages, 0 reason, e.doresign lwdate," +
  	  	  			"e.emp_code,e.pf_no,t.esis_value,t.employer_esis_value,t.serialno,t.pf_value,t.advance," +
  	  	  			"t.loan+ifnull((select repay_amt  from loan  where fin_year=? and depo_code=? and cmp_code=? and emp_code=t.emp_code and mnth_code=? and instl_amt=0),0) loan," +
  	  	  			"t.stair_days,t.stair_alw,t.stair_value," +
  	  	  			"t.extra_hrs,t.arrear_days,t.ot_rate,t.ot_value,t.employer_pf,t.eps_pf,(t.basic_value+t.da_value+t.food_value+t.incentive_value+t.medical_value) grosspf,(t.basic+t.da) salarypf," +
  	  	  			"(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incentive_value+t.spl_incen_value+t.stair_value+t.misc_value+t.ot_value+t.lta_value+t.medical_value+t.food_value) netwages," +
  	  	  			"t.absent_days,t.misc_value,  " +
  	  	  			"ifnull(e.bank,'Direct Cheque'),e.bank_add1,e.ifsc_code,ifnull(e.bank_accno,''),e.bank_code,t.lta_value,t.medical_value,t.incentive_value,t.spl_incen_value,t.coupon_amt,e.uan_no," +
  	  	  			"t.machine1_rate,t.machine2_rate,t.machine1_days,t.machine2_days,t.machine1_value,t.machine2_value,e.adhar_no,ifnull(e.pan_no,''),t.basicpf_value,0 prevdays,0 prevgrosswages,0 prevesisvalue,0 prevemployeresisvalue,0 prevarreardays,0 prevnetwages "+
  	  	  			" from emptran t,employeemast e "+
  	  				" where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and ifnull(t.del_tag,'')<>'D' and e.depo_Code=? and e.cmp_code=? "+
  	  				" and e.emp_code=t.emp_code "+orderby;
  				
  			}
  			
  			
  			if(repno<3)
  			{
  				/*query="select e.esic_no,e.emp_name,sum(t.atten_days),sum(totalwages), 0 reason, e.doresign lwdate, "+ 
  						"e.emp_code,e.pf_no,sum(t.esis_value),sum(t.employer_esis_value),t.serialno,sum(t.pf_value),sum(t.advance),sum(t.loan),t.stair_days,sum(t.stair_alw),sum(t.stair_value), "+ 
  						"sum(t.extra_hrs),sum(t.arrear_days),t.ot_rate,sum(t.ot_value),sum(t.employer_pf),sum(t.eps_pf), sum(grosspf), sum(salarypf),sum(netwages), "+
  						"sum(t.absent_days),sum(t.misc_value),   "+
  						"ifnull(e.bank,'Direct Cheque'),e.bank_add1,e.ifsc_code,ifnull(e.bank_accno,''),e.bank_code,sum(t.lta_value),sum(t.medical_value),sum(t.incentive_value),sum(t.spl_incen_value),sum(t.coupon_amt),e.uan_no," +
  						"sum(t.machine1_rate),sum(t.machine2_rate),sum(t.machine1_days),sum(t.machine2_days),sum(t.machine1_value),sum(t.machine2_value),e.adhar_no,ifnull(e.pan_no,'')  from "+ 
  						"(select t.emp_code,0 atten_days,0 totalwages, "+
  						"sum(t.esic_value) esis_value,0 employer_esis_value,0 serialno,sum(t.pf_value) pf_value,0 advance,0 loan,0 stair_days,0 stair_alw,0 stair_value, "+ 
  						"0 extra_hrs,0 arrear_days,0 ot_rate,0 ot_value,0 employer_pf,0 eps_pf,sum(t.basic_value+t.da_value+t.incen_value) grosspf,0  salarypf,  "+
  						"sum(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incen_value+t.spl_incen_value) netwages, "+
  						"0 absent_days,0 misc_value,0 lta_value,0 medical_value,0 incentive_value,0 spl_incen_value,0 coupon_amt, "+
  			  			"0 machine1_rate,0 machine2_rate,0 machine1_days,0 machine2_days,0 machine1_value,0 machine2_value "+
  						"from arrear t where t.fin_year<=? and t.depo_code=? and t.cmp_code=? and t.arrear_mon=? and t.arrear_paid='Y' group by t.emp_code "+ 
  						"union all "+
  						"select t.emp_code,0 atten_days,0 totalwages, "+ 
  						"sum(t.esic_value) esis_value,0 employer_esis_value,0 serialno,sum(t.pf_value) pf_value,0 advance,0 loan,0 stair_days,0 stair_alw,0 stair_value, "+ 
  						"0 extra_hrs,0 arrear_days,0 ot_rate,0 ot_value,0 employer_pf,0 eps_pf,sum(t.basic_value+t.da_value+t.incen_value) grosspf,0  salarypf,  "+
  						"sum(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incen_value+t.spl_incen_value) netwages, "+
  						"0 absent_days,0 misc_value,0 lta_value,0 medical_value,0 incentive_value,0 spl_incen_value,0 coupon_amt, "+
  			  			"0 machine1_rate,0 machine2_rate,0 machine1_days,0 machine2_days,0 machine1_value,0 machine2_value "+
  						"from arrear t where t.fin_year<=? and t.depo_code=? and t.cmp_code=? and t.arrear_mon=? and t.arrear_paid='O' group by t.emp_code) t,employeemast e "+ 
  						"where  e.depo_Code=? and e.cmp_code=? and e.emp_code=t.emp_code group by t.emp_code "; */

  				// for arrear only implemented on 16/05/2020 special case
  				/*query="select e.esic_no,e.emp_name,sum(t.atten_days),sum(totalwages), 0 reason, e.doresign lwdate, "+ 
				"e.emp_code,e.pf_no,sum(t.esis_value),sum(t.employer_esis_value),t.serialno,sum(t.pf_value),sum(t.advance),sum(t.loan),t.stair_days,sum(t.stair_alw),sum(t.stair_value), "+ 
				"sum(t.extra_hrs),sum(t.arrear_days),t.ot_rate,sum(t.ot_value),sum(t.employer_pf),sum(t.eps_pf), sum(grosspf), sum(salarypf),sum(netwages), "+
				"sum(t.absent_days),sum(t.misc_value),   "+
				"ifnull(e.bank,'Direct Cheque'),e.bank_add1,e.ifsc_code,ifnull(e.bank_accno,''),e.bank_code,sum(t.lta_value),sum(t.medical_value),sum(t.incentive_value),sum(t.spl_incen_value),sum(t.coupon_amt),e.uan_no," +
				"sum(t.machine1_rate),sum(t.machine2_rate),sum(t.machine1_days),sum(t.machine2_days),sum(t.machine1_value),sum(t.machine2_value),e.adhar_no,ifnull(e.pan_no,'')  from "+ 
				"(select t.emp_code,t.atten_days,(t.basic+t.da+t.hra+t.add_hra+t.incentive+t.spl_incentive) totalwages, "+
				"t.esis_value,t.employer_esis_value,t.serialno,t.pf_value,t.advance,t.loan,t.stair_days,t.stair_alw,t.stair_value, "+ 
				"t.extra_hrs,t.arrear_days,t.ot_rate,t.ot_value,t.employer_pf,t.eps_pf,0 grosspf,(t.basic+t.da) salarypf, "+ 
				"0 netwages, "+ 
				"t.absent_days,t.misc_value,t.lta_value,t.medical_value,t.incentive_value,t.spl_incen_value,t.coupon_amt, "+
	  			"t.machine1_rate,t.machine2_rate,t.machine1_days,t.machine2_days,t.machine1_value,t.machine2_value "+
				"from emptran t where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=?  and ifnull(t.del_tag,'')<>'D' "+ 
				"union all "+
				"select t.emp_code,0 atten_days,0 totalwages, "+
				"sum(t.esic_value) esic_value,0 employer_esis_value,0 serialno,sum(t.pf_value) pf_value,0 advance,0 loan,0 stair_days,0 stair_alw,0 stair_value, "+ 
				"0 extra_hrs,0 arrear_days,0 ot_rate,0 ot_value,0 employer_pf,0 eps_pf,sum(t.basic_value+t.da_value+t.incen_value) grosspf,0  salarypf,  "+
				"sum(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incen_value+t.spl_incen_value) netwages, "+
				"0 absent_days,0 misc_value,0 lta_value,0 medical_value,0 incen_value,0 spl_incen_value,0 coupon_amt, "+
	  			"0 machine1_rate,0 machine2_rate,0 machine1_days,0 machine2_days,0 machine1_value,0 machine2_value "+
				"from arrear t where t.fin_year<=? and t.depo_code=? and t.cmp_code=? and t.arrear_mon=? and t.arrear_paid='Y' group by t.emp_code "+ 
				"union all "+
				"select t.emp_code,0 atten_days,0 totalwages, "+
				"sum(t.esic_value) esic_value,0 employer_esis_value,0 serialno,sum(t.pf_value) pf_value,0 advance,0 loan,0 stair_days,0 stair_alw,0 stair_value, "+ 
				"0 extra_hrs,0 arrear_days,0 ot_rate,0 ot_value,0 employer_pf,0 eps_pf,sum(t.basic_value+t.da_value+t.incen_value) grosspf,0  salarypf,  "+
				"sum(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incen_value+t.spl_incen_value) netwages, "+
				"0 absent_days,0 misc_value,0 lta_value,0 medical_value,0 incen_value,0 spl_incen_value,0 coupon_amt, "+
	  			"0 machine1_rate,0 machine2_rate,0 machine1_days,0 machine2_days,0 machine1_value,0 machine2_value "+
				"from arrear t where t.fin_year<=? and t.depo_code=? and t.cmp_code=? and t.arrear_mon=? and t.arrear_paid='O' group by t.emp_code) t,employeemast e "+ 
				"where  e.depo_Code=? and e.cmp_code=? and e.emp_code=t.emp_code group by t.emp_code "; */

  					query="select e.esic_no,e.emp_name,sum(t.atten_days),sum(totalwages), 0 reason, e.doresign lwdate, "+ 
  							"e.emp_code,e.pf_no,sum(t.esis_value),sum(t.employer_esis_value),t.serialno,sum(t.pf_value),sum(t.advance),sum(t.loan),t.stair_days,sum(t.stair_alw),sum(t.stair_value), "+ 
  							"sum(t.extra_hrs),sum(t.arrear_days),t.ot_rate,sum(t.ot_value),sum(t.employer_pf),sum(t.eps_pf), sum(grosspf), sum(salarypf),sum(netwages), "+
  							"sum(t.absent_days),sum(t.misc_value),   "+
  							"ifnull(e.bank,'Direct Cheque'),e.bank_add1,e.ifsc_code,ifnull(e.bank_accno,''),e.bank_code,sum(t.lta_value),sum(t.medical_value),sum(t.incentive_value),sum(t.spl_incen_value),sum(t.coupon_amt),e.uan_no," +
  							"sum(t.machine1_rate),sum(t.machine2_rate),sum(t.machine1_days),sum(t.machine2_days),sum(t.machine1_value),sum(t.machine2_value),e.adhar_no,ifnull(e.pan_no,''),sum(t.basicpf_value),sum(t.prevdays),sum(t.prevgrosswages),sum(t.prevesisvalue),sum(t.prevemployeresisvalue),sum(t.prevarreardays),sum(t.prevnetwages)  from "+ 
  							"(select t.emp_code,t.atten_days,(t.basic+t.da+t.hra+t.add_hra+t.incentive+t.spl_incentive) totalwages, "+
  							"t.esis_value,t.employer_esis_value,t.serialno,t.pf_value,t.advance,t.loan,t.stair_days,t.stair_alw,t.stair_value, "+ 
  							"t.extra_hrs,t.arrear_days,t.ot_rate,t.ot_value,t.employer_pf,t.eps_pf,(t.basic_value+t.da_value+t.food_value+t.incentive_value+t.medical_value) grosspf,(t.basic+t.da+t.incentive+t.medical_value+t.food_alw) salarypf, "+ // added +t.incentive+t.medical_value+t.food_alw in salarypf on 19/12/2024
  							"(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incentive_value+t.spl_incen_value+t.stair_value+t.misc_value+t.ot_value+t.lta_value+t.medical_value+t.machine1_value+t.machine2_value+t.food_value) netwages, "+ 
  							"t.absent_days,t.misc_value,t.lta_value,t.medical_value,t.incentive_value,t.spl_incen_value,t.coupon_amt, "+
  							"t.machine1_rate,t.machine2_rate,t.machine1_days,t.machine2_days,t.machine1_value,t.machine2_value,t.basicpf_value,0 prevdays,0 prevgrosswages,0 prevesisvalue,0 prevemployeresisvalue,0 prevarreardays,0 prevnetwages "+
  							"from emptran t where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=?  and ifnull(t.del_tag,'')<>'D' "+ 
  							"union all "+
  							"select t.emp_code,0 atten_days,0 totalwages, "+
  							"0 esic_value,0 employer_esis_value,0 serialno,0 pf_value,0 advance,0 loan,0 stair_days,0 stair_alw,0 stair_value, "+ 
  							"0 extra_hrs,0 arrear_days,0 ot_rate,0 ot_value,0 employer_pf,0 eps_pf,0 grosspf,0  salarypf,  "+
  							"0 netwages, "+
  							"0 absent_days,0 misc_value,0 lta_value,0 medical_value,0 incen_value,0 spl_incen_value,0 coupon_amt, "+
  							"0 machine1_rate,0 machine2_rate,0 machine1_days,0 machine2_days,0 machine1_value,0 machine2_value,0 basicpf_value,t.atten_days prevdays,(t.basic+t.da+t.hra+t.add_hra+t.incentive+t.spl_incentive) prevgrosswages,t.esis_value prevesisvalue,t.employer_esis_value prevemployeresisvalue,t.arrear_days prevarreardays, "+
  							"(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incentive_value+t.spl_incen_value+t.stair_value+t.misc_value+t.ot_value+t.lta_value+t.medical_value+t.machine1_value+t.machine2_value+t.food_value) prevnetwages "+ 
  							"from emptran t where t.fin_year<=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and ifnull(t.del_tag,'')<>'D' group by t.emp_code "+ 
  							"union all "+
  							"select t.emp_code,0 atten_days,0 totalwages, "+
  							"sum(t.esic_value) esic_value,sum(t.employer_esic_value)  employer_esis_value,0 serialno,sum(t.pf_value) pf_value,0 advance,0 loan,0 stair_days,0 stair_alw,0 stair_value, "+ 
  							"0 extra_hrs,0 arrear_days,0 ot_rate,0 ot_value,0 employer_pf,0 eps_pf,sum(t.basic_value+t.da_value+t.incen_value) grosspf,0  salarypf,  "+
  							"sum(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incen_value+t.spl_incen_value) netwages, "+
  							"0 absent_days,0 misc_value,0 lta_value,0 medical_value,0 incen_value,0 spl_incen_value,0 coupon_amt, "+
  							"0 machine1_rate,0 machine2_rate,0 machine1_days,0 machine2_days,0 machine1_value,0 machine2_value,0 basicpf_value,0 prevdays,0 prevgrosswages,0 prevesisvalue,0 prevemployeresisvalue,0 prevarreardays,0 prevnetwages "+
  							"from arrear t where t.fin_year<=? and t.depo_code=? and t.cmp_code=? and t.arrear_mon=? and t.arrear_paid='Y' group by t.emp_code "+ 
  							"union all "+
  							"select t.emp_code,0 atten_days,0 totalwages, "+
  							"sum(t.esic_value) esic_value,sum(t.employer_esic_value) employer_esis_value,0 serialno,sum(t.pf_value) pf_value,0 advance,0 loan,0 stair_days,0 stair_alw,0 stair_value, "+ 
  							"0 extra_hrs,0 arrear_days,0 ot_rate,0 ot_value,0 employer_pf,0 eps_pf,sum(t.basic_value+t.da_value+t.incen_value) grosspf,sum(t.basic_value)  salarypf,  "+
  							"sum(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incen_value+t.spl_incen_value) netwages, "+
  							"0 absent_days,0 misc_value,0 lta_value,0 medical_value,0 incen_value,0 spl_incen_value,0 coupon_amt, "+
  							"0 machine1_rate,0 machine2_rate,0 machine1_days,0 machine2_days,0 machine1_value,0 machine2_value,0 basicpf_value,0 prevdays,0 prevgrosswages,0 prevesisvalue,0 prevemployeresisvalue,0 prevarreardays,0 prevnetwages "+
  							"from arrear t where t.fin_year<=? and t.depo_code=? and t.cmp_code=? and t.arrear_mon=? and t.arrear_paid='O' group by t.emp_code) t,employeemast e "+ 
  							"where  e.depo_Code=? and e.cmp_code=?  and e.emp_code=t.emp_code group by t.emp_code "+orderby; 
  					
  				
  				if(mnth_code==202004 && repno==2)
  				{
  					System.out.println("yaha par ayaa hai dekho");
  	  				query="select e.esic_no,e.emp_name,sum(t.atten_days),sum(totalwages), 0 reason, e.doresign lwdate, "+ 
  	  					"e.emp_code,e.pf_no,sum(t.esis_value),sum(t.employer_esis_value),t.serialno,sum(t.pf_value),sum(t.advance),sum(t.loan),t.stair_days,sum(t.stair_alw),sum(t.stair_value), "+ 
  	  					"sum(t.extra_hrs),sum(t.arrear_days),t.ot_rate,sum(t.ot_value),sum(t.employer_pf),sum(t.eps_pf), sum(grosspf), sum(salarypf),sum(netwages), "+
  	  					"sum(t.absent_days),sum(t.misc_value),   "+
  	  					"ifnull(e.bank,'Direct Cheque'),e.bank_add1,e.ifsc_code,ifnull(e.bank_accno,''),e.bank_code,sum(t.lta_value),sum(t.medical_value),sum(t.incentive_value),sum(t.spl_incen_value),sum(t.coupon_amt),e.uan_no," +
  	  					"sum(t.machine1_rate),sum(t.machine2_rate),sum(t.machine1_days),sum(t.machine2_days),sum(t.machine1_value),sum(t.machine2_value),e.adhar_no,ifnull(e.pan_no,''),sum(t.basicpf_value),sum(t.prevdays)  from "+ 
  	  					"(select t.emp_code,t.atten_days,(t.basic+t.da+t.hra+t.add_hra+t.incentive+t.spl_incentive) totalwages, "+
  	  					"t.esis_value,t.employer_esis_value,t.serialno,t.pf_value,t.advance,t.loan,t.stair_days,t.stair_alw,t.stair_value, "+ 
  	  					"t.extra_hrs,t.arrear_days,t.ot_rate,t.ot_value,t.employer_pf,t.eps_pf,(t.basic_value+t.da_value+t.food_value+t.incentive_value+t.medical_value) grosspf,(t.basic+t.da) salarypf, "+ 
  	  					"(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incentive_value+t.spl_incen_value+t.stair_value+t.misc_value+t.ot_value+t.lta_value+t.medical_value+t.machine1_value+t.machine2_value+t.food_value) netwages, "+ 
  	  					"t.absent_days,t.misc_value,t.lta_value,t.medical_value,t.incentive_value,t.spl_incen_value,t.coupon_amt, "+
  	  		  			"t.machine1_rate,t.machine2_rate,t.machine1_days,t.machine2_days,t.machine1_value,t.machine2_value,t.basicpf_value,0 prevdays,0 prevgrosswages,0 prevesisvalue,0 prevemployeresisvalue,0 prevarreardays,0 prevnetwages "+
  	  					"from emptran t where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=?  and ifnull(t.del_tag,'')<>'D' ) t,employeemast e "+ 
  	  					"where  e.depo_Code=? and e.cmp_code=? and e.emp_code=t.emp_code AND E.EMP_CODE IN (642,11002) group by t.emp_code "; 
  				}

  			} 
  			ps = con.prepareStatement(query); 
  			if(repno<3)
  			{
  				if(mnth_code==202004 && repno==2)
  				{
	  	  			ps.setInt(1, fyear);
	  	  			ps.setInt(2, depo_code);
	  	  			ps.setInt(3, cmp_code);
	  	  			ps.setInt(4, mnth_code);
	  	  			ps.setInt(5, depo_code);
	  	  			ps.setInt(6, cmp_code);
  				}
  				else
  				{
	  	  		    ps.setInt(1, fyear);
		  			ps.setInt(2, depo_code);
		  			ps.setInt(3, cmp_code);
		  			ps.setInt(4, mnth_code);
		  			ps.setInt(5, fyear);
		  			ps.setInt(6, depo_code);
		  			ps.setInt(7, cmp_code);
		  			ps.setInt(8, lmnth_code);
		  			ps.setInt(9, fyear);
		  			ps.setInt(10, depo_code);
		  			ps.setInt(11, cmp_code); 
		  			ps.setInt(12, mnth_code);
	  	  		    ps.setInt(13, fyear);
		  			ps.setInt(14, depo_code);
		  			ps.setInt(15, cmp_code);
		  			ps.setInt(16, mnth_code);
		  			ps.setInt(17, depo_code);
		  			ps.setInt(18, cmp_code);
  				}

  			}
  			else if(repno==3)
  			{
  				ps.setInt(1, fyear); 
  				ps.setInt(2, depo_code);
  				ps.setInt(3, cmp_code);
  				ps.setInt(4, mnth_code);
  				ps.setInt(5, fyear); 
  				ps.setInt(6, depo_code);
  				ps.setInt(7, cmp_code);
  				ps.setInt(8, mnth_code);
  				ps.setInt(9, depo_code);
  				ps.setInt(10, cmp_code);
  			}
  			else
  			{
  				ps.setInt(1, fyear); 
  				ps.setInt(2, depo_code);
  				ps.setInt(3, cmp_code);
  				ps.setInt(4, mnth_code);
  				ps.setInt(5, depo_code);
  				ps.setInt(6, cmp_code);
  			}
  			rs =ps.executeQuery();
  			
  			v = new ArrayList();
  			while (rs.next())
  			{
  				if(repno==2)
  				{
  					// arrearbasic = roundTwoDecimals((rs.getDouble(25)/30)*rs.getDouble(19)); //uncomment on 14/12/2024
  					//arrearbasic = roundTwoDecimals((rs.getDouble(48)/30)*rs.getDouble(19)); // salary_pfvalue 11/05/2024
  					
  					if(mnth_code>202410) // added on 16/12/2024 by y	ashpal
  						arrearbasic = roundTwoDecimals(((rs.getDouble(25)-rs.getDouble(35))/30)*rs.getDouble(19)); //uncomment on 14/12/2024
  					else
  						arrearbasic = roundTwoDecimals((rs.getDouble(48)/30)*rs.getDouble(19)); // salary_pfvalue 11/05/2024
  					
  					if(mnth_code>=202503)
  							arrearbasic = roundTwoDecimals((rs.getDouble(48)/30)*rs.getDouble(19));
  					if(mnth_code>=202505 && rs.getInt(27)> 0)
  						arrearbasic = roundTwoDecimals((rs.getDouble(25)/30)*rs.getDouble(19));
  					
  					arrearnet = roundTwoDecimals((rs.getDouble(4)/30)*rs.getDouble(19));
  				
  				if(rs.getInt(7)==10  )
  				{
  					System.out.println("26 "+rs.getDouble(26)+" 35 "+rs.getDouble(35)+" 19 "+rs.getDouble(19)+" 48 "+rs.getDouble(48)+" arrearnet "+arrearnet);
  					System.out.println("EMP_CODE &&&&&"+rs.getInt(7)+" arrearbasic "+arrearbasic+" 25 "+rs.getDouble(25)+" arrearDays ->19 "+rs.getDouble(19)); 
  					System.out.println("EMP_CODE LINE 913 CAL "+rs.getInt(7)+" "+rs.getDouble(48)+"  "+rs.getDouble(19)+" "+arrearbasic);
  				}
  				}
  				else if(repno==1)
  				{
  					arrearbasic = roundTwoDecimals((rs.getDouble(4)/30)*rs.getDouble(19));
  					prevarrearbasic = roundTwoDecimals((rs.getDouble(50)/30)*rs.getDouble(53));
  					arrearnet = roundTwoDecimals((rs.getDouble(4)/30)*rs.getDouble(19));
  				}
  				
  				emp = new EmptranDto();
  				emp.setEsic_no(rs.getLong(1));  // ip number
  				emp.setEmp_name(rs.getString(2)); // ip name
  				//emp.setAtten_days((int) (rs.getDouble(3)+.50)); // paid days
  				emp.setAtten_days(rs.getDouble(3)); // paid days
  				emp.setGross(rs.getDouble(4)); // total wages
  				emp.setCreated_by(rs.getInt(5)); // reason
  				emp.setCreated_date(rs.getDate(6)); // last working date
  				emp.setEmp_code(rs.getInt(7));
  				emp.setPf_no(rs.getInt(8));
  				emp.setEsis_value(rs.getDouble(9));
  				emp.setEmployer_esis_value(rs.getDouble(10));
  				emp.setArrear_days(rs.getDouble(19));

  				emp.setArrear_basic_value(arrearbasic);
  				
  				if(repno==1)
  				{
  					if(mnth_code>201906)
  					{
  						if(opt==2) // ARREAR OPTION
  						{
  							emp.setEsis_value(roundTwoDecimals(arrearbasic*0.75/100));
  							emp.setEmployer_esis_value(roundTwoDecimals(arrearbasic*3.25/100));
  							
  							// below calculation is added on 17/08/2024
  							emp.setEsis_value(rs.getDouble(51)-roundTwoDecimals(prevarrearbasic*0.75/100)+roundTwoDecimals(arrearbasic*0.75/100));
  							emp.setEmployer_esis_value(rs.getDouble(52)-roundTwoDecimals(prevarrearbasic*3.25/100)+roundTwoDecimals(arrearbasic*3.25/100));

  			  				
  			  				emp.setArrear_basic_value(arrearbasic+rs.getDouble(54)-prevarrearbasic);
  			  				

  			  			    //emp.setArrear_days(rs.getDouble(49)+rs.getDouble(19)); // paid days + prev atten days
  							
  						}
  						else if(opt==1)  // ESIC OPTION add if condition in else on 17/08/2024 (YashPal)
  						{
  							emp.setEsis_value(emp.getEsis_value()-roundTwoDecimals(arrearbasic*0.75/100));
  							emp.setEmployer_esis_value(emp.getEmployer_esis_value()-roundTwoDecimals(arrearbasic*3.25/100));
// 							emp.setEmployer_esis_value(emp.getEmployer_esis_value()-roundTwoDecimals(arrearbasic*3.25/100,0));
  							System.out.println("emp name "+rs.getString(2)+" arrearebasic "+arrearbasic+" "+emp.getEmployer_esis_value());
 							

  						}
 
  					}
  					else
  					{
  						emp.setEsis_value(roundTwoDecimals(arrearbasic*1.75/100));
  						emp.setEmployer_esis_value(roundTwoDecimals(arrearbasic*4.75/100));
  					}
  				}
  				
  				if (emp.getArrear_days()>0)
  				 System.out.println("ARREAR DAYS "+emp.getArrear_days()+ " emp COde "+emp.getEmp_code());
  				emp.setSerialno(sno++);
  				emp.setPf_value(rs.getDouble(12));
  				emp.setAdvance(rs.getDouble(13));
  				emp.setLoan(rs.getDouble(14));
  				emp.setStair_days(rs.getInt(15));
  				emp.setStair_alw(rs.getDouble(16));
  				emp.setStair_value(rs.getDouble(17));
  				emp.setExtra_hrs(rs.getDouble(18));
//  				emp.setArrear_days(rs.getDouble(19));
  				emp.setOt_rate(rs.getDouble(20));
  				emp.setOt_value(rs.getDouble(21));
  				 
  				emp.setEmployee_pf(rs.getDouble(12));
  				emp.setEmployer_pf(rs.getDouble(22));
  				emp.setEps_pf(rs.getDouble(23));
  				
//  				emp.setBasic_value(rs.getDouble(24)); //TEMPORARY COMMENT ON 14/12/2024
   				if(repno==2 && mnth_code>202410) // PF/ Arrear List we use field 48 i.e basicPF_value 16/12/2024
  					emp.setBasic_value(rs.getDouble(48)); 
  				else
  					emp.setBasic_value(rs.getDouble(24)); 

  				
   				if(repno==2 && mnth_code>=202503)
  					emp.setBasic_value(rs.getDouble(24)); 
   				
					if(emp.getEmp_code()==977)
  						System.out.println("24 ki value "+rs.getDouble(24)+" 48 ki value "+rs.getDouble(48)+" 25 ki value "+rs.getDouble(25));

  				
  				if(repno==2 && mnth_code==202502 && (emp.getEmp_code()==15 || emp.getEmp_code()==139 || emp.getEmp_code()==403 || emp.getEmp_code()==849 || emp.getEmp_code()==171)) // PF/ Arrear List we use field 48 i.e basicPF_value 27/02/2025
  					System.out.println("do nothing"); 
  				else if(repno==2 && mnth_code==202502)
  				{
  					emp.setBasic_value(rs.getDouble(24));
  				}
  				emp.setBasic(rs.getDouble(25));
  				emp.setNet_value(rs.getDouble(26));
  				emp.setAbsent_days(rs.getDouble(27));
  				emp.setMisc_value(rs.getDouble(28));
  				
  				//setting bank detials
  				emp.setBank(rs.getString(29).length()>1?rs.getString(29):"Direct Cheque");
  				emp.setBank_add1(rs.getString(30));
  				emp.setIfsc_code(rs.getString(31));
  				emp.setBank_accno(rs.getString(32));
  				emp.setBank_code(rs.getInt(33));
  				emp.setLta_value(rs.getDouble(34));
  				emp.setMedical_value(rs.getDouble(35));
  				emp.setIncentive_value(rs.getDouble(36));
  				emp.setSpl_incen_value(rs.getDouble(37));
  				emp.setCoupon_amt(rs.getDouble(38));
  				emp.setUan_no(rs.getLong(39));
  				emp.setMachine1_rate(rs.getDouble(40));
  				emp.setMachine2_rate(rs.getDouble(41));
  				emp.setMachine1_days(rs.getDouble(42));
  				emp.setMachine2_days(rs.getDouble(43));
  				emp.setMachine1_value(rs.getDouble(44));
  				emp.setMachine2_value(rs.getDouble(45));
  				//emp.setArrear_basic_value(arrearbasic);
  				emp.setArrear_net_value(arrearnet);
  				emp.setAdhar_no(rs.getLong(46));  
  				emp.setPan_no(rs.getString(47));
  				emp.setDiffdays(0);
  				emp.setMnth_code(mnth_code);
  				emp.setBasicpf_value(rs.getDouble(48));
  				
  				//System.out.println(rs.getDouble(48));
  				emp.setPrev_days(rs.getDouble(49));
  				emp.setNcp_days(totalmnthdays-(rs.getDouble(19)+rs.getDouble(49)));
  				
  				//System.out.println("basic VALUE AND NET VALUE  24 "+rs.getDouble(24)+" 26 "+rs.getDouble(26)+" BASICPF "+rs.getDouble(48));
  				
  				if(rs.getDate(6)!=null)
  				{
  					if(rs.getDate(6).after(edate))
  					{
  						diffDays=0;
  						emp.setCreated_date(null);
  					}
  					else
  					{
  						diff = (rs.getDate(6).getTime() - edate.getTime());
  						diffDays = diff / (24 * 60 * 60 * 1000);
  					}
  					emp.setDiffdays(diffDays);
  					//System.out.println("days is "+diffDays);
  				}
  				if(repno==1 && (emp.getGross()==0 ||  emp.getGross()>21000))
  					sno--;
  				else 
  					v.add(emp);
  			}



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
  			System.out.println("-------------Exception in PayrollDAO.getEsicList " + ex);

  		}
  		finally {
  			try {
  				System.out.println("No. of Records Update/Insert : " );

  				if(rs != null){rs.close();}
  				if(ps != null){ps.close();}
  				if(con != null){con.close();}
  			} catch (SQLException e) {
  				System.out.println("-------------Exception in PayrollDAO.Connection.close --- getEsicList "+e);
  			}
  		}
  		return v;
  	}    

    public ArrayList getEsicListNew(int depo_code,int cmp_code,int fyear,int mnth_code,int repno,int opt)
  	{
  		PreparedStatement ps = null;
  		ResultSet rs=null;
  		PreparedStatement ps1 = null;
  		ResultSet rs1=null;
  		Connection con=null;
  		ArrayList v =null;
  		 
  		EmptranDto emp=null;
  		int sno=1;
  		int bkcode=0;
  		double total=0.00;
  		boolean first=false;
  		long diff=0;
  		long diffDays=0;
  		double arrearbasic=0.00;
  		double prevarrearbasic=0.00;
  		double arrearnet=0.00;
  		String mncode = String.valueOf(mnth_code);
  		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
  		Date edate=null;
  		try {
			 edate = sdf.parse((mncode.substring(0,4)+"-"+mncode.substring(5)+"-01"));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
  		String orderby="";
  		if(repno==10)
  			orderby="  order by e.bank_code,e.emp_code ";
  		else if(repno==1)
  			orderby="  order by e.esic_no ";
  		 
  		int lmnth_code=0;
  		int totalmnthdays=0;
  		try 
  		{
  			con=ConnectionFactory.getConnection();
  			con.setAutoCommit(false);

  			
  			String lastmonthQuery="SELECT MNTH_CODE,day(todate) FROM PERDMAST WHERE MNTH_CODE<? ORDER BY MNTH_CODE DESC LIMIT 1";
  			ps1=con.prepareStatement(lastmonthQuery);
  			ps1.setInt(1, mnth_code);
  			rs1=ps1.executeQuery();
  			if(rs1.next())
  			{
  				lmnth_code=rs1.getInt(1);
  				totalmnthdays=rs1.getInt(2);
  			}
  			
  			rs1.close();
  			ps1.close();
  			
  			
  			String query="select e.esic_no,e.emp_name,t.atten_days,(t.basic+t.da+t.hra+t.add_hra+t.incentive+t.spl_incentive) totalwages, 0 reason, e.doresign lwdate," +
  			"e.emp_code,e.pf_no,t.esis_value,t.employer_esis_value,t.serialno,t.pf_value,t.advance,t.loan,t.stair_days,t.stair_alw,t.stair_value," +
  			"t.extra_hrs,t.arrear_days,t.ot_rate,t.ot_value,t.employer_pf,t.eps_pf,(t.basic_value+t.da_value+t.food_value+t.incentive_value+t.medical_value) grosspf,(t.basic+t.da) salarypf," +
  			"(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incentive_value+t.spl_incen_value+t.stair_value+t.misc_value+t.ot_value+t.lta_value+t.medical_value+t.food_value) netwages," +
  			"t.absent_days,t.misc_value,  " +
  			"ifnull(e.bank,'Direct Cheque'),e.bank_add1,e.ifsc_code,ifnull(e.bank_accno,''),e.bank_code,t.lta_value,t.medical_value,t.incentive_value,t.spl_incen_value,t.coupon_amt,e.uan_no," +
  			"t.machine1_rate,t.machine2_rate,t.machine1_days,t.machine2_days,t.machine1_value,t.machine2_value,e.adhar_no,ifnull(e.pan_no,''),t.basicpf_value,0 prevdays,0 prevgrosswages,0 prevesisvalue,0 prevemployeresisvalue,0 prevarreardays,0 prevnetwages "+
  			" from emptran t,employeemast e "+
			" where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and ifnull(t.del_tag,'')<>'D' and e.depo_Code=? and e.cmp_code=? "+
			" and e.emp_code=t.emp_code "+orderby;

  			if(repno==3)
  			{
  	  			query="select e.esic_no,e.emp_name,t.atten_days,(t.basic+t.da+t.hra+t.add_hra+t.incentive+t.spl_incentive) totalwages, 0 reason, e.doresign lwdate," +
  	  	  			"e.emp_code,e.pf_no,t.esis_value,t.employer_esis_value,t.serialno,t.pf_value,t.advance," +
  	  	  			"t.loan+ifnull((select repay_amt  from loan  where fin_year=? and depo_code=? and cmp_code=? and emp_code=t.emp_code and mnth_code=? and instl_amt=0),0) loan," +
  	  	  			"t.stair_days,t.stair_alw,t.stair_value," +
  	  	  			"t.extra_hrs,t.arrear_days,t.ot_rate,t.ot_value,t.employer_pf,t.eps_pf,(t.basic_value+t.da_value+t.food_value+t.incentive_value+t.medical_value) grosspf,(t.basic+t.da) salarypf," +
  	  	  			"(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incentive_value+t.spl_incen_value+t.stair_value+t.misc_value+t.ot_value+t.lta_value+t.medical_value+t.food_value) netwages," +
  	  	  			"t.absent_days,t.misc_value,  " +
  	  	  			"ifnull(e.bank,'Direct Cheque'),e.bank_add1,e.ifsc_code,ifnull(e.bank_accno,''),e.bank_code,t.lta_value,t.medical_value,t.incentive_value,t.spl_incen_value,t.coupon_amt,e.uan_no," +
  	  	  			"t.machine1_rate,t.machine2_rate,t.machine1_days,t.machine2_days,t.machine1_value,t.machine2_value,e.adhar_no,ifnull(e.pan_no,''),t.basicpf_value,0 prevdays,0 prevgrosswages,0 prevesisvalue,0 prevemployeresisvalue,0 prevarreardays,0 prevnetwages "+
  	  	  			" from emptran t,employeemast e "+
  	  				" where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and ifnull(t.del_tag,'')<>'D' and e.depo_Code=? and e.cmp_code=? "+
  	  				" and e.emp_code=t.emp_code "+orderby;
  				
  			}
  			
  			
  			if(repno<3)
  			{
   					query="select e.esic_no,e.emp_name,sum(t.atten_days),sum(totalwages), 0 reason, e.doresign lwdate, "+ 
  							"e.emp_code,e.pf_no,sum(t.esis_value),sum(t.employer_esis_value),t.serialno,sum(t.pf_value),sum(t.advance),sum(t.loan),t.stair_days,sum(t.stair_alw),sum(t.stair_value), "+ 
  							"sum(t.extra_hrs),sum(t.arrear_days),t.ot_rate,sum(t.ot_value),sum(t.employer_pf),sum(t.eps_pf), sum(grosspf), sum(salarypf),ROUND(sum(netwages),0), "+
  							"sum(t.absent_days),sum(t.misc_value),   "+
  							"ifnull(e.bank,'Direct Cheque'),e.bank_add1,e.ifsc_code,ifnull(e.bank_accno,''),e.bank_code,sum(t.lta_value),sum(t.medical_value),sum(t.incentive_value),sum(t.spl_incen_value),sum(t.coupon_amt),e.uan_no," +
  							"sum(t.machine1_rate),sum(t.machine2_rate),sum(t.machine1_days),sum(t.machine2_days),sum(t.machine1_value),sum(t.machine2_value),e.adhar_no,ifnull(e.pan_no,''),sum(t.basicpf_value),sum(t.prevdays),sum(t.prevgrosswages),sum(t.prevesisvalue),sum(t.prevemployeresisvalue),sum(t.prevarreardays),sum(t.prevnetwages)  from "+ 
  							"(select t.emp_code,t.atten_days,(t.basic+t.da+t.hra+t.add_hra+t.incentive+t.spl_incentive) totalwages, "+
  							"t.esis_value,t.employer_esis_value,t.serialno,t.pf_value,t.advance,t.loan,t.stair_days,t.stair_alw,t.stair_value, "+ 
  							"t.extra_hrs,t.arrear_days,t.ot_rate,t.ot_value,t.employer_pf,t.eps_pf,(t.basic_value+t.da_value+t.food_value+t.incentive_value+t.medical_value) grosspf,(t.basic+t.da+t.incentive+t.medical_value+t.food_alw) salarypf, "+ // added +t.incentive+t.medical_value+t.food_alw in salarypf on 19/12/2024
  							"round ((t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incentive_value+t.spl_incen_value+t.stair_value+t.misc_value+t.ot_value+t.lta_value+t.medical_value+t.machine1_value+t.machine2_value+t.food_value+t.arear_basic_value+t.arear_da_value+t.arear_hra_value+t.arear_incentive_value+t.arear_medical_value),2)+.50 netwages, "+  							
  							"t.absent_days,t.misc_value,t.lta_value,t.medical_value,t.incentive_value,t.spl_incen_value,t.coupon_amt, "+
  							"t.machine1_rate,t.machine2_rate,t.machine1_days,t.machine2_days,t.machine1_value,t.machine2_value,t.basicpf_value,0 prevdays,0 prevgrosswages,0 prevesisvalue,0 prevemployeresisvalue,0 prevarreardays,0 prevnetwages "+
  							"from emptran t where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=?  and ifnull(t.del_tag,'')<>'D' "+ 
  							"union all "+
  							"select t.emp_code,0 atten_days,0 totalwages, "+
  							"0 esic_value,0 employer_esis_value,0 serialno,0 pf_value,0 advance,0 loan,0 stair_days,0 stair_alw,0 stair_value, "+ 
  							"0 extra_hrs,0 arrear_days,0 ot_rate,0 ot_value,0 employer_pf,0 eps_pf,0 grosspf,0  salarypf,  "+
  							"0 netwages, "+
  							"0 absent_days,0 misc_value,0 lta_value,0 medical_value,0 incen_value,0 spl_incen_value,0 coupon_amt, "+
  							"0 machine1_rate,0 machine2_rate,0 machine1_days,0 machine2_days,0 machine1_value,0 machine2_value,0 basicpf_value,t.atten_days prevdays,(t.basic+t.da+t.hra+t.add_hra+t.incentive+t.spl_incentive) prevgrosswages,t.esis_value prevesisvalue,t.employer_esis_value prevemployeresisvalue,t.arrear_days prevarreardays, "+
  							"(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incentive_value+t.spl_incen_value+t.stair_value+t.misc_value+t.ot_value+t.lta_value+t.medical_value+t.machine1_value+t.machine2_value+t.food_value) prevnetwages "+ 
  							"from emptran t where t.fin_year<=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and ifnull(t.del_tag,'')<>'D' group by t.emp_code "+ 
  							"union all "+
  							"select t.emp_code,0 atten_days,0 totalwages, "+
  							"sum(t.esic_value) esic_value,sum(t.employer_esic_value)  employer_esis_value,0 serialno,sum(t.pf_value) pf_value,0 advance,0 loan,0 stair_days,0 stair_alw,0 stair_value, "+ 
  							"0 extra_hrs,0 arrear_days,0 ot_rate,0 ot_value,0 employer_pf,0 eps_pf,sum(t.basic_value+t.da_value+t.incen_value) grosspf,0  salarypf,  "+
  							"sum(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incen_value+t.spl_incen_value) netwages, "+
  							"0 absent_days,0 misc_value,0 lta_value,0 medical_value,0 incen_value,0 spl_incen_value,0 coupon_amt, "+
  							"0 machine1_rate,0 machine2_rate,0 machine1_days,0 machine2_days,0 machine1_value,0 machine2_value,0 basicpf_value,0 prevdays,0 prevgrosswages,0 prevesisvalue,0 prevemployeresisvalue,0 prevarreardays,0 prevnetwages "+
  							"from arrear t where t.fin_year<=? and t.depo_code=? and t.cmp_code=? and t.arrear_mon=? " +
  							"and t.arrear_paid in ('Y','N') group by t.emp_code ) t,employeemast e "+ 
  							"where  e.depo_Code=? and e.cmp_code=?  and e.emp_code=t.emp_code group by t.emp_code "+orderby; 
  				System.out.println(query);	
  				
  			} 
  			ps = con.prepareStatement(query); 
  			if(repno<3)
  			{
	  	  		    ps.setInt(1, fyear);
		  			ps.setInt(2, depo_code);
		  			ps.setInt(3, cmp_code);
		  			ps.setInt(4, mnth_code);
		  			ps.setInt(5, fyear);
		  			ps.setInt(6, depo_code);
		  			ps.setInt(7, cmp_code);
		  			ps.setInt(8, lmnth_code);
		  			ps.setInt(9, fyear);
		  			ps.setInt(10, depo_code);
		  			ps.setInt(11, cmp_code); 
		  			ps.setInt(12, mnth_code);
		  			ps.setInt(13, depo_code);
		  			ps.setInt(14, cmp_code);
  				

  			}
  			else if(repno==3)
  			{
  				ps.setInt(1, fyear); 
  				ps.setInt(2, depo_code);
  				ps.setInt(3, cmp_code);
  				ps.setInt(4, mnth_code);
  				ps.setInt(5, fyear); 
  				ps.setInt(6, depo_code);
  				ps.setInt(7, cmp_code);
  				ps.setInt(8, mnth_code);
  				ps.setInt(9, depo_code);
  				ps.setInt(10, cmp_code);
  			}
  			else
  			{
  				ps.setInt(1, fyear); 
  				ps.setInt(2, depo_code);
  				ps.setInt(3, cmp_code);
  				ps.setInt(4, mnth_code);
  				ps.setInt(5, depo_code);
  				ps.setInt(6, cmp_code);
  			}
  			rs =ps.executeQuery();
  			
  			v = new ArrayList();
  			while (rs.next())
  			{
  				if(rs.getInt(7)==178)
  				System.out.println("26 ki vlaue"+ rs.getDouble(26) );
  				if(repno==2)
  				{
  					
  					if(mnth_code>202410) // added on 16/12/2024 by y	ashpal
  						arrearbasic = roundTwoDecimals(((rs.getDouble(25)-rs.getDouble(35))/30)*rs.getDouble(19)); //uncomment on 14/12/2024
  					else
  						arrearbasic = roundTwoDecimals((rs.getDouble(48)/30)*rs.getDouble(19)); // salary_pfvalue 11/05/2024
  					
  					if(mnth_code>=202503)
  							arrearbasic = roundTwoDecimals((rs.getDouble(48)/30)*rs.getDouble(19));
  					if(mnth_code>=202505 && rs.getInt(27)> 0)
  						arrearbasic = roundTwoDecimals((rs.getDouble(25)/30)*rs.getDouble(19));
  					
  					arrearnet = roundTwoDecimals((rs.getDouble(4)/30)*rs.getDouble(19));
  				
  				if(rs.getInt(7)==8  )
  				{
  					System.out.println("26 "+rs.getDouble(26)+" 35 "+rs.getDouble(35)+" 19 "+rs.getDouble(19)+" 48 "+rs.getDouble(48)+" arrearnet "+arrearnet);
  					System.out.println("EMP_CODE &&&&&"+rs.getInt(7)+" arrearbasic "+arrearbasic+" 25 "+rs.getDouble(25)+" arrearDays ->19 "+rs.getDouble(19)); 
  					System.out.println("EMP_CODE LINE 913 CAL "+rs.getInt(7)+" "+rs.getDouble(48)+"  "+rs.getDouble(19)+" "+arrearbasic);
  				}
  				}
  				else if(repno==1)
  				{
  					arrearbasic = roundTwoDecimals((rs.getDouble(4)/30)*rs.getDouble(19));
  					prevarrearbasic = roundTwoDecimals((rs.getDouble(50)/30)*rs.getDouble(53));
  					arrearnet = roundTwoDecimals((rs.getDouble(4)/30)*rs.getDouble(19));
  				}
  				arrearbasic=rs.getDouble(10);
  				emp = new EmptranDto();
  				emp.setEsic_no(rs.getLong(1));  // ip number
  				emp.setEmp_name(rs.getString(2)); // ip name
  				//emp.setAtten_days((int) (rs.getDouble(3)+.50)); // paid days
  				emp.setAtten_days(rs.getDouble(3)); // paid days
  				emp.setGross(rs.getDouble(4)); // total wages
  				emp.setCreated_by(rs.getInt(5)); // reason
  				emp.setCreated_date(rs.getDate(6)); // last working date
  				emp.setEmp_code(rs.getInt(7));
  				emp.setPf_no(rs.getInt(8));
  				emp.setEsis_value(rs.getDouble(9));
  				emp.setEmployer_esis_value(rs.getDouble(10));
  				emp.setArrear_days(rs.getDouble(19));

  				emp.setArrear_basic_value(arrearbasic);
  				
  				if(repno==1)
  				{
  					if(mnth_code>201906)
  					{
  						if(opt==2) // ARREAR OPTION
  						{
  							emp.setEsis_value(roundTwoDecimals(arrearbasic*0.75/100));
  							emp.setEmployer_esis_value(roundTwoDecimals(arrearbasic*3.25/100));
  							
  							// below calculation is added on 17/08/2024
  							emp.setEsis_value(rs.getDouble(51)-roundTwoDecimals(prevarrearbasic*0.75/100)+roundTwoDecimals(arrearbasic*0.75/100));
  							emp.setEmployer_esis_value(rs.getDouble(52)-roundTwoDecimals(prevarrearbasic*3.25/100)+roundTwoDecimals(arrearbasic*3.25/100));

  			  				
  			  				emp.setArrear_basic_value(arrearbasic+rs.getDouble(54)-prevarrearbasic);
  			  				

  			  			    //emp.setArrear_days(rs.getDouble(49)+rs.getDouble(19)); // paid days + prev atten days
  							
  						}
  						else if(opt==1)  // ESIC OPTION add if condition in else on 17/08/2024 (YashPal)
  						{
  							emp.setEsis_value(emp.getEsis_value()-roundTwoDecimals(arrearbasic*0.75/100));
  							emp.setEmployer_esis_value(emp.getEmployer_esis_value()-roundTwoDecimals(arrearbasic*3.25/100));
// 							emp.setEmployer_esis_value(emp.getEmployer_esis_value()-roundTwoDecimals(arrearbasic*3.25/100,0));

  						}
 
  					}
  					else
  					{
  						emp.setEsis_value(roundTwoDecimals(arrearbasic*1.75/100));
  						emp.setEmployer_esis_value(roundTwoDecimals(arrearbasic*4.75/100));
  					}
  				}
					emp.setEsis_value(rs.getDouble(9));
					emp.setEmployer_esis_value(rs.getDouble(10));

  				emp.setSerialno(sno++);
  				emp.setPf_value(rs.getDouble(12));
  				emp.setAdvance(rs.getDouble(13));
  				emp.setLoan(rs.getDouble(14));
  				emp.setStair_days(rs.getInt(15));
  				emp.setStair_alw(rs.getDouble(16));
  				emp.setStair_value(rs.getDouble(17));
  				emp.setExtra_hrs(rs.getDouble(18));
//  				emp.setArrear_days(rs.getDouble(19));
  				emp.setOt_rate(rs.getDouble(20));
  				emp.setOt_value(rs.getDouble(21));
  				 
  				emp.setEmployee_pf(rs.getDouble(12));
  				emp.setEmployer_pf(rs.getDouble(22));
  				emp.setEps_pf(rs.getDouble(23));
  				
//  				emp.setBasic_value(rs.getDouble(24)); //TEMPORARY COMMENT ON 14/12/2024
   				if(repno==2 && mnth_code>202410) // PF/ Arrear List we use field 48 i.e basicPF_value 16/12/2024
  					emp.setBasic_value(rs.getDouble(48)); 
  				else
  					emp.setBasic_value(rs.getDouble(24)); 

  				
   				if(repno==2 && mnth_code>=202503)
  					emp.setBasic_value(rs.getDouble(24)+arrearbasic); // CURRENT CORRECTION FOR JUNE 2025 ONWARD
   				
					if(emp.getEmp_code()==977)
  						System.out.println("24 ki value "+rs.getDouble(24)+" 48 ki value "+rs.getDouble(48)+" 25 ki value "+rs.getDouble(25));

  				
  				if(repno==2 && mnth_code==202502 && (emp.getEmp_code()==15 || emp.getEmp_code()==139 || emp.getEmp_code()==403 || emp.getEmp_code()==849 || emp.getEmp_code()==171)) // PF/ Arrear List we use field 48 i.e basicPF_value 27/02/2025
  					System.out.println("do nothing"); 
  				else if(repno==2 && mnth_code==202502)
  				{
  					emp.setBasic_value(rs.getDouble(24));
  				}
  				emp.setBasic(rs.getDouble(25));
  				emp.setNet_value((int)(rs.getDouble(26)));
  				emp.setArear_basic_value(0.00);
  				emp.setAbsent_days(rs.getDouble(27));
  				emp.setMisc_value(rs.getDouble(28));
  				
  				//setting bank detials
  				emp.setBank(rs.getString(29).length()>1?rs.getString(29):"Direct Cheque");
  				emp.setBank_add1(rs.getString(30));
  				emp.setIfsc_code(rs.getString(31));
  				emp.setBank_accno(rs.getString(32));
  				emp.setBank_code(rs.getInt(33));
  				emp.setLta_value(rs.getDouble(34));
  				emp.setMedical_value(rs.getDouble(35));
  				emp.setIncentive_value(rs.getDouble(36));
  				emp.setSpl_incen_value(rs.getDouble(37));
  				emp.setCoupon_amt(rs.getDouble(38));
  				emp.setUan_no(rs.getLong(39));
  				emp.setMachine1_rate(rs.getDouble(40));
  				emp.setMachine2_rate(rs.getDouble(41));
  				emp.setMachine1_days(rs.getDouble(42));
  				emp.setMachine2_days(rs.getDouble(43));
  				emp.setMachine1_value(rs.getDouble(44));
  				emp.setMachine2_value(rs.getDouble(45));
  				//emp.setArrear_basic_value(arrearbasic);
  				emp.setArrear_net_value(arrearnet);
  				emp.setAdhar_no(rs.getLong(46));  
  				emp.setPan_no(rs.getString(47));
  				emp.setDiffdays(0);
  				emp.setMnth_code(mnth_code);
  				emp.setBasicpf_value(rs.getDouble(48));
  				
  				//System.out.println(rs.getDouble(48));
  				emp.setPrev_days(rs.getDouble(49));
  				emp.setNcp_days(totalmnthdays-(rs.getDouble(19)+rs.getDouble(49)));
  				
  				//System.out.println("basic VALUE AND NET VALUE  24 "+rs.getDouble(24)+" 26 "+rs.getDouble(26)+" BASICPF "+rs.getDouble(48));
  				
  				if(rs.getDate(6)!=null)
  				{
  					if(rs.getDate(6).after(edate))
  					{
  						diffDays=0;
  						emp.setCreated_date(null);
  					}
  					else
  					{
  						diff = (rs.getDate(6).getTime() - edate.getTime());
  						diffDays = diff / (24 * 60 * 60 * 1000);
  					}
  					emp.setDiffdays(diffDays);
  					//System.out.println("days is "+diffDays);
  				}
  				if(repno==1 && (emp.getGross()==0 ||  emp.getGross()>21000))
  					sno--;
  				else 
  					v.add(emp);
  			}



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
  			System.out.println("-------------Exception in PayrollDAO.getEsicList " + ex);

  		}
  		finally {
  			try {
  				System.out.println("No. of Records Update/Insert : " );

  				if(rs != null){rs.close();}
  				if(ps != null){ps.close();}
  				if(con != null){con.close();}
  			} catch (SQLException e) {
  				System.out.println("-------------Exception in PayrollDAO.Connection.close --- getEsicList "+e);
  			}
  		}
  		return v;
  	}    

    public ArrayList getEsicListOld(int depo_code,int cmp_code,int fyear,int mnth_code,int repno,int opt)
  	{
  		PreparedStatement ps = null;
  		ResultSet rs=null;
  		PreparedStatement ps1 = null;
  		ResultSet rs1=null;
  		Connection con=null;
  		ArrayList v =null;
  		 
  		EmptranDto emp=null;
  		int sno=1;
  		int bkcode=0;
  		double total=0.00;
  		boolean first=false;
  		long diff=0;
  		long diffDays=0;
  		double arrearbasic=0.00;
  		double prevarrearbasic=0.00;
  		double arrearnet=0.00;
  		String mncode = String.valueOf(mnth_code);
  		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
  		Date edate=null;
  		try {
			 edate = sdf.parse((mncode.substring(0,4)+"-"+mncode.substring(5)+"-01"));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
  		String orderby="";
  		if(repno==10)
  			orderby="  order by e.bank_code,e.emp_code ";
  		else if(repno==1)
  			orderby="  order by e.esic_no ";
  		 
  		int lmnth_code=0;
  		int totalmnthdays=0;
  		try 
  		{
  			con=ConnectionFactory.getConnection();
  			con.setAutoCommit(false);

  			
  			String lastmonthQuery="SELECT MNTH_CODE,day(todate) FROM PERDMAST WHERE MNTH_CODE<? ORDER BY MNTH_CODE DESC LIMIT 1";
  			ps1=con.prepareStatement(lastmonthQuery);
  			ps1.setInt(1, mnth_code);
  			rs1=ps1.executeQuery();
  			if(rs1.next())
  			{
  				lmnth_code=rs1.getInt(1);
  				totalmnthdays=rs1.getInt(2);
  			}
  			
  			rs1.close();
  			ps1.close();
  			
  			
  			String query="select e.esic_no,e.emp_name,t.atten_days,(t.basic+t.da+t.hra+t.add_hra+t.incentive+t.spl_incentive) totalwages, 0 reason, e.doresign lwdate," +
  			"e.emp_code,e.pf_no,t.esis_value,t.employer_esis_value,t.serialno,t.pf_value,t.advance,t.loan,t.stair_days,t.stair_alw,t.stair_value," +
  			"t.extra_hrs,t.arrear_days,t.ot_rate,t.ot_value,t.employer_pf,t.eps_pf,(t.basic_value+t.da_value+t.food_value+t.incentive_value+t.medical_value) grosspf,(t.basic+t.da) salarypf," +
  			"(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incentive_value+t.spl_incen_value+t.stair_value+t.misc_value+t.ot_value+t.lta_value+t.medical_value+t.food_value) netwages," +
  			"t.absent_days,t.misc_value,  " +
  			"ifnull(e.bank,'Direct Cheque'),e.bank_add1,e.ifsc_code,ifnull(e.bank_accno,''),e.bank_code,t.lta_value,t.medical_value,t.incentive_value,t.spl_incen_value,t.coupon_amt,e.uan_no," +
  			"t.machine1_rate,t.machine2_rate,t.machine1_days,t.machine2_days,t.machine1_value,t.machine2_value,e.adhar_no,ifnull(e.pan_no,''),t.basicpf_value,0 prevdays,0 prevgrosswages,0 prevesisvalue,0 prevemployeresisvalue,0 prevarreardays,0 prevnetwages "+
  			" from emptran t,employeemast e "+
			" where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and ifnull(t.del_tag,'')<>'D' and e.depo_Code=? and e.cmp_code=? "+
			" and e.emp_code=t.emp_code "+orderby;

  			if(repno==3)
  			{
  	  			query="select e.esic_no,e.emp_name,t.atten_days,(t.basic+t.da+t.hra+t.add_hra+t.incentive+t.spl_incentive) totalwages, 0 reason, e.doresign lwdate," +
  	  	  			"e.emp_code,e.pf_no,t.esis_value,t.employer_esis_value,t.serialno,t.pf_value,t.advance," +
  	  	  			"t.loan+ifnull((select repay_amt  from loan  where fin_year=? and depo_code=? and cmp_code=? and emp_code=t.emp_code and mnth_code=? and instl_amt=0),0) loan," +
  	  	  			"t.stair_days,t.stair_alw,t.stair_value," +
  	  	  			"t.extra_hrs,t.arrear_days,t.ot_rate,t.ot_value,t.employer_pf,t.eps_pf,(t.basic_value+t.da_value+t.food_value+t.incentive_value+t.medical_value) grosspf,(t.basic+t.da) salarypf," +
  	  	  			"(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incentive_value+t.spl_incen_value+t.stair_value+t.misc_value+t.ot_value+t.lta_value+t.medical_value+t.food_value) netwages," +
  	  	  			"t.absent_days,t.misc_value,  " +
  	  	  			"ifnull(e.bank,'Direct Cheque'),e.bank_add1,e.ifsc_code,ifnull(e.bank_accno,''),e.bank_code,t.lta_value,t.medical_value,t.incentive_value,t.spl_incen_value,t.coupon_amt,e.uan_no," +
  	  	  			"t.machine1_rate,t.machine2_rate,t.machine1_days,t.machine2_days,t.machine1_value,t.machine2_value,e.adhar_no,ifnull(e.pan_no,''),t.basicpf_value,0 prevdays,0 prevgrosswages,0 prevesisvalue,0 prevemployeresisvalue,0 prevarreardays,0 prevnetwages "+
  	  	  			" from emptran t,employeemast e "+
  	  				" where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and ifnull(t.del_tag,'')<>'D' and e.depo_Code=? and e.cmp_code=? "+
  	  				" and e.emp_code=t.emp_code "+orderby;
  				
  			}
  			
  			
  			if(repno<3)
  			{
   					query="select e.esic_no,e.emp_name,sum(t.atten_days),sum(totalwages), 0 reason, e.doresign lwdate, "+ 
  							"e.emp_code,e.pf_no,sum(t.esis_value),sum(t.employer_esis_value),t.serialno,sum(t.pf_value),sum(t.advance),sum(t.loan),t.stair_days,sum(t.stair_alw),sum(t.stair_value), "+ 
  							"sum(t.extra_hrs),sum(t.arrear_days),t.ot_rate,sum(t.ot_value),sum(t.employer_pf),sum(t.eps_pf), sum(grosspf), sum(salarypf),sum(netwages), "+
  							"sum(t.absent_days),sum(t.misc_value),   "+
  							"ifnull(e.bank,'Direct Cheque'),e.bank_add1,e.ifsc_code,ifnull(e.bank_accno,''),e.bank_code,sum(t.lta_value),sum(t.medical_value),sum(t.incentive_value),sum(t.spl_incen_value),sum(t.coupon_amt),e.uan_no," +
  							"sum(t.machine1_rate),sum(t.machine2_rate),sum(t.machine1_days),sum(t.machine2_days),sum(t.machine1_value),sum(t.machine2_value),e.adhar_no,ifnull(e.pan_no,''),sum(t.basicpf_value),sum(t.prevdays),sum(t.prevgrosswages),sum(t.prevesisvalue),sum(t.prevemployeresisvalue),sum(t.prevarreardays),sum(t.prevnetwages)  from "+ 
  							"(select t.emp_code,t.atten_days,(t.basic+t.da+t.hra+t.add_hra+t.incentive+t.spl_incentive) totalwages, "+
  							"t.esis_value,t.employer_esis_value,t.serialno,t.pf_value,t.advance,t.loan,t.stair_days,t.stair_alw,t.stair_value, "+ 
  							"t.extra_hrs,t.arrear_days,t.ot_rate,t.ot_value,t.employer_pf,t.eps_pf,(t.basic_value+t.da_value+t.food_value+t.incentive_value+t.medical_value) grosspf,(t.basic+t.da+t.incentive+t.medical_value+t.food_alw) salarypf, "+ // added +t.incentive+t.medical_value+t.food_alw in salarypf on 19/12/2024
  							"(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incentive_value+t.spl_incen_value+t.stair_value+t.misc_value+t.ot_value+t.lta_value+t.medical_value+t.machine1_value+t.machine2_value+t.food_value) netwages, "+ 
  							"t.absent_days,t.misc_value,t.lta_value,t.medical_value,t.incentive_value,t.spl_incen_value,t.coupon_amt, "+
  							"t.machine1_rate,t.machine2_rate,t.machine1_days,t.machine2_days,t.machine1_value,t.machine2_value,t.basicpf_value,0 prevdays,0 prevgrosswages,0 prevesisvalue,0 prevemployeresisvalue,0 prevarreardays,0 prevnetwages "+
  							"from emptran t where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=?  and ifnull(t.del_tag,'')<>'D' "+ 
  							"union all "+
  							"select t.emp_code,0 atten_days,0 totalwages, "+
  							"0 esic_value,0 employer_esis_value,0 serialno,0 pf_value,0 advance,0 loan,0 stair_days,0 stair_alw,0 stair_value, "+ 
  							"0 extra_hrs,0 arrear_days,0 ot_rate,0 ot_value,0 employer_pf,0 eps_pf,0 grosspf,0  salarypf,  "+
  							"0 netwages, "+
  							"0 absent_days,0 misc_value,0 lta_value,0 medical_value,0 incen_value,0 spl_incen_value,0 coupon_amt, "+
  							"0 machine1_rate,0 machine2_rate,0 machine1_days,0 machine2_days,0 machine1_value,0 machine2_value,0 basicpf_value,t.atten_days prevdays,(t.basic+t.da+t.hra+t.add_hra+t.incentive+t.spl_incentive) prevgrosswages,t.esis_value prevesisvalue,t.employer_esis_value prevemployeresisvalue,t.arrear_days prevarreardays, "+
  							"(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incentive_value+t.spl_incen_value+t.stair_value+t.misc_value+t.ot_value+t.lta_value+t.medical_value+t.machine1_value+t.machine2_value+t.food_value) prevnetwages "+ 
  							"from emptran t where t.fin_year<=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and ifnull(t.del_tag,'')<>'D' group by t.emp_code "+ 
  							"union all "+
  							"select t.emp_code,0 atten_days,0 totalwages, "+
  							"sum(t.esic_value) esic_value,sum(t.employer_esic_value)  employer_esis_value,0 serialno,sum(t.pf_value) pf_value,0 advance,0 loan,0 stair_days,0 stair_alw,0 stair_value, "+ 
  							"0 extra_hrs,0 arrear_days,0 ot_rate,0 ot_value,0 employer_pf,0 eps_pf,sum(t.basic_value+t.da_value+t.incen_value) grosspf,0  salarypf,  "+
  							"sum(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incen_value+t.spl_incen_value) netwages, "+
  							"0 absent_days,0 misc_value,0 lta_value,0 medical_value,0 incen_value,0 spl_incen_value,0 coupon_amt, "+
  							"0 machine1_rate,0 machine2_rate,0 machine1_days,0 machine2_days,0 machine1_value,0 machine2_value,0 basicpf_value,0 prevdays,0 prevgrosswages,0 prevesisvalue,0 prevemployeresisvalue,0 prevarreardays,0 prevnetwages "+
  							"from arrear t where t.fin_year<=? and t.depo_code=? and t.cmp_code=? and t.arrear_mon=? and t.arrear_paid='Y' group by t.emp_code "+ 
  							"union all "+
  							"select t.emp_code,0 atten_days,0 totalwages, "+
  							"sum(t.esic_value) esic_value,sum(t.employer_esic_value) employer_esis_value,0 serialno,sum(t.pf_value) pf_value,0 advance,0 loan,0 stair_days,0 stair_alw,0 stair_value, "+ 
  							"0 extra_hrs,0 arrear_days,0 ot_rate,0 ot_value,0 employer_pf,0 eps_pf,sum(t.basic_value+t.da_value+t.incen_value) grosspf,sum(t.basic_value)  salarypf,  "+
  							"sum(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incen_value+t.spl_incen_value) netwages, "+
  							"0 absent_days,0 misc_value,0 lta_value,0 medical_value,0 incen_value,0 spl_incen_value,0 coupon_amt, "+
  							"0 machine1_rate,0 machine2_rate,0 machine1_days,0 machine2_days,0 machine1_value,0 machine2_value,0 basicpf_value,0 prevdays,0 prevgrosswages,0 prevesisvalue,0 prevemployeresisvalue,0 prevarreardays,0 prevnetwages "+
  							"from arrear t where t.fin_year<=? and t.depo_code=? and t.cmp_code=? and t.arrear_mon=? and t.arrear_paid='O' group by t.emp_code) t,employeemast e "+ 
  							"where  e.depo_Code=? and e.cmp_code=?  and e.emp_code=t.emp_code group by t.emp_code "+orderby; 
  				System.out.println(query);	
  				
  			} 
  			ps = con.prepareStatement(query); 
  			if(repno<3)
  			{
	  	  		    ps.setInt(1, fyear);
		  			ps.setInt(2, depo_code);
		  			ps.setInt(3, cmp_code);
		  			ps.setInt(4, mnth_code);
		  			ps.setInt(5, fyear);
		  			ps.setInt(6, depo_code);
		  			ps.setInt(7, cmp_code);
		  			ps.setInt(8, lmnth_code);
		  			ps.setInt(9, fyear);
		  			ps.setInt(10, depo_code);
		  			ps.setInt(11, cmp_code); 
		  			ps.setInt(12, mnth_code);
	  	  		    ps.setInt(13, fyear);
		  			ps.setInt(14, depo_code);
		  			ps.setInt(15, cmp_code);
		  			ps.setInt(16, mnth_code);
		  			ps.setInt(17, depo_code);
		  			ps.setInt(18, cmp_code);
  				

  			}
  			else if(repno==3)
  			{
  				ps.setInt(1, fyear); 
  				ps.setInt(2, depo_code);
  				ps.setInt(3, cmp_code);
  				ps.setInt(4, mnth_code);
  				ps.setInt(5, fyear); 
  				ps.setInt(6, depo_code);
  				ps.setInt(7, cmp_code);
  				ps.setInt(8, mnth_code);
  				ps.setInt(9, depo_code);
  				ps.setInt(10, cmp_code);
  			}
  			else
  			{
  				ps.setInt(1, fyear); 
  				ps.setInt(2, depo_code);
  				ps.setInt(3, cmp_code);
  				ps.setInt(4, mnth_code);
  				ps.setInt(5, depo_code);
  				ps.setInt(6, cmp_code);
  			}
  			rs =ps.executeQuery();
  			
  			v = new ArrayList();
  			while (rs.next())
  			{
  				if(repno==2)
  				{
  					
  					if(mnth_code>202410) // added on 16/12/2024 by y	ashpal
  						arrearbasic = roundTwoDecimals(((rs.getDouble(25)-rs.getDouble(35))/30)*rs.getDouble(19)); //uncomment on 14/12/2024
  					else
  						arrearbasic = roundTwoDecimals((rs.getDouble(48)/30)*rs.getDouble(19)); // salary_pfvalue 11/05/2024
  					
  					if(mnth_code>=202503)
  							arrearbasic = roundTwoDecimals((rs.getDouble(48)/30)*rs.getDouble(19));
  					if(mnth_code>=202505 && rs.getInt(27)> 0)
  						arrearbasic = roundTwoDecimals((rs.getDouble(25)/30)*rs.getDouble(19));
  					
  					arrearnet = roundTwoDecimals((rs.getDouble(4)/30)*rs.getDouble(19));
  				
  				if(rs.getInt(7)==10  )
  				{
  					System.out.println("26 "+rs.getDouble(26)+" 35 "+rs.getDouble(35)+" 19 "+rs.getDouble(19)+" 48 "+rs.getDouble(48)+" arrearnet "+arrearnet);
  					System.out.println("EMP_CODE &&&&&"+rs.getInt(7)+" arrearbasic "+arrearbasic+" 25 "+rs.getDouble(25)+" arrearDays ->19 "+rs.getDouble(19)); 
  					System.out.println("EMP_CODE LINE 913 CAL "+rs.getInt(7)+" "+rs.getDouble(48)+"  "+rs.getDouble(19)+" "+arrearbasic);
  				}
  				}
  				else if(repno==1)
  				{
  					arrearbasic = roundTwoDecimals((rs.getDouble(4)/30)*rs.getDouble(19));
  					prevarrearbasic = roundTwoDecimals((rs.getDouble(50)/30)*rs.getDouble(53));
  					arrearnet = roundTwoDecimals((rs.getDouble(4)/30)*rs.getDouble(19));
  				}
  				
  				emp = new EmptranDto();
  				emp.setEsic_no(rs.getLong(1));  // ip number
  				emp.setEmp_name(rs.getString(2)); // ip name
  				//emp.setAtten_days((int) (rs.getDouble(3)+.50)); // paid days
  				emp.setAtten_days(rs.getDouble(3)); // paid days
  				emp.setGross(rs.getDouble(4)); // total wages
  				emp.setCreated_by(rs.getInt(5)); // reason
  				emp.setCreated_date(rs.getDate(6)); // last working date
  				emp.setEmp_code(rs.getInt(7));
  				emp.setPf_no(rs.getInt(8));
  				emp.setEsis_value(rs.getDouble(9));
  				emp.setEmployer_esis_value(rs.getDouble(10));
  				emp.setArrear_days(rs.getDouble(19));

  				emp.setArrear_basic_value(arrearbasic);
  				
  				if(repno==1)
  				{
  					if(mnth_code>201906)
  					{
  						if(opt==2) // ARREAR OPTION
  						{
  							emp.setEsis_value(roundTwoDecimals(arrearbasic*0.75/100));
  							emp.setEmployer_esis_value(roundTwoDecimals(arrearbasic*3.25/100));
  							
  							// below calculation is added on 17/08/2024
  							emp.setEsis_value(rs.getDouble(51)-roundTwoDecimals(prevarrearbasic*0.75/100)+roundTwoDecimals(arrearbasic*0.75/100));
  							emp.setEmployer_esis_value(rs.getDouble(52)-roundTwoDecimals(prevarrearbasic*3.25/100)+roundTwoDecimals(arrearbasic*3.25/100));

  			  				
  			  				emp.setArrear_basic_value(arrearbasic+rs.getDouble(54)-prevarrearbasic);
  			  				

  			  			    //emp.setArrear_days(rs.getDouble(49)+rs.getDouble(19)); // paid days + prev atten days
  							
  						}
  						else if(opt==1)  // ESIC OPTION add if condition in else on 17/08/2024 (YashPal)
  						{
  							emp.setEsis_value(emp.getEsis_value()-roundTwoDecimals(arrearbasic*0.75/100));
  							emp.setEmployer_esis_value(emp.getEmployer_esis_value()-roundTwoDecimals(arrearbasic*3.25/100));
// 							emp.setEmployer_esis_value(emp.getEmployer_esis_value()-roundTwoDecimals(arrearbasic*3.25/100,0));
  							System.out.println("emp name "+rs.getString(2)+" arrearebasic "+arrearbasic+" "+emp.getEmployer_esis_value());
 							

  						}
 
  					}
  					else
  					{
  						emp.setEsis_value(roundTwoDecimals(arrearbasic*1.75/100));
  						emp.setEmployer_esis_value(roundTwoDecimals(arrearbasic*4.75/100));
  					}
  				}
  				
  				if (emp.getArrear_days()>0)
  				 System.out.println("ARREAR DAYS "+emp.getArrear_days()+ " emp COde "+emp.getEmp_code());
  				emp.setSerialno(sno++);
  				emp.setPf_value(rs.getDouble(12));
  				emp.setAdvance(rs.getDouble(13));
  				emp.setLoan(rs.getDouble(14));
  				emp.setStair_days(rs.getInt(15));
  				emp.setStair_alw(rs.getDouble(16));
  				emp.setStair_value(rs.getDouble(17));
  				emp.setExtra_hrs(rs.getDouble(18));
//  				emp.setArrear_days(rs.getDouble(19));
  				emp.setOt_rate(rs.getDouble(20));
  				emp.setOt_value(rs.getDouble(21));
  				 
  				emp.setEmployee_pf(rs.getDouble(12));
  				emp.setEmployer_pf(rs.getDouble(22));
  				emp.setEps_pf(rs.getDouble(23));
  				
//  				emp.setBasic_value(rs.getDouble(24)); //TEMPORARY COMMENT ON 14/12/2024
   				if(repno==2 && mnth_code>202410) // PF/ Arrear List we use field 48 i.e basicPF_value 16/12/2024
  					emp.setBasic_value(rs.getDouble(48)); 
  				else
  					emp.setBasic_value(rs.getDouble(24)); 

  				
   				if(repno==2 && mnth_code>=202503)
  					emp.setBasic_value(rs.getDouble(24)+arrearbasic); // CURRENT CORRECTION FOR JUNE 2025 ONWARD
   				
					if(emp.getEmp_code()==977)
  						System.out.println("24 ki value "+rs.getDouble(24)+" 48 ki value "+rs.getDouble(48)+" 25 ki value "+rs.getDouble(25));

  				
  				if(repno==2 && mnth_code==202502 && (emp.getEmp_code()==15 || emp.getEmp_code()==139 || emp.getEmp_code()==403 || emp.getEmp_code()==849 || emp.getEmp_code()==171)) // PF/ Arrear List we use field 48 i.e basicPF_value 27/02/2025
  					System.out.println("do nothing"); 
  				else if(repno==2 && mnth_code==202502)
  				{
  					emp.setBasic_value(rs.getDouble(24));
  				}
  				emp.setBasic(rs.getDouble(25));
  				emp.setNet_value(rs.getDouble(26)+arrearnet);
  				emp.setAbsent_days(rs.getDouble(27));
  				emp.setMisc_value(rs.getDouble(28));
  				
  				//setting bank detials
  				emp.setBank(rs.getString(29).length()>1?rs.getString(29):"Direct Cheque");
  				emp.setBank_add1(rs.getString(30));
  				emp.setIfsc_code(rs.getString(31));
  				emp.setBank_accno(rs.getString(32));
  				emp.setBank_code(rs.getInt(33));
  				emp.setLta_value(rs.getDouble(34));
  				emp.setMedical_value(rs.getDouble(35));
  				emp.setIncentive_value(rs.getDouble(36));
  				emp.setSpl_incen_value(rs.getDouble(37));
  				emp.setCoupon_amt(rs.getDouble(38));
  				emp.setUan_no(rs.getLong(39));
  				emp.setMachine1_rate(rs.getDouble(40));
  				emp.setMachine2_rate(rs.getDouble(41));
  				emp.setMachine1_days(rs.getDouble(42));
  				emp.setMachine2_days(rs.getDouble(43));
  				emp.setMachine1_value(rs.getDouble(44));
  				emp.setMachine2_value(rs.getDouble(45));
  				//emp.setArrear_basic_value(arrearbasic);
  				emp.setArrear_net_value(arrearnet);
  				emp.setAdhar_no(rs.getLong(46));  
  				emp.setPan_no(rs.getString(47));
  				emp.setDiffdays(0);
  				emp.setMnth_code(mnth_code);
  				emp.setBasicpf_value(rs.getDouble(48));
  				
  				//System.out.println(rs.getDouble(48));
  				emp.setPrev_days(rs.getDouble(49));
  				emp.setNcp_days(totalmnthdays-(rs.getDouble(19)+rs.getDouble(49)));
  				
  				//System.out.println("basic VALUE AND NET VALUE  24 "+rs.getDouble(24)+" 26 "+rs.getDouble(26)+" BASICPF "+rs.getDouble(48));
  				
  				if(rs.getDate(6)!=null)
  				{
  					if(rs.getDate(6).after(edate))
  					{
  						diffDays=0;
  						emp.setCreated_date(null);
  					}
  					else
  					{
  						diff = (rs.getDate(6).getTime() - edate.getTime());
  						diffDays = diff / (24 * 60 * 60 * 1000);
  					}
  					emp.setDiffdays(diffDays);
  					//System.out.println("days is "+diffDays);
  				}
  				if(repno==1 && (emp.getGross()==0 ||  emp.getGross()>21000))
  					sno--;
  				else 
  					v.add(emp);
  			}



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
  			System.out.println("-------------Exception in PayrollDAO.getEsicList " + ex);

  		}
  		finally {
  			try {
  				System.out.println("No. of Records Update/Insert : " );

  				if(rs != null){rs.close();}
  				if(ps != null){ps.close();}
  				if(con != null){con.close();}
  			} catch (SQLException e) {
  				System.out.println("-------------Exception in PayrollDAO.Connection.close --- getEsicList "+e);
  			}
  		}
  		return v;
  	}    

    
	private double roundTwoDecimals(double d, int i) {
		// TODO Auto-generated method stub
		return 0;
	}





	public Vector getSterliteDaysList(int depo_code,int cmp_code,int fyear,int mnth_code,int option)
	{
		PreparedStatement ps = null;
		ResultSet rs=null;
		PreparedStatement ps1 = null;
		ResultSet rs1=null;
		Connection con=null;
		Vector v =null;
		Vector col=null;
		int sno=1;
		int lock=0;
		try 
		{
			con=ConnectionFactory.getConnection();
			con.setAutoCommit(false);

			String query="select e.emp_name,e.esic_no,e.pf_no,e.emp_code,e.stair_alw,ifnull(t.stair_days,0) ster,t.advance,t.loan,t.misc_value,t.coupon_amt," +
			"e.machine1_rate,e.machine2_rate,t.machine1_days,t.machine2_days,t.prof_tax " +
			" from employeemast e, emptran t  "+
			" where  e.cmp_code=t.cmp_code and e.emp_code=t.emp_code and t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? " +
			" and ifnull(t.del_tag,'')<>'D' and e.depo_code=? and e.cmp_code=? and t.atten_days>0 and t.atten_lock=1 ";

			String query1="select locked from locktable  where depo_Code=? and cmp_code=? and mnth_code=? and entry_type=? ";
			ps1 = con.prepareStatement(query1);
			ps1.setInt(1, depo_code);
			ps1.setInt(2, cmp_code);
			ps1.setInt(3, mnth_code);
			ps1.setInt(4, option);
			rs1 =ps1.executeQuery();
			if(rs1.next())
			{
				lock=rs1.getInt(1);
			}
			rs1.close();
			ps1.close();
			
			ps = con.prepareStatement(query);
			ps.setInt(1, fyear);
			ps.setInt(2, depo_code);
			ps.setInt(3, cmp_code);
			ps.setInt(4, mnth_code);
			ps.setInt(5, depo_code);
			ps.setInt(6, cmp_code);
			rs =ps.executeQuery();
			
			v = new Vector();
			while (rs.next())
			{
				col= new Vector();
				col.add(sno++);  // sno 0
				col.add(rs.getString(1)); // emp_name //1
				col.add(rs.getString(2)); // esic no  2
				col.add(rs.getString(3)); // pf no  3
				col.add(rs.getInt(4)); // emp_code  4
				if(option==1)
				{
					col.add(rs.getDouble(5)); // Sterlite allowance  5
					col.add(rs.getInt(6)); // Sterlite days  6
					col.add(lock); // record lock (if 1=yes 0=no)  field   7
				}
				else if(option==2)
				{
					col.add(rs.getDouble(7)); // Advance  5
					col.add(rs.getDouble(8)); // Loan  6
					col.add(lock); // record lock (if 1=yes 0=no)  field   7
				}
				else if(option==3) // misc Entry 
				{
					col.add(rs.getDouble(9)); // Misc Value  5
					col.add(0.00); // exra faltu field   6
					col.add(lock); // record lock (if 1=yes 0=no)  field   7
				}
				else if(option==4) // Canteen Coupon Entry 
				{
					col.add(rs.getDouble(10)); // Coupon amt  5
					col.add(0.00); // exra faltu field   6
					col.add(lock); // record lock (if 1=yes 0=no)  field   7
				}
				else if(option==7) // Professional Taxn Entry 
				{
					col.add(rs.getDouble(15)); // ptax amt  5
					col.add(0.00); // exra faltu field   6
					col.add(lock); // record lock (if 1=yes 0=no)  field   7
				}
				else if(option==6) // Machine Operator Entry 
				{
					col.add(rs.getDouble(13)); // Machine1_days 5
					col.add(rs.getDouble(14)); // Machine2 days 6
					col.add(lock); // record lock (if 1=yes 0=no)  field   7
					col.add(rs.getDouble(11)); // Machine1_rate hidden 8
					col.add(rs.getDouble(12)); // Machine2 rate hidden 9
				}
	
				//yah line if condition mein likh di hai 11/08/2017
//				col.add(lock); // record lock (if 1=yes 0=no)  field   7
				v.add(col);
			}



			con.commit();
			con.setAutoCommit(true);
			rs.close();
			ps.close();

		} catch (Exception ex) { ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in PayrollDAO.getSterliteDaysList " + ex);

		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : " );

				if(rs != null){rs.close();}
				if(ps != null){ps.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in PayrollDAO.Connection.close --- getSterliteDaysList "+e);
			}
		}
		return v;
	}
	

    public int updateSterliteList(ArrayList<?> attnlist)
    {
  	  
    	PreparedStatement ps1 = null;
    	Connection con=null;
    	EmptranDto emp=null;
		double sterilevalue=0.00;
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();

			
			String query1="update emptran set stair_days=?,stair_value=?,modified_by=?,modified_date=? " +
			" where  fin_year=? and depo_code=? and cmp_code=? and  mnth_code=? and emp_code=? " ;


			
			con.setAutoCommit(false);
			
			ps1 = con.prepareStatement(query1);
			
			int s=attnlist.size();
			for (int j=0;j<s;j++)
			{
				emp= (EmptranDto) attnlist.get(j);
				 
				sterilevalue=roundTwoDecimals(emp.getStair_days()*emp.getStair_alw());
				ps1.setInt(1, emp.getStair_days());
				ps1.setDouble(2, sterilevalue); // calculate value 
				ps1.setInt(3,emp.getModified_by());
				ps1.setDate(4,setSqlDate(emp.getModified_date()));
				// where clause
				ps1.setInt(5,emp.getFin_year());
				ps1.setInt(6,emp.getDepo_code());
				ps1.setInt(7,emp.getCmp_code());
				ps1.setInt(8,emp.getMnth_code());
				ps1.setInt(9,emp.getEmp_code());
				i=ps1.executeUpdate();
		  			
			}
			
			
			con.commit();
			con.setAutoCommit(true);
			ps1.close();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in PayrollDAO.updateSterliteList " + ex);
			i=-1;
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : "+i);

				if(ps1 != null){ps1.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in PayrollDAO.Connection.close "+e);
			}
		}
		return i;
	}
	
    
    public int updateAdvanceList(ArrayList<?> attnlist)
    {
  	  
    	PreparedStatement ps1 = null;
    	PreparedStatement ps2 = null;
    	Connection con=null;
    	EmptranDto emp=null;
		
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();

			
			String query1="update emptran set advance=?,loan=?,modified_by=?,modified_date=? " +
			" where  fin_year=? and depo_code=? and cmp_code=? and  mnth_code=? and emp_code=? " ;

			String query2="update loan set repay_amt=? " +
			" where  fin_year=? and depo_code=? and cmp_code=? and  mnth_code=? and emp_code=? " ;

		
			con.setAutoCommit(false);
			
			ps1 = con.prepareStatement(query1);
			ps2 = con.prepareStatement(query2);
			
			int s=attnlist.size();
			for (int j=0;j<s;j++)
			{
				emp= (EmptranDto) attnlist.get(j);

				// updated loan table for repay_amt
				ps2.setDouble(1, emp.getLoan());  
				// where clause
				ps2.setInt(2,emp.getFin_year());
				ps2.setInt(3,emp.getDepo_code());
				ps2.setInt(4,emp.getCmp_code());
				ps2.setInt(5,emp.getMnth_code());
				ps2.setInt(6,emp.getEmp_code());
				i=ps2.executeUpdate();

				
				// update emptran for advance and loan 
				ps1.setDouble(1, emp.getAdvance());
				ps1.setDouble(2, emp.getLoan());  
				ps1.setInt(3,emp.getModified_by());
				ps1.setDate(4,setSqlDate(emp.getModified_date()));
				// where clause
				ps1.setInt(5,emp.getFin_year());
				ps1.setInt(6,emp.getDepo_code());
				ps1.setInt(7,emp.getCmp_code());
				ps1.setInt(8,emp.getMnth_code());
				ps1.setInt(9,emp.getEmp_code());
				i=ps1.executeUpdate();
				

		  			
			}
			con.commit();
			con.setAutoCommit(true);
			ps1.close();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in PayrollDAO.updateSterliteList " + ex);
			i=-1;
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : "+i);

				if(ps1 != null){ps1.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in PayrollDAO.Connection.close "+e);
			}
		}
		return i;
	}
	

    public int updateMiscList(ArrayList<?> attnlist)
    {
  	  
    	PreparedStatement ps1 = null;
    	Connection con=null;
    	EmptranDto emp=null;
		
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();

			
			String query1="update emptran set misc_value=?,modified_by=?,modified_date=? " +
			" where  fin_year=? and depo_code=? and cmp_code=? and  mnth_code=? and emp_code=? " ;

		
			con.setAutoCommit(false);
			
			ps1 = con.prepareStatement(query1);
			
			int s=attnlist.size();
			for (int j=0;j<s;j++)
			{
				emp= (EmptranDto) attnlist.get(j);
				 
				ps1.setDouble(1, emp.getMisc_value());
				ps1.setInt(2,emp.getModified_by());
				ps1.setDate(3,setSqlDate(emp.getModified_date()));
				// where clause
				ps1.setInt(4,emp.getFin_year());
				ps1.setInt(5,emp.getDepo_code());
				ps1.setInt(6,emp.getCmp_code());
				ps1.setInt(7,emp.getMnth_code());
				ps1.setInt(8,emp.getEmp_code());
				i=ps1.executeUpdate();
		  			
			}
			con.commit();
			con.setAutoCommit(true);
			ps1.close();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in PayrollDAO.updateMiscList " + ex);
			i=-1;
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : "+i);

				if(ps1 != null){ps1.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in PayrollDAO.Connection.close "+e);
			}
		}
		return i;
	}
	
    
    public int updateCouponList(ArrayList<?> attnlist)
    {
  	  
    	PreparedStatement ps1 = null;
    	Connection con=null;
    	EmptranDto emp=null;
		
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();

			
			String query1="update emptran set coupon_amt=?,modified_by=?,modified_date=? " +
			" where  fin_year=? and depo_code=? and cmp_code=? and  mnth_code=? and emp_code=? " ;

		
			con.setAutoCommit(false);
			
			ps1 = con.prepareStatement(query1);
			
			int s=attnlist.size();
			for (int j=0;j<s;j++)
			{
				emp= (EmptranDto) attnlist.get(j);
				 
				ps1.setDouble(1, emp.getCoupon_amt());
				ps1.setInt(2,emp.getModified_by());
				ps1.setDate(3,setSqlDate(emp.getModified_date()));
				// where clause
				ps1.setInt(4,emp.getFin_year());
				ps1.setInt(5,emp.getDepo_code());
				ps1.setInt(6,emp.getCmp_code());
				ps1.setInt(7,emp.getMnth_code());
				ps1.setInt(8,emp.getEmp_code());
				i=ps1.executeUpdate();
		  			
			}
			con.commit();
			con.setAutoCommit(true);
			ps1.close();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in PayrollDAO.updateCouponList " + ex);
			i=-1;
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : "+i);

				if(ps1 != null){ps1.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in PayrollDAO.Connection.close "+e);
			}
		}
		return i;
	}


    public int updateProfTaxList(ArrayList<?> attnlist)
    {
  	  
    	PreparedStatement ps1 = null;
    	Connection con=null;
    	EmptranDto emp=null;
		
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();

			
			String query1="update emptran set prof_tax=?,modified_by=?,modified_date=? " +
			" where  fin_year=? and depo_code=? and cmp_code=? and  mnth_code=? and emp_code=? " ;

		
			con.setAutoCommit(false);
			
			ps1 = con.prepareStatement(query1);
			
			int s=attnlist.size();
			for (int j=0;j<s;j++)
			{
				emp= (EmptranDto) attnlist.get(j);
				 
				ps1.setDouble(1, emp.getProf_tax());
				ps1.setInt(2,emp.getModified_by());
				ps1.setDate(3,setSqlDate(emp.getModified_date()));
				// where clause
				ps1.setInt(4,emp.getFin_year());
				ps1.setInt(5,emp.getDepo_code());
				ps1.setInt(6,emp.getCmp_code());
				ps1.setInt(7,emp.getMnth_code());
				ps1.setInt(8,emp.getEmp_code());
				i=ps1.executeUpdate();
		  			
			}
			con.commit();
			con.setAutoCommit(true);
			ps1.close();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in PayrollDAO.updateProfTaxList " + ex);
			i=-1;
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : "+i);

				if(ps1 != null){ps1.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in PayrollDAO.Connection.close "+e);
			}
		}
		return i;
	}

    
    public int updateMachineList(ArrayList<?> attnlist)
    {
  	  
    	PreparedStatement ps1 = null;
    	Connection con=null;
    	EmptranDto emp=null;
		double machine1value=0.00;
		double machine2value=0.00;
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();

			
			String query1="update emptran set machine1_days=?,machine2_days=?,machine1_value=?,machine2_value=?,modified_by=?,modified_date=?,machine1_rate=?,machine2_rate=? " +
			" where  fin_year=? and depo_code=? and cmp_code=? and  mnth_code=? and emp_code=? " ;


			
			con.setAutoCommit(false);
			
			ps1 = con.prepareStatement(query1);
			
			int s=attnlist.size();
			for (int j=0;j<s;j++)
			{
				emp= (EmptranDto) attnlist.get(j);
				 
				machine1value=roundTwoDecimals(emp.getMachine1_days()*emp.getMachine1_rate());
				machine2value=roundTwoDecimals(emp.getMachine2_days()*emp.getMachine2_rate());
				ps1.setDouble(1, emp.getMachine1_days());
				ps1.setDouble(2, emp.getMachine2_days());
				ps1.setDouble(3, machine1value); // calculate value 
				ps1.setDouble(4, machine2value); // calculate value 
				ps1.setInt(5,emp.getModified_by());
				ps1.setDate(6,setSqlDate(emp.getModified_date()));
				ps1.setDouble(7, emp.getMachine1_rate()); // machine1 rate 
				ps1.setDouble(8, emp.getMachine2_rate()); // machine2 rate 

				// where clause
				ps1.setInt(9,emp.getFin_year());
				ps1.setInt(10,emp.getDepo_code());
				ps1.setInt(11,emp.getCmp_code());
				ps1.setInt(12,emp.getMnth_code());
				ps1.setInt(13,emp.getEmp_code());
				i=ps1.executeUpdate();
		  			
			}
			
			
			con.commit();
			con.setAutoCommit(true);
			ps1.close();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in PayrollDAO.updateSterliteList " + ex);
			i=-1;
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : "+i);

				if(ps1 != null){ps1.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in PayrollDAO.Connection.close "+e);
			}
		}
		return i;
	}

    
    public ArrayList<EmptranDto> getSalaryRegister(int depo_code,int cmp_code,int fyear,int mnth_code,int repno)
 	{
 		PreparedStatement ps = null;
 		ResultSet rs=null;
 		Connection con=null;
 		ArrayList<EmptranDto>  v =null;
 		 
 		EmptranDto emp=null;
 		int sno=1;
		double basic,da,hra,add_hra,incentive,spl_incentive,ot,lta,medical,pf,advance,loan,esis,misc,stair_value,coupon_amt,machine1_value,machine2_value;
		double atten_days,absent_days,arrear_days,extra_hrs,machine1_days,machine2_days,food_value,prof_tax,pfamt;
 		String orderby="";
 		String query=null;
  		if(repno==10)
  			orderby="  order by e.bank_code,e.emp_code ";
  			 
  		   
 		try 
 		{
 			con=ConnectionFactory.getConnection();
 			con.setAutoCommit(false);

 			
 			/// old query for salary Register 	
/* 			query="select e.emp_code,e.emp_name,ifnull(e.designation,'Worker'),e.pf_no,e.esic_no,t.basic,t.da,t.hra,t.add_hra,t.incentive,t.spl_incentive,t.ot_rate,t.lta,t.medical,t.stair_alw, "+
			"t.basic_value,t.da_value,t.hra_value,t.add_hra_value,t.incentive_value,t.spl_incen_value,t.ot_value,t.lta_value, "+
			"t.medical_value,t.pf_value,t.advance,t.loan,t.esis_value,t.atten_days,t.arrear_days,t.absent_days,t.stair_days,t.extra_hrs,t.misc_value,t.stair_value," +
			"ifnull(e.bank,'Direct Cheque'),e.bank_add1,e.ifsc_code,ifnull(e.bank_accno,''),e.bank_code,t.coupon_amt,e.uan_no from emptran t,employeemast e "+
			"where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and t.atten_days<>0 and ifnull(t.del_tag,'')<>'D' "+
			"and e.depo_code=? and e.cmp_code=? and e.emp_code=t.emp_code and ifnull(e.del_tag,'')<>'D' "+orderby;
*/ 			
 			query="select e.emp_code,e.emp_name,ifnull(e.designation,'Worker'),e.pf_no,e.esic_no,sum(t.basic),sum(t.da),sum(t.hra),sum(t.add_hra),sum(t.incentive),sum(t.spl_incentive),sum(t.ot_rate),sum(t.lta),sum(t.medical),sum(t.stair_alw), "+ 
 			"sum(t.basic_value),sum(t.da_value),sum(t.hra_value),sum(t.add_hra_value),sum(t.incentive_value),sum(t.spl_incen_value),sum(t.ot_value),sum(t.lta_value), "+
 			"sum(t.medical_value),sum(t.pf_value),sum(t.advance),sum(t.loan),sum(t.esis_value),sum(t.atten_days),sum(t.arrear_days),sum(t.absent_days),sum(t.stair_days),sum(t.extra_hrs),sum(t.misc_value),sum(t.stair_value),"+ 
 			"ifnull(e.bank,'Direct Cheque'),e.bank_add1,e.ifsc_code,ifnull(e.bank_accno,''),e.bank_code,t.coupon_amt,e.uan_no," +
 			"t.machine1_value,t.machine2_value,t.machine1_days,t.machine2_days,sum(t.food_value),sum(t.prof_tax),sum(t.pfamt) from "+
 			"(select t.emp_code,t.basic,t.da,t.hra,t.add_hra,t.incentive,t.spl_incentive,t.ot_rate,t.lta,t.medical,t.stair_alw, "+
 			"t.basic_value,t.da_value,t.hra_value,t.add_hra_value,t.incentive_value,t.spl_incen_value,t.ot_value,t.lta_value, "+
 			"t.medical_value,t.pf_value,t.advance,t.loan,t.esis_value,t.atten_days,t.arrear_days,t.absent_days,t.stair_days,t.extra_hrs,t.misc_value,t.stair_value, "+ 
 			"t.coupon_amt,t.machine1_value,t.machine2_value,t.machine1_days,t.machine2_days,t.food_value,t.prof_tax,t.pf_value pfamt from emptran t "+
 			"where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and t.atten_days<>0 and ifnull(t.del_tag,'')<>'D' "+  
 			"union all "+
 			"select a.emp_code,0 basic,0 da,0 hra,0 add_hra,0 incentive,0 spl_incentive,0 ot_rate,0 lta,0 medical,0 stair_alw,"+ 
 			"sum(basic_value),sum(da_value),sum(hra_value),sum(add_hra_value),sum(incen_value),sum(spl_incen_value),0 ot_value,0 lta_value,"+ 
 			"0 medical_value,sum(pf_value),0 advance,0 loan,sum(esic_value),0 atten_days,0 arrear_days,0 absent_days,0 stair_days,0 extra_hrs,0 misc_value,0 stair_value,"+ 
 			"0 coupon_amt,0 machine1_value,0 machine2_value,0 machine1_days,0 machine2_days,0 food_value,sum(prof_tax),0 pfamt from arrear a "+
 			"where fin_year<=? and depo_code=? and cmp_code=? and arrear_mon=? and arrear_paid='Y' group by emp_code "+  
 			"union all "+
 			"select a.emp_code,0 basic,0 da,0 hra,0 add_hra,0 incentive,0 spl_incentive,0 ot_rate,0 lta,0 medical,0 stair_alw,"+ 
 			"sum(basic_value),sum(da_value),sum(hra_value),sum(add_hra_value),sum(incen_value),sum(spl_incen_value),0 ot_value,0 lta_value,"+ 
 			"0 medical_value,sum(pf_value),0 advance,0 loan,sum(esic_value),0 atten_days,0 arrear_days,0 absent_days,0 stair_days,0 extra_hrs,0 misc_value,0 stair_value,"+ 
 			"0 coupon_amt,0 machine1_value,0 machine2_value,0 machine1_days,0 machine2_days,0 food_value,0 prof_tax,0 pfamt from arrear a "+
 			"where fin_year<=? and depo_code=? and cmp_code=? and arrear_mon=? and arrear_paid='O' group by emp_code) t,employeemast e "+  
 			"where  e.depo_code=? and e.cmp_code=? and e.emp_code=t.emp_code and ifnull(e.del_tag,'')<>'D'  group by t.emp_code "+orderby;

 			
 		 
 			System.out.println(" yeh salery register");

 			ps = con.prepareStatement(query);
 			ps.setInt(1, fyear);
 			ps.setInt(2, depo_code);
 			ps.setInt(3, cmp_code);
 			ps.setInt(4, mnth_code);
 			ps.setInt(5, fyear);
 			ps.setInt(6, depo_code);
 			ps.setInt(7, cmp_code);
 			ps.setInt(8, mnth_code);
 			ps.setInt(9, fyear);
 			ps.setInt(10, depo_code);
 			ps.setInt(11, cmp_code);
 			ps.setInt(12, mnth_code);
 			ps.setInt(13, depo_code);
 			ps.setInt(14, cmp_code);
 			rs =ps.executeQuery();
 			
 			v = new ArrayList<EmptranDto> ();
 			basic=0.00;
 			da=0.00;
 			hra=0.00;
 			add_hra=0.00;
 			incentive=0.00;
 			spl_incentive=0.00;
 			ot=0.00;
 			lta=0.00;
 			medical=0.00;
 			pf=0.00;
 			advance=0.00;
 			loan=0.00;
 			esis=0.00;
 			misc=0.00;;
			stair_value=0.00;	
			atten_days=0.00;
			absent_days=0.00;
			arrear_days=0.00;
			extra_hrs=0.00;
			coupon_amt=0.00;
			machine1_value=0.00;
			machine1_days=0.00;
			machine2_value=0.00;
			machine2_days=0.00;
			food_value=0.00;
			prof_tax=0.00;
			pfamt=0.00;
			double tot=0.00;
			double newtot;
 			while (rs.next())
 			{

 				if(rs.getInt(1)==12028)
 					System.out.println("pfamt is "+rs.getDouble(25)+" 49 MAI "+rs.getDouble(49));
 				
 				pfamt=rs.getDouble(25);
 				
 				tot=rs.getDouble(6)+rs.getDouble(7)+rs.getDouble(10)+rs.getDouble(14)+rs.getDouble(47);
 				if(rs.getInt(1)==12028 )
 				{
 					System.out.println(rs.getDouble(6)+" 7 "+rs.getDouble(7)+" 10 "+rs.getDouble(10)+" 14 "+rs.getDouble(14)+" 47 "+rs.getDouble(47));
 					System.out.println("value of tot "+tot);
 				}
 				if(tot>15000)
 					pfamt=rs.getDouble(49);
 
 					
 				emp = new EmptranDto();
 				emp.setSerialno(sno);
 				emp.setEmp_code(rs.getInt(1));
 				emp.setEmp_name(rs.getString(2));
 				emp.setDesignation(rs.getString(3));
 				emp.setPf_no(rs.getInt(4));
 				emp.setEsic_no(rs.getLong(5));
 				emp.setBasic(rs.getDouble(6));
 				emp.setDa(rs.getDouble(7));
 				emp.setHra(rs.getDouble(8));
 				emp.setAdd_hra(rs.getDouble(9));
 				emp.setIncentive(rs.getDouble(10));
 				emp.setSpl_incentive(rs.getDouble(11));
 				emp.setOt_rate(rs.getDouble(12));
 				emp.setLta(rs.getDouble(13));
 				emp.setMedical(rs.getDouble(14));
 				emp.setStair_alw(rs.getDouble(15));
 				
 				
 				

 				if(rs.getInt(1)==12028)
 					System.out.println("basic "+rs.getDouble(16)+" DA "+rs.getDouble(17));
 				emp.setBasic_value(rs.getDouble(16));
 				emp.setDa_value(rs.getDouble(17));
 				emp.setHra_value(rs.getDouble(18));
 				emp.setAdd_hra_value(rs.getDouble(19));
 				emp.setIncentive_value(rs.getDouble(20));
 				emp.setSpl_incen_value(rs.getDouble(21));
 				emp.setOt_value(rs.getDouble(22));
 				emp.setLta_value(rs.getDouble(23));
 				emp.setMedical_value(rs.getDouble(24));
//				emp.setPf_value(rs.getDouble(25));

 				if(mnth_code== 202502 && (emp.getEmp_code()==15 || emp.getEmp_code()==139 || emp.getEmp_code()==403 || emp.getEmp_code()==849 || emp.getEmp_code()==171)) 
 					System.out.println("do nothing ");
 				else if(mnth_code==202502)
 				{   newtot=rs.getDouble(16)+rs.getDouble(17)+rs.getDouble(20)+rs.getDouble(24)+rs.getDouble(47);
 					pfamt=(rs.getDouble(25)-rs.getDouble(48))+rs.getDouble(48);
//			pfamt=  roundTwoDecimals(newtot*12/100);
 				}
 				emp.setPf_value(pfamt);
 				emp.setAdvance(rs.getDouble(26));
 				emp.setLoan(rs.getDouble(27));
 				emp.setEsis_value(rs.getDouble(28));
 				
 				emp.setAtten_days(rs.getDouble(29));
 				emp.setArrear_days(rs.getDouble(30));
 				emp.setAbsent_days(rs.getDouble(31));
 				emp.setStair_days(rs.getInt(32));
 				emp.setExtra_hrs(rs.getDouble(33));
 				emp.setMisc_value(rs.getDouble(34));
 				emp.setStair_value(rs.getDouble(35));
 				
  				emp.setBank(rs.getString(36).length()>1?rs.getString(36):"Direct Cheque");
  				emp.setBank_add1(rs.getString(37));
  				emp.setIfsc_code(rs.getString(38));
  				emp.setBank_accno(rs.getString(39));
  				emp.setBank_code(rs.getInt(40));
 				emp.setCoupon_amt(rs.getDouble(41));
 				emp.setUan_no(rs.getLong(42));
 				emp.setMachine1_value(rs.getDouble(43)); 
 				emp.setMachine2_value(rs.getDouble(44)); 
 				emp.setMachine1_days(rs.getDouble(45)); 
 				emp.setMachine2_days(rs.getDouble(46)); 
 				emp.setFood_value(rs.getDouble(47)); 
 				emp.setProf_tax(rs.getDouble(48));
 				

 				basic+=rs.getDouble(16);
 				da+=rs.getDouble(17);
 				hra+=rs.getDouble(18);
 				add_hra+=rs.getDouble(19);
 				incentive+=rs.getDouble(20);
 				spl_incentive+=rs.getDouble(21);
 				ot+=rs.getDouble(22);
 				lta+=rs.getDouble(23);
 				medical+=rs.getDouble(24);
// 				pf+=rs.getDouble(25);
 				pf+=pfamt;
 				advance+=rs.getDouble(26);
 				loan+=rs.getDouble(27);
 				esis+=rs.getDouble(28);
 				misc+=rs.getDouble(34);
 				stair_value+=rs.getDouble(35);
 				coupon_amt+=rs.getDouble(41);
 				machine1_value+=rs.getDouble(43);
 				machine2_value+=rs.getDouble(44);
 				
 				atten_days+=rs.getDouble(29);
 				arrear_days+=rs.getDouble(30);
 				absent_days+=rs.getDouble(31);
 				extra_hrs+=rs.getDouble(33);
 				machine1_days+=rs.getDouble(45);
 				machine2_days+=rs.getDouble(46);
 				food_value+=rs.getDouble(47);
 				prof_tax+=rs.getDouble(48);

 				
 				sno++;
 				
 				v.add(emp);
 			}

 				emp = new EmptranDto();
 				emp.setSerialno(0);
 				emp.setEmp_name("Grand Total");
 				emp.setBasic_value(basic);
				emp.setDa_value(da);
				emp.setHra_value(hra);
				emp.setAdd_hra_value(add_hra);
				emp.setIncentive_value(incentive);
				emp.setSpl_incen_value(spl_incentive);
				emp.setOt_value(ot);
				emp.setLta_value(lta);
				emp.setMedical_value(medical);
				emp.setPf_value(pf);
				emp.setAdvance(advance);
				emp.setLoan(loan);
				emp.setEsis_value(esis);
				emp.setMisc_value(misc);
				emp.setStair_value(stair_value);
				emp.setAtten_days(atten_days);
 				emp.setArrear_days(arrear_days);
 				emp.setAbsent_days(absent_days);
 				emp.setExtra_hrs(extra_hrs);
 				emp.setCoupon_amt(coupon_amt);
 				emp.setMachine1_value(machine1_value); // total machine value
 				emp.setMachine2_value(machine2_value); // total machine value
 				emp.setMachine1_days(machine1_days); // total machine days
 				emp.setMachine2_days(machine2_days); // total machine days
 				emp.setFood_value(food_value); // total food value 
 				emp.setProf_tax(prof_tax); // total prof tax 
 				
				v.add(emp);
				
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
 			System.out.println("-------------Exception in PayrollDAO.getSalaryRegister " + ex);

 		}
 		finally {
 			try {
 				System.out.println("No. of Records Update/Insert : " );

 				if(rs != null){rs.close();}
 				if(ps != null){ps.close();}
 				if(con != null){con.close();}
 			} catch (SQLException e) {
 				System.out.println("-------------Exception in PayrollDAO.Connection.close --- getSalaryRegister "+e);
 			}
 		}
 		return v;
 	}    
 
    
    
    
    
    public ArrayList<EmptranDto> getSalaryRegisterNew(int depo_code,int cmp_code,int fyear,int mnth_code,int repno,int emnth_code,int code)
  	{
  		PreparedStatement ps = null;
  		ResultSet rs=null;
  		PreparedStatement ps1 = null;
  		ResultSet rs1=null;
  		String query1;
  		
  		Connection con=null;
  		ArrayList<EmptranDto>  v =null;
  		 
  		EmptranDto emp=null;
  		int sno=1;
 		double basic,da,hra,add_hra,incentive,spl_incentive,ot,lta,medical,pf,advance,loan,esis,misc,stair_value,coupon_amt,machine1_value,machine2_value;
 		double atten_days,absent_days,arrear_days,extra_hrs,machine1_days,machine2_days,food_value,prof_tax,pfamt;
 		double arearbasic,arearda,arearhra,arearincentive,arearmedical,arearpf,arearesis;
 		double arearbasic2,arearda2,arearhra2,arearincentive2,arearpf2,arearesis2,arearprof2,arear2,pf2,esic2;
 		double gbasic,gda,ghra,gincentive,gmedical,gfood;
 		int stair_days=0;
 		double ltot5;
 		double ltot6;
 		double ltot7;
 		double ltot9;
 		double ltot10;
 		double ltot11;
 		double basetotal;
 		double epfwages;
 		double arrearepfwages;
  		long diff=0;
  		long diffDays=0;
  		String mncode = String.valueOf(mnth_code);
  		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
  		Date edate=null;
  		try {
			 edate = sdf.parse((mncode.substring(0,4)+"-"+mncode.substring(5)+"-01"));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

 		
  		String query=null;
 		String orderby=" order by e.emp_code";
 		
 		System.out.println("REPNO "+repno);
  		if(repno==10)
  			orderby="  order by e.bank_code,e.emp_code ";
  		else if(repno==1)
  			orderby="  order by e.esic_no ";  
  		
  		
  		
  		int lmnth_code=0;
  		int totalmnthdays=0;
  		int smon=0;
  		try 
  		{
  			con=ConnectionFactory.getConnection();
  			con.setAutoCommit(false);

  			String perd="select mnth_code from perdmast where fin_year=? and fin_ord=1";
  			ps1=con.prepareStatement(perd);
  			ps1.setInt(1, fyear);
  			rs1=ps1.executeQuery();
  			if(rs1.next())
  			{
  				smon=rs1.getInt(1);
  			}
  			
  			rs1.close();
  			ps1.close();
  			ps1=null;
  			
  			String lastmonthQuery="SELECT MNTH_CODE,day(todate) FROM PERDMAST WHERE MNTH_CODE<? ORDER BY MNTH_CODE DESC LIMIT 1";
  			ps1=con.prepareStatement(lastmonthQuery);
  			ps1.setInt(1, mnth_code);
  			rs1=ps1.executeQuery();
  			if(rs1.next())
  			{
  				lmnth_code=rs1.getInt(1);
  				totalmnthdays=rs1.getInt(2);
  			}
  			
  			rs1.close();
  			ps1.close();

  			String condition=" and t.atten_days<>0 ";
  			if(repno==1) // esic 
  				condition=" ";
  		
  			// selective employee for selected month query 
  			if(repno==1903)  // salary register cumulative 
  			{

  				query="	select e.emp_code,e.emp_name,ifnull(e.designation,'Worker'),e.pf_no,e.esic_no,t.basic,t.da,t.hra,t.add_hra,"+
  						" t.incentive,t.spl_incentive,t.ot_rate,t.lta,t.medical,t.stair_alw,  "+
  						" t.basic_value,t.da_value,t.hra_value,t.add_hra_value,t.incentive_value,t.misc_value spl_incen_value,t.ot_value,t.lta_value, "+ 
  						" t.medical_value,t.pf_value,t.advance,t.loan,t.esis_value,t.atten_days,t.arrear_days,t.absent_days,t.stair_days,t.extra_hrs,t.misc_value,t.stair_value,"+ 
  						" ifnull(e.bank,'Direct Cheque'),e.bank_add1,e.ifsc_code,ifnull(e.bank_accno,''),e.bank_code,t.coupon_amt,e.uan_no,"+
  						" t.machine1_value,t.machine2_value,t.machine1_days,t.machine2_days,t.food_value,t.prof_tax,t.pf_value,"+
  						" t.arear_basic_value,t.arear_da_value,t.arear_hra_value,t.arear_incentive_value,t.arear_medical_value,t.arear1_pf_value,"+
  						" t.arear2_pf_value,t.arear1_esic_value,t.arear2_esic_value,t.machine1_rate,t.machine2_rate,"+
  						" ifnull(a.arear2basic,0),ifnull(a.arear2da,0),ifnull(a.arear2hra,0),ifnull(a.arear2incentive,0),ifnull(a.arear2pf,0),"+
  						" ifnull(a.arear2esic,0),ifnull(a.arear2proftax,0),e.adhar_no,e.pan_no,"+
  						" (datediff(ifnull(e.dobirth,curDate()),(select todate from perdmast where fin_year=? and mnth_code=t.mnth_code))/365.25)*-1 age,e.doresign lwdate,"+
  						" MONTHNAME(t.doc_date) AS 'Month Name',year(t.doc_date),"+
  						" employer_esis_value,arear1_esic_value,ifnull(a.arear2empesic,0) "+ 
  						" from emptran t JOIN employeemast e "+
  						"   ON e.emp_code=t.emp_code "+
  						"   AND e.depo_code=? AND e.cmp_code=? AND IFNULL(e.del_tag,'')<>'D' "+
			  			" left join  "+
			  			" (select a.emp_code,arrear_mon,sum(basic_value) arear2basic,sum(da_value) arear2da,sum(hra_value) arear2hra,sum(incen_value)arear2incentive, "+
			  			" sum(pf_value)arear2pf,sum(esic_value)arear2esic,sum(prof_tax) arear2proftax, sum(employer_esic_value) arear2empesic from arrear a "+
			  			" where fin_year<=? and depo_code=? and cmp_code=?  and arrear_paid in('Y','O') group by emp_code ,arrear_mon) a "+
			  			" on  e.emp_code=a.emp_code  AND a.arrear_mon=t.mnth_code "+
			  			" where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code between ? and ? and t.emp_code=?  "+condition+ " and ifnull(t.del_tag,'')<>'D' "+   
			  			" AND  e.depo_code=? and e.cmp_code=? and e.emp_code=t.emp_code and ifnull(e.del_tag,'')<>'D' " ;   
  			}
  			
  			
  			
  			else if(repno==1902)  // salary register cumulative 
  			{
  				
  				query="SELECT e.emp_code,e.emp_name,IFNULL(e.designation,'Worker'),e.pf_no,e.esic_no,"+
  				" SUM(t.basic),SUM(t.da),SUM(t.hra),SUM(t.add_hra),SUM(t.incentive),SUM(t.spl_incentive),SUM(t.ot_rate),SUM(t.lta),SUM(t.medical), "+
  				" SUM(t.stair_alw),SUM(t.basic_value),SUM(t.da_value),SUM(t.hra_value),SUM(t.add_hra_value),SUM(t.incentive_value),SUM(t.misc_value) spl_incen_value, "+
  				"    SUM(t.ot_value),SUM(t.lta_value),SUM(t.medical_value),SUM(t.pf_value),SUM(t.advance),SUM(t.loan),SUM(t.esis_value),SUM(t.atten_days),SUM(t.arrear_days), "+
  				"    SUM(t.absent_days),SUM(t.stair_days),SUM(t.extra_hrs),SUM(t.misc_value),SUM(t.stair_value),IFNULL(e.bank,'Direct Cheque'),e.bank_add1,e.ifsc_code,"+
  				"    IFNULL(e.bank_accno,''),e.bank_code,SUM(t.coupon_amt),e.uan_no,SUM(t.machine1_value),SUM(t.machine2_value),SUM(t.machine1_days),SUM(t.machine2_days),"+
  				"    SUM(t.food_value),SUM(t.prof_tax),SUM(t.pf_value),SUM(t.arear_basic_value),SUM(t.arear_da_value),SUM(t.arear_hra_value),SUM(t.arear_incentive_value),"+
  				"    SUM(t.arear_medical_value),SUM(t.arear1_pf_value),SUM(t.arear2_pf_value),SUM(t.arear1_esic_value)," +
  				"    SUM(t.arear2_esic_value),SUM(t.machine1_rate), SUM(t.machine2_rate), " +
  				" MAX(IFNULL(a.arear2basic,0)) AS arear2basic,MAX(IFNULL(a.arear2da,0)) AS arear2da, MAX(IFNULL(a.arear2hra,0)) AS arear2hra,"+
  				" MAX(IFNULL(a.arear2incentive,0)) AS arear2incentive,MAX(IFNULL(a.arear2pf,0)) AS arear2pf, " +
  				" MAX(IFNULL(a.arear2esic,0)) AS arear2esic,MAX(IFNULL(a.arear2proftax,0)) AS arear2proftax, "+
  				"    e.adhar_no,e.pan_no,0 age,e.doresign lwdate,"+
//  				"    MONTHNAME(t.doc_date) AS `Month Name`,t.fin_year, "+
  				"    MONTHNAME(t.doc_date) AS `Month Name`,year(MAX(t.doc_Date)), "+
  				" 0 employer_esis_value,0 arear1_esic_value,0 arear2empesic "+
  				"    FROM emptran t "+
  				"    INNER JOIN employeemast e "+ 
  				"    ON e.emp_code = t.emp_code AND e.depo_code = ? AND e.cmp_code = ? AND IFNULL(e.del_tag,'') <> 'D'"+
  				"	LEFT JOIN ( "+
  				"    SELECT a.emp_code, SUM(a.basic_value) arear2basic,SUM(a.da_value) arear2da,SUM(a.hra_value) arear2hra, SUM(a.incen_value) arear2incentive,"+
  				"    SUM(a.pf_value) arear2pf,SUM(a.esic_value) arear2esic,SUM(a.prof_tax) arear2proftax"+
  				"    FROM arrear a" +
				"    INNER JOIN emptran t "+ 
				"       ON t.emp_code = a.emp_code "+
				"       AND t.mnth_code = a.arrear_mon AND t.atten_days > 0 "+
  				"   WHERE a.fin_year <= ? AND a.depo_code = ? AND a.cmp_code = ? AND a.arrear_mon BETWEEN ? AND ? AND a.arrear_paid IN ('Y','O')"+
  				"    GROUP BY a.emp_code"+
  				"    ) a ON e.emp_code = a.emp_code"+
  				" WHERE t.fin_year = ? AND t.depo_code = ? AND t.cmp_code = ? AND t.mnth_code BETWEEN ? AND ?  "+condition+ " AND IFNULL(t.del_tag,'') <> 'D' "+
  				"GROUP BY e.emp_code ";
  				;

/*  				LEFT JOIN (
  					    SELECT a.emp_code,
  					           SUM(a.basic_value) AS arear2basic,
  					           SUM(a.da_value) AS arear2da,
  					           SUM(a.hra_value) AS arear2hra
  					    FROM arrear a
  					    INNER JOIN emptran t 
  					       ON t.emp_code = a.emp_code
  					       AND t.mnth_code = a.arrear_mon
  					       AND t.atten_days > 0
  					    WHERE a.fin_year <= 2025
  					      AND a.depo_code = 10
  					      AND a.cmp_code = 1
  					      AND a.arrear_mon BETWEEN :FromMonth AND :ToMonth
  					      AND a.arrear_paid IN ('Y','O')
  					    GROUP BY a.emp_code
  					) a ON e.emp_code = a.emp_code  				
*/
  			}
  			else
  			{
  		 		query="select e.emp_code,e.emp_name,ifnull(e.designation,'Worker'),e.pf_no,e.esic_no,t.basic,t.da,t.hra,t.add_hra," +
  		 	 			" t.incentive,t.spl_incentive,t.ot_rate,t.lta,t.medical,t.stair_alw,"+  
  		 	  			" t.basic_value,t.da_value,t.hra_value,t.add_hra_value,t.incentive_value,t.misc_value spl_incen_value,t.ot_value,t.lta_value,"+ 
  		 	  			" t.medical_value,t.pf_value,t.advance,t.loan,t.esis_value,t.atten_days,t.arrear_days,t.absent_days,t.stair_days,t.extra_hrs,t.misc_value,t.stair_value,"+ 
  		 	  			" ifnull(e.bank,'Direct Cheque'),e.bank_add1,e.ifsc_code,ifnull(e.bank_accno,''),e.bank_code,t.coupon_amt,e.uan_no,"+
  		 	  			" t.machine1_value,t.machine2_value,t.machine1_days,t.machine2_days,t.food_value,t.prof_tax,t.pf_value,"+
  		 	  			" t.arear_basic_value,t.arear_da_value,t.arear_hra_value,t.arear_incentive_value,t.arear_medical_value,t.arear1_pf_value,"+
  		 	  			" t.arear2_pf_value,t.arear1_esic_value,t.arear2_esic_value,t.machine1_rate,t.machine2_rate,"+
  		 	  			" ifnull(a.arear2basic,0),ifnull(a.arear2da,0),ifnull(a.arear2hra,0),ifnull(a.arear2incentive,0),ifnull(a.arear2pf,0)," +
  		 	  			" ifnull(a.arear2esic,0),ifnull(a.arear2proftax,0),e.adhar_no,e.pan_no,"+
  		 	  			"(datediff(ifnull(e.dobirth,curDate()),(select todate from perdmast where fin_year=? and mnth_code=?))/365.25)*-1 age,e.doresign lwdate," +
//  		 	  			" MONTHNAME(t.doc_date) AS 'Month Name',t.fin_year," +
  		 	  			" MONTHNAME(t.doc_date) AS 'Month Name',year(t.doc_date)," +
  		 	  			" employer_esis_value,arear1_esic_value,ifnull(a.arear2empesic,0) "+
  		 	  			" from emptran t ,employeemast e left join "+
  		 	  			" (select a.emp_code,sum(basic_value) arear2basic,sum(da_value) arear2da,sum(hra_value) arear2hra,sum(incen_value)arear2incentive,"+
  		 	  			" sum(pf_value)arear2pf,sum(esic_value)arear2esic,sum(prof_tax) arear2proftax, sum(employer_esic_value) arear2empesic from arrear a "+
  		 	  			" where fin_year<=? and depo_code=? and cmp_code=? and arrear_mon=? and arrear_paid in('Y','O') group by emp_code ) a"+
  		 	  			" on  e.emp_code=a.emp_code "+
  		 	  			" where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? "+condition+ " and ifnull(t.del_tag,'')<>'D'"+   
  		 	  			" AND  e.depo_code=? and e.cmp_code=? and e.emp_code=t.emp_code and ifnull(e.del_tag,'')<>'D' "+orderby;  
  			}
	 	  		query1="select t.emp_code, "+ 
	 	  		" t.atten_days prevdays,(t.basic+t.da+t.hra+t.add_hra+t.incentive+t.spl_incentive) prevgrosswages,"+
	 	  		" t.esis_value prevesisvalue,t.employer_esis_value prevemployeresisvalue,t.arrear_days prevarreardays,"+
	 	  		"(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incentive_value+t.spl_incen_value+t.stair_value+t.misc_value+t.ot_value+t.lta_value+t.medical_value+t.machine1_value+t.machine2_value+t.food_value) prevnetwages"+ 
	 	  		" from emptran t where t.fin_year<=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and t.emp_code=? and ifnull(t.del_tag,'')<>'D' group by t.emp_code";
			
  			
  			
  		ps1 = con.prepareStatement(query1);
  			System.out.println(" yeh salery register  NEW WALI "+orderby);

  			if(repno==1902)
  			{
  				ps = con.prepareStatement(query);
  				ps.setInt(1, depo_code);
  				ps.setInt(2, cmp_code);
  				ps.setInt(3, fyear);
  				ps.setInt(4, depo_code);
  				ps.setInt(5, cmp_code);
  				ps.setInt(6, smon);
  				ps.setInt(7, mnth_code);
  				ps.setInt(8, fyear);
  				ps.setInt(9, depo_code);
  				ps.setInt(10, cmp_code);
  				ps.setInt(11, smon);
  				ps.setInt(12, mnth_code);
  				
  			}
  			else if(repno==1903)  // sellective emp code
  			{
  				ps = con.prepareStatement(query);
  				ps.setInt(1, fyear);
  				ps.setInt(2, depo_code);
  				ps.setInt(3, cmp_code);
  				ps.setInt(4, fyear);
  				ps.setInt(5, depo_code);
  				ps.setInt(6, cmp_code);
  				ps.setInt(7, fyear);
  				ps.setInt(8, depo_code);
  				ps.setInt(9, cmp_code);
  				ps.setInt(10, mnth_code);
  				ps.setInt(11, emnth_code);
  				ps.setInt(12, code);
  				ps.setInt(13, depo_code);
  				ps.setInt(14, cmp_code);
  				
  			}
  			else
  			{
  				ps = con.prepareStatement(query);
  				ps.setInt(1, fyear);
  				ps.setInt(2, mnth_code);
  				ps.setInt(3, fyear);
  				ps.setInt(4, depo_code);
  				ps.setInt(5, cmp_code);
  				ps.setInt(6, mnth_code);
  				ps.setInt(7, fyear);
  				ps.setInt(8, depo_code);
  				ps.setInt(9, cmp_code);
  				ps.setInt(10, mnth_code);
  				ps.setInt(11, depo_code);
  				ps.setInt(12, cmp_code);
  			}
  			rs =ps.executeQuery();
  			
  			v = new ArrayList<EmptranDto> ();
  			
  			gbasic=0.00;
  			gda=0.00;
  			ghra=0.00;
  			gincentive=0.00;
  			gmedical=0.00;
  			gfood=0.00;
  			
  			basic=0.00;
  			da=0.00;
  			hra=0.00;
  			add_hra=0.00;
  			incentive=0.00;
  			spl_incentive=0.00;
  			ot=0.00;
  			lta=0.00;
  			medical=0.00;
  			pf=0.00;
  			advance=0.00;
  			loan=0.00;
  			esis=0.00;
  			misc=0.00;;
 			stair_value=0.00;	
 			atten_days=0.00;
 			absent_days=0.00;
 			arrear_days=0.00;
 			stair_days=0;
 			extra_hrs=0.00;
 			coupon_amt=0.00;
 			machine1_value=0.00;
 			machine1_days=0.00;
 			machine2_value=0.00;
 			machine2_days=0.00;
 			food_value=0.00;
 			prof_tax=0.00;
 			pfamt=0.00;
  			arearbasic=0.00;
  			arearda=0.00;
  			arearhra=0.00;
  			arearincentive=0.00;
  			arearmedical=0.00;
  			arearpf=0.00;
  			arearesis=0.00;
  			arearbasic2=0.00;
  			arearda2=0.00;
  			arearhra2=0.00;
  			arearincentive2=0.00;
  			arearpf2=0.00;
  			arearesis2=0.00;
  			arearprof2=0.00;
 			ltot5=0.00;
 			ltot6=0.00;
 			ltot7=0.00;
 			ltot9=0.00;
 			ltot10=0.00;
 			ltot11=0.00;
 			epfwages=0.00;
 			arrearepfwages=0.00;
 			basetotal=0.00;
 			double tot=0.00;
 			double gtot=0.00;
 			double newtot;
  			while (rs.next())
  			{
  				emp = new EmptranDto();
  				
  				arear2=0.00;
  				pf2=0.00;
  				esic2=0.00;
  				emp.setSerialno(sno);
  				emp.setEmp_code(rs.getInt(1));
  				emp.setEmp_name(rs.getString(2));
  				emp.setDesignation(rs.getString(3));
  				emp.setPf_no(rs.getInt(4));
  				emp.setEsic_no(rs.getLong(5));
  				emp.setBasic(rs.getDouble(6));
  				emp.setDa(rs.getDouble(7));
  				emp.setHra(rs.getDouble(8));
  				emp.setAdd_hra(rs.getDouble(9));
  				emp.setIncentive(rs.getDouble(10));
  				emp.setSpl_incentive(rs.getDouble(11));
  				emp.setOt_rate(rs.getDouble(12));
  				emp.setLta(rs.getDouble(13));
  				emp.setMedical(rs.getDouble(14));
  				emp.setStair_alw(rs.getDouble(15));
				emp.setBasic_value(rs.getDouble(16));
				emp.setDa_value(rs.getDouble(17));
				emp.setHra_value(rs.getDouble(18));
				emp.setAdd_hra_value(rs.getDouble(19));
				emp.setIncentive_value(rs.getDouble(20));
				emp.setSpl_incen_value(rs.getDouble(21));
  				emp.setMedical_value(rs.getDouble(24));
  			    emp.setPf_value(rs.getDouble(25));
  		        emp.setEsis_value(rs.getDouble(28));
  		        emp.setProf_tax(rs.getDouble(48));

  				emp.setOt_value(rs.getDouble(22));
  				emp.setLta_value(rs.getDouble(23));
  				emp.setAdvance(rs.getDouble(26));
  				emp.setLoan(rs.getDouble(27));
  				
  				
  				emp.setAtten_days(rs.getDouble(29));
  				emp.setArrear_days(rs.getDouble(30));
  				emp.setAbsent_days(rs.getDouble(31));
  				emp.setStair_days(rs.getInt(32));
  				emp.setExtra_hrs(rs.getDouble(33));
  				emp.setMisc_value(rs.getDouble(34));
  				emp.setStair_value(rs.getDouble(35));
  				
   				emp.setBank(rs.getString(36).length()>1?rs.getString(36):"Direct Cheque");
   				emp.setBank_add1(rs.getString(37));
   				emp.setIfsc_code(rs.getString(38));
   				emp.setBank_accno(rs.getString(39));
   				emp.setBank_code(rs.getInt(40));
  				emp.setCoupon_amt(rs.getDouble(41));
  				emp.setUan_no(rs.getLong(42));
  				emp.setMachine1_value(rs.getDouble(43)); 
  				emp.setMachine2_value(rs.getDouble(44)); 
  				emp.setMachine1_days(rs.getDouble(45)); 
  				emp.setMachine2_days(rs.getDouble(46)); 
  				emp.setFood_value(rs.getDouble(47)); 
  				
  				emp.setArear_basic_value(rs.getDouble(50));
  				emp.setArear_da_value(rs.getDouble(51));
  				emp.setArear_hra_value(rs.getDouble(52));
  				emp.setArear_incentive_value(rs.getDouble(53));
  				emp.setArear_medical_value(rs.getDouble(54));
  				emp.setArear1_pf_value(rs.getDouble(55));
  				emp.setArear2_pf_value(rs.getDouble(65)+pf2);
  				emp.setArear1_esic_value(rs.getDouble(57));
  				emp.setArear2_esic_value(rs.getDouble(66)+esic2);
  				emp.setMachine1_rate(rs.getDouble(59));
  				emp.setMachine2_rate(rs.getDouble(60));
  				emp.setArear2_basic_value(rs.getDouble(61)+arear2);
  				emp.setArear2_da_value(rs.getDouble(62));
  				emp.setArear2_hra_value(rs.getDouble(63));
  				emp.setArear2_incentive_value(rs.getDouble(64));
  				emp.setArear2_prof_value(rs.getDouble(67));
  				emp.setAdhar_no(rs.getLong(68));
  				emp.setPan_no(rs.getString(69));
  				
				emp.setMonname(rs.getString(72));
				emp.setFin_year(rs.getInt(73));

				   double epfshare=0.00;
				   double arrearepfshare=0.00;

  				
				   epfwages+=(int) ((emp.getBasic_value()+emp.getDa_value()+emp.getIncentive_value()+emp.getMedical_value())+0.50);
				   if(epfwages >15000)
					   epfwages=(int)  ((15000+emp.getArear2_basic_value()+emp.getArear2_da_value()+emp.getArear2_incentive_value())+0.50);
				   else
					   epfwages+=(int) ((emp.getArear2_basic_value()+emp.getArear2_da_value()+emp.getArear2_incentive_value())+0.50);
				   arrearepfwages+=(int)((emp.getArear_basic_value()+emp.getArear_da_value()+emp.getArear_incentive_value()+emp.getArear_medical_value())+0.50);
  				
				   basetotal+=(int) ((emp.getBasic()+emp.getDa()+emp.getHra()+emp.getIncentive()+emp.getMedical())+0.50);
				   ltot5+=(int) ((emp.getBasic_value()+emp.getDa_value()+emp.getHra_value()+emp.getIncentive_value()+emp.getMedical_value()+emp.getOt_value()+emp.getMachine1_value()+emp.getMachine2_value()+emp.getStair_value()+emp.getSpl_incen_value()+emp.getLta_value())+0.50);
				   ltot6+=(int)((emp.getArear_basic_value()+emp.getArear_da_value()+emp.getArear_hra_value()+emp.getArear_incentive_value()+emp.getArear_medical_value())+0.50);
				   ltot7+=(int)((emp.getArear2_basic_value()+emp.getArear2_da_value()+emp.getArear2_hra_value()+emp.getArear2_incentive_value())+0.50);

				   ltot9+=(int) ((emp.getPf_value()+emp.getEsis_value()+emp.getProf_tax()+emp.getLoan()+emp.getAdvance())+0.50);   
				   ltot10+=(int) ((emp.getArear1_pf_value()+emp.getArear1_esic_value())+0.50);   
				   ltot11+=(int)((emp.getArear2_pf_value()+emp.getArear2_esic_value()+emp.getArear2_prof_value())+0.50);   

				   tot=ltot5+ltot6+ltot7-ltot9-ltot10-ltot11;
				   emp.setNet_value(tot);
				   emp.setPf_gross_wages(ltot5+ltot7);
				   emp.setArrear_gross_wages(ltot6);
				   emp.setEpf_wages(epfwages);
				   emp.setArrear_epf_wages(arrearepfwages);
				   emp.setArrear_eps_wages(arrearepfwages);
				   emp.setArrear_edl_wages(arrearepfwages);
				   emp.setBasic_total(basetotal);
				   emp.setBasic_earning(ltot5);
				   emp.setArrear1_earning(ltot6);
				   emp.setArrear2_earning(ltot7);

				   if(mnth_code>202601 && rs.getDouble(28) == 0)
				   {
					   epfshare=0; 
					   emp.setEmployer_esis_value(epfshare);
					   emp.setArrear1_employer_esis_value(epfshare);
					   emp.setArrear2_employer_esis_value(epfshare);
				   }
				   else
				   {
					   epfshare=(int) Math.ceil((ltot5*0.75)/100); 
					   emp.setBasic_esis_value(epfshare);
					   epfshare=(int) Math.ceil((ltot5*3.25)/100); 
					   if(mnth_code >202508)
						   epfshare=(int) (((ltot5*3.25)/100)+.50); 
					   emp.setEmployer_esis_value(epfshare);

					   epfshare=(int) Math.ceil((ltot6*0.75)/100); 
					   emp.setArrear1_esis_value(epfshare);
					   epfshare=(int) Math.ceil((ltot6*3.25)/100); 
					   if(mnth_code >202508)
						   epfshare=(int) (((ltot6*3.25)/100)+.50); 
					   emp.setArrear1_employer_esis_value(epfshare);

					   epfshare=(int) Math.ceil((ltot7*0.75)/100); 
					   emp.setArrear2_esis_value(epfshare);
					   epfshare=(int) Math.ceil((ltot7*3.25)/100);
					   if(mnth_code >202508)
						   epfshare=(int) (((ltot7*3.25)/100)+.50); 

					   emp.setArrear2_employer_esis_value(epfshare);
				   }

				   
				   
				   emp.setBasic_esis_value(rs.getDouble(28));
//				   emp.setEmployer_esis_value(rs.getDouble(74));
				   emp.setArrear1_esis_value(rs.getDouble(57));
//				   emp.setArrear1_employer_esis_value(rs.getDouble(75));
				   emp.setArrear2_esis_value(rs.getDouble(66)+esic2);
//				   emp.setArrear2_employer_esis_value(rs.getDouble(76));
				   
				   if(epfwages>15000)
				   {
					      emp.setEps_wages(15000);  // IF AGE=>58
					   emp.setEdl_wages(15000);
				   }
				   else
				   {
  					      emp.setEps_wages(epfwages);   // IF AGE=>58
					   emp.setEdl_wages(epfwages);
					   
				   }
				   epfshare=(int) ((epfwages*12/100)+0.50);

				   arrearepfshare=(int) ((arrearepfwages*12/100)+0.50);

				   emp.setEpf_share((int)epfshare);  
				   emp.setArrear_epf_share((int)arrearepfshare);

				   // 26/03/2026
				   if(mnth_code>202602)
				   {
					   emp.setEpf_share(emp.getPf_value()+emp.getArear2_pf_value());  
					   emp.setArrear_epf_share((emp.getArear1_pf_value()));
				   }
				   
				   
				   epfshare=(int) ((emp.getEps_wages()*8.33/100)+0.50);
				   arrearepfshare=(int) ((emp.getArrear_eps_wages()*8.33/100)+0.50);

				   emp.setEps_share((int)epfshare);   // IF AGE=>58
				   emp.setArrear_eps_share((int)arrearepfshare);

				   emp.setEpf_er_share(emp.getEpf_share()-emp.getEps_share());
				   emp.setArrear_epf_er_share(emp.getArrear_epf_share()-emp.getArrear_eps_share());

				   
	  		        if(repno==99) // for salary slip
	  	  		    {
	  					emp.setBasic_value((rs.getDouble(16)+rs.getDouble(50)+rs.getDouble(61)+arear2));
	  					emp.setDa_value   ((rs.getDouble(17)+rs.getDouble(51)+rs.getDouble(62)));
	  					emp.setHra_value(((rs.getDouble(18)+rs.getDouble(52)+rs.getDouble(63))));
	  					emp.setIncentive_value( ((rs.getDouble(20)+rs.getDouble(53)+rs.getDouble(64))));
	  	  				emp.setMedical_value(rs.getDouble(24)+rs.getDouble(54));
	  	  			    emp.setPf_value(  ((rs.getDouble(25)+rs.getDouble(55)+rs.getDouble(65)+pf2)));
	  	  			    emp.setEsis_value(  ((rs.getDouble(28)+rs.getDouble(57)+rs.getDouble(66)+esic2)));
	  					emp.setProf_tax(rs.getDouble(48));
	  	  		    	
	  	  		    }

				   

				   gtot+=tot;
				   ltot5=0.00;
				   ltot6=0.00;
				   ltot7=0.00;
				   ltot9=0.00;
				   ltot10=0.00;
				   ltot11=0.00;
				   epfwages=0.00;
				   arrearepfwages=0.00;
//				   basetotal=0.00;
  				emp.setDash(0);
  					ps1.setInt(1, fyear);
  					ps1.setInt(2, depo_code);
  					ps1.setInt(3, cmp_code);
  					ps1.setInt(4, lmnth_code);
  					ps1.setInt(5,emp.getEmp_code());
  				rs1=ps1.executeQuery();
  				if(rs1.next())
  				{
  					emp.setPrev_days(rs1.getDouble(2));
  					emp.setNcp_days(totalmnthdays-(rs.getDouble(30)+rs1.getDouble(2)));  
  					emp.setPrevious_arrear_basic(rs1.getDouble(7));
  				}
  				rs1.close();
  				



  				
  				gbasic+=rs.getDouble(6);
  				gda+=rs.getDouble(7);
  				ghra+=rs.getDouble(8);
  				gincentive+=rs.getDouble(10);
  				gmedical+=rs.getDouble(14);

  				
  						basic+=rs.getDouble(16);
  		  				da+=rs.getDouble(17);
  		  				hra+=rs.getDouble(18);
  		  				incentive+=rs.getDouble(20);
  		  				medical+=rs.getDouble(24);
  		  				pf+=rs.getDouble(25);
  		  				esis+=rs.getDouble(28);
  		  			    prof_tax+=rs.getDouble(48);
//  				   }

  				add_hra+=rs.getDouble(19);
  				spl_incentive+=rs.getDouble(21);
  				ot+=rs.getDouble(22);
  				lta+=rs.getDouble(23);
  				advance+=rs.getDouble(26);
  				loan+=rs.getDouble(27);
  				misc+=rs.getDouble(34);
  				stair_value+=rs.getDouble(35);
  				coupon_amt+=rs.getDouble(41);
  				machine1_value+=rs.getDouble(43);
  				machine2_value+=rs.getDouble(44);
  				
  				atten_days+=rs.getDouble(29);
  				arrear_days+=rs.getDouble(30);
  				absent_days+=rs.getDouble(31);
  				stair_days+=rs.getDouble(32);
  				extra_hrs+=rs.getDouble(33);
  				machine1_days+=rs.getDouble(45);
  				machine2_days+=rs.getDouble(46);
  				food_value+=rs.getDouble(47);
  				

  				
  				
				   arearbasic+=rs.getDouble(50);
				   arearda+=rs.getDouble(51);
				   arearhra+=rs.getDouble(52);
				   arearincentive+=rs.getDouble(53);
				   arearmedical+=rs.getDouble(54);
				   arearpf+=rs.getDouble(55);
				   arearesis+=rs.getDouble(57);

				   arearbasic2+=rs.getDouble(61)+arear2;
				   arearda2+=rs.getDouble(62);
				   arearhra2+=rs.getDouble(63);
				   arearincentive2+=rs.getDouble(64);
				   arearpf2+=rs.getDouble(65)+pf2;
				   arearesis2+=rs.getDouble(66)+esic2;
				   arearprof2+=rs.getDouble(67);

				   
//   				sno++;

				if(rs.getInt(70)>=58)
				{
					System.out.println("emp code is "+emp.getEmp_code()+" age "+rs.getInt(70));
					emp.setEps_wages(0);
					emp.setEps_share(0);
					emp.setEpf_er_share(emp.getEpf_share());
				}
				emp.setDiffdays(0);
  				if(rs.getDate(71)!=null)
  				{
  					if(rs.getDate(71).after(edate))
  					{
  						diffDays=0;
  						emp.setCreated_date(null);
  					}
  					else
  					{
  						diff = (rs.getDate(71).getTime() - edate.getTime());
  						diffDays = diff / (24 * 60 * 60 * 1000);
  					}
  					emp.setDiffdays(diffDays);
  					//System.out.println("days is "+diffDays);
  				}

  				if(repno==1 && mnth_code >202508 && basetotal>21000 && rs.getDouble(28) == 0)
  				{
  					System.out.println(emp.getEmp_code());
  					
  					emp.setBasic_earning(0);
  					emp.setArrear1_earning(0);
  					emp.setArrear2_earning(0);
  					emp.setBasic_esis_value(0);
  					emp.setArrear1_esis_value(0);
  					emp.setArrear2_esis_value(0);
  					emp.setEmployer_esis_value(0);
  					emp.setArrear1_employer_esis_value(0);
  					emp.setArrear2_employer_esis_value(0);
  					
  				}
  				else if(repno==1 && mnth_code > 202508)
  				{
  					System.out.println(emp.getEmp_code()+" basetotal "+basetotal);
  				}
  				else if(repno==1 && mnth_code >202507 && (emp.getBasic_value()+emp.getDa_value()+emp.getHra_value()+emp.getIncentive_value()+emp.getMedical_value()+emp.getArear_basic_value()+emp.getArear_da_value()+emp.getArear_hra_value()+emp.getArear_incentive_value()+emp.getArear_medical_value()) > 21000) 
  				{
  					emp.setBasic_earning(0);
  					emp.setArrear1_earning(0);
  					emp.setArrear2_earning(0);
  					emp.setBasic_esis_value(0);
  					emp.setArrear1_esis_value(0);
  					emp.setArrear2_esis_value(0);
  					emp.setEmployer_esis_value(0);
  					emp.setArrear1_employer_esis_value(0);
  					emp.setArrear2_employer_esis_value(0);
  				}

 
  				
  				if(repno==1 && mnth_code > 202508 && basetotal>21000 && rs.getDouble(28) == 0)
  					System.out.println("do not add in list");
  				else
  				{
  					v.add(emp);
  					sno++;
  				}
  				 basetotal=0.00;
  			}


  			
  				emp = new EmptranDto();
  				emp.setSerialno(0);
  				emp.setEmp_name("Grand Total");

  				emp.setBasic(gbasic);
 				emp.setDa(gda);
 				emp.setHra(ghra);
 				emp.setIncentive(gincentive);
 				emp.setMedical(gmedical);
 				emp.setFood_alw(gfood);


  				emp.setBasic_value(basic);
 				emp.setDa_value(da);
 				emp.setHra_value(hra);
 				emp.setAdd_hra_value(add_hra);
 				emp.setIncentive_value(incentive);
 				emp.setSpl_incen_value(spl_incentive);
 				emp.setOt_value(ot);
 				emp.setLta_value(lta);
 				emp.setMedical_value(medical);
 				emp.setPf_value(pf);
 				emp.setAdvance(advance);
 				emp.setLoan(loan);
 				emp.setEsis_value(esis);
 				emp.setMisc_value(misc);
 				emp.setStair_value(stair_value);
 				emp.setAtten_days(atten_days);
  				emp.setArrear_days(arrear_days);
  				emp.setAbsent_days(absent_days);
  				emp.setStair_days(stair_days);
  				emp.setExtra_hrs(extra_hrs);
  				emp.setCoupon_amt(coupon_amt);
  				emp.setMachine1_value(machine1_value); // total machine value
  				emp.setMachine2_value(machine2_value); // total machine value
  				emp.setMachine1_days(machine1_days); // total machine days
  				emp.setMachine2_days(machine2_days); // total machine days
  				emp.setFood_value(food_value); // total food value 
  				emp.setProf_tax(prof_tax); // total prof tax 
  				emp.setArear_basic_value(arearbasic);
  				emp.setArear_da_value(arearda);
  				emp.setArear_hra_value(arearhra);
  				emp.setArear_incentive_value(arearincentive);
  				emp.setArear_medical_value(arearmedical);
  				emp.setArear1_pf_value(arearpf);
  				emp.setArear1_esic_value(arearesis);
  				emp.setMachine1_rate(0);
  				emp.setMachine2_rate(0);

  				emp.setArear2_basic_value(arearbasic2);
  				emp.setArear2_da_value(arearda2);
  				emp.setArear2_hra_value(arearhra2);
  				emp.setArear2_incentive_value(arearincentive2);
  				emp.setArear_medical_value(arearmedical);
  				emp.setArear2_pf_value(arearpf2);
  				emp.setArear2_esic_value(arearesis2);
  				emp.setArear2_prof_value(arearprof2);
  				emp.setNet_value(gtot);
  				emp.setDash(1);
 				v.add(emp);

 				
  	  				System.out.println("TOTAL VALUE "+emp.getBasic()+" da "+emp.getDa()+" hra "+emp.getHra()+" incent "+emp.getIncentive()+" medical "+emp.getMedical());

 				
  			con.commit();
  			con.setAutoCommit(true);
  			rs.close();
  			ps.close();
  			ps1.close();

  		} catch (Exception ex) {
  			ex.printStackTrace();
  			try {
  				con.rollback();
  			} catch (SQLException e) {
  				e.printStackTrace();
  			}
  			System.out.println("-------------Exception in PayrollDAO.getSalaryRegister " + ex);

  		}
  		finally {
  			try {
  				System.out.println("No. of Records Update/Insert : " );

  				if(rs != null){rs.close();}
  				if(rs1 != null){rs1.close();}
  				if(ps != null){ps.close();}
  				if(ps1 != null){ps1.close();}
  				if(con != null){con.close();}
  			} catch (SQLException e) {
  				System.out.println("-------------Exception in PayrollDAO.Connection.close --- getSalaryRegister "+e);
  			}
  		}
  		return v;
  	}    
     
    
    public ArrayList getYearlyAbsentReport(int depo_code,int cmp_code,int fyear,int repno)
  	{
  		PreparedStatement ps = null;
  		ResultSet rs=null;

  		PreparedStatement ps1 = null;
  		ResultSet rs1=null;

  		Connection con=null;
  		ArrayList v =null;
  		 
  		int mon[] = new int[12];
  		int mont[] ;
  		EmptranDto emp=null;
  		String condition="";
  		String condition1="";
  		String condition2="";
  		String field="absent_days";
  		String query="";
  		if(repno==2)
  			 field="extra_hrs";
  		else if(repno==3)
 			 field="stair_days";
  		else if(repno==4)
			 field="atten_days";
  		else if(repno==7)
  		{
			 field="prof_tax";
			 condition=" and (basic+da+hra+incentive)>18750  ";
			 if(fyear>2024)
			 {
				 condition=" and atten_days>0 and prof_tax>0   ";
				 condition1=" and atten_days>0 and prof_tax>0  ";
				 condition2=" and (basic_value+da_value+hra_value+incen_value)>18750  ";
			 }
  		}
  		 
  		try 
  		{
  			con=ConnectionFactory.getConnection();
  			con.setAutoCommit(false);
  			
  			String query1 = "select mnth_code from perdmast where fin_year=? order by fin_ord";
  			ps1 = con.prepareStatement(query1);
  			ps1.setInt(1, fyear);
  			rs1=ps1.executeQuery();
  			int k=0;
  			while(rs1.next())
  			{
  				mon[k]=rs1.getInt(1);
  				k++;
  			}
  			rs1.close();
  			ps1.close();

  			if(repno==7)
  			{
  				if(fyear>2024)
  				{
  					query="select e.emp_code,e1.emp_name,sum(e.apr),sum(e.may),sum(e.jun),sum(e.jul),sum(e.aug),sum(e.sep),sum(e.octt),sum(e.nov),sum(e.decc),sum(e.jan),sum(e.feb),sum(e.mar) from "+ 
  							"(select emp_code,"+field+"  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
  							"union all "+
  							"select emp_code,0  apr,"+field+" may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
  							"union all "+
  							"select emp_code,0  apr,0 may,"+field+" jun,0 jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,"+field+" jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,"+field+" aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition1+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, "+field+" sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition1+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, "+field+" octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition1+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, "+field+"  nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition1+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, "+field+" decc, 0 jan, 0 feb, 0 mar from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition1+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, 0 decc, "+field+" jan, 0 feb, 0 mar  from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition1+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, 0 decc, 0 jan, "+field+" feb, 0 mar  from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition1+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, 0 decc, 0 jan, 0 feb, "+field+" mar  from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition1+"" +
  							"union all "+
  							"select emp_code,"+field+"  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition2+
  							"union all "+
  							"select emp_code,0  apr,"+field+" may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition2+
  							"union all "+
  							"select emp_code,0  apr,0 may,"+field+" jun,0 jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition2+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,"+field+" jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition2+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,"+field+" aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition2+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, "+field+" sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition2+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, "+field+" octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition2+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, "+field+"  nov, 0 decc, 0 jan, 0 feb, 0 mar  from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition2+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, "+field+" decc, 0 jan, 0 feb, 0 mar from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition2+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, 0 decc, "+field+" jan, 0 feb, 0 mar  from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition2+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, 0 decc, 0 jan, "+field+" feb, 0 mar  from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition2+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, 0 decc, 0 jan, 0 feb, "+field+" mar  from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition2+
  							") e, employeemast e1 "+
  							"where e1.depo_code=? and e1.cmp_code=? and e1.emp_code=e.emp_code group by e.emp_code ";
  				}
  				else
  				{
  					query="select e.emp_code,e1.emp_name,sum(e.apr),sum(e.may),sum(e.jun),sum(e.jul),sum(e.aug),sum(e.sep),sum(e.octt),sum(e.nov),sum(e.decc),sum(e.jan),sum(e.feb),sum(e.mar) from "+ 
  							"(select emp_code,"+field+"  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
  							"union all "+
  							"select emp_code,0  apr,"+field+" may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
  							"union all "+
  							"select emp_code,0  apr,0 may,"+field+" jun,0 jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,"+field+" jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,"+field+" aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, "+field+" sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, "+field+" octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, "+field+"  nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, "+field+" decc, 0 jan, 0 feb, 0 mar from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, 0 decc, "+field+" jan, 0 feb, 0 mar  from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, 0 decc, 0 jan, "+field+" feb, 0 mar  from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, 0 decc, 0 jan, 0 feb, "+field+" mar  from emptran "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+"" +
  							"union all "+
  							"select emp_code,"+field+"  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition1+
  							"union all "+
  							"select emp_code,0  apr,"+field+" may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition1+
  							"union all "+
  							"select emp_code,0  apr,0 may,"+field+" jun,0 jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition1+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,"+field+" jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition1+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,"+field+" aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition1+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, "+field+" sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition1+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, "+field+" octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition1+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, "+field+"  nov, 0 decc, 0 jan, 0 feb, 0 mar  from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition1+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, "+field+" decc, 0 jan, 0 feb, 0 mar from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition1+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, 0 decc, "+field+" jan, 0 feb, 0 mar  from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition1+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, 0 decc, 0 jan, "+field+" feb, 0 mar  from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition1+
  							"union all "+
  							"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, 0 decc, 0 jan, 0 feb, "+field+" mar  from arrear "+ 
  							"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition1+
  							") e, employeemast e1 "+
  							"where e1.depo_code=? and e1.cmp_code=? and e1.emp_code=e.emp_code group by e.emp_code ";
  				}
  				
  			}
  			else
  			{
  			query="select e.emp_code,e1.emp_name,sum(e.apr),sum(e.may),sum(e.jun),sum(e.jul),sum(e.aug),sum(e.sep),sum(e.octt),sum(e.nov),sum(e.decc),sum(e.jan),sum(e.feb),sum(e.mar) from "+ 
				"(select emp_code,"+field+"  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
				"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
				"union all "+
				"select emp_code,0  apr,"+field+" may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
				"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
				"union all "+
				"select emp_code,0  apr,0 may,"+field+" jun,0 jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
				"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
				"union all "+
				"select emp_code,0  apr,0 may,0 jun,"+field+" jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
				"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
				"union all "+
				"select emp_code,0  apr,0 may,0 jun,0 jul,"+field+" aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
				"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
				"union all "+
				"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, "+field+" sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
				"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
				"union all "+
				"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, "+field+" octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar from emptran "+ 
				"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
				"union all "+
				"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, "+field+"  nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
				"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
				"union all "+
				"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, "+field+" decc, 0 jan, 0 feb, 0 mar from emptran "+ 
				"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
				"union all "+
				"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, 0 decc, "+field+" jan, 0 feb, 0 mar  from emptran "+ 
				"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
				"union all "+
				"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, 0 decc, 0 jan, "+field+" feb, 0 mar  from emptran "+ 
				"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+
				"union all "+
				"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, 0 decc, 0 jan, 0 feb, "+field+" mar  from emptran "+ 
				"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+condition+") e, employeemast e1 "+
				"where e1.depo_code=? and e1.cmp_code=? and e1.emp_code=e.emp_code group by e.emp_code "; 
  			}

  			 
  			ps = con.prepareStatement(query);
  			k=1;
  			if(repno==7)
  			{
  	  			for(int j=0;j<12;j++)
  	  			{
  	  	  			ps.setInt(k++, fyear);
  	  	  			ps.setInt(k++, depo_code);
  	  	  			ps.setInt(k++, cmp_code);
  	  	  			ps.setInt(k++, mon[j]);
  	  				
  	  			}
  	  			for(int j=0;j<12;j++)
  	  			{
  	  	  			ps.setInt(k++, fyear);
  	  	  			ps.setInt(k++, depo_code);
  	  	  			ps.setInt(k++, cmp_code);
  	  	  			ps.setInt(k++, mon[j]);
  	  				
  	  			}

  		  			ps.setInt(k++, depo_code);
  		  			ps.setInt(k++, cmp_code);
  		  			k=1;
  			}
  			else
  			{
  				for(int j=0;j<12;j++)
  				{
  					ps.setInt(k++, fyear);
  					ps.setInt(k++, depo_code);
  					ps.setInt(k++, cmp_code);
  					ps.setInt(k++, mon[j]);

  				}
  				ps.setInt(k++, depo_code);
  				ps.setInt(k++, cmp_code);
  			}
	  			rs =ps.executeQuery();
  			
  			v = new ArrayList();
  			int sno=1;
  			while (rs.next())
  			{
  				emp = new EmptranDto();
  				emp.setSerialno(sno++);
  				emp.setEmp_code(rs.getInt(1));  // ip number
  				emp.setEmp_name(rs.getString(2)); // ip name
  				
  				mont= new int[12];
  				for(int j=0;j<12;j++)
  				{
  					mont[j]=rs.getInt(j+3);
  				}
  				
  				emp.setMon(mont);
  				
  				v.add(emp);
  			}



  			con.commit();
  			con.setAutoCommit(true);
  			rs.close();
  			ps.close();

  		} catch (Exception ex)  { ex.printStackTrace();
  			try {
  				con.rollback();
  			} catch (SQLException e) {
  				e.printStackTrace();
  			}
  			System.out.println("-------------Exception in PayrollDAO.getEsicList " + ex);

  		}
  		finally {
  			try {
  				System.out.println("No. of Records Update/Insert : " );

  				if(rs != null){rs.close();}
  				if(ps != null){ps.close();}
  				if(rs1 != null){rs1.close();}
  				if(ps1 != null){ps1.close();}
  				if(con != null){con.close();}
  			} catch (SQLException e) {
  				System.out.println("-------------Exception in PayrollDAO.Connection.close --- getEsicList "+e);
  			}
  		}
  		return v;
  	}    
    
    
    
    public ArrayList getMonthlyAbsentReport(int depo_code,int cmp_code,int fyear,int emp_code,int repno)
  	{
  		PreparedStatement ps = null;
  		ResultSet rs=null;


  		Connection con=null;
  		ArrayList v =null;
  		 
  		EmptranDto emp=null;
  		String query=null;
  		 
  		try 
  		{
  			con=ConnectionFactory.getConnection();
  			con.setAutoCommit(false);
  			
  			
  			if(repno==1)
  			{
  				query="select p.month_nm,ifnull(e.emp_code,0),e.emp_name,ifnull(e.absent_days,0) from perdmast p left join "+
				"(select e.emp_code,e1.emp_name,e.absent_days,mnth_code from emptran e,employeemast e1 "+
				"where e.fin_year=? and e.depo_code=? and e.cmp_code=? and e1.depo_code=? and e1.cmp_code=? "+
				"and e.emp_code=? and e.emp_code=e1.emp_code) e "+
				"on p.mnth_code=e.mnth_code where p.fin_year=? ";
  			}
  			else if(repno==2)
  			{
				query="select p.month_nm,ifnull(e.emp_code,0),e.emp_name,ifnull(e.extra_hrs,0),ifnull(e.ot_rate,0),ifnull(e.ot_value,0.00) from perdmast p left join "+
				"(select e.emp_code,e1.emp_name,e.extra_hrs,e.ot_rate,e.ot_value,e.mnth_code from emptran e,employeemast e1 "+
				"where e.fin_year=? and e.depo_code=? and e.cmp_code=? and e1.depo_code=? and e1.cmp_code=? "+
				"and e.emp_code=? and e.emp_code=e1.emp_code) e "+
				"on p.mnth_code=e.mnth_code where p.fin_year=? ";
  			}
  			else if(repno==3)
  			{
				query="select p.month_nm,ifnull(e.emp_code,0),e.emp_name,ifnull(e.stair_days,0),ifnull(e.stair_alw,0),ifnull(e.stair_value,0.00) from perdmast p left join "+
				"(select e.emp_code,e1.emp_name,e.stair_days,e.stair_alw,e.stair_value,e.mnth_code from emptran e,employeemast e1 "+
				"where e.fin_year=? and e.depo_code=? and e.cmp_code=? and e1.depo_code=? and e1.cmp_code=? "+
				"and e.emp_code=? and e.emp_code=e1.emp_code) e "+
				"on p.mnth_code=e.mnth_code where p.fin_year=? ";
  			}
  			else if(repno==4)
  			{
  				query="select p.month_nm,ifnull(e.emp_code,0),e.emp_name,ifnull(e.atten_days,0) from perdmast p left join "+
				"(select e.emp_code,e1.emp_name,e.atten_days,mnth_code from emptran e,employeemast e1 "+
				"where e.fin_year=? and e.depo_code=? and e.cmp_code=? and e1.depo_code=? and e1.cmp_code=? "+
				"and e.emp_code=? and e.emp_code=e1.emp_code) e "+
				"on p.mnth_code=e.mnth_code where p.fin_year=? ";
  			}
  			else if(repno==5)
  			{
  				query="select p.month_nm,ifnull(e.emp_code,0),ifnull(e.emp_name,''),ifnull(e.esic_no,''),ifnull(e.pf_no,''),ifnull(e.uan_no,''),ifnull(e.adhar_no,''), "+
  				"ifnull(e.basic,0.00),ifnull(e.da,0.00),ifnull(e.hra,0.00),ifnull(e.add_hra,0.00),ifnull(e.incentive,0.00),ifnull(e.spl_incentive,0.00), "+
  				"ifnull(e.lta,0.00),ifnull(e.medical,0.00),ifnull(e.ot_rate,0.00),ifnull(e.stair_alw,0.00),ifnull(e.food_value,0.00)  from perdmast p left join  "+
  				"(select e.emp_code,e1.emp_name,e1.esic_no,e1.pf_no,e1.uan_no,e1.adhar_no,e.basic,e.da,e.hra,e.add_hra,e.incentive,e.spl_incentive,e.lta,e.medical,e.ot_rate,e.stair_alw ,e.mnth_code,e.food_value from emptran e,employeemast e1 "+ 
  				"where e.fin_year=? and e.depo_code=? and e.cmp_code=? and e1.depo_code=? and e1.cmp_code=? "+
  				"and e.emp_code=? and e.emp_code=e1.emp_code) e "+
  				"on  p.mnth_code=e.mnth_code where p.fin_year=? "  ;
  			}
  			ps = con.prepareStatement(query);
			ps.setInt(1, fyear);
  			ps.setInt(2, depo_code);
  			ps.setInt(3, cmp_code);
  			ps.setInt(4, depo_code);
  			ps.setInt(5, cmp_code);
  			ps.setInt(6, emp_code);
  			ps.setInt(7, fyear);
  			
  			rs =ps.executeQuery();
  			
  			v = new ArrayList();
  			boolean first=true;
  			int empcode=0;
  			String empname="";
  			while (rs.next())
  			{
  				
  				if(first)
  				{
  					empcode=rs.getInt(2);
  					empname=rs.getString(3);
  					if(empcode>0)
  					   first=false;
  				}
  				emp = new EmptranDto();
  				emp.setMonname(rs.getString(1));
  				emp.setEmp_code(empcode);  
  				emp.setEmp_name(empname); 
  				if(repno==1)
  					emp.setAbsent_days(rs.getInt(4));
  				else if(repno==2)
  				{
  					emp.setExtra_hrs(rs.getDouble(4));
  					emp.setOt_rate(rs.getDouble(5));
  					emp.setOt_value(rs.getDouble(6));
  				}
  				else if(repno==3)
  				{
  					emp.setStair_days(rs.getInt(4));
  					emp.setStair_alw(rs.getDouble(5));
  					emp.setStair_value(rs.getDouble(6));
  				}
  				else if(repno==4)
  					emp.setAtten_days(rs.getInt(4));
  				else if(repno==5)
  				{
  					emp.setEsic_no(rs.getLong(4));
  					emp.setPf_no(rs.getInt(5));
  					emp.setUan_no(rs.getLong(6));
  					emp.setAdhar_no(rs.getLong(7));
  					emp.setBasic(rs.getDouble(8));
  					emp.setDa(rs.getDouble(9));
  					emp.setHra(rs.getDouble(10));
  					emp.setAdd_hra(rs.getDouble(11));
  					emp.setIncentive(rs.getDouble(12));
  					emp.setSpl_incentive(rs.getDouble(13));
  					emp.setLta(rs.getDouble(14));
  					emp.setMedical(rs.getDouble(15));
  					emp.setOt_rate(rs.getDouble(16));
  					emp.setStair_alw(rs.getDouble(17));
  					emp.setFood_value(rs.getDouble(18));
  				}
  				v.add(emp);
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
  			System.out.println("-------------Exception in PayrollDAO.getMonthlyAbsentReport " + ex);

  		}
  		finally {
  			try {
  				System.out.println("No. of Records Update/Insert : " );

  				if(rs != null){rs.close();}
  				if(ps != null){ps.close();}
  				if(con != null){con.close();}
  			} catch (SQLException e) {
  				System.out.println("-------------Exception in PayrollDAO.Connection.close --- getEsicList "+e);
  			}
  		}
  		return v;
  	}    
        

    public int salaryRegeneration(int fyear,int depo,int cmp,int mcode)
    {
  	  
    	PreparedStatement ps1 = null;
    	Connection con=null;
    	String query1=null;
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();

			
			if(mcode <202506)
			{
				query1="update emptran e1, "+
						"(select emp_code,gross,basic,da,hra,add_hra,incentive,spl_incentive,lta,medical,ot_rate,stair_alw  from employeemast where depo_code=? and cmp_code=?) e2 "+
						"set e1.gross=e2.gross,e1.basic=e2.basic,e1.da=e2.da,e1.hra=e2.hra,e1.add_hra=e2.add_hra,e1.incentive=e2.incentive,e1.spl_incentive=e2.spl_incentive,"+
						"e1.lta=e2.lta,e1.medical=e2.medical,e1.ot_rate=e2.ot_rate,e1.stair_alw=e2.stair_alw "+
						"where e1.fin_year=? and e1.depo_code=? and e1.cmp_code=? and e1.mnth_code=? and  e1.emp_code=e2.emp_code"; 
			}
			
			else
			{
				query1="update emptran e1, "+
						"(select emp_code,gross,basic,da,hra,add_hra,incentive,spl_incentive,lta,medical,ot_rate,stair_alw  from employeemast where depo_code=? and cmp_code=?) e2 "+
						"set e1.gross=e2.gross,e1.basic=e2.basic,e1.da=e2.da,e1.hra=e2.hra,e1.add_hra=e2.add_hra,e1.incentive=e2.incentive,e1.spl_incentive=e2.spl_incentive,"+
						"e1.lta=e2.lta,e1.medical=e2.medical/12,e1.ot_rate=e2.ot_rate,e1.stair_alw=e2.stair_alw "+
						"where e1.fin_year=? and e1.depo_code=? and e1.cmp_code=? and e1.mnth_code=? and  e1.emp_code=e2.emp_code"; 
				
			}
			
			con.setAutoCommit(false);
			
			ps1 = con.prepareStatement(query1);
			ps1.setInt(1,depo);
			ps1.setInt(2,cmp);
			ps1.setInt(3,fyear);
			ps1.setInt(4,depo);
			ps1.setInt(5,cmp);
			ps1.setInt(6,mcode);
			i=ps1.executeUpdate();
			
			con.commit();
			con.setAutoCommit(true);
			ps1.close();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in PayrollDAO.salaryRegeneration " + ex);
			i=-1;
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : "+i);

				if(ps1 != null){ps1.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in PayrollDAO.Connection.close "+e);
			}
		}
		return i;
	}
    
    
    


    public int lockAttendance(int year,int depo,int cmpcode,int mnthcode,int lock)
    {
  	  
    	PreparedStatement ps1 = null;
    	PreparedStatement ps2 = null;
    	PreparedStatement ps3 = null;
    	PreparedStatement ps4 = null;
    	
    	Connection con=null;
     
		
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();


			String query1="update  emptran set atten_lock=? where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? ";
			String query2="insert into locktable values (?,?,?,?,?,?,?,?)";
			String query3="delete from locktable where  depo_code=? and cmp_code=? and mnth_code=? ";

			/*
			 * After Lock update emptran with loan amount automtically after attendance entry is over {Incomplete} */
			String query4="update emptran e, "+
			"(SELECT emp_code,INSTL_AMT FROM LOAN WHERE FIN_YEAR=? AND DEPO_CODE=? AND CMP_CODE=? AND MNTH_CODE=?) l "+
			"set e.loan=l.instl_amt  "+
			"where e.fin_year=? and e.depo_code=? and e.cmp_code=? and e.mnth_code=? and e.emp_code=l.emp_code ";
			
			
			
			
			con.setAutoCommit(false);
			
			ps1 = con.prepareStatement(query1);
			ps1.setInt(1,lock);
			ps1.setInt(2,year);
  			ps1.setInt(3,depo);
  			ps1.setInt(4,cmpcode);
  			ps1.setInt(5,mnthcode); 
  			i=ps1.executeUpdate();

  			ps3 = con.prepareStatement(query3);
  			ps3.setInt(1,depo);
  			ps3.setInt(2,cmpcode);
  			ps3.setInt(3,mnthcode); 
  			i=ps3.executeUpdate();
  			
			ps4 = con.prepareStatement(query4);
			ps4.setInt(1,year);
			ps4.setInt(2,depo);
			ps4.setInt(3,cmpcode);
			ps4.setInt(4,mnthcode);
			ps4.setInt(5,year);
			ps4.setInt(6,depo);
			ps4.setInt(7,cmpcode);
			ps4.setInt(8,mnthcode);
  			i=ps4.executeUpdate();

  			
  			
			ps2 = con.prepareStatement(query2);
			
			for(int j=1;j<8;j++)
			{
				ps2.setInt(1,depo);
				ps2.setInt(2,cmpcode);
				ps2.setInt(3,j);
				ps2.setInt(4,mnthcode);
				ps2.setInt(5,0); 
				ps2.setString(6,null); 
				ps2.setDate(7,null); 
				ps2.setInt(8,0); 
				i=ps2.executeUpdate();
			}

  			
			con.commit();
			con.setAutoCommit(true);
			ps1.close();
			ps2.close();
			ps3.close();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in PayrollDAO.lockAttendance " + ex);
			i=-1;
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : "+i);

				if(ps1 != null){ps1.close();}
				if(ps2 != null){ps2.close();}
				if(ps3 != null){ps3.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in PayrollDAO.Connection.close "+e);
			}
		}
		return i;
	}

    
    public int bonusGeneration(int fyear,int depo,int cmp,int mcode,double bonus_per,double bonus_limit)
    {
  	  
    	PreparedStatement ps1 = null;
    	PreparedStatement ps2 = null;
    	PreparedStatement ps3 = null;
    	PreparedStatement ps4 = null;
    	Connection con=null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	int[] mon = new int[12];
    	int[] mondays = new int[12];
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();
			con.setAutoCommit(false);

			String monquery="select mnth_code,day(todate) from perdmast where fin_year=? order by fin_ord ";
			ps = con.prepareStatement(monquery);
			ps.setInt(1,fyear);
			rs=ps.executeQuery();
			while (rs.next())
			{
				mon[i]=rs.getInt(1);
				mondays[i]=rs.getInt(2);
				i++;
			}
			
				
			String query2="delete from bonus where fin_year=? and depo_code=? and cmp_code=? ";
			
			String query1="insert into bonus "+
			"select depo_code,cmp_code,emp_code,"+bonus_per+" bper,"+bonus_limit+" blimit,sum(attnapr),sum(attnmay),sum(attnjun),sum(attnjul),sum(attnaug),sum(attnsep),sum(attnoct), "+
			"sum(attnnov),sum(attndec),sum(attnjan),sum(attnfeb),sum(attnmar), "+
			"sum(apr)-sum(aapr),sum(may-amay),sum(jun)-sum(ajun),sum(jul)-sum(ajul),sum(aug)-sum(aaug),sum(sep)-sum(asep),sum(octm)-sum(aoct),sum(nov)-sum(anov),sum(decm)-sum(adec),sum(jan)-sum(ajan),sum(feb)-sum(afeb),sum(mar)-sum(amar)," +
			"(sum(apr)+sum(may)+sum(jun)+sum(jul)+sum(aug)+sum(sep)+sum(octm)+sum(nov)+sum(decm)+sum(jan)+sum(feb)+sum(mar)) bonusapp," +
			"0.00,fin_year,"+mcode+" mnthcd,0 bonuspaid,0 bonuscheck,  "+ 
			"sum(aapr),sum(amay),sum(ajun),sum(ajul),sum(aaug),sum(asep),sum(aoct),sum(anov),sum(adec),sum(ajan),sum(afeb),sum(amar), " +
			"sum(adays_apr),sum(adays_may),sum(adays_jun),sum(adays_jul),sum(adays_aug),sum(adays_sep),sum(adays_oct),sum(adays_nov),sum(adays_dec),sum(adays_jan),sum(adays_feb),sum(adays_mar) from " +
			"(select (basic_value+da_value) apr,0 may,0 jun,0 jul, 0 aug,0 sep,0 octm,0 nov,0 decm,0 jan,0 feb,0 mar,mnth_code,emp_code, "+
			"atten_days attnapr,0 attnmay,0 attnjun,0 attnjul,0 attnaug,0 attnsep,0 attnoct,0 attnnov,0 attndec,0 attnjan,0 attnfeb,0 attnmar," +
			"round(((basic+da)/30)*arrear_days) aapr,0 amay,0 ajun,0 ajul,0 aaug,0 asep,0 aoct,0 anov,0 adec,0 ajan,0 afeb,0 amar," +
			"arrear_days adays_apr ,0 adays_may,0 adays_jun,0 adays_jul,0 adays_aug,0 adays_sep,0 adays_oct,0 adays_nov,0 adays_dec,0 adays_jan,0 adays_feb,0 adays_mar," +
			"depo_code,cmp_code,fin_year from emptran where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all "+ 
			"select 0 apr,(basic_value+da_value) may,0 jun,0 jul, 0 aug,0 sep,0 octm,0 nov,0 decm,0 jan,0 feb,0 mar,mnth_code,emp_code, "+
			"0 attnapr,atten_days attnmay,0 attnjun,0 attnjul,0 attnaug,0 attnsep,0 attnoct,0 attnnov,0 attndec,0 attnjan,0 attnfeb,0 attnmar,"+   
			"0 aapr,round(((basic+da)/30)*arrear_days) amay,0 ajun,0 ajul,0 aaug,0 asep,0 aoct,0 anov,0 adec,0 ajan,0 afeb,0 amar," +
			"0 adays_apr ,arrear_days adays_may,0 adays_jun,0 adays_jul,0 adays_aug,0 adays_sep,0 adays_oct,0 adays_nov,0 adays_dec,0 adays_jan,0 adays_feb,0 adays_mar," +
			"depo_code,cmp_code,fin_year from emptran where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all "+ 
			"select 0 apr,0 may,(basic_value+da_value) jun,0 jul, 0 aug,0 sep,0 octm,0 nov,0 decm,0 jan,0 feb,0 mar,mnth_code,emp_code, "+
			"0 attnapr,0 attnmay,atten_days attnjun,0 attnjul,0 attnaug,0 attnsep,0 attnoct,0 attnnov,0 attndec,0 attnjan,0 attnfeb,0 attnmar, "+   
			"0 aapr,0 amay,round(((basic+da)/30)*arrear_days) ajun,0 ajul,0 aaug,0 asep,0 aoct,0 anov,0 adec,0 ajan,0 afeb,0 amar," +
			"0 adays_apr ,0 adays_may,arrear_days adays_jun,0 adays_jul,0 adays_aug,0 adays_sep,0 adays_oct,0 adays_nov,0 adays_dec,0 adays_jan,0 adays_feb,0 adays_mar," +
			"depo_code,cmp_code,fin_year from emptran where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all  "+
			"select 0 apr,0 may,0 jun,(basic_value+da_value) jul, 0 aug,0 sep,0 octm,0 nov,0 decm,0 jan,0 feb,0 mar,mnth_code,emp_code, "+
			"0 attnapr,0 attnmay,0 attnjun,atten_days attnjul,0 attnaug,0 attnsep,0 attnoct,0 attnnov,0 attndec,0 attnjan,0 attnfeb,0 attnmar,"+   
			"0 aapr,0 amay,0 ajun,round(((basic+da)/30)*arrear_days) ajul,0 aaug,0 asep,0 aoct,0 anov,0 adec,0 ajan,0 afeb,0 amar," +
			"0 adays_apr ,0 adays_may,0 adays_jun,arrear_days adays_jul,0 adays_aug,0 adays_sep,0 adays_oct,0 adays_nov,0 adays_dec,0 adays_jan,0 adays_feb,0 adays_mar," +
			"depo_code,cmp_code,fin_year from emptran where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all  "+
			"select 0 apr,0 may,0 jun,0 jul, (basic_value+da_value) aug,0 sep,0 octm,0 nov,0 decm,0 jan,0 feb,0 mar,mnth_code,emp_code, "+
			"0 attnapr,0 attnmay,0 attnjun,0 attnjul,atten_days attnaug,0 attnsep,0 attnoct,0 attnnov,0 attndec,0 attnjan,0 attnfeb,0 attnmar, "+   
			"0 aapr,0 amay,0 ajun,0 ajul,round(((basic+da)/30)*arrear_days) aaug,0 asep,0 aoct,0 anov,0 adec,0 ajan,0 afeb,0 amar," +
			"0 adays_apr ,0 adays_may,0 adays_jun,0 adays_jul,arrear_days adays_aug,0 adays_sep,0 adays_oct,0 adays_nov,0 adays_dec,0 adays_jan,0 adays_feb,0 adays_mar," +
			"depo_code,cmp_code,fin_year from emptran where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all  "+
			"select 0 apr,0 may,0 jun,0 jul, 0 aug,(basic_value+da_value) sep,0 octm,0 nov,0 decm,0 jan,0 feb,0 mar,mnth_code,emp_code, "+
			"0 attnapr,0 attnmay,0 attnjun,0 attnjul,0 attnaug,atten_days attnsep,0 attnoct,0 attnnov,0 attndec,0 attnjan,0 attnfeb,0 attnmar, "+   
			"0 aapr,0 amay,0 ajun,0 ajul,0 aaug,round(((basic+da)/30)*arrear_days) asep,0 aoct,0 anov,0 adec,0 ajan,0 afeb,0 amar," +
			"0 adays_apr ,0 adays_may,0 adays_jun,0 adays_jul,0 adays_aug,arrear_days adays_sep,0 adays_oct,0 adays_nov,0 adays_dec,0 adays_jan,0 adays_feb,0 adays_mar," +
			"depo_code,cmp_code,fin_year from emptran where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all "+ 
			"select 0 apr,0 may,0 jun,0 jul, 0 aug,0 sep,(basic_value+da_value) octm,0 nov,0 decm,0 jan,0 feb,0 mar,mnth_code,emp_code, "+
			"0 attnapr,0 attnmay,0 attnjun,0 attnjul,0 attnaug,0 attnsep,atten_days attnoct,0 attnnov,0 attndec,0 attnjan,0 attnfeb,0 attnmar, "+   
			"0 aapr,0 amay,0 ajun,0 ajul,0 aaug,0 asep,round(((basic+da)/30)*arrear_days) aoct,0 anov,0 adec,0 ajan,0 afeb,0 amar," +
			"0 adays_apr ,0 adays_may,0 adays_jun,0 adays_jul,0 adays_aug,0 adays_sep,arrear_days adays_oct,0 adays_nov,0 adays_dec,0 adays_jan,0 adays_feb,0 adays_mar," +
			"depo_code,cmp_code,fin_year from emptran where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all "+ 
			"select 0 apr,0 may,0 jun,0 jul, 0 aug,0 sep,0 octm,(basic_value+da_value) nov,0 decm,0 jan,0 feb,0 mar,mnth_code,emp_code, "+
			"0 attnapr,0 attnmay,0 attnjun,0 attnjul,0 attnaug,0 attnsep,0 attnoct,atten_days attnnov,0 attndec,0 attnjan,0 attnfeb,0 attnmar, "+   
			"0 aapr,0 amay,0 ajun,0 ajul,0 aaug,0 asep,0 aoct,round(((basic+da)/30)*arrear_days) anov,0 adec,0 ajan,0 afeb,0 amar," +
			"0 adays_apr ,0 adays_may,0 adays_jun,0 adays_jul,0 adays_aug,0 adays_sep,0 adays_oct,arrear_days adays_nov,0 adays_dec,0 adays_jan,0 adays_feb,0 adays_mar," +
			"depo_code,cmp_code,fin_year from emptran where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all "+ 
			"select 0 apr,0 may,0 jun,0 jul, 0 aug,0 sep,0 octm,0 nov,(basic_value+da_value) decm,0 jan,0 feb,0 mar,mnth_code,emp_code, "+
			"0 attnapr,0 attnmay,0 attnjun,0 attnjul,0 attnaug,0 attnsep,0 attnoct,0 attnnov,atten_days attndec,0 attnjan,0 attnfeb,0 attnmar, "+   
			"0 aapr,0 amay,0 ajun,0 ajul,0 aaug,0 asep,0 aoct,0 anov,round(((basic+da)/30)*arrear_days) adec,0 ajan,0 afeb,0 amar," +
			"0 adays_apr ,0 adays_may,0 adays_jun,0 adays_jul,0 adays_aug,0 adays_sep,0 adays_oct,0 adays_nov,arrear_days adays_dec,0 adays_jan,0 adays_feb,0 adays_mar," +
			"depo_code,cmp_code,fin_year from emptran where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all "+ 
			"select 0 apr,0 may,0 jun,0 jul, 0 aug,0 sep,0 octm,0 nov,0 decm,(basic_value+da_value) jan,0 feb,0 mar,mnth_code,emp_code, "+
			"0 attnapr,0 attnmay,0 attnjun,0 attnjul,0 attnaug,0 attnsep,0 attnoct,0 attnnov,0 attndec,atten_days attnjan,0 attnfeb,0 attnmar, "+   
			"0 aapr,0 amay,0 ajun,0 ajul,0 aaug,0 asep,0 aoct,0 anov,0 adec,round(((basic+da)/30)*arrear_days) ajan,0 afeb,0 amar," +
			"0 adays_apr ,0 adays_may,0 adays_jun,0 adays_jul,0 adays_aug,0 adays_sep,0 adays_oct,0 adays_nov,0 adays_dec,arrear_days adays_jan,0 adays_feb,0 adays_mar," +
			"depo_code,cmp_code,fin_year from emptran where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all "+ 
			"select 0 apr,0 may,0 jun,0 jul, 0 aug,0 sep,0 octm,0 nov,0 decm,0 jan,(basic_value+da_value) feb,0 mar,mnth_code,emp_code, "+
			"0 attnapr,0 attnmay,0 attnjun,0 attnjul,0 attnaug,0 attnsep,0 attnoct,0 attnnov,0 attndec,0 attnjan,atten_days attnfeb,0 attnmar, "+   
			"0 aapr,0 amay,0 ajun,0 ajul,0 aaug,0 asep,0 aoct,0 anov,0 adec,0 ajan,round(((basic+da)/30)*arrear_days) afeb,0 amar," +
			"0 adays_apr ,0 adays_may,0 adays_jun,0 adays_jul,0 adays_aug,0 adays_sep,0 adays_oct,0 adays_nov,0 adays_dec,0 adays_jan,arrear_days adays_feb,0 adays_mar," +
			"depo_code,cmp_code,fin_year from emptran where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all "+ 
			"select 0 apr,0 may,0 jun,0 jul, 0 aug,0 sep,0 octm,0 nov,0 decm,0 jan,0 feb,(basic_value+da_value) mar,mnth_code,emp_code, "+
			"0 attnapr,0 attnmay,0 attnjun,0 attnjul,0 attnaug,0 attnsep,0 attnoct,0 attnnov,0 attndec,0 attnjan,0 attnfeb,atten_days attnmar, "+   
			"0 aapr,0 amay,0 ajun,0 ajul,0 aaug,0 asep,0 aoct,0 anov,0 adec,0 ajan,0 afeb,round(((basic+da)/30)*arrear_days) amar," +
			"0 adays_apr ,0 adays_may,0 adays_jun,0 adays_jul,0 adays_aug,0 adays_sep,0 adays_oct,0 adays_nov,0 adays_dec,0 adays_jan,0 adays_feb,arrear_days adays_mar," +
			"depo_code,cmp_code,fin_year  "+
			"from emptran where fin_year=? and depo_code=? and cmp_code=? and mnth_code=?) a  "+
			" group by a.emp_code ";
			 
			
			String query3="update bonus b,employeemast e set b.bonus_per=e.bonus_per,b.bonus_limit=e.bonus,b.bonus_check=e.bonus_check "+
			" where b.fin_year=? and b.depo_code=? and  b.cmp_code=? "+
			" and e.depo_code=? and  e.cmp_code=? and b.emp_Code=e.emp_code "; 
			
//			select if(bonus_apr>round(bonus_check+(bonus_check/30)*(atten_apr-30)),if(bonus_check=0,bonus_apr,round(bonus_check+(bonus_check/30)*(atten_apr-30))),bonus_apr) bonus,
//			select if(arrear_apr>round((bonus_check/30)*adays_apr),if(bonus_check=0,arrear_apr,round((bonus_check/30)*adays_apr)),arrear_apr) bonus,
			
			String query4="update bonus set bonus_apr=if(bonus_apr>round(bonus_check+(bonus_check/30)*(atten_apr-if(atten_apr=0,0,"+mondays[0]+"))),if(bonus_check=0,bonus_apr,round(bonus_check+(bonus_check/30)*(atten_apr-if(atten_apr=0,0,"+mondays[0]+")))),bonus_apr),"+
			"bonus_may=if(bonus_may>round(bonus_check+(bonus_check/30)*(atten_may-if(atten_may=0,0,"+mondays[1]+"))),if(bonus_check=0,bonus_may,if(atten_may>0 and atten_may<=1,bonus_may,round(bonus_check+(bonus_check/30)*(atten_may-if(atten_may=0,0,"+mondays[1]+"))))),bonus_may),"+			
			"bonus_jun=if(bonus_jun>round(bonus_check+(bonus_check/30)*(atten_jun-if(atten_jun=0,0,"+mondays[2]+"))),if(bonus_check=0,bonus_jun,round(bonus_check+(bonus_check/30)*(atten_jun-if(atten_jun=0,0,"+mondays[2]+")))),bonus_jun),"+			
			"bonus_jul=if(bonus_jul>round(bonus_check+(bonus_check/30)*(atten_jul-if(atten_jul=0,0,"+mondays[3]+"))),if(bonus_check=0,bonus_jul,if(atten_jul>0 and atten_jul<=1,bonus_jul,round(bonus_check+(bonus_check/30)*(atten_jul-if(atten_jul=0,0,"+mondays[3]+"))))),bonus_jul),"+			
			"bonus_aug=if(bonus_aug>round(bonus_check+(bonus_check/30)*(atten_aug-if(atten_aug=0,0,"+mondays[4]+"))),if(bonus_check=0,bonus_aug,if(atten_aug>0 and atten_aug<=1,bonus_aug,round(bonus_check+(bonus_check/30)*(atten_aug-if(atten_aug=0,0,"+mondays[4]+"))))),bonus_aug),"+			
			"bonus_sep=if(bonus_sep>round(bonus_check+(bonus_check/30)*(atten_sep-if(atten_sep=0,0,"+mondays[5]+"))),if(bonus_check=0,bonus_sep,round(bonus_check+(bonus_check/30)*(atten_sep-if(atten_sep=0,0,"+mondays[5]+")))),bonus_sep),"+			
			"bonus_oct=if(bonus_oct>round(bonus_check+(bonus_check/30)*(atten_oct-if(atten_oct=0,0,"+mondays[6]+"))),if(bonus_check=0,bonus_oct,if(atten_oct>0 and atten_oct<=1,bonus_oct,round(bonus_check+(bonus_check/30)*(atten_oct-if(atten_oct=0,0,"+mondays[6]+"))))),bonus_oct),"+			
			"bonus_nov=if(bonus_nov>round(bonus_check+(bonus_check/30)*(atten_nov-if(atten_nov=0,0,"+mondays[7]+"))),if(bonus_check=0,bonus_nov,round(bonus_check+(bonus_check/30)*(atten_nov-if(atten_nov=0,0,"+mondays[7]+")))),bonus_nov),"+			
			"bonus_dec=if(bonus_dec>round(bonus_check+(bonus_check/30)*(atten_dec-if(atten_dec=0,0,"+mondays[8]+"))),if(bonus_check=0,bonus_dec,if(atten_dec>0 and atten_dec<=1,bonus_dec,round(bonus_check+(bonus_check/30)*(atten_dec-if(atten_dec=0,0,"+mondays[8]+"))))),bonus_dec),"+			
			"bonus_jan=if(bonus_jan>round(bonus_check+(bonus_check/30)*(atten_jan-if(atten_jan=0,0,"+mondays[9]+"))),if(bonus_check=0,bonus_jan,if(atten_jan>0 and atten_jan<=1,bonus_jan,round(bonus_check+(bonus_check/30)*(atten_jan-if(atten_jan=0,0,"+mondays[9]+"))))),bonus_jan),"+			
			"bonus_feb=if(bonus_feb>round(bonus_check+(bonus_check/30)*(atten_feb-if(atten_feb=0,0,"+mondays[10]+"))),if(bonus_check=0,bonus_feb,round(bonus_check+(bonus_check/30)*(atten_feb-if(atten_feb=0,0,"+mondays[10]+")))),bonus_feb),"+			
			"bonus_mar=if(bonus_mar>round(bonus_check+(bonus_check/30)*(atten_mar-if(atten_mar=0,0,"+mondays[11]+"))),if(bonus_check=0,bonus_mar,if(atten_mar>0 and atten_mar<=1,bonus_mar,round(bonus_check+(bonus_check/30)*(atten_mar-if(atten_mar=0,0,"+mondays[11]+"))))),bonus_mar),"+			

			"arrear_apr=if(arrear_apr>round((bonus_check/30)*adays_apr),if(bonus_check=0,arrear_apr,round((bonus_check/30)*adays_apr)),arrear_apr),"+
			"arrear_may=if(arrear_may>round((bonus_check/30)*adays_may),if(bonus_check=0,arrear_may,round((bonus_check/30)*adays_may)),arrear_may),"+
			"arrear_jun=if(arrear_jun>round((bonus_check/30)*adays_jun),if(bonus_check=0,arrear_jun,round((bonus_check/30)*adays_jun)),arrear_jun),"+
			"arrear_jul=if(arrear_jul>round((bonus_check/30)*adays_jul),if(bonus_check=0,arrear_jul,round((bonus_check/30)*adays_jul)),arrear_jul),"+
			"arrear_aug=if(arrear_aug>round((bonus_check/30)*adays_aug),if(bonus_check=0,arrear_aug,round((bonus_check/30)*adays_aug)),arrear_aug),"+
			"arrear_sep=if(arrear_sep>round((bonus_check/30)*adays_sep),if(bonus_check=0,arrear_sep,round((bonus_check/30)*adays_sep)),arrear_sep),"+
			"arrear_oct=if(arrear_oct>round((bonus_check/30)*adays_oct),if(bonus_check=0,arrear_oct,round((bonus_check/30)*adays_oct)),arrear_oct),"+
			"arrear_nov=if(arrear_nov>round((bonus_check/30)*adays_nov),if(bonus_check=0,arrear_nov,round((bonus_check/30)*adays_nov)),arrear_nov),"+
			"arrear_dec=if(arrear_dec>round((bonus_check/30)*adays_dec),if(bonus_check=0,arrear_dec,round((bonus_check/30)*adays_dec)),arrear_dec),"+
			"arrear_jan=if(arrear_jan>round((bonus_check/30)*adays_jan),if(bonus_check=0,arrear_jan,round((bonus_check/30)*adays_jan)),arrear_jan),"+
			"arrear_feb=if(arrear_feb>round((bonus_check/30)*adays_feb),if(bonus_check=0,arrear_feb,round((bonus_check/30)*adays_feb)),arrear_feb),"+
			"arrear_mar=if(arrear_mar>round((bonus_check/30)*adays_mar),if(bonus_check=0,arrear_mar,round((bonus_check/30)*adays_mar)),arrear_mar),"+
			"bonus_applicable=(bonus_apr+bonus_may+bonus_jun+bonus_jul+bonus_aug+bonus_sep+bonus_oct+bonus_nov+bonus_dec+bonus_jan+bonus_feb+bonus_mar+arrear_apr+arrear_may+arrear_jun+arrear_jul+arrear_aug+arrear_sep+arrear_oct+arrear_nov+arrear_dec+arrear_jan+arrear_feb+arrear_mar),"+
			"bonus_value=round((bonus_applicable*bonus_per)/100),"+
			"bonus_paid=if(bonus_value>bonus_limit,if(bonus_limit=0,bonus_value,bonus_limit),bonus_value) "+
			"where fin_year=? and depo_code=? and cmp_code=? ";
			
			
			 // delete old generated record 
			ps2 = con.prepareStatement(query2);
			ps2.setInt(1,fyear);
			ps2.setInt(2,depo);
			ps2.setInt(3,cmp);
			i = ps2.executeUpdate();
			
			ps1 = con.prepareStatement(query1);
			ps1.setInt(1,fyear);
			ps1.setInt(2,depo);
			ps1.setInt(3,cmp);
			ps1.setInt(4,mon[0]);
			ps1.setInt(5,fyear);
			ps1.setInt(6,depo);
			ps1.setInt(7,cmp);
			ps1.setInt(8,mon[1]);
			ps1.setInt(9,fyear);
			ps1.setInt(10,depo);
			ps1.setInt(11,cmp);
			ps1.setInt(12,mon[2]);
			ps1.setInt(13,fyear);
			ps1.setInt(14,depo);
			ps1.setInt(15,cmp);
			ps1.setInt(16,mon[3]);
			ps1.setInt(17,fyear);
			ps1.setInt(18,depo);
			ps1.setInt(19,cmp);
			ps1.setInt(20,mon[4]);
			ps1.setInt(21,fyear);
			ps1.setInt(22,depo);
			ps1.setInt(23,cmp);
			ps1.setInt(24,mon[5]);
			ps1.setInt(25,fyear);
			ps1.setInt(26,depo);
			ps1.setInt(27,cmp);
			ps1.setInt(28,mon[6]);
			ps1.setInt(29,fyear);
			ps1.setInt(30,depo);
			ps1.setInt(31,cmp);
			ps1.setInt(32,mon[7]);
			ps1.setInt(33,fyear);
			ps1.setInt(34,depo);
			ps1.setInt(35,cmp);
			ps1.setInt(36,mon[8]);
			ps1.setInt(37,fyear);
			ps1.setInt(38,depo);
			ps1.setInt(39,cmp);
			ps1.setInt(40,mon[9]);
			ps1.setInt(41,fyear);
			ps1.setInt(42,depo);
			ps1.setInt(43,cmp);
			ps1.setInt(44,mon[10]);
			ps1.setInt(45,fyear);
			ps1.setInt(46,depo);
			ps1.setInt(47,cmp);
			ps1.setInt(48,mon[11]);
			i=ps1.executeUpdate();
			
			
			 // update bonusper & limit from employeemast 
			ps3 = con.prepareStatement(query3);
			ps3.setInt(1,fyear);
			ps3.setInt(2,depo);
			ps3.setInt(3,cmp);
			ps3.setInt(4,depo);
			ps3.setInt(5,cmp);
			i = ps3.executeUpdate();

			 // calculate bonusvalue and bonuspaid  
			ps4 = con.prepareStatement(query4);
			ps4.setInt(1,fyear);
			ps4.setInt(2,depo);
			ps4.setInt(3,cmp);
			i = ps4.executeUpdate();

			
			con.commit();
			con.setAutoCommit(true);
			ps1.close();
			ps2.close();
			ps3.close();
			ps4.close();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in PayrollDAO.salaryRegeneration " + ex);
			i=-1;
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : "+i);

				if(ps1 != null){ps1.close();}
				if(ps2 != null){ps2.close();}
				if(ps3 != null){ps3.close();}
				if(ps4 != null){ps4.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in PayrollDAO.Connection.close "+e);
			}
		}
		return i;
	}
    
    
	public Vector getBonusList(int depo_code,int cmp_code,int fyear,int mnth_code,int option)
	{
		PreparedStatement ps = null;
		ResultSet rs=null;
		Connection con=null;
		Vector v =null;
		Vector col=null;
		int sno=1;
		try 
		{
			con=ConnectionFactory.getConnection();
			con.setAutoCommit(false);

			String query="select e.emp_name,e.esic_no,e.pf_no,e.emp_code,b.bonus_per,b.bonus_limit,b.mnth_code,b.bonus_applicable " +
			" from employeemast e, bonus b  "+
			" where  e.cmp_code=b.cmp_code and e.emp_code=b.emp_code and b.fin_year=? and b.depo_code=? and b.cmp_code=?  " +
			"  and e.depo_code=? and e.cmp_code=? ";

			ps = con.prepareStatement(query);
			ps.setInt(1, fyear);
			ps.setInt(2, depo_code);
			ps.setInt(3, cmp_code);
			ps.setInt(4, depo_code);
			ps.setInt(5, cmp_code);
			rs =ps.executeQuery();
			
			boolean first=true;
			while (rs.next())
			{
			
				if(first)
				{
					v = new Vector();
					first=false;
				}
				
				col= new Vector();
				col.add(sno++);  // sno 0
				col.add(rs.getString(1)); // emp_name //1
				col.add(rs.getString(2)); // esic no  2
				col.add(rs.getString(3)); // pf no  3
				col.add(rs.getInt(4)); // emp_code  4
				col.add(rs.getDouble(5)); // bonus per  5
				col.add(rs.getDouble(6)); // bonus limit  6
				col.add(rs.getInt(7)); // mnth_code  7
				col.add(rs.getDouble(8)); // bonus applicable 8
				v.add(col);
			}



			con.commit();
			con.setAutoCommit(true);
			rs.close();
			ps.close();

		} catch (Exception ex) { ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in PayrollDAO.getBonusList " + ex);

		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : " );

				if(rs != null){rs.close();}
				if(ps != null){ps.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in PayrollDAO.Connection.close --- getBonusList "+e);
			}
		}
		return v;
	}
	    
    
	   public int updateBonusList(ArrayList<?> attnlist)
	    {
	  	  
	    	PreparedStatement ps1 = null;
	    	PreparedStatement ps2 = null;
	    	Connection con=null;
	    	EmptranDto emp=null;
			double bonusamt=0.00;
			int i=0;
			try 
			{
				con=ConnectionFactory.getConnection();

				
				String query1="update bonus set bonus_per=?,bonus_limit=?,bonus_value=?,bonus_paid=? " +
				" where  fin_year=? and depo_code=? and cmp_code=? and  mnth_code=? and emp_code=? " ;

				String query2="update emptran set bonus_per=?,bonus_value=? " +
				" where  fin_year=? and depo_code=? and cmp_code=? and  mnth_code=? and emp_code=? " ;
			
				con.setAutoCommit(false);
				
				ps1 = con.prepareStatement(query1);
				ps2 = con.prepareStatement(query2);
				
				int s=attnlist.size();
				for (int j=0;j<s;j++)
				{
					emp= (EmptranDto) attnlist.get(j);
					
					bonusamt=(emp.getNet_value()*emp.getBonus_per())/100;
					if(bonusamt>emp.getBonus_value())
						bonusamt=emp.getBonus_value();
					
					ps1.setDouble(1, emp.getBonus_per());
					ps1.setDouble(2, emp.getBonus_value()); // bonus_limit  
					ps1.setDouble(3, bonusamt); // bonusamt  
					ps1.setInt(4,0);
					// where clause
					ps1.setInt(5,emp.getFin_year());
					ps1.setInt(6,emp.getDepo_code());
					ps1.setInt(7,emp.getCmp_code());
					ps1.setInt(8,emp.getMnth_code());
					ps1.setInt(9,emp.getEmp_code());
					i=ps1.executeUpdate();
					
					
					ps2.setDouble(1, emp.getBonus_per());
					ps2.setDouble(2, bonusamt); // bonusamt  
					// where clause
					ps2.setInt(3,emp.getFin_year());
					ps2.setInt(4,emp.getDepo_code());
					ps2.setInt(5,emp.getCmp_code());
					ps2.setInt(6,emp.getMnth_code());
					ps2.setInt(7,emp.getEmp_code());
					i=ps2.executeUpdate();
					
					if(i>0)
					{
						ps1.setDouble(1, emp.getBonus_per());
						ps1.setDouble(2, emp.getBonus_value()); // bonus_limit  
						ps1.setDouble(3, bonusamt); // bonusamt  
						ps1.setInt(4,emp.getMnth_code());
						// where clause
						ps1.setInt(5,emp.getFin_year());
						ps1.setInt(6,emp.getDepo_code());
						ps1.setInt(7,emp.getCmp_code());
						ps1.setInt(8,emp.getMnth_code());
						ps1.setInt(9,emp.getEmp_code());
						i=ps1.executeUpdate();
					}
			  			
				}
				con.commit();
				con.setAutoCommit(true);
				ps1.close();
				ps2.close();
				
			} catch (SQLException ex) {
				ex.printStackTrace();
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.out.println("-------------Exception in PayrollDAO.updateBonusList " + ex);
				i=-1;
			}
			finally {
				try {
					System.out.println("No. of Records Update/Insert : "+i);

					if(ps1 != null){ps1.close();}
					if(ps2 != null){ps2.close();}
					if(con != null){con.close();}
				} catch (SQLException e) {
					System.out.println("-------------Exception in PayrollDAO.Connection.close "+e);
				}
			}
			return i;
		}	
	
	   
	   public ArrayList getBonusRegister(int depo_code,int cmp_code,int fyear,int repno)
	  	{
	  		PreparedStatement ps = null;
	  		ResultSet rs=null;
 

	  		Connection con=null;
	  		ArrayList v =null;
	  		
	  		 
	  		double mont[] ;
	  		double amont[] ;
	  		double bonusval[];
	  		EmptranDto emp=null;
	  		String orderby="";
	  		if(repno==16)
	  			orderby="  order by e.bank_code,e.emp_code ";
	  		else if(repno==5 || repno==51)
	  			orderby="  order by e.emp_code ";	  			
	  		 
	  		 
	  		
	  		 
	  		
	  		try 
	  		{
	  			con=ConnectionFactory.getConnection();
	  			con.setAutoCommit(false);
	  		 

	  			String query=" select b.emp_code,e.emp_name, b.bonus_per, b.bonus_limit," +
	  			" b.atten_apr, b.atten_may, b.atten_jun, b.atten_jul, b.atten_aug, b.atten_sep, b.atten_oct,b.atten_nov, b.atten_dec, b.atten_jan, b.atten_feb, b.atten_mar," +
	  			" b.bonus_apr+b.arrear_apr, b.bonus_may+b.arrear_may, b.bonus_jun+b.arrear_jun, b.bonus_jul+b.arrear_jul, b.bonus_aug+b.arrear_aug, b.bonus_sep+b.arrear_sep, b.bonus_oct+b.arrear_oct,b.bonus_nov+b.arrear_nov, b.bonus_dec+b.arrear_dec, b.bonus_jan+b.arrear_jan, b.bonus_feb+b.arrear_feb, b.bonus_mar+b.arrear_mar," +
	  			" b.adays_apr, b.adays_may, b.adays_jun, b.adays_jul, b.adays_aug, b.adays_sep, b.adays_oct,b.adays_nov, b.adays_dec, b.adays_jan, b.adays_feb, b.adays_mar," +
	  			" b.bonus_applicable, b.bonus_value,e.bank_accno,e.bank,e.bank_code "+ 
				" from  bonus b,employeemast e where b.fin_year=? and b.depo_code=? and b.cmp_code=? and e.emp_code=b.emp_code" +
				" and (e.doresign is null or e.doresign >= (select frdate from yearfil where year=?)) " +
				" and ifnull(e.del_tag,'')<>'D' "+orderby;


	  			 
	  			ps = con.prepareStatement(query);
	  			ps.setInt(1, fyear);
  	  			ps.setInt(2, depo_code);
  	  			ps.setInt(3, cmp_code);
  	  			ps.setInt(4, fyear);
  	  		 
		  		rs =ps.executeQuery();
	  			
	  			v = new ArrayList();
	  			int sno=1;
	  			while (rs.next())
	  			{
	  				emp = new EmptranDto();
	  				emp.setSerialno(sno++);
	  				emp.setEmp_code(rs.getInt(1));  // ip number
	  				emp.setEmp_name(rs.getString(2)); // ip name
	  				
	  				mont= new double[12];
	  				amont= new double[12];
	  				bonusval= new double[12];
	  				for(int j=0;j<12;j++)
	  				{
	  					mont[j]=rs.getDouble(j+5);
	  					bonusval[j]=rs.getDouble(j+17);
	  					amont[j]=rs.getDouble(j+29);
	  				}
	  				
	  				emp.setAtten(mont);
	  				emp.setArrdays(amont);
	  				emp.setBonusval(bonusval);
	  				emp.setBonus_value(rs.getDouble(42));
	  				emp.setBank_accno(rs.getString(43));
	  				emp.setBank(rs.getString(44));
	  				emp.setBank_code(rs.getInt(45));
	  				
	  				v.add(emp);
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
	  			System.out.println("-------------Exception in PayrollDAO.getBonusRegister " + ex);

	  		}
	  		finally {
	  			try {
	  				System.out.println("No. of Records Update/Insert : " );

	  				if(rs != null){rs.close();}
	  				if(ps != null){ps.close();}
	  				if(con != null){con.close();}
	  			} catch (SQLException e) {
	  				System.out.println("-------------Exception in PayrollDAO.Connection.close --- getBonusRegister "+e);
	  			}
	  		}
	  		return v;
	  	}    	   


	   
	   public int arrearGeneration(int fyear,int depo,int cmp,int smon,int emon,int cmon)
	    {
	  	  
		   	System.out.println("cmon ki value "+cmon);
		   
	    	Connection con=null;
	    	PreparedStatement ps3 = null;
	    	PreparedStatement ps2 = null;
	    	PreparedStatement ps1 = null;
	    	PreparedStatement ps4 = null;
	    	PreparedStatement ps5 = null;
	    	PreparedStatement ps6 = null;
	    	PreparedStatement ps7 = null;
	    	ResultSet rs6=null;
	    	ResultSet rs7=null;
			int i=0;
			try 
			{
				con=ConnectionFactory.getConnection();
				con.setAutoCommit(false);
				
				//// old query 
				
/*				String query3="insert into arrear "+
				"select a.depo_code,a.cmp_code,a.emp_code,a.basic,a.da,a.hra,a.ahra,a.incen,a.splincen, "+
				"if(a.atten_days=a.mdays,a.basic,round((a.basic*a.atten_days/a.mdays))) bvalue, "+
				"if(a.atten_days=a.mdays,a.da,round((a.da*a.atten_days/a.mdays))) davalue, "+
				"if(a.atten_days=a.mdays,a.hra,round((a.hra*a.atten_days/a.mdays))) hravalue, "+
				"if(a.atten_days=a.mdays,a.ahra,round((a.ahra*a.atten_days/a.mdays))) ahravalue, "+
				"if(a.atten_days=a.mdays,a.incen,round((a.incen*a.atten_days/a.mdays))) incenvalue, "+
				"if(a.atten_days=a.mdays,a.splincen,round((a.splincen*a.atten_days/a.mdays))) sincenvalue, "+
				"a.atten_days,"+cmon+","+smon+","+emon+",a.mnth_code,0.00,0.00,0.00,"+fyear+
				" from  "+
				"(select e.depo_code,e.cmp_code,e.emp_code,(e.basic-t.basic) basic,(e.da-t.da) da,(e.hra-t.hra) hra,(e.add_hra-t.add_hra) ahra ,(e.incentive-t.incentive) incen "+
				",(e.spl_incentive-t.spl_incentive) splincen,t.atten_days,t.mnth_code,day(last_day(concat(left(t.mnth_code,4),'-',right(t.mnth_code,2),'-01'))) mdays from employeemast e "+
				"left join  "+
				"(select emp_code,basic,da,hra,add_hra,incentive,spl_incentive,atten_days,mnth_code from emptran "+ 
				"where depo_code=? and cmp_code=? and atten_days<>0 and mnth_code between ? and ? group by emp_code,mnth_Code) t "+
				"on e.emp_Code=t.emp_code where e.depo_code=? and e.cmp_code=? and e.doresign is null and(t.basic-e.basic) is not null ) a"; 
*/
	
				String query1="delete  from arrear where fin_year=? and depo_code=? and cmp_code=? and arrear_mon=? ";
				//basic = roundTwoDecimals(rs.getDouble(4)+((rs.getDouble(4)/30)*(rs.getDouble(17)-rs.getDouble(16))));
//				basic = roundTwoDecimals(rs.getDouble(4)+((rs.getDouble(4)/30)*rs.getDouble(17))); // absend is 0

/*				String query3="insert into arrear "+
				"select a.depo_code,a.cmp_code,a.emp_code,a.basic,a.da,a.hra,a.ahra,a.incen,a.splincen, "+
				"if(a.atten_days=a.mdays,(a.basic+round((a.basic*a.arrear_days)/30)),round((a.basic*(a.atten_days+a.arrear_days-a.absent_days)/30))) bvalue, "+
				"if(a.atten_days=a.mdays,(a.da+round((a.da*a.arrear_days)/30)),round((a.da*(a.atten_days+a.arrear_days-a.absent_days)/30))) davalue, "+
				"if(a.atten_days=a.mdays,(a.hra+round((a.hra*a.arrear_days)/30)),round((a.hra*(a.atten_days+a.arrear_days-a.absent_days)/30))) hravalue, "+
				"if(a.atten_days=a.mdays,(a.ahra+round((a.ahra*a.arrear_days)/30)),round((a.ahra*(a.atten_days+a.arrear_days-a.absent_days)/30))) ahravalue, "+
				"if(a.atten_days=a.mdays,(a.incen+round((a.incen*a.arrear_days)/30)),round((a.incen*(a.atten_days+a.arrear_days-a.absent_days)/30))) incenvalue, "+
	 			"if(a.atten_days=a.mdays,(a.splincen+round((a.splincen*a.arrear_days)/30)),round((a.splincen*(a.atten_days+a.arrear_days-a.absent_days)/30))) sincenvalue, "+
				"a.atten_days,"+cmon+","+smon+","+emon+",a.mnth_code,0.00,0.00,0.00,"+fyear+",a.oldwages,a.newwages,a.arrear_days "+
				" from  "+
				"(select e.depo_code,e.cmp_code,e.emp_code,(e.basic-t.basic) basic,(e.da-t.da) da,(e.hra-t.hra) hra,(e.add_hra-t.add_hra) ahra ,(e.incentive-t.incentive) incen "+
				",(e.spl_incentive-t.spl_incentive) splincen,t.atten_days,t.mnth_code,day(last_day(concat(left(t.mnth_code,4),'-',right(t.mnth_code,2),'-01'))) mdays," +
				"t.arrear_days,t.oldwages,(e.basic+e.da+e.hra+e.add_hra+e.incentive+e.spl_incentive) newwages,t.absent_days from employeemast e "+
				"left join  "+
				"(select emp_code,basic,da,hra,add_hra,incentive,spl_incentive,atten_days,mnth_code,if(mnth_code="+smon+",0,arrear_days) arrear_days,(basic+da+hra+add_hra+incentive+spl_incentive) oldwages,absent_days from emptran "+ 
				"where depo_code=? and cmp_code=? and atten_days<>0 and mnth_code between ? and ? group by emp_code,mnth_Code) t "+
				"on e.emp_Code=t.emp_code where e.depo_code=? and e.cmp_code=? and e.doresign is null and(t.basic-e.basic) is not null ) a"; 
*/

			//	before modfinaction on 28/02/2026
/*				String query3="insert into arrear "+
				"select a.depo_code,a.cmp_code,a.emp_code,a.basic,a.da,a.hra,a.ahra,a.incen,a.splincen, "+
				"if(a.absent_days=0,(round(a.basic)+round((a.basic*a.arrear_days)/30)),round((a.basic+(a.basic*(a.arrear_days-a.absent_days)/30)))) bvalue, "+
				"if(a.absent_days=0,(round(a.da)+round((a.da*a.arrear_days)/30)),round((a.da+(a.da*(a.arrear_days-a.absent_days)/30)))) davalue, "+
				"if(a.absent_days=0,(round(a.hra)+round((a.hra*a.arrear_days)/30)),round((a.hra+(a.hra*(a.arrear_days-a.absent_days)/30)))) hravalue, "+
				"if(a.absent_days=0,(round(a.ahra)+round((a.ahra*a.arrear_days)/30)),round((a.ahra+(a.ahra*(a.arrear_days-a.absent_days)/30)))) ahravalue, "+
				"if(a.absent_days=0,(round(a.incen)+round((a.incen*a.arrear_days)/30)),round((a.incen+(a.incen*(a.arrear_days-a.absent_days)/30)))) incenvalue, "+
	 			"if(a.absent_days=0,(round(a.splincen)+round((a.splincen*a.arrear_days)/30)),round((a.splincen+(a.splincen*(a.arrear_days-a.absent_days)/30)))) sincenvalue, "+
				"a.atten_days,"+cmon+","+smon+","+emon+",a.mnth_code,0.00,0.00,0.00,"+fyear+",a.oldwages,a.newwages,a.arrear_days,'N' arrearpaid,0.00 "+
				" from  "+
				"(select e.depo_code,e.cmp_code,e.emp_code,(e.basic-t.basic) basic,(e.da-t.da) da,(e.hra-t.hra) hra,(e.add_hra-t.add_hra) ahra ,(e.incentive-t.incentive) incen "+
				",(e.spl_incentive-t.spl_incentive) splincen,t.atten_days,t.mnth_code,day(last_day(concat(left(t.mnth_code,4),'-',right(t.mnth_code,2),'-01'))) mdays," +
				"t.arrear_days,t.oldwages,(e.basic+e.da+e.hra+e.add_hra+e.incentive+e.spl_incentive) newwages,t.absent_days from employeemast e "+
				"left join  "+
				"(select emp_code,basic,da,hra,add_hra,incentive,spl_incentive,atten_days,mnth_code,if(mnth_code="+smon+",0,arrear_days) arrear_days,(basic+da+hra+add_hra+incentive+spl_incentive) oldwages,absent_days from emptran "+ 
				"where depo_code=? and cmp_code=? and atten_days<>0 and mnth_code between ? and ? group by emp_code,mnth_Code) t "+
				"on e.emp_Code=t.emp_code where e.depo_code=? and e.cmp_code=? and e.doresign is null and(t.basic-e.basic) is not null ) a"; 
*/
				// modified on 27/02/2026
				String query3="insert into arrear "+
				"select a.depo_code,a.cmp_code,a.emp_code,a.basic,a.da,a.hra,a.ahra,a.incen,a.splincen, "+
				"round((a.basic*(a.atten_days+a.arrear_days))/a.mdays) bvalue, "+
				"round((a.da*(a.atten_days+a.arrear_days))/a.mdays) davalue, "+
				"round((a.hra*(a.atten_days+a.arrear_days))/a.mdays) hravalue, "+
				"round((a.ahra*(a.atten_days+a.arrear_days))/a.mdays) ahravalue, "+
				"round((a.incen*(a.atten_days+a.arrear_days))/a.mdays) incenvalue, "+
				"round((a.splincen*(a.atten_days+a.arrear_days))/a.mdays) sincenvalue, "+
				"a.atten_days,"+cmon+","+smon+","+emon+",a.mnth_code,0.00,0.00,0.00,"+fyear+",a.oldwages,a.newwages,a.arrear_days,'N' arrearpaid,0.00 "+
				" from  "+
				"(select e.depo_code,e.cmp_code,e.emp_code,(e.basic-t.basic) basic,(e.da-t.da) da,(e.hra-t.hra) hra,(e.add_hra-t.add_hra) ahra ,(e.incentive-t.incentive) incen "+
				",(e.spl_incentive-t.spl_incentive) splincen,t.atten_days,t.mnth_code,day(last_day(concat(left(t.mnth_code,4),'-',right(t.mnth_code,2),'-01'))) mdays," +
				"t.arrear_days,t.oldwages,(e.basic+e.da+e.hra+e.add_hra+e.incentive+e.spl_incentive) newwages,t.absent_days from employeemast e "+
				"left join  "+
				"(select emp_code,basic,da,hra,add_hra,incentive,spl_incentive,atten_days,mnth_code,if(mnth_code="+smon+",0,arrear_days) arrear_days,(basic+da+hra+add_hra+incentive+spl_incentive) oldwages,absent_days from emptran "+ 
				"where depo_code=? and cmp_code=? and atten_days<>0 and mnth_code between ? and ? group by emp_code,mnth_Code) t "+
				"on e.emp_Code=t.emp_code where e.depo_code=? and e.cmp_code=? and e.doresign is null and(t.basic-e.basic) is not null ) a"; 

				
				
				// 27/04/23 change arreay days with  monthdays it should be 30 not more  cancel kar diya
/*				String query3="insert into arrear "+
				"select a.depo_code,a.cmp_code,a.emp_code,a.basic,a.da,a.hra,a.ahra,a.incen,a.splincen, "+
				"if(a.absent_days=0,(round(a.basic)+round((a.basic*if(a.mdays=a.arrear_days,30,a.arrear_days))/30)),round((a.basic+(a.basic*(a.arrear_days-a.absent_days)/30)))) bvalue, "+
				"if(a.absent_days=0,(round(a.da)+round((a.da*if(a.mdays=a.arrear_days,30,a.arrear_days))/30)),round((a.da+(a.da*(a.arrear_days-a.absent_days)/30)))) davalue, "+
				"if(a.absent_days=0,(round(a.hra)+round((a.hra*if(a.mdays=a.arrear_days,30,a.arrear_days))/30)),round((a.hra+(a.hra*(a.arrear_days-a.absent_days)/30)))) hravalue, "+
				"if(a.absent_days=0,(round(a.ahra)+round((a.ahra*if(a.mdays=a.arrear_days,30,a.arrear_days))/30)),round((a.ahra+(a.ahra*(a.arrear_days-a.absent_days)/30)))) ahravalue, "+
				"if(a.absent_days=0,(round(a.incen)+round((a.incen*if(a.mdays=a.arrear_days,30,a.arrear_days))/30)),round((a.incen+(a.incen*(a.arrear_days-a.absent_days)/30)))) incenvalue, "+
	 			"if(a.absent_days=0,(round(a.splincen)+round((a.splincen*if(a.mdays=a.arrear_days,30,a.arrear_days))/30)),round((a.splincen+(a.splincen*(a.arrear_days-a.absent_days)/30)))) sincenvalue, "+
				"a.atten_days,"+cmon+","+smon+","+emon+",a.mnth_code,0.00,0.00,0.00,"+fyear+",a.oldwages,a.newwages,a.arrear_days,'N' arrearpaid "+
				" from  "+
				"(select e.depo_code,e.cmp_code,e.emp_code,(e.basic-t.basic) basic,(e.da-t.da) da,(e.hra-t.hra) hra,(e.add_hra-t.add_hra) ahra ,(e.incentive-t.incentive) incen "+
				",(e.spl_incentive-t.spl_incentive) splincen,t.atten_days,t.mnth_code,day(last_day(concat(left(t.mnth_code,4),'-',right(t.mnth_code,2),'-01'))) mdays," +
				"t.arrear_days,t.oldwages,(e.basic+e.da+e.hra+e.add_hra+e.incentive+e.spl_incentive) newwages,t.absent_days from employeemast e "+
				"left join  "+
				"(select emp_code,basic,da,hra,add_hra,incentive,spl_incentive,atten_days,mnth_code,if(mnth_code="+smon+",0,arrear_days) arrear_days,(basic+da+hra+add_hra+incentive+spl_incentive) oldwages,absent_days from emptran "+ 
				"where depo_code=? and cmp_code=? and atten_days<>0 and mnth_code between ? and ? group by emp_code,mnth_Code) t "+
				"on e.emp_Code=t.emp_code where e.depo_code=? and e.cmp_code=? and e.doresign is null and(t.basic-e.basic) is not null ) a"; 
*/
				
				
				//String query2="update arrear set pf_value=round(round((basic_value+da_value+incen_value)*12/100,2)),esic_value=round((basic_value+da_value+hra_value+add_hra_value+incen_value+spl_incen_value)*1.75/100) "+ 
				//"where fin_year=? and depo_code=? and cmp_code=? ";
// AS PER NEW RULE OCT TO FEB ESIC CALCLULATION 27/02/2026				
/*				String query2="update arrear set pf_value=round(round((basic_value+da_value+incen_value)*12/100,2))," +
				"esic_value=round((basic_value+da_value+hra_value+add_hra_value+incen_value+spl_incen_value)*0.75/100)," +
				"employer_esic_value=round((basic_value+da_value+hra_value+add_hra_value+incen_value+spl_incen_value)*3.25/100) "+ 
				"where fin_year=? and depo_code=? and cmp_code=? ";
*/
				
				String query2="update arrear set pf_value=round(round((basic_value+da_value+incen_value)*12/100,2)) " +
				"where fin_year=? and depo_code=? and cmp_code=? ";

				
				
				if(cmon>2020044) // yashpal baghela 26/11/2020 
					 query2="update arrear set pf_value=round(round((basic_value+da_value+incen_value)*10/100,2)),esic_value=round((basic_value+da_value+hra_value+add_hra_value+incen_value+spl_incen_value)*0.75/100) "+ 
					 "where fin_year=? and depo_code=? and cmp_code=? ";

				
				System.out.println("fyear "+fyear+" arrear_mon "+cmon);
				
				String query4="update arrear a,emptran e set a.arrear_paid='Y' "+ 
				"where a.fin_year=? and a.depo_code=? and a.cmp_code=? and a.arrear_mon=? "+
				"and e.fin_year>=? and e.depo_code=? and e.cmp_code=? and a.emp_code=e.emp_Code and e.mnth_code=? and e.atten_days<>0 ";

				// prof tax calculation 2024-04-18 in arrear file
				String query5="update arrear set prof_tax = "+
						" if(oldwages>=18750,0,if(oldwages+basic+da+hra+add_hra+incen>18750 "+ 
						" and (oldwages+basic+da+hra+add_hra+incen)<25000 ,125,"+
						" if(oldwages+basic+da+hra+add_hra+incen>=25000"+ 
						" and (oldwages+basic+da+hra+add_hra+incen)<33333,if(right(mnth_code,2)=03,174,166), "+
						" if(oldwages+basic+da+hra+add_hra+incen>=25000,if(right(mnth_code,2)=03,212,208),0)))) "+ 
// 27/02/2026			" esic_value=if(oldwages+basic+da+hra+add_hra+incen>21000,0.00,round((basic_value+da_value+hra_value+add_hra_value+incen_value+spl_incen_value)*0.75/100)) "+
						" where fin_year=? and depo_code=? and cmp_code=? and arrear_mon=?";
				
				
				String query6="UPDATE ARREAR SET " +
				"ESIC_VALUE=round((IFNULL(basic_value,0)+IFNULL(da_value,0)+IFNULL(hra_value,0)+IFNULL(add_hra_value,0)+IFNULL(incen_value,0)+IFNULL(spl_incen_value,0))*0.0075), "+
				"employer_esic_value=round((IFNULL(basic_value,0)+IFNULL(da_value,0)+IFNULL(hra_value,0)+IFNULL(add_hra_value,0)+IFNULL(incen_value,0)+IFNULL(spl_incen_value,0))*0.0325) "+
				" WHERE FIN_YEAR=? AND CMP_CODE=? AND DEPO_cODE=? AND EMP_cODE=?";
				
				
				String query7="select emp_code,sum(esis_value) esis_value"+  
						" from emptran where fin_year=? AND CMP_CODE=? and mnth_code between ? and ? group by emp_code" ;
	 			 
				ps7=con.prepareStatement(query7);

				ps2=con.prepareStatement(query2);

				
				ps6 = con.prepareStatement(query6);


				ps1 = con.prepareStatement(query1);
				ps1.setInt(1, fyear);
				ps1.setInt(2, depo); 
				ps1.setInt(3, cmp);
				ps1.setInt(4, cmon);
				i=ps1.executeUpdate();

				
				ps3 = con.prepareStatement(query3);
				ps3.setInt(1, depo);
				ps3.setInt(2, cmp);
				ps3.setInt(3, smon);
				ps3.setInt(4, emon);
				ps3.setInt(5, depo);
				ps3.setInt(6, cmp);
				i=ps3.executeUpdate();
				
				
				ps2 = con.prepareStatement(query2);
				ps2.setInt(1, fyear);
				ps2.setInt(2, depo);
				ps2.setInt(3, cmp);
				i=ps2.executeUpdate();
				System.out.println("NO OF RECORD UPDATED IN QUERY2 "+i+" fyear "+fyear+" depo "+depo+" cmp "+cmp);
	 
				ps4 = con.prepareStatement(query4);
				ps4.setInt(1, fyear);
				ps4.setInt(2, depo);
				ps4.setInt(3, cmp);
				ps4.setInt(4, cmon);
				ps4.setInt(5, fyear);
				ps4.setInt(6, depo);
				ps4.setInt(7, cmp);
				ps4.setInt(8, cmon);
				i=ps4.executeUpdate();

				ps5 = con.prepareStatement(query5);
				ps5.setInt(1, fyear);
				ps5.setInt(2, depo); 
				ps5.setInt(3, cmp);
				ps5.setInt(4, cmon);
				i=ps5.executeUpdate();

				
				
				ps7.setInt(1, fyear);
				ps7.setInt(2, cmp);
				ps7.setInt(3, smon);
				ps7.setInt(4, emon);
				rs7=ps7.executeQuery();
				while(rs7.next())
				{
					if(rs7.getDouble(2)>0)
					{
						ps6.setInt(1, fyear);
						ps6.setInt(2, cmp);
						ps6.setInt(3, depo);
						ps6.setInt(4, rs7.getInt(1));
						i=ps6.executeUpdate();
						
					}
				}
				
				con.commit();
				con.setAutoCommit(true);
				ps3.close();
				ps2.close();
				ps1.close();
				ps4.close();
				ps5.close();
				ps6.close();
				rs7.close();
				ps7.close();
				
				
			} catch (SQLException ex) {
				ex.printStackTrace();
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.out.println("-------------Exception in PayrollDAO.arrearGeneration " + ex);
				i=-1;
			}
			finally {
				try {
					System.out.println("No. of Records Update/Insert : "+i);

					if(ps3 != null){ps3.close();}
					if(ps2 != null){ps2.close();}
					if(ps1 != null){ps1.close();}
					if(ps4 != null){ps4.close();}
					if(ps5 != null){ps5.close();}
					if(ps6 != null){ps6.close();}
					if(rs7 != null){rs7.close();}
					if(ps7 != null){ps7.close();}
					if(con != null){con.close();}
				} catch (SQLException e) {
					System.out.println("-------------Exception in PayrollDAO.Connection.close "+e);
				}
			}
			return i;
		}	   

	   
		public Vector getArrearList(int depo_code,int cmp_code,int fyear,int mnth_code,int option)
		{
			PreparedStatement ps = null;
			ResultSet rs=null;
			Connection con=null;
			Vector v =null;
			Vector col=null;
			int sno=1;
			String mon="b.arrear_mon";
			if(option==2)
				mon="b.mnth_code";
			try 
			{
				con=ConnectionFactory.getConnection();
				con.setAutoCommit(false);

				String query="select e.emp_name,e.esic_no,e.pf_no,e.emp_code,(b.basic_value+b.da_value+b.hra_value+b.add_hra_value+b.incen_value+b.spl_incen_value) arreara_amt,"+mon+
				" from employeemast e, arrear b "+  
				" where  e.cmp_code=b.cmp_code and e.emp_code=b.emp_code and b.fin_year=? and b.depo_code=? and b.cmp_code=? "+   
				" and e.depo_code=? and e.cmp_code=? and "+mon+"=? group by arrear_mon,emp_code ";

				ps = con.prepareStatement(query);
				ps.setInt(1, fyear);
				ps.setInt(2, depo_code);
				ps.setInt(3, cmp_code);
				ps.setInt(4, depo_code);
				ps.setInt(5, cmp_code);
				ps.setInt(6, mnth_code);
				rs =ps.executeQuery();
				
				
				boolean first=true;
				while (rs.next())
				{
					if(first)
					{
						v = new Vector();
						first=false;
					}
					col= new Vector();
					col.add(sno++);  // sno 0
					col.add(rs.getString(1)); // emp_name //1
					col.add(rs.getString(2)); // esic no  2
					col.add(rs.getString(3)); // pf no  3
					col.add(rs.getInt(4)); // emp_code  4
					col.add(rs.getDouble(5)); // arrear amt  5
					col.add(rs.getDouble(5)); // arrear amt (sanction) 6
					col.add(rs.getInt(6)); // mnth_code  7
					v.add(col);
				}



				con.commit();
				con.setAutoCommit(true);
				rs.close();
				ps.close();

			} catch (Exception ex) { ex.printStackTrace();
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.out.println("-------------Exception in PayrollDAO.getArrearList " + ex);

			}
			finally {
				try {
					System.out.println("No. of Records Update/Insert : " );

					if(rs != null){rs.close();}
					if(ps != null){ps.close();}
					if(con != null){con.close();}
				} catch (SQLException e) {
					System.out.println("-------------Exception in PayrollDAO.Connection.close --- getArrearList "+e);
				}
			}
			return v;
		}


	   
	   
		public Vector getPendingArrearList(int depo_code,int cmp_code,int ayear,int mnth_code,int fyear,String paid)
		{
			PreparedStatement ps = null;
			ResultSet rs=null;
			Connection con=null;
			Vector v =null;
			Vector col=null;
			int sno=1;
			String mon="b.arrear_mon";
			
			int smon = Integer.parseInt(ayear+"04");
			int emon = Integer.parseInt((ayear+1)+"03");
			try 
			{
				con=ConnectionFactory.getConnection();
				con.setAutoCommit(false);

/*				String query="select e.emp_name,e.esic_no,e.pf_no,e.emp_code,(b.basic_value+b.da_value+b.hra_value+b.add_hra_value+b.incen_value+b.spl_incen_value) arreara_amt,"+mon+
				" from employeemast e, arrear b "+  
				" where  e.cmp_code=b.cmp_code and e.emp_code=b.emp_code and b.fin_year=? and b.depo_code=? and b.cmp_code=? "+   
				" and e.depo_code=? and e.cmp_code=? and "+mon+"=? group by arrear_mon,emp_code ";
*/				
				
/*				String query = "select e.emp_name,monthname(concat(left(a.mnth_code,4),'-',right(a.mnth_code,2),'-01')) mon,e.esic_no,e.pf_no,a.emp_code, "+ 
				"(a.basic_value+a.da_value+a.hra_value+a.add_hra_value+a.incen_Value+a.spl_incen_value)-(a.pf_value+a.esic_value) net,a.mnth_code,a.arrear_paid "+
				"from arrear a, employeemast e "+
				"where a.fin_year=? and a.depo_code=? and a.cmp_code=? and a.mnth_code between ? and ? "+ 
				"and e.depo_code=? and e.cmp_code=? and a.emp_code=e.emp_code and a.arrear_paid=? ";
	*/			
				
				//Query implemented on 07/07/2020 (Yashpal) 
/*				String query = "select e.emp_name,monthname(concat(left(a.mnth_code,4),'-',right(a.mnth_code,2),'-01')) mon,e.esic_no,e.pf_no,a.emp_code, "+
				"(a.basic_value+a.da_value+a.hra_value+a.add_hra_value+a.incen_Value+a.spl_incen_value)-(a.pf_value+a.esic_value) net,a.mnth_code,a.arrear_paid  "+
				"from arrear a, employeemast e,emptran t  "+
				"where a.fin_year=? and a.depo_code=? and a.cmp_code=? and a.mnth_code between ? and ? "+
				"and e.depo_code=? and e.cmp_code=? and a.emp_code=e.emp_code and a.arrear_paid in(?,'N') "+
				"and t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and t.atten_days>0 and t.atten_lock=1  and t.emp_code=e.emp_code and t.emp_code=a.emp_code "+
				"and ifnull(t.del_tag,'')<>'D' ";
*/
				//New Query implemented on 27/02/2026 (Yashpal) 
/*				String query = "select e.emp_name," +
				"DATE_FORMAT(CONCAT(LEFT(a.mnth_code,4),'-',RIGHT(a.mnth_code,2),'-01'),'%M-%Y') AS MON," +
				"e.esic_no,e.pf_no,a.emp_code, "+
				"(a.basic_value+a.da_value+a.hra_value+a.add_hra_value+a.incen_Value+a.spl_incen_value)-(a.pf_value+a.esic_value) net,a.mnth_code,a.arrear_paid  "+
				"from arrear a, employeemast e,emptran t  "+
				"where a.fin_year=? and a.depo_code=? and a.cmp_code=? and a.mnth_code between ? and ? "+
				"and e.depo_code=? and e.cmp_code=? and a.emp_code=e.emp_code and a.arrear_paid =?  "+
				"and t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=?  and t.atten_lock=1  and t.emp_code=e.emp_code and t.emp_code=a.emp_code "+
				"and ifnull(t.del_tag,'')<>'D' ";
*/
				
				//New Query implemented on 15/04/2026 (Yashpal) 
				String query = "select concat(e.emp_name,' ',if(doresign is null,' ','- Resigned')) emp_name," +
				"DATE_FORMAT(CONCAT(LEFT(a.mnth_code,4),'-',RIGHT(a.mnth_code,2),'-01'),'%M-%Y') AS MON," +
				"e.esic_no,e.pf_no,a.emp_code, "+
				"(a.basic_value+a.da_value+a.hra_value+a.add_hra_value+a.incen_Value+a.spl_incen_value)-(a.pf_value+a.esic_value) net,a.mnth_code,a.arrear_paid  "+
				"from arrear a, employeemast e,emptran t  "+
				"where a.fin_year<=? and a.depo_code=? and a.cmp_code=? " +
//				"and a.mnth_code between ? and ? "+
				"and e.depo_code=? and e.cmp_code=? and a.emp_code=e.emp_code and a.arrear_paid =?  "+
				"and t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=?  and t.atten_lock=1  and t.emp_code=e.emp_code and t.emp_code=a.emp_code "+
				"and ifnull(t.del_tag,'')<>'D' having net > 0 order by a.emp_code,a.mnth_code desc ";

				
				
				System.out.println(ayear+" "+depo_code+" "+cmp_code+" "+smon+" "+emon+" "+paid+" "+fyear+" ");
				
				ps = con.prepareStatement(query);
				ps.setInt(1, ayear); 
				ps.setInt(2, depo_code);
				ps.setInt(3, cmp_code);
//				ps.setInt(4, smon);
//				ps.setInt(5, emon);
				ps.setInt(4, depo_code);
				ps.setInt(5, cmp_code);
				ps.setString(6, paid);
				ps.setInt(7, fyear);
				ps.setInt(8, depo_code);
				ps.setInt(9, cmp_code);
				ps.setInt(10, mnth_code);
				rs =ps.executeQuery();
				

				
				boolean first=true;
				while (rs.next())
				{
					if(first)
					{
						v = new Vector();
						first=false;
					}
					col= new Vector();
					if(paid.equals("O"))
						col.addElement(new Boolean(true));
					else
						col.addElement(new Boolean(false));
					col.addElement(sno++);  // sno 1
					col.addElement(rs.getString(1)); // emp_name //2
					col.addElement(rs.getString(2)); // month name  3
					col.addElement(rs.getString(3)); // esic no  4
					col.addElement(rs.getString(4)); // pf no  5
					col.addElement(rs.getInt(5)); // emp_code  6
					col.addElement(rs.getDouble(6)); // arrear amt  7
					col.addElement(rs.getDouble(6)); // arrear amt (sanction) 8
					col.addElement(rs.getInt(7)); // mnth_code  9
					col.addElement(rs.getString(8)); // paid 10
					v.add(col);
				}



				con.commit();
				con.setAutoCommit(true);
				rs.close();
				ps.close();

			} catch (Exception ex) { ex.printStackTrace();
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.out.println("-------------Exception in PayrollDAO.getArrearList " + ex);

			}
			finally {
				try {
					System.out.println("No. of Records Update/Insert : " );

					if(rs != null){rs.close();}
					if(ps != null){ps.close();}
					if(con != null){con.close();}
				} catch (SQLException e) {
					System.out.println("-------------Exception in PayrollDAO.Connection.close --- getArrearList "+e);
				}
			}
			return v;
		}

		   public ArrayList getArrearReport(int depo_code,int cmp_code,int fyear,String paid,int smon,int emon)
		  	{
		  		PreparedStatement ps = null;
		  		ResultSet rs=null;
		  		Connection con=null;
		  		ArrayList v =null;
		  		EmptranDto emp=null;
		  		double days=0.00;
		  		double basic=0.00;
		  		double da=0.00;
		  		double hra=0.00;
		  		double addhra=0.00;
		  		double incen=0.00;
		  		double spincen=0.00;
		  		double totearn=0.00;
		  		double pf=0.00;
		  		double esic=0.00;
		  		double totded=0.00;
		  		double adays=0.00; 
		  		double proftax=0.00;
		  		double gdays=0.00;
		  		double gbasic=0.00;
		  		double gda=0.00;
		  		double ghra=0.00;
		  		double gaddhra=0.00;
		  		double gincen=0.00;
		  		double gspincen=0.00;
		  		double gtotearn=0.00;
		  		double gpf=0.00;
		  		double gesic=0.00;
		  		double gtotded=0.00;
		  		double gadays=0.00; 
		  		double gproftax=0.00;
		  		
		  		 
		  		 
		  		try 
		  		{
		  			con=ConnectionFactory.getConnection();
		  			con.setAutoCommit(false);
		  		 

		  			String query=" select a.emp_code,e.emp_name,monthname(concat(left(a.mnth_code,4),'-',right(a.mnth_code,2),'-01')) mon,a.arrear_atten,a.basic_value,a.da_value, "+
					"a.hra_value,a.add_hra_value,a.incen_Value,a.spl_incen_value,(a.basic_value+a.da_value+a.hra_value+a.add_hra_value+a.incen_Value+a.spl_incen_value) totearn, "+
					"a.pf_value,a.esic_value,(a.pf_value+a.esic_value+a.prof_tax) totded,a.arrear_days,a.oldwages,a.newwages,a.prof_tax "+
					"from arrear a, employeemast e "+
					"where a.fin_year=? and a.depo_code=? and a.cmp_code=? and a.mnth_code between ? and ? "+
					"and e.depo_code=? and e.cmp_code=? and a.emp_code=e.emp_code and a.arrear_paid in (?,?) ";

		  			 
		  			ps = con.prepareStatement(query);
		  			ps.setInt(1, fyear);
	  	  			ps.setInt(2, depo_code);
	  	  			ps.setInt(3, cmp_code);
	  	  			ps.setInt(4, smon);
	  	  			ps.setInt(5, emon);
	  	  			ps.setInt(6, depo_code);
	  	  			ps.setInt(7, cmp_code);
	  	  			if(paid.equalsIgnoreCase("Y"))
	  	  			{
		  	  			ps.setString(8, paid);
		  	  			ps.setString(9, "X");
	  	  				
	  	  			}
	  	  			else
	  	  			{
		  	  			ps.setString(8, paid);
		  	  			ps.setString(9, "N");
	  	  				
	  	  			}
	  	  			ps.setString(8, paid);
			  		rs =ps.executeQuery();
		  			
		  			v = new ArrayList();
		  			int sno=1;
		  			boolean first=true;
		  			int empcode=0;
		  			while (rs.next())
		  			{
		  				
		  				if(first)
		  				{
		  					empcode=rs.getInt(1);
		  				}
		  				
		  				if(empcode!=rs.getInt(1))
		  				{
			  				emp = new EmptranDto();
			  				emp.setEmp_code(-1);  // ip number
			  				emp.setEmp_name(""); // ip name
			  				emp.setMonname("Total");
			  				emp.setAtten_days(days);
			  				emp.setBasic_value(basic);
			  				emp.setDa_value(da);
			  				emp.setHra_value(hra);
			  				emp.setAdd_hra_value(addhra);
			  				emp.setIncentive_value(incen);
			  				emp.setSpl_incen_value(spincen);
			  				emp.setTds_value(totearn); //tot earn
			  				emp.setPf_value(pf);
			  				emp.setEsis_value(esic);
			  				emp.setProf_tax(proftax);
			  				emp.setMisc_value(totded); // tot ded
			  				emp.setNet_value(totearn-totded); // tot payable
			  				emp.setArrear_days(adays);
			  				v.add(emp);
					  		days=0.00;
					  		basic=0.00;
					  		da=0.00;
					  		hra=0.00;
					  		addhra=0.00;
					  		incen=0.00;
					  		spincen=0.00;
					  		totearn=0.00;
					  		pf=0.00;
					  		esic=0.00;
					  		totded=0.00;
					  		adays=0.00; 
					  		proftax=0.00;
		  					first=true;
		  					empcode=rs.getInt(1);
		  					
		  				}
		  				emp = new EmptranDto();
		  				if(first)
		  				{
			  				emp.setEmp_code(rs.getInt(1));  // ip number
			  				emp.setEmp_name(rs.getString(2)); // ip name
		  				}
		  				else
		  				{
			  				emp.setEmp_code(0);  // ip number
			  				emp.setEmp_name(""); // ip name
		  				}
		  				emp.setMonname(rs.getString(3));
		  				emp.setAtten_days(rs.getDouble(4));
		  				emp.setBasic_value(rs.getDouble(5));
		  				emp.setDa_value(rs.getDouble(6));
		  				emp.setHra_value(rs.getDouble(7));
		  				emp.setAdd_hra_value(rs.getDouble(8));
		  				emp.setIncentive_value(rs.getDouble(9));
		  				emp.setSpl_incen_value(rs.getDouble(10));
		  				emp.setTds_value(rs.getDouble(11)); //tot earn
		  				emp.setPf_value(rs.getDouble(12));
		  				emp.setEsis_value(rs.getDouble(13));
		  				emp.setMisc_value(rs.getDouble(14)); // tot ded
		  				emp.setNet_value(rs.getDouble(11)-rs.getDouble(14)); // tot payable
		  				emp.setArrear_days(rs.getDouble(15));
		  				emp.setOldwages(rs.getDouble(16));
		  				emp.setNewwages(rs.getDouble(17));
		  				emp.setProf_tax(rs.getDouble(18));
		  				
		  				v.add(emp);
		  				days+=rs.getDouble(4);
				  		basic+=rs.getDouble(5);
				  		da+=rs.getDouble(6);
				  		hra+=rs.getDouble(7);
				  		addhra+=rs.getDouble(8);
				  		incen+=rs.getDouble(9);
				  		spincen+=rs.getDouble(10);
				  		totearn+=rs.getDouble(11);
				  		pf+=rs.getDouble(12);
				  		esic+=rs.getDouble(13);
				  		totded+=rs.getDouble(14);
				  		adays+=rs.getDouble(15);
				  		proftax+=rs.getDouble(18);
		  				
				  		gdays+=rs.getDouble(4);
				  		gbasic+=rs.getDouble(5);
				  		gda+=rs.getDouble(6);
				  		ghra+=rs.getDouble(7);
				  		gaddhra+=rs.getDouble(8);
				  		gincen+=rs.getDouble(9);
				  		gspincen+=rs.getDouble(10);
				  		gtotearn+=rs.getDouble(11);
				  		gpf+=rs.getDouble(12);
				  		gesic+=rs.getDouble(13);
				  		gtotded+=rs.getDouble(14);
				  		gadays+=rs.getDouble(15);
				  		gproftax+=rs.getDouble(18);

				  		first=false;

		  				
		  			}
		  			emp = new EmptranDto();
	  				emp.setEmp_code(-1);  // ip number
	  				emp.setEmp_name(""); // ip name
	  				emp.setMonname("Total");
	  				emp.setAtten_days(days);
	  				emp.setBasic_value(basic);
	  				emp.setDa_value(da);
	  				emp.setHra_value(hra);
	  				emp.setAdd_hra_value(addhra);
	  				emp.setIncentive_value(incen);
	  				emp.setSpl_incen_value(spincen);
	  				emp.setTds_value(totearn); //tot earn
	  				emp.setPf_value(pf);
	  				emp.setEsis_value(esic);
	  				emp.setProf_tax(proftax);
	  				emp.setMisc_value(totded); // tot ded
	  				emp.setNet_value(totearn-totded); // tot payable
	  				emp.setArrear_days(adays);
	  				v.add(emp);

	  				emp = new EmptranDto();
	  				emp.setEmp_code(-1);  // ip number
	  				emp.setEmp_name(""); // ip name
	  				emp.setMonname("Grand Total");
	  				emp.setAtten_days(gdays);
	  				emp.setBasic_value(gbasic);
	  				emp.setDa_value(gda);
	  				emp.setHra_value(ghra);
	  				emp.setAdd_hra_value(gaddhra);
	  				emp.setIncentive_value(gincen);
	  				emp.setSpl_incen_value(gspincen);
	  				emp.setTds_value(gtotearn); //tot earn
	  				emp.setPf_value(gpf);
	  				emp.setEsis_value(gesic);
	  				emp.setProf_tax(gproftax);
	  				emp.setMisc_value(gtotded); // tot ded
	  				emp.setNet_value(gtotearn-gtotded); // tot payable
	  				emp.setArrear_days(gadays);
	  				v.add(emp);


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
		  			System.out.println("-------------Exception in PayrollDAO.getArrearReport " + ex);

		  		}
		  		finally {
		  			try {
		  				System.out.println("No. of Records Update/Insert : " );

		  				if(rs != null){rs.close();}
		  				if(ps != null){ps.close();}
		  				if(con != null){con.close();}
		  			} catch (SQLException e) {
		  				System.out.println("-------------Exception in PayrollDAO.Connection.close --- getArrearReport "+e);
		  			}
		  		}
		  		return v;
		  	}    	   
		
		   public int updateArrearList(ArrayList<?> attnlist)
		    {
		  	  
		    	PreparedStatement ps1 = null;
		    	PreparedStatement ps2 = null;
		    	Connection con=null;
		    	EmptranDto emp=null;
				double bonusamt=0.00;
				int i=0;
				try 
				{
					con=ConnectionFactory.getConnection();

					
					String query1="update arrear set arrear_paid=?,arrear_mon=? " +
					" where  fin_year<=? and depo_code=? and cmp_code=? and  mnth_code=? and emp_code=? and arrear_paid in ('N','O') " ;

					//String query2="update emptran set bonus_per=?,bonus_value=? " +
					//" where  fin_year=? and depo_code=? and cmp_code=? and  mnth_code=? and emp_code=? " ;
				
					con.setAutoCommit(false);
					
					ps1 = con.prepareStatement(query1);
					//ps2 = con.prepareStatement(query2);
					
					int s=attnlist.size();
					for (int j=0;j<s;j++)
					{
						emp= (EmptranDto) attnlist.get(j);

/*						System.out.println(emp.getFin_year());
						System.out.println(emp.getDepo_code());
						System.out.println(emp.getCmp_code());
						System.out.println(emp.getMnth_code());
						System.out.println(emp.getEmp_code());
						System.out.println("paid = "+emp.getPaid());
*/						
						
						ps1.setString(1, emp.getPaid()); // Paid 'N' or 'O'
						ps1.setInt(2, emp.getMnthdays()); // arrear month in which arrear is paid
						 
						// where clause
						ps1.setInt(3,emp.getFin_year());
						ps1.setInt(4,emp.getDepo_code());
						ps1.setInt(5,emp.getCmp_code());
						ps1.setInt(6,emp.getMnth_code());
						ps1.setInt(7,emp.getEmp_code());
						i=ps1.executeUpdate();
						
						
/*						ps2.setDouble(1, emp.getBonus_per());
						ps2.setDouble(2, bonusamt); // bonusamt  
						// where clause
						ps2.setInt(3,emp.getFin_year());
						ps2.setInt(4,emp.getDepo_code());
						ps2.setInt(5,emp.getCmp_code());
						ps2.setInt(6,emp.getMnth_code());
						ps2.setInt(7,emp.getEmp_code());
						i=ps2.executeUpdate();
*/						
/*						if(i>0)
						{
							ps1.setDouble(1, emp.getBonus_per());
							ps1.setDouble(2, emp.getBonus_value()); // bonus_limit  
							ps1.setDouble(3, bonusamt); // bonusamt  
							ps1.setInt(4,emp.getMnth_code());
							// where clause
							ps1.setInt(5,emp.getFin_year());
							ps1.setInt(6,emp.getDepo_code());
							ps1.setInt(7,emp.getCmp_code());
							ps1.setInt(8,emp.getMnth_code());
							ps1.setInt(9,emp.getEmp_code());
							i=ps1.executeUpdate();
						}
*/				  			
					}
					con.commit();
					con.setAutoCommit(true);
					ps1.close();
//					ps2.close();
					
				} catch (SQLException ex) {
					ex.printStackTrace();
					try {
						con.rollback();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("-------------Exception in PayrollDAO.updateBonusList " + ex);
					i=-1;
				}
				finally {
					try {
						System.out.println("No. of Records Update/Insert : "+i);

						if(ps1 != null){ps1.close();}
//						if(ps2 != null){ps2.close();}
						if(con != null){con.close();}
					} catch (SQLException e) {
						System.out.println("-------------Exception in PayrollDAO.Connection.close "+e);
					}
				}
				return i;
			}	

		   
		   
		   
		   public int ltaGeneration(int fyear,int depo,int cmp,int mcode,double lta_value,double medical_value)
		    {
		  	  
		    	PreparedStatement ps1 = null;
		    	Connection con=null;
				int i=0;
				try 
				{
					con=ConnectionFactory.getConnection();
					con.setAutoCommit(false);

					
					String query1="update emptran set lta_value=?,medical_value=?  "+
					" where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? and atten_days>0 ";
					
					ps1 = con.prepareStatement(query1);
					ps1.setDouble(1, 0.00);
					ps1.setDouble(2, 0.00);
					// where clause 
					ps1.setInt(3,fyear);
					ps1.setInt(4,depo);
					ps1.setInt(5,cmp);
					ps1.setInt(6,mcode);
					
					i=ps1.executeUpdate();
					
					ps1.setDouble(1, lta_value);
					ps1.setDouble(2, medical_value);
					// where clause 
					ps1.setInt(3,fyear);
					ps1.setInt(4,depo);
					ps1.setInt(5,cmp);
					ps1.setInt(6,mcode);
					
					i=ps1.executeUpdate();
					
					con.commit();
					con.setAutoCommit(true);
					ps1.close();
					
				} catch (SQLException ex) {
					ex.printStackTrace();
					try {
						con.rollback();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("-------------Exception in PayrollDAO.ltaGeneration " + ex);
					i=-1;
				}
				finally {
					try {
						System.out.println("No. of Records Update/Insert : "+i);

						if(ps1 != null){ps1.close();}
						if(con != null){con.close();}
					} catch (SQLException e) {
						System.out.println("-------------Exception in PayrollDAO.Connection.close "+e);
					}
				}
				return i;
			}
		   
		   
			public Vector getLTAList(int depo_code,int cmp_code,int fyear,int mnth_code,int option)
			{
				PreparedStatement ps = null;
				ResultSet rs=null;
				PreparedStatement ps1 = null;
				ResultSet rs1=null;
				Connection con=null;
				Vector v =null;
				Vector col=null;
				int sno=1;
				int lock=0;
				try 
				{
					con=ConnectionFactory.getConnection();
					con.setAutoCommit(false);

					String query="select e.emp_name,e.esic_no,e.pf_no,e.emp_code,b.lta_value,b.medical_value,b.mnth_code,MONTHNAME(STR_TO_DATE(right(b.mnth_code,2), '%m')) mon "+
					" from employeemast e, emptran b  "+
					" where  e.cmp_code=b.cmp_code and e.emp_code=b.emp_code and b.fin_year=? and b.depo_code=? and b.cmp_code=?  " +
					"  and e.depo_code=? and e.cmp_code=? and b.mnth_code=? and b.atten_days>0 ";

					if(option==2)
					{
						query="select e.emp_name,e.esic_no,e.pf_no,e.emp_code,b.lta_value,b.medical_value,b.mnth_code,MONTHNAME(STR_TO_DATE(right(b.mnth_code,2), '%m')) mon "+
						" from employeemast e, emptran b  "+
						" where  e.cmp_code=b.cmp_code and e.emp_code=b.emp_code and b.fin_year=? and b.depo_code=? and b.cmp_code=?  " +
						"  and e.depo_code=? and e.cmp_code=?  and b.atten_lock=1 and b.mnth_code=? ";
//						"  and e.depo_code=? and e.cmp_code=? and (b.lta_value+b.medical_value)>0 ";
					}

					
					String query1="select locked from locktable  where depo_Code=? and cmp_code=? and mnth_code=? and entry_type=? ";
					ps1 = con.prepareStatement(query1);
					ps1.setInt(1, depo_code);
					ps1.setInt(2, cmp_code);
					ps1.setInt(3, mnth_code);
					ps1.setInt(4, option);
					rs1 =ps1.executeQuery();
					if(rs1.next())
					{
						lock=rs1.getInt(1);
					}
					rs1.close();
					ps1.close();
					
					ps = con.prepareStatement(query);
					ps.setInt(1, fyear);
					ps.setInt(2, depo_code);
					ps.setInt(3, cmp_code);
					ps.setInt(4, depo_code);
					ps.setInt(5, cmp_code);
					if(option==5 || option==2)
						ps.setInt(6, mnth_code);
					rs =ps.executeQuery();
					
					boolean first=true;
					while (rs.next())
					{
					
						if(first)
						{
							v = new Vector();
							first=false;
						}
						
						col= new Vector();
						col.add(sno++);  // sno 0
						col.add(rs.getString(1)); // emp_name //1
						col.add(rs.getString(2)); // esic no  2
						col.add(rs.getString(3)); // pf no  3
						col.add(rs.getInt(4)); // emp_code  4
						col.add(rs.getDouble(5)); // lta value  5
						col.add(rs.getDouble(6)); // medical value  6
						col.add(rs.getInt(7)); // mnth_code  7
						col.add(rs.getString(8)); // mnth name 8
						col.add(lock); // lock 9
						v.add(col);
					}



					con.commit();
					con.setAutoCommit(true);
					rs.close();
					ps.close();

				} catch (Exception ex) { ex.printStackTrace();
					try {
						con.rollback();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("-------------Exception in PayrollDAO.getLTAList " + ex);

				}
				finally {
					try {
						System.out.println("No. of Records Update/Insert : " );

						if(rs != null){rs.close();}
						if(ps != null){ps.close();}
						if(con != null){con.close();}
					} catch (SQLException e) {
						System.out.println("-------------Exception in PayrollDAO.Connection.close --- getLTAList "+e);
					}
				}
				return v;
			}
		   
		   

		    public ArrayList getLTAList(int depo_code,int cmp_code,int fyear,int repno)
		  	{
		  		PreparedStatement ps = null;
		  		ResultSet rs=null;
		  		Connection con=null;
		  		ArrayList v =null;
		  		EmptranDto emp=null;
		  		int sno=1;
		  		try 
		  		{
		  			con=ConnectionFactory.getConnection();
		  			con.setAutoCommit(false);

					String query="select e.emp_code,e.emp_name,b.lta_value,b.medical_value,MONTHNAME(STR_TO_DATE(right(b.mnth_code,2), '%m')) mon "+
					" from employeemast e, emptran b  "+
					" where  e.cmp_code=b.cmp_code and e.emp_code=b.emp_code and b.fin_year=? and b.depo_code=? and b.cmp_code=?  " +
					"  and e.depo_code=? and e.cmp_code=? and (b.lta_value+b.medical_value)>0 ";

					if(repno==14)
					{
						query="select emp_code,emp_name,0.00 lta,0.00 medical,'UnPaid' mon from employeemast where depo_code=? and cmp_code=? and emp_code not in "+  
						"(select emp_code from emptran  where fin_year=? and depo_code=? and cmp_code=? and (lta_value+medical_value)>0) and doresign is null and (lta+medical)>0 ";
					}
		  			 
		  			ps = con.prepareStatement(query);
		  			if(repno==14)
		  			{
		  				ps.setInt(1, depo_code);
		  				ps.setInt(2, cmp_code);
		  				ps.setInt(3, fyear);
		  				ps.setInt(4, depo_code);
		  				ps.setInt(5, cmp_code);
		  			}
		  			else
		  			{
		  				ps.setInt(1, fyear);
		  				ps.setInt(2, depo_code);
		  				ps.setInt(3, cmp_code);
		  				ps.setInt(4, depo_code);
		  				ps.setInt(5, cmp_code);
		  			}
		  			rs =ps.executeQuery();
		  			
		  			v = new ArrayList();
		  			while (rs.next())
		  			{
		  				
		  				emp = new EmptranDto();
		  				emp.setSerialno(sno++);
		  				emp.setEmp_code(rs.getInt(1));
		  				emp.setEmp_name(rs.getString(2)); // ip name
		  				emp.setLta_value(rs.getDouble(3));
		  				emp.setMedical_value(rs.getDouble(4));
		  				emp.setMonname(rs.getString(5));
		  				v.add(emp);
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
		  			System.out.println("-------------Exception in PayrollDAO.getLTAList(ArrayList) " + ex);

		  		}
		  		finally {
		  			try {
		  				System.out.println("No. of Records Update/Insert : " );

		  				if(rs != null){rs.close();}
		  				if(ps != null){ps.close();}
		  				if(con != null){con.close();}
		  			} catch (SQLException e) {
		  				System.out.println("-------------Exception in PayrollDAO.Connection.close --- getLTAList(ArrayList) "+e);
		  			}
		  		}
		  		return v;
		  	}    

			
			
			   public int updateLTAList(ArrayList<?> attnlist)
			    {
			  	  
			    	PreparedStatement ps2 = null;
			    	Connection con=null;
			    	EmptranDto emp=null;
					double bonusamt=0.00;
					int i=0;
					try 
					{
						con=ConnectionFactory.getConnection();

						String query2="update emptran set lta_value=?,medical_value=? " +
						" where  fin_year=? and depo_code=? and cmp_code=? and  mnth_code=? and emp_code=? " ;
					
						con.setAutoCommit(false);
						
						ps2 = con.prepareStatement(query2);
						
						int s=attnlist.size();
						for (int j=0;j<s;j++)
						{
							emp= (EmptranDto) attnlist.get(j);
							
							ps2.setDouble(1, emp.getLta_value());
							ps2.setDouble(2, emp.getMedical_value()); // medical value  
							// where clause
							ps2.setInt(3,emp.getFin_year());
							ps2.setInt(4,emp.getDepo_code());
							ps2.setInt(5,emp.getCmp_code());
							ps2.setInt(6,emp.getMnth_code());
							ps2.setInt(7,emp.getEmp_code());
							i=ps2.executeUpdate();
							
									
						}
						con.commit();
						con.setAutoCommit(true);
						ps2.close();
						
					} catch (SQLException ex) {
						ex.printStackTrace();
						try {
							con.rollback();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						System.out.println("-------------Exception in PayrollDAO.updateLTAList " + ex);
						i=-1;
					}
					finally {
						try {
							System.out.println("No. of Records Update/Insert : "+i);
							if(ps2 != null){ps2.close();}
							if(con != null){con.close();}
						} catch (SQLException e) {
							System.out.println("-------------Exception in PayrollDAO.Connection.close "+e);
						}
					}
					return i;
				}
			   
			   
			   
			    public int lockEntry(int userid,String uname,int depo,int cmpcode,int mnthcode,int type)
			    {
			  	  
			    	PreparedStatement ps2 = null;
			    	Connection con=null;
			     
							
					int i=0;
					try 
					{
						con=ConnectionFactory.getConnection();

						String query2="update  locktable set locked_by=?,locked_user=?,locked_date=now(),locked=? where depo_code=? and cmp_code=? and mnth_code=? and entry_type=? ";
						
						con.setAutoCommit(false);
						ps2 = con.prepareStatement(query2);
						ps2.setInt(1,userid);
						ps2.setString(2,uname);
						ps2.setInt(3,1);
						ps2.setInt(4,depo);
						ps2.setInt(5,cmpcode);
						ps2.setInt(6,mnthcode);
						ps2.setInt(7,type);
			  			i=ps2.executeUpdate();
						
			  			
						con.commit();
						con.setAutoCommit(true);
						ps2.close();
						
					} catch (SQLException ex) {
						ex.printStackTrace();
						try {
							con.rollback();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						System.out.println("-------------Exception in PayrollDAO.lockEntries " + ex);
						i=-1;
					}
					finally {
						try {
							System.out.println("No. of Records Update/Insert : "+i);

							if(ps2 != null){ps2.close();}
							if(con != null){con.close();}
						} catch (SQLException e) {
							System.out.println("-------------Exception in PayrollDAO.Connection.close "+e);
						}
					}
					return i;
				}
			   
			    
				public Vector getUnlockList(int depo_code,int cmp_code,int mnth_code)
				{
					PreparedStatement ps = null;
					ResultSet rs=null;
					Connection con=null;
					Vector v =null;
					Vector col=null;
					int sno=1;
					int lock=0;
					String name[]={"zero","Sterile Days","Advance Entry","Other Allowance","Canteen Coupon","LTA/Medical","Machine Operator Entry","Professional Tax"};
					try 
					{
						con=ConnectionFactory.getConnection();
						con.setAutoCommit(false);


						String query1="select entry_type,locked_user,locked_date,locked from locktable  where depo_Code=? and cmp_code=? and mnth_code=? ";
						ps = con.prepareStatement(query1);
						ps.setInt(1, depo_code);
						ps.setInt(2, cmp_code);
						ps.setInt(3, mnth_code);
						rs =ps.executeQuery();
						
						v = new Vector();
						System.out.println("YEHA PER AAYA LOCKTABKE MAI "+mnth_code);
						while (rs.next())
						{
							System.out.println("YEHA PER AAYA LOCKTABKE MAI ANDAR");

							col= new Vector();
							col.add(rs.getInt(1));  // sno 0
							col.add(name[rs.getInt(1)]); // emp_name //1
							col.add(rs.getString(2)); // user name  2
							col.add(rs.getString(3)); // date  3
							if(rs.getInt(4)==1)
								col.add(true); // locked
							else
								col.add(false); // locked
							v.add(col);
							
							System.out.println("ENTRY TYPE "+rs.getInt(1));
						}



						con.commit();
						con.setAutoCommit(true);
						rs.close();
						ps.close();

					} catch (Exception ex) { ex.printStackTrace();
						try {
							con.rollback();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						System.out.println("-------------Exception in PayrollDAO.getUnlockList " + ex);

					}
					finally {
						try {
							System.out.println("No. of Records Update/Insert : " );

							if(rs != null){rs.close();}
							if(ps != null){ps.close();}
							if(con != null){con.close();}
						} catch (SQLException e) {
							System.out.println("-------------Exception in PayrollDAO.Connection.close --- getUnlockList "+e);
						}
					}
					return v;
				}

			    public int updateUnlockList(ArrayList<?> attnlist)
			    {
			  	  
			    	PreparedStatement ps1 = null;
			    	Connection con=null;
			    	EmptranDto emp=null;
					int i=0;
					try 
					{
						con=ConnectionFactory.getConnection();

						
						String query1="update locktable set locked=? where  depo_code=? and cmp_code=? and  mnth_code=? and entry_type=? " ;

						
						con.setAutoCommit(false);
						
						ps1 = con.prepareStatement(query1);
						
						int s=attnlist.size();
						for (int j=0;j<s;j++)
						{
							emp= (EmptranDto) attnlist.get(j);
							 
							ps1.setInt(1, emp.getAtten_lock());
							// where clause
							ps1.setInt(2,emp.getDepo_code());
							ps1.setInt(3,emp.getCmp_code());
							ps1.setInt(4,emp.getMnth_code());
							ps1.setInt(5,emp.getSerialno());
							i=ps1.executeUpdate();
					  			
						}
						
						
						con.commit();
						con.setAutoCommit(true);
						ps1.close();
						
					} catch (SQLException ex) {
						ex.printStackTrace();
						try {
							con.rollback();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						System.out.println("-------------Exception in PayrollDAO.updateUnlockList " + ex);
						i=-1;
					}
					finally {
						try {
							System.out.println("No. of Records Update/Insert : "+i);

							if(ps1 != null){ps1.close();}
							if(con != null){con.close();}
						} catch (SQLException e) {
							System.out.println("-------------Exception in PayrollDAO.Connection.close "+e);
						}
					}
					return i;
				}
				

			    public ArrayList getIncrementList(int depo_code,int cmp_code,int fyear,int repno,int mnth_code)
			  	{
			  		PreparedStatement ps = null;
			  		ResultSet rs=null;
			  		Connection con=null;
			  		ArrayList v =null;
			  		EmptranDto emp=null;
			  		double diff=0.00;
			  		double prev=0.00;
			  		int empCode=0;
			  		int sno=1;
			  		try 
			  		{
			  			con=ConnectionFactory.getConnection();
			  			con.setAutoCommit(false);


						String query="select e.*,concat(p.month_nm,'-',p.yy) monname from perdmast p, "+
						"(select e.fin_year,e.mnth_code,e.emp_code,m.emp_name,e.gross  from emptran e,employeemast m "+ 
						"where e.fin_year<=? and e.cmp_code=? and e.mnth_code<=? and m.cmp_code=? and m.emp_code=e.emp_code and (m.doresign is null or m.doresign > (select frdate from perdmast where mnth_code=?)) " +
						"group by e.emp_code,e.gross) e  where e.mnth_code=p.mnth_code order by e.emp_code,e.mnth_code";
			  			 
			  			ps = con.prepareStatement(query);
		  				ps.setInt(1, fyear);
		  				ps.setInt(2, cmp_code);
		  				ps.setInt(3, mnth_code);
		  				ps.setInt(4, cmp_code);
		  				ps.setInt(5, mnth_code);
			  			rs =ps.executeQuery();
			  			
			  			v = new ArrayList();
			  			boolean first=true;
			  			while (rs.next())
			  			{
			  				
			  				if(first)
			  				{
			  					empCode=rs.getInt(3);
			  					prev=rs.getDouble(5);
			  					first=false;
			  					
			  				}
			  				if(rs.getInt(3)!=empCode)
			  				{
			  					empCode=rs.getInt(3);
			  					prev=rs.getDouble(5);
			  				}
			  				
			  				diff=rs.getDouble(5)-prev;
			  				emp = new EmptranDto();
			  				emp.setSerialno(sno++);
			  				emp.setEmp_code(rs.getInt(3));
			  				emp.setEmp_name(rs.getString(4)); // ip name
			  				emp.setGross(rs.getDouble(5));
			  				emp.setMedical_value(diff);
			  				emp.setMonname(rs.getString(6));
			  				v.add(emp);
			  				prev=rs.getDouble(5);
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
			  			System.out.println("-------------Exception in PayrollDAO.getEarningList(ArrayList) " + ex);

			  		}
			  		finally {
			  			try {
			  				System.out.println("No. of Records Update/Insert : " );

			  				if(rs != null){rs.close();}
			  				if(ps != null){ps.close();}
			  				if(con != null){con.close();}
			  			} catch (SQLException e) {
			  				System.out.println("-------------Exception in PayrollDAO.Connection.close --- getEarningList(ArrayList) "+e);
			  			}
			  		}
			  		return v;
			  	}    


			    
			    public ArrayList getSalaryDetail(int depo_code,int cmp_code,int fyear,int repno,int mnth_code,int opt)
			    {
			    	PreparedStatement ps = null;
			    	ResultSet rs=null;
			    	PreparedStatement ps1 = null;
			    	ResultSet rs1=null;
			    	Connection con=null;
			    	ArrayList v =null;
			    	EmptranDto emp=null;
			    	String query=null;
			    	int smon=0;
			    	double total=0.00;
			    	try 
			    	{
			    		con=ConnectionFactory.getConnection();
			    		con.setAutoCommit(false);

			    		String perd="select mnth_code from perdmast where fin_year=? and fin_ord=1";
			    		
			    		System.out.println("YEHA SE READ KAR RAHAH HAI");
			    		
/*			    		String query="Select MONTHNAME(e.doc_date) AS 'Month Name',e.fin_year,e.emp_code,m.pf_no,m.uan_no,m.esic_no,m.emp_name,M.DESIGNATION,e.basic,e.da,e.hra,e.add_hra,e.incentive,(e.basic+e.da+e.hra+e.add_hra+e.incentive) btotal,"+
	    				"e.atten_days,e.basic_value,e.da_value,e.hra_value,e.add_hra_value,e.incentive_value, "+
	    				"(e.basic_value+e.da_value+e.hra_value+e.add_hra_value+e.incentive_value+e.food_value) grosstotal,e.arrear_days, "+
	    				"e.ot_rate,e.extra_hrs,e.ot_value,e.pf_value,e.esis_value,(e.pf_value+e.esis_value) totded, "+
	    				"(e.basic_value+e.da_value+e.hra_value+e.add_hra_value+e.incentive_value+e.spl_incen_value+e.lta_value+e.medical_value+e.stair_value+e.ot_value+e.food_value-e.pf_value-e.esis_value) net,e.food_value," +
	    				"e.absent_days,e.stair_days, e.Advance, e.loan, e.gross,e.spl_incentive, e.lta, e.medical,e.stair_alw,"+  
	    				"e.spl_incen_value, e.lta_value, e.medical_value,"+ 
	    				"e.stair_value, e.employer_esis_value, e.prof_tax, e.tds_value,"+ 
	    				"e.bonus_value, e.misc_value,"+ 
	    				"e.employer_pf, e.eps_pf,e.coupon_amt,"+  
	    				"e.machine1_value, e.machine2_value, e.food_alw,e.basicpf_value "+  
	    				"from emptran e, employeemast m where e.fin_year=? and e.cmp_code=?  and e.mnth_code=? and e.atten_days<>0 and ifnull(e.del_tag,'')<>'D' and m.cmp_code=? and e.emp_code=m.emp_code ";

*/			    		
			    		
			    		if(opt==1)  // selective month
			    		{
			    			query="select MONTHNAME(t.doc_date) AS 'Month Name',t.fin_year,e.emp_code,e.pf_no,e.uan_no,e.esic_no," +
			    					"e.emp_name,ifnull(e.designation,'Worker'),"+
			    					" sum(t.basic),sum(t.da),sum(t.hra),sum(t.add_hra),sum(t.incentive),0 btotal,"+
			    					" sum(t.atten_days),sum(t.basic_value),sum(t.da_value),sum(t.hra_value),sum(t.add_hra_value),sum(t.incentive_value),"+
			    					" 0 grosstotal,sum(t.arrear_days),sum(t.ot_rate),sum(t.extra_hrs),sum(t.ot_value),sum(t.pf_value),sum(t.esis_value),0 totded,0 net,"+
			    					" sum(t.food_value),sum(t.absent_days),sum(t.stair_days),sum(t.advance),sum(t.loan),"+
			    					" sum(t.gross) gross,"+
			    					" sum(t.spl_incentive),sum(t.lta),sum(t.medical),sum(t.stair_alw),"+ //39
			    					" sum(t.spl_incen_value),sum(t.lta_value),sum(t.medical_value),sum(t.stair_value),"+
			    					" 0 employer_esis_value,"+
			    					" sum(t.prof_tax),"+
			    					" 0 tds_value,0 bonus_value,"+
			    					" sum(t.misc_value),"+
			    					" 0 employer_pf,0 eps_pf,"+
			    					" t.coupon_amt,t.machine1_value,t.machine2_value,"+
			    					" 0 food_alw,0 basicpf_value,"+  
			    					" sum(t.spl_incentive),"+ 
			    					" ifnull(e.bank,'Direct Cheque'),e.bank_add1,e.ifsc_code,ifnull(e.bank_accno,''),e.bank_code,"+
			    					" t.machine1_days,t.machine2_days,sum(t.pfamt),T.MNTH_CODE from"+ 
			    					" (select t.emp_code,t.basic,t.da,t.hra,t.add_hra,t.incentive,t.spl_incentive,t.ot_rate,t.lta,t.medical,t.stair_alw,"+ 
			    					" t.basic_value,t.da_value,t.hra_value,t.add_hra_value,t.incentive_value,t.spl_incen_value,t.ot_value,t.lta_value, "+
			    					" t.medical_value,t.pf_value,t.advance,t.loan,t.esis_value,t.atten_days,t.arrear_days,t.absent_days,t.stair_days,t.extra_hrs,t.misc_value,t.stair_value,"+ 
			    					" t.coupon_amt,t.machine1_value,t.machine2_value,t.machine1_days,t.machine2_days,t.food_value,t.prof_tax,t.doc_date,t.fin_year,t.gross,t.pf_value pfamt,MNTH_CODE from emptran t "+
			    					" where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and t.atten_days<>0 and ifnull(t.del_tag,'')<>'D'"+   
			    					" union all "+
			    					" select a.emp_code,0 basic,0 da,0 hra,0 add_hra,0 incentive,0 spl_incentive,0 ot_rate,0 lta,0 medical,0 stair_alw,"+
			    					" sum(basic_value),sum(da_value),sum(hra_value),sum(add_hra_value),sum(incen_value),sum(spl_incen_value),0 ot_value,0 lta_value,"+
			    					" 0 medical_value,sum(pf_value),0 advance,0 loan,sum(esic_value),0 atten_days,0 arrear_days,0 absent_days,0 stair_days,0 extra_hrs,0 misc_value,0 stair_value,"+
			    					" 0 coupon_amt,0 machine1_value,0 machine2_value,0 machine1_days,0 machine2_days,0 food_value,SUM(prof_tax),0,0,0,0,MNTH_CODE from arrear a "+
			    					" where fin_year<=? and depo_code=? and cmp_code=? and arrear_mon=? and arrear_paid='Y' group by emp_code"+ 
			    					" union all "+
			    					" select a.emp_code,0 basic,0 da,0 hra,0 add_hra,0 incentive,0 spl_incentive,0 ot_rate,0 lta,0 medical,0 stair_alw,"+
			    					" sum(basic_value),sum(da_value),sum(hra_value),sum(add_hra_value),sum(incen_value),sum(spl_incen_value),0 ot_value,0 lta_value,"+
			    					" 0 medical_value,sum(pf_value),0 advance,0 loan,sum(esic_value),0 atten_days,0 arrear_days,0 absent_days,0 stair_days,0 extra_hrs,0 misc_value,0 stair_value,"+
			    					" 0 coupon_amt,0 machine1_value,0 machine2_value,0 machine1_days,0 machine2_days,0 food_value,0 prof_tax,0,0,0,0,MNTH_CODE from arrear a "+
			    					" where fin_year<=? and depo_code=? and cmp_code=? and arrear_mon=? and arrear_paid='O' group by emp_code) t,employeemast e"+   
			    					" where  e.depo_code=? and e.cmp_code=? and e.emp_code=t.emp_code and ifnull(e.del_tag,'')<>'D'  group by t.emp_code order by e.bank_code,e.emp_code";
			    		}
			    		else
			    		{
			    			query="select MONTHNAME(t.doc_date) AS 'Month Name',t.fin_year,e.emp_code,e.pf_no,e.uan_no,e.esic_no," +
			    					"e.emp_name,ifnull(e.designation,'Worker'),"+
			    					" sum(t.basic),sum(t.da),sum(t.hra),sum(t.add_hra),sum(t.incentive),0 btotal,"+
			    					" sum(t.atten_days),sum(t.basic_value),sum(t.da_value),sum(t.hra_value),sum(t.add_hra_value),sum(t.incentive_value),"+
			    					" 0 grosstotal,sum(t.arrear_days),sum(t.ot_rate),sum(t.extra_hrs),sum(t.ot_value),sum(t.pf_value),sum(t.esis_value),0 totded,0 net,"+
			    					" sum(t.food_value),sum(t.absent_days),sum(t.stair_days),sum(t.advance),sum(t.loan),"+
			    					" sum(t.gross) gross,"+
			    					" sum(t.spl_incentive),sum(t.lta),sum(t.medical),sum(t.stair_alw),"+ //39
			    					" sum(t.spl_incen_value),sum(t.lta_value),sum(t.medical_value),sum(t.stair_value),"+
			    					" 0 employer_esis_value,"+
			    					" sum(t.prof_tax),"+
			    					" 0 tds_value,0 bonus_value,"+
			    					" sum(t.misc_value),"+
			    					" 0 employer_pf,0 eps_pf,"+
			    					" sum(t.coupon_amt),sum(t.machine1_value),sum(t.machine2_value),"+
			    					" 0 food_alw,0 basicpf_value,"+  
			    					" sum(t.spl_incentive),"+ 
			    					" ifnull(e.bank,'Direct Cheque'),e.bank_add1,e.ifsc_code,ifnull(e.bank_accno,''),e.bank_code,"+
			    					" t.machine1_days,t.machine2_days,sum(t.pfamt),T.MNTH_CODE from"+ 
			    					" (select t.emp_code,t.basic,t.da,t.hra,t.add_hra,t.incentive,t.spl_incentive,t.ot_rate,t.lta,t.medical,t.stair_alw,"+ 
			    					" t.basic_value,t.da_value,t.hra_value,t.add_hra_value,t.incentive_value,t.spl_incen_value,t.ot_value,t.lta_value, "+
			    					" t.medical_value,t.pf_value,t.advance,t.loan,t.esis_value,t.atten_days,t.arrear_days,t.absent_days,t.stair_days,t.extra_hrs,t.misc_value,t.stair_value,"+ 
			    					" t.coupon_amt,t.machine1_value,t.machine2_value,t.machine1_days,t.machine2_days,t.food_value,t.prof_tax,t.doc_date,t.fin_year,t.gross,t.pf_value pfamt,t.mnth_code from emptran t "+
			    					" where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code between ? and ?  and t.atten_days<>0 and ifnull(t.del_tag,'')<>'D'"+   
			    					" union all "+
			    					" select a.emp_code,0 basic,0 da,0 hra,0 add_hra,0 incentive,0 spl_incentive,0 ot_rate,0 lta,0 medical,0 stair_alw,"+
			    					" sum(basic_value),sum(da_value),sum(hra_value),sum(add_hra_value),sum(incen_value),sum(spl_incen_value),0 ot_value,0 lta_value,"+
			    					" 0 medical_value,sum(pf_value),0 advance,0 loan,sum(esic_value),0 atten_days,0 arrear_days,0 absent_days,0 stair_days,0 extra_hrs,0 misc_value,0 stair_value,"+
			    					" 0 coupon_amt,0 machine1_value,0 machine2_value,0 machine1_days,0 machine2_days,0 food_value,SUM(prof_tax),0,0,0,0 PFAMT,a.arrear_mon from arrear a "+
			    					" where fin_year<=? and depo_code=? and cmp_code=? and arrear_mon between ? and ?  and arrear_paid='Y' group by emp_code,arrear_mon"+ 
			    					" union all "+
			    					" select a.emp_code,0 basic,0 da,0 hra,0 add_hra,0 incentive,0 spl_incentive,0 ot_rate,0 lta,0 medical,0 stair_alw,"+
			    					" sum(basic_value),sum(da_value),sum(hra_value),sum(add_hra_value),sum(incen_value),sum(spl_incen_value),0 ot_value,0 lta_value,"+
			    					" 0 medical_value,sum(pf_value),0 advance,0 loan,sum(esic_value),0 atten_days,0 arrear_days,0 absent_days,0 stair_days,0 extra_hrs,0 misc_value,0 stair_value,"+
			    					" 0 coupon_amt,0 machine1_value,0 machine2_value,0 machine1_days,0 machine2_days,0 food_value,0 prof_tax,0,0,0,0 PFAMT,a.arrear_mon from arrear a "+
			    					" where fin_year<=? and depo_code=? and cmp_code=? and arrear_mon between ? and ?  and arrear_paid='O' group by emp_code,arrear_mon) t,employeemast e"+   
			    					" where  e.depo_code=? and e.cmp_code=? and e.emp_code=t.emp_code and ifnull(e.del_tag,'')<>'D'  group by t.emp_code,t.mnth_code order by e.bank_code,e.emp_code,t.mnth_code";
			    			
/*			    			query="select MONTHNAME(t.doc_date) AS 'Month Name',t.fin_year,e.emp_code,e.pf_no,e.uan_no,e.esic_no," +
			    					"e.emp_name,ifnull(e.designation,'Worker'),"+
			    					" sum(t.basic),sum(t.da),sum(t.hra),sum(t.add_hra),sum(t.incentive),0 btotal,"+
			    					" sum(t.atten_days),sum(t.basic_value),sum(t.da_value),sum(t.hra_value),sum(t.add_hra_value),sum(t.incentive_value),"+
			    					" 0 grosstotal,sum(t.arrear_days),t.ot_rate,sum(t.extra_hrs),sum(t.ot_value),sum(t.pf_value),sum(t.esis_value),0 totded,0 net,"+
			    					" sum(t.food_value),sum(t.absent_days),sum(t.stair_days),sum(t.advance),sum(t.loan),"+
			    					" sum(t.gross) gross,"+
			    					" sum(t.spl_incentive),sum(t.lta),sum(t.medical),sum(t.stair_alw),"+ //39
			    					" sum(t.spl_incen_value),sum(t.lta_value),sum(t.medical_value),sum(t.stair_value),"+
			    					" 0 employer_esis_value,"+
			    					" sum(t.prof_tax),"+
			    					" 0 tds_value,0 bonus_value,"+
			    					" sum(t.misc_value),"+
			    					" 0 employer_pf,0 eps_pf,"+
			    					" SUM(t.coupon_amt),SUM(t.machine1_value),SUM(t.machine2_value),"+
			    					" 0 food_alw,0 basicpf_value,"+  
			    					" sum(t.spl_incentive),"+ 
			    					" ifnull(e.bank,'Direct Cheque'),e.bank_add1,e.ifsc_code,ifnull(e.bank_accno,''),e.bank_code,"+
			    					" t.machine1_days,t.machine2_days,sum(t.pfamt) from"+ 
			    					" (select t.emp_code,t.basic,t.da,t.hra,t.add_hra,t.incentive,t.spl_incentive,t.ot_rate,t.lta,t.medical,t.stair_alw,"+ 
			    					" t.basic_value,t.da_value,t.hra_value,t.add_hra_value,t.incentive_value,t.spl_incen_value,t.ot_value,t.lta_value, "+
			    					" t.medical_value,t.pf_value,t.advance,t.loan,t.esis_value,t.atten_days,t.arrear_days,t.absent_days,t.stair_days,t.extra_hrs,t.misc_value,t.stair_value,"+ 
			    					" t.coupon_amt,t.machine1_value,t.machine2_value,t.machine1_days,t.machine2_days,t.food_value,t.prof_tax,t.doc_date,t.fin_year,t.gross,t.pf_value pfamt from emptran t "+
			    					" where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code between ? and ? and t.atten_days<>0 and ifnull(t.del_tag,'')<>'D'"+   
			    					" union all "+
			    					" select a.emp_code,0 basic,0 da,0 hra,0 add_hra,0 incentive,0 spl_incentive,0 ot_rate,0 lta,0 medical,0 stair_alw,"+
			    					" sum(basic_value),sum(da_value),sum(hra_value),sum(add_hra_value),sum(incen_value),sum(spl_incen_value),0 ot_value,0 lta_value,"+
			    					" 0 medical_value,sum(pf_value),0 advance,0 loan,sum(esic_value),0 atten_days,0 arrear_days,0 absent_days,0 stair_days,0 extra_hrs,0 misc_value,0 stair_value,"+
			    					" 0 coupon_amt,0 machine1_value,0 machine2_value,0 machine1_days,0 machine2_days,0 food_value,SUM(prof_tax),0,0,0,0 from arrear a "+
			    					" where fin_year<=? and depo_code=? and cmp_code=? and arrear_mon between ? and ? and arrear_paid='Y' group by emp_code"+ 
			    					" union all "+
			    					" select a.emp_code,0 basic,0 da,0 hra,0 add_hra,0 incentive,0 spl_incentive,0 ot_rate,0 lta,0 medical,0 stair_alw,"+
			    					" sum(basic_value),sum(da_value),sum(hra_value),sum(add_hra_value),sum(incen_value),sum(spl_incen_value),0 ot_value,0 lta_value,"+
			    					" 0 medical_value,sum(pf_value),0 advance,0 loan,sum(esic_value),0 atten_days,0 arrear_days,0 absent_days,0 stair_days,0 extra_hrs,0 misc_value,0 stair_value,"+
			    					" 0 coupon_amt,0 machine1_value,0 machine2_value,0 machine1_days,0 machine2_days,0 food_value,0 prof_tax,0,0,0,0 from arrear a "+
			    					" where fin_year<=? and depo_code=? and cmp_code=? and arrear_mon between ? and ? and arrear_paid='O' group by emp_code) t,employeemast e"+   
			    					" where  e.depo_code=? and e.cmp_code=? and e.emp_code=t.emp_code and ifnull(e.del_tag,'')<>'D'  group by t.emp_code order by e.bank_code,e.emp_code";
*/			    		}			    		

			    		// for arrear table query implement later after discussion
/*			    		select mnth_code,emp_code,arrear_days,arrear_atten,arrear_mon,
			    		sum(basic_value),sum(da_value),sum(hra_value),sum(add_hra_value),sum(incen_value),sum(spl_incen_value) from arrear 
			    		where fin_year=2018 and depo_code=10 and cmp_code=1 AND EMP_CODE=8  group by emp_code,mnth_code;
*/
			    		
			    		ps1 = con.prepareStatement(perd);
			    		ps1.setInt(1, fyear);
			    		rs1 =ps1.executeQuery();
			    		if(rs1.next())
			    		{
			    			smon=rs1.getInt(1);
			    		}
			    		rs1.close();
			    		ps1.close();
			    		ps = con.prepareStatement(query);
			    		
			    		if(opt==1)
			    		{
			    			ps.setInt(1, fyear);
			    			ps.setInt(2, depo_code);
			    			ps.setInt(3, cmp_code);
			    			ps.setInt(4, mnth_code);
			    			ps.setInt(5, fyear);
			    			ps.setInt(6, depo_code);
			    			ps.setInt(7, cmp_code);
			    			ps.setInt(8, mnth_code);
			    			ps.setInt(9, fyear);
			    			ps.setInt(10, depo_code);
			    			ps.setInt(11, cmp_code);
			    			ps.setInt(12, mnth_code);
			    			ps.setInt(13, depo_code);
			    			ps.setInt(14, cmp_code);
			    		}
			    		else
			    		{
			    			ps.setInt(1, fyear);
			    			ps.setInt(2, depo_code);
			    			ps.setInt(3, cmp_code);
			    			ps.setInt(4, smon);
			    			ps.setInt(5, mnth_code);
			    			ps.setInt(6, fyear);
			    			ps.setInt(7, depo_code);
			    			ps.setInt(8, cmp_code);
			    			ps.setInt(9, smon);
			    			ps.setInt(10, mnth_code);
			    			ps.setInt(11, fyear);
			    			ps.setInt(12, depo_code);
			    			ps.setInt(13, cmp_code);
			    			ps.setInt(14, smon);
			    			ps.setInt(15, mnth_code);
			    			ps.setInt(16, depo_code);
			    			ps.setInt(17, cmp_code);
			    			
			    		}
			    		rs =ps.executeQuery();

			    		v = new ArrayList();

			    		double pfamt=0.00;
			    		double tot=0.00;
			    		double newtot=0.00;
			    		double sum[]=new double[56];
			    		boolean first=true;
			    		int empCode=0;
			    		int stairDays=0;
			    		int wmm=0;
			    		while (rs.next())
			    		{
			    			wmm=rs.getInt(65);
			    			if(first)
			    			{
			    				emp = new EmptranDto();

			    				emp.setMonname(rs.getString(1));
			    				emp.setFin_year(rs.getInt(2));
			    				emp.setEmp_code(rs.getInt(3));
			    				emp.setPf_no(rs.getInt(4));
			    				emp.setUan_no(rs.getLong(5));
			    				emp.setEsic_no(rs.getLong(6));
			    				emp.setEmp_name(rs.getString(7)); // ip name
			    				emp.setDesignation(rs.getString(8));
			    				empCode=rs.getInt(3);
			    				first=false;
			    						
			    			}

			    			if(empCode!=rs.getInt(3))
			    			{

				    			emp.setBasic(sum[9]);
				    			emp.setDa(sum[10]);
				    			emp.setHra(sum[11]);
				    			emp.setAdd_hra(sum[12]);
				    			emp.setIncentive(sum[13]);
				    			emp.setGross(sum[14]);
				    			emp.setAtten_days(sum[15]);
				    			emp.setBasic_value(sum[16]);
				    			emp.setDa_value(sum[17]);
				    			emp.setHra_value(sum[18]);
				    			emp.setAdd_hra_value(sum[19]);
				    			emp.setIncentive_value(sum[20]);
//				    			emp.setMisc_value(sum[21]); // Total Wages Paid before deduction
				    			emp.setArrear_days(sum[22]);
				    			emp.setOt_rate(sum[23]);
				    			emp.setExtra_hrs(sum[24]);
				    			emp.setOt_value(sum[25]);
//				    			emp.setPf_value(rs.getDouble(26]);
				    			emp.setPf_value(pfamt);
				    			emp.setEsis_value(sum[27]);
				    			emp.setNet_value(sum[29]);
				    			emp.setFood_value(sum[30]);
				    			
				    			emp.setAbsent_days(sum[31]);
				    			emp.setStair_days(stairDays);
				    			emp.setAdvance(sum[33]);
				    			emp.setLoan(sum[34]);
				    			emp.setGross(sum[35]);
				    			emp.setSpl_incentive(sum[36]);
				    			emp.setLta(sum[37]);
				    			emp.setMedical(sum[38]);
				    			emp.setStair_alw(sum[39]);
				    			
				    			emp.setSpl_incen_value(sum[40]);
				    			emp.setLta_value(sum[41]);
				    			emp.setMedical_value(sum[42]);
				    			emp.setStair_value(sum[43]);
				    			emp.setEmployer_esis_value(sum[44]);
				    			
				    			emp.setProf_tax(sum[45]);
				    			emp.setTds_value(sum[46]);
				    			emp.setBonus_value(sum[47]);
				    			emp.setMisc_value(sum[48]);
				    			emp.setEmployee_pf(sum[49]);
				    			emp.setEps_pf(sum[50]);
				    			emp.setCoupon_amt(sum[51]);
				    			emp.setMachine1_value(sum[52]);
				    			emp.setMachine2_value(sum[53]);
				    			emp.setFood_alw(sum[54]);
				    			emp.setBasicpf_value(sum[55]);

				    			v.add(emp);
			    				
			    				
			    				emp = new EmptranDto();
			    				emp.setMonname(rs.getString(1));
			    				emp.setFin_year(rs.getInt(2));
			    				emp.setEmp_code(rs.getInt(3));
			    				emp.setPf_no(rs.getInt(4));
			    				emp.setUan_no(rs.getLong(5));
			    				emp.setEsic_no(rs.getLong(6));
			    				emp.setEmp_name(rs.getString(7)); // ip name
			    				emp.setDesignation(rs.getString(8));
			    				empCode=rs.getInt(3);
			    				pfamt=0;
			    				stairDays=0;
			    				sum=new double[56];
			    			}
			    			
			    			for (int i=9;i<=55;i++)
			    			{
			    				sum[i]+= rs.getDouble(i);
			    			}
			    			
			    			
							tot=rs.getDouble(9)+rs.getDouble(10)+rs.getDouble(13)+rs.getDouble(30)+rs.getDouble(38);
							stairDays+=rs.getInt(32);
			 				if(wmm== 202502 && (rs.getInt(3)==15 || rs.getInt(3)==139 || rs.getInt(3)==403 || rs.getInt(3)==849 || rs.getInt(3)==171)) 
			 				{
			 					if(tot>15000)
				 					pfamt+=rs.getDouble(64);
				 				else
					    			pfamt+=rs.getDouble(26);
			 				}
			 				else if(wmm==202502)
			 				{  
			 					pfamt+=(rs.getDouble(26)-rs.getDouble(64))+rs.getDouble(64);
			 				}
			 				else if(tot>15000)
			 					pfamt+=rs.getDouble(64);
			 				else
				    			pfamt+=rs.getDouble(26);

	 		 				
/*			    			emp = new EmptranDto();
			    			
			    			emp.setMonname(rs.getString(1));
			    			emp.setFin_year(rs.getInt(2));
			    			emp.setEmp_code(rs.getInt(3));
			    			emp.setPf_no(rs.getInt(4));
			    			emp.setUan_no(rs.getLong(5));
			    			emp.setEsic_no(rs.getLong(6));
			    			emp.setEmp_name(rs.getString(7)); // ip name
			    			emp.setDesignation(rs.getString(8));

			    			emp.setBasic(rs.getDouble(9));
			    			emp.setDa(rs.getDouble(10));
			    			emp.setHra(rs.getDouble(11));
			    			emp.setAdd_hra(rs.getDouble(12));
			    			emp.setIncentive(rs.getDouble(13));
			    			emp.setGross(rs.getDouble(14));
			    			emp.setAtten_days(rs.getDouble(15));
			    			emp.setBasic_value(rs.getDouble(16));
			    			emp.setDa_value(rs.getDouble(17));
			    			emp.setHra_value(rs.getDouble(18));
			    			emp.setAdd_hra_value(rs.getDouble(19));
			    			emp.setIncentive_value(rs.getDouble(20));
//			    			emp.setMisc_value(rs.getDouble(21)); // Total Wages Paid before deduction
			    			emp.setArrear_days(rs.getDouble(22));
			    			emp.setOt_rate(rs.getDouble(23));
			    			emp.setExtra_hrs(rs.getDouble(24));
			    			emp.setOt_value(rs.getDouble(25));
//			    			emp.setPf_value(rs.getDouble(26));
			    			emp.setPf_value(pfamt);
			    			emp.setEsis_value(rs.getDouble(27));
			    			emp.setNet_value(rs.getDouble(29));
			    			emp.setFood_value(rs.getDouble(30));
			    			
			    			emp.setAbsent_days(rs.getDouble(31));
			    			emp.setStair_days(rs.getInt(32));
			    			emp.setAdvance(rs.getDouble(33));
			    			emp.setLoan(rs.getDouble(34));
			    			emp.setGross(rs.getDouble(35));
			    			emp.setSpl_incentive(rs.getDouble(36));
			    			emp.setLta(rs.getDouble(37));
			    			emp.setMedical(rs.getDouble(38));
			    			emp.setStair_alw(rs.getDouble(39));
			    			
			    			emp.setSpl_incen_value(rs.getDouble(40));
			    			emp.setLta_value(rs.getDouble(41));
			    			emp.setMedical_value(rs.getDouble(42));
			    			emp.setStair_value(rs.getDouble(43));
			    			emp.setEmployer_esis_value(rs.getDouble(44));
			    			
			    			emp.setProf_tax(rs.getDouble(45));
			    			emp.setTds_value(rs.getDouble(46));
			    			emp.setBonus_value(rs.getDouble(47));
			    			emp.setMisc_value(rs.getDouble(48));
			    			emp.setEmployee_pf(rs.getDouble(49));
			    			emp.setEps_pf(rs.getDouble(50));
			    			emp.setCoupon_amt(rs.getDouble(51));
			    			emp.setMachine1_value(rs.getDouble(52));
			    			emp.setMachine2_value(rs.getDouble(53));
			    			emp.setFood_alw(rs.getDouble(54));
			    			emp.setBasicpf_value(rs.getDouble(55));

			    			v.add(emp);*/

			    		}

			    		
		    			emp.setBasic(sum[9]);
		    			emp.setDa(sum[10]);
		    			emp.setHra(sum[11]);
		    			emp.setAdd_hra(sum[12]);
		    			emp.setIncentive(sum[13]);
		    			emp.setGross(sum[14]);
		    			emp.setAtten_days(sum[15]);
		    			emp.setBasic_value(sum[16]);
		    			emp.setDa_value(sum[17]);
		    			emp.setHra_value(sum[18]);
		    			emp.setAdd_hra_value(sum[19]);
		    			emp.setIncentive_value(sum[20]);
//		    			emp.setMisc_value(sum[21]); // Total Wages Paid before deduction
		    			emp.setArrear_days(sum[22]);
		    			emp.setOt_rate(sum[23]);
		    			emp.setExtra_hrs(sum[24]);
		    			emp.setOt_value(sum[25]);
//		    			emp.setPf_value(rs.getDouble(26]);
		    			emp.setPf_value(pfamt);
		    			emp.setEsis_value(sum[27]);
		    			emp.setNet_value(sum[29]);
		    			emp.setFood_value(sum[30]);
		    			
		    			emp.setAbsent_days(sum[31]);
		    			emp.setStair_days(stairDays);
		    			emp.setAdvance(sum[33]);
		    			emp.setLoan(sum[34]);
		    			emp.setGross(sum[35]);
		    			emp.setSpl_incentive(sum[36]);
		    			emp.setLta(sum[37]);
		    			emp.setMedical(sum[38]);
		    			emp.setStair_alw(sum[39]);
		    			
		    			emp.setSpl_incen_value(sum[40]);
		    			emp.setLta_value(sum[41]);
		    			emp.setMedical_value(sum[42]);
		    			emp.setStair_value(sum[43]);
		    			emp.setEmployer_esis_value(sum[44]);
		    			
		    			emp.setProf_tax(sum[45]);
		    			emp.setTds_value(sum[46]);
		    			emp.setBonus_value(sum[47]);
		    			emp.setMisc_value(sum[48]);
		    			emp.setEmployee_pf(sum[49]);
		    			emp.setEps_pf(sum[50]);
		    			emp.setCoupon_amt(sum[51]);
		    			emp.setMachine1_value(sum[52]);
		    			emp.setMachine2_value(sum[53]);
		    			emp.setFood_alw(sum[54]);
		    			emp.setBasicpf_value(sum[55]);

		    			v.add(emp);

			    		
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
			    		System.out.println("-------------Exception in PayrollDAO.getSalaryDetail(ArrayList) " + ex);

			    	}
			    	finally {
			    		try {
			    			System.out.println("No. of Records Update/Insert : " );

			    			if(rs != null){rs.close();}
			    			if(ps != null){ps.close();}
			    			if(con != null){con.close();}
			    		} catch (SQLException e) {
			    			System.out.println("-------------Exception in PayrollDAO.Connection.close --- getSalaryDetail(ArrayList) "+e);
			    		}
			    	}
			    	return v;
			    }    


			    public ArrayList getLoanRepyamentReport(int depo_code,int cmp_code,int emp_code)
			  	{
			  		PreparedStatement ps = null;
			  		ResultSet rs=null;

			  		Connection con=null;
			  		ArrayList v =null;
			  		EmptranDto emp=null;
			  		 
			  		try 
			  		{
			  			con=ConnectionFactory.getConnection();
			  			con.setAutoCommit(false);
			  			

			  			String query="select p.month_nm,YEAR(P.FRDATE),l.emp_Code,e.emp_name,loan_amt loan,"+
			  			"sum(repay_amt) recovery from loan l,employeemast e,perdmast p  "+ 
			  			"where l.cmp_Code=? and l.depo_code=? and l.emp_Code=? " +
			  			"and e.emp_Code=l.emp_Code and p.mnth_code=l.mnth_code group by l.mnth_code ";;

			  			

			  			 
			  			ps = con.prepareStatement(query);
			  				ps.setInt(1, cmp_code);
			  				ps.setInt(2, depo_code);
			  				ps.setInt(3, emp_code);
				  			rs =ps.executeQuery();
			  			
			  			v = new ArrayList();
			  			double balance=0.00;
			  			boolean first=true;
			  			while (rs.next())
			  			{
			  				if(first)
			  				{
			  					first=false;
			  					balance=rs.getDouble(5);
			  				}
			  				balance=balance-rs.getDouble(6);
			  				emp = new EmptranDto();
			  				emp.setBank(rs.getString(1));	
			  				emp.setFin_year(rs.getInt(2));
			  				emp.setEmp_code(rs.getInt(3));  // ip number
			  				emp.setEmp_name(rs.getString(4)); // ip name
			  				emp.setLoan(rs.getDouble(5));
			  				emp.setLta(rs.getDouble(6));   // loan repoayment amount
			  				emp.setLta_value(balance);
			  				v.add(emp);
			  			}



			  			con.commit();
			  			con.setAutoCommit(true);
			  			rs.close();
			  			ps.close();

			  		} catch (Exception ex)  { ex.printStackTrace();
			  			try {
			  				con.rollback();
			  			} catch (SQLException e) {
			  				e.printStackTrace();
			  			}
			  			System.out.println("-------------Exception in PayrollDAO.getLoanRepaymentList " + ex);

			  		}
			  		finally {
			  			try {
			  				System.out.println("No. of Records Update/Insert : " );

			  				if(rs != null){rs.close();}
			  				if(ps != null){ps.close();}
			  				if(con != null){con.close();}
			  			} catch (SQLException e) {
			  				System.out.println("-------------Exception in PayrollDAO.Connection.close --- getEsicList "+e);
			  			}
			  		}
			  		return v;
			  	}    
			    
		    
			    
			    
			    
	public double roundTwoDecimals(double d) 
	{
	    DecimalFormat twoDForm = new DecimalFormat("0.00");
	    try {
			double roundval = Double.valueOf(twoDForm.format(d));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 //   return ((int) (roundval+.50));
//	    return Double.valueOf(twoDForm.format((int) (d+.50)));
	    return Double.valueOf(twoDForm.format(d));
	}	

	public double roundTwoDecimalsnew(double d) 
	{
	    DecimalFormat twoDForm = new DecimalFormat("0.00");
	    double roundval = Double.valueOf(twoDForm.format(d));
	    return ((int) (roundval+.50));
//	    return Double.valueOf(twoDForm.format((int) (d+.50)));
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
