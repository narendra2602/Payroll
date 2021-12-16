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
			" modified_by=?, modified_date=?,absent_days=? where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? and emp_code=? ";
			

			
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
	
	
    
    public Vector getSalaryList(int depo_code,int cmp_code,int fyear,int mnth_code,int mnthdays)
	{
		PreparedStatement ps = null;
		ResultSet rs=null;
		Connection con=null;
		Vector v =null;
		Vector col=null;
		EmptranDto emp=null;
		int sno=1;
		double basic,da,hra,incentive,add_hra,spl_incentive,gross,pf,esic,empesic,emppf,epspf,food_allowance,basicForPf;
		double basicpf,dapf,incentivepf;
		double presentdays;
		basicForPf=0.00;
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
			"absent_days,arrear_days,add_hra,spl_incentive,misc_value,stair_value,machine1_value,machine2_value,food_alw from "+ 
			"(select e.emp_code,e.emp_name,e.designation,e.pf_no,e.esic_no,t.basic,t.da,t.hra,t.incentive, "+
			" round(t.extra_hrs*t.ot_rate) ot,t.medical_value,t.lta_value,t.advance,t.atten_days,t.absent_days,t.arrear_days," +
			" t.add_hra,t.spl_incentive,t.misc_value,t.stair_value,t.machine1_value,t.machine2_value,t.food_alw "+
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
				
				//Salary calculation 
				if(rs.getDouble(16)==0) // if absent days is 0 calculate full salary
				{
					basic = roundTwoDecimals(rs.getDouble(4)+((rs.getDouble(4)/30)*rs.getDouble(17)));
					da = roundTwoDecimals(rs.getDouble(5)+((rs.getDouble(5)/30)*rs.getDouble(17)));
					hra = roundTwoDecimals(rs.getDouble(6)+((rs.getDouble(6)/30)*rs.getDouble(17)));
					incentive = roundTwoDecimals(rs.getDouble(7)+((rs.getDouble(7)/30)*rs.getDouble(17)));
					add_hra = roundTwoDecimals(rs.getDouble(18)+((rs.getDouble(18)/30)*rs.getDouble(17)));
					spl_incentive = roundTwoDecimals(rs.getDouble(19)+((rs.getDouble(19)/30)*rs.getDouble(17)));
					food_allowance=roundTwoDecimals(rs.getDouble(3)*rs.getDouble(24)); // presentDays*Food_alw (perday)
					
					basicpf = roundTwoDecimals(rs.getDouble(4));
					dapf = roundTwoDecimals(rs.getDouble(5));
					incentivepf = roundTwoDecimals(rs.getDouble(7));

					
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
					
				}
				else if(rs.getDouble(3)<5) // if present days is < 5  calculate present days salary
				{
					basic = roundTwoDecimals((rs.getDouble(4)/30)*(rs.getDouble(17)+rs.getDouble(3)));
					da = roundTwoDecimals((rs.getDouble(5)/30)*(rs.getDouble(17)+rs.getDouble(3)));
					hra = roundTwoDecimals((rs.getDouble(6)/30)*(rs.getDouble(17)+rs.getDouble(3)));
					incentive = roundTwoDecimals((rs.getDouble(7)/30)*(rs.getDouble(17)+rs.getDouble(3)));
					add_hra = roundTwoDecimals((rs.getDouble(18)/30)*(rs.getDouble(17)+rs.getDouble(3)));
					spl_incentive = roundTwoDecimals((rs.getDouble(19)/30)*(rs.getDouble(17)+rs.getDouble(3)));
					food_allowance=roundTwoDecimals(rs.getDouble(3)*rs.getDouble(24)); // presentDays*Food_alw (perday)
					
					basicpf = roundTwoDecimals((rs.getDouble(4)/30)*(rs.getDouble(3)));
					dapf = roundTwoDecimals((rs.getDouble(5)/30)*(rs.getDouble(3)));
					incentivepf = roundTwoDecimals((rs.getDouble(7)/30)*(rs.getDouble(3)));

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
					basic = roundTwoDecimals(rs.getDouble(4)+((rs.getDouble(4)/30)*(rs.getDouble(17)-rs.getDouble(16))));
					da = roundTwoDecimals(rs.getDouble(5)+((rs.getDouble(5)/30)*(rs.getDouble(17)-rs.getDouble(16))));
					hra = roundTwoDecimals(rs.getDouble(6)+((rs.getDouble(6)/30)*(rs.getDouble(17)-rs.getDouble(16))));
					incentive = roundTwoDecimals(rs.getDouble(7)+((rs.getDouble(7)/30)*(rs.getDouble(17)-rs.getDouble(16))));
					add_hra = roundTwoDecimals(rs.getDouble(18)+((rs.getDouble(18)/30)*(rs.getDouble(17)-rs.getDouble(16))));
					spl_incentive = roundTwoDecimals(rs.getDouble(19)+((rs.getDouble(19)/30)*(rs.getDouble(17)-rs.getDouble(16))));
					food_allowance=roundTwoDecimals(rs.getDouble(3)*rs.getDouble(24)); // presentDays*Food_alw (perday)
					
					basicpf = roundTwoDecimals(rs.getDouble(4)+((rs.getDouble(4)/30)*(rs.getDouble(16)*-1)));
					dapf = roundTwoDecimals(rs.getDouble(5)+((rs.getDouble(5)/30)*(rs.getDouble(16)*-1)));
					incentivepf = roundTwoDecimals(rs.getDouble(7)+((rs.getDouble(7)/30)*(rs.getDouble(16)*-1)));

					
				}
				
//				gross=roundTwoDecimals(basic+da+hra+add_hra+incentive+spl_incentive+rs.getDouble(8)+rs.getDouble(20)); // last field Misc value

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
					if(basicForPf>15000)
						pf=roundTwoDecimals(15000*12/100);
					else
						pf=roundTwoDecimals((basic+da+incentive+rs.getDouble(9)+food_allowance)*12/100);
					
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
					esic=roundTwoDecimals((gross*0.75)/100);
					empesic=roundTwoDecimals((gross*3.25)/100);
				}
				else
				{
					esic=roundTwoDecimals((gross*1.75)/100);
					empesic=roundTwoDecimals((gross*4.75)/100);
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
				
				col.add(emp); // hidden emptranDto 16
				
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
			"ot_value=?,pf_value=?,esis_value=?,employer_esis_value=?,modified_by=?,modified_date=?,add_hra_value=?,employer_pf=?,eps_pf=?,food_value=?,basicpf_value=? " +
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
				// where clause
				ps1.setInt(17,emp.getFin_year());
				ps1.setInt(18,emp.getDepo_code());
				ps1.setInt(19,emp.getCmp_code());
				ps1.setInt(20,emp.getMnth_code());
				ps1.setInt(21,emp.getEmp_code());
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
	
    
    
    public ArrayList getEsicList(int depo_code,int cmp_code,int fyear,int mnth_code,int repno)
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
  			"t.machine1_rate,t.machine2_rate,t.machine1_days,t.machine2_days,t.machine1_value,t.machine2_value,e.adhar_no,ifnull(e.pan_no,''),t.basicpf_value,0 prevdays "+
  			" from emptran t,employeemast e "+
			" where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and ifnull(t.del_tag,'')<>'D' and e.depo_Code=? and e.cmp_code=? "+
			" and e.emp_code=t.emp_code "+orderby;

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
				"sum(t.machine1_rate),sum(t.machine2_rate),sum(t.machine1_days),sum(t.machine2_days),sum(t.machine1_value),sum(t.machine2_value),e.adhar_no,ifnull(e.pan_no,''),sum(t.basicpf_value),sum(t.prevdays)  from "+ 
				"(select t.emp_code,t.atten_days,(t.basic+t.da+t.hra+t.add_hra+t.incentive+t.spl_incentive) totalwages, "+
				"t.esis_value,t.employer_esis_value,t.serialno,t.pf_value,t.advance,t.loan,t.stair_days,t.stair_alw,t.stair_value, "+ 
				"t.extra_hrs,t.arrear_days,t.ot_rate,t.ot_value,t.employer_pf,t.eps_pf,(t.basic_value+t.da_value+t.food_value+t.incentive_value+t.medical_value) grosspf,(t.basic+t.da) salarypf, "+ 
				"(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incentive_value+t.spl_incen_value+t.stair_value+t.misc_value+t.ot_value+t.lta_value+t.medical_value+t.machine1_value+t.machine2_value+t.food_value) netwages, "+ 
				"t.absent_days,t.misc_value,t.lta_value,t.medical_value,t.incentive_value,t.spl_incen_value,t.coupon_amt, "+
	  			"t.machine1_rate,t.machine2_rate,t.machine1_days,t.machine2_days,t.machine1_value,t.machine2_value,t.basicpf_value,0 prevdays "+
				"from emptran t where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=?  and ifnull(t.del_tag,'')<>'D' "+ 
				"union all "+
				"select t.emp_code,0 atten_days,0 totalwages, "+
				"0 esic_value,0 employer_esis_value,0 serialno,0 pf_value,0 advance,0 loan,0 stair_days,0 stair_alw,0 stair_value, "+ 
				"0 extra_hrs,0 arrear_days,0 ot_rate,0 ot_value,0 employer_pf,0 eps_pf,0 grosspf,0  salarypf,  "+
				"0 netwages, "+
				"0 absent_days,0 misc_value,0 lta_value,0 medical_value,0 incen_value,0 spl_incen_value,0 coupon_amt, "+
	  			"0 machine1_rate,0 machine2_rate,0 machine1_days,0 machine2_days,0 machine1_value,0 machine2_value,0 basicpf_value,t.atten_days prevdays "+
				"from emptran t where t.fin_year<=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and ifnull(t.del_tag,'')<>'D' group by t.emp_code "+ 
				"union all "+
				"select t.emp_code,0 atten_days,0 totalwages, "+
				"sum(t.esic_value) esic_value,0 employer_esis_value,0 serialno,sum(t.pf_value) pf_value,0 advance,0 loan,0 stair_days,0 stair_alw,0 stair_value, "+ 
				"0 extra_hrs,0 arrear_days,0 ot_rate,0 ot_value,0 employer_pf,0 eps_pf,sum(t.basic_value+t.da_value+t.incen_value) grosspf,0  salarypf,  "+
				"sum(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incen_value+t.spl_incen_value) netwages, "+
				"0 absent_days,0 misc_value,0 lta_value,0 medical_value,0 incen_value,0 spl_incen_value,0 coupon_amt, "+
	  			"0 machine1_rate,0 machine2_rate,0 machine1_days,0 machine2_days,0 machine1_value,0 machine2_value,0 basicpf_value,0 prevdays "+
				"from arrear t where t.fin_year<=? and t.depo_code=? and t.cmp_code=? and t.arrear_mon=? and t.arrear_paid='Y' group by t.emp_code "+ 
				"union all "+
				"select t.emp_code,0 atten_days,0 totalwages, "+
				"sum(t.esic_value) esic_value,0 employer_esis_value,0 serialno,sum(t.pf_value) pf_value,0 advance,0 loan,0 stair_days,0 stair_alw,0 stair_value, "+ 
				"0 extra_hrs,0 arrear_days,0 ot_rate,0 ot_value,0 employer_pf,0 eps_pf,sum(t.basic_value+t.da_value+t.incen_value) grosspf,0  salarypf,  "+
				"sum(t.basic_value+t.da_value+t.hra_value+t.add_hra_value+t.incen_value+t.spl_incen_value) netwages, "+
				"0 absent_days,0 misc_value,0 lta_value,0 medical_value,0 incen_value,0 spl_incen_value,0 coupon_amt, "+
	  			"0 machine1_rate,0 machine2_rate,0 machine1_days,0 machine2_days,0 machine1_value,0 machine2_value,0 basicpf_value,0 prevdays "+
				"from arrear t where t.fin_year<=? and t.depo_code=? and t.cmp_code=? and t.arrear_mon=? and t.arrear_paid='O' group by t.emp_code) t,employeemast e "+ 
				"where  e.depo_Code=? and e.cmp_code=? and e.emp_code=t.emp_code group by t.emp_code "; 
  				
  				
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
  	  		  			"t.machine1_rate,t.machine2_rate,t.machine1_days,t.machine2_days,t.machine1_value,t.machine2_value,t.basicpf_value,0 prevdays "+
  	  					"from emptran t where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=?  and ifnull(t.del_tag,'')<>'D' ) t,employeemast e "+ 
  	  					"where  e.depo_Code=? and e.cmp_code=? and e.emp_code=t.emp_code group by t.emp_code "; 
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
  					arrearbasic = roundTwoDecimals((rs.getDouble(25)/30)*rs.getDouble(19));
  					arrearnet = roundTwoDecimals((rs.getDouble(4)/30)*rs.getDouble(19));
  				}
  				else if(repno==1)
  				{
  					arrearbasic = roundTwoDecimals((rs.getDouble(4)/30)*rs.getDouble(19));
  					arrearnet = roundTwoDecimals((rs.getDouble(4)/30)*rs.getDouble(19));
  				}
  				
  				//System.out.println("arrear basic "+arrearbasic+" 4 "+rs.getDouble(4)+" 19 "+rs.getDouble(19)); 
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
  				if(repno==1)
  				{
  					if(mnth_code>201906)
  					{
  						emp.setEsis_value(roundTwoDecimals(arrearbasic*0.75/100));
  						emp.setEmployer_esis_value(roundTwoDecimals(arrearbasic*3.25/100));

  					}
  					else
  					{
  						emp.setEsis_value(roundTwoDecimals(arrearbasic*1.75/100));
  						emp.setEmployer_esis_value(roundTwoDecimals(arrearbasic*4.75/100));
  					}
  				}
  				emp.setSerialno(sno++);
  				emp.setPf_value(rs.getDouble(12));
  				emp.setAdvance(rs.getDouble(13));
  				emp.setLoan(rs.getDouble(14));
  				emp.setStair_days(rs.getInt(15));
  				emp.setStair_alw(rs.getDouble(16));
  				emp.setStair_value(rs.getDouble(17));
  				emp.setExtra_hrs(rs.getDouble(18));
  				emp.setArrear_days(rs.getDouble(19));
  				emp.setOt_rate(rs.getDouble(20));
  				emp.setOt_value(rs.getDouble(21));
  				 
  				emp.setEmployee_pf(rs.getDouble(12));
  				emp.setEmployer_pf(rs.getDouble(22));
  				emp.setEps_pf(rs.getDouble(23));
  				emp.setBasic_value(rs.getDouble(24));
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
  				emp.setArrear_basic_value(arrearbasic);
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
			"e.machine1_rate,e.machine2_rate,t.machine1_days,t.machine2_days " +
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
		double atten_days,absent_days,arrear_days,extra_hrs,machine1_days,machine2_days,food_value;
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
 			"ifnull(e.bank,'Direct Cheque'),e.bank_add1,e.ifsc_code,ifnull(e.bank_accno,''),e.bank_code,t.coupon_amt,e.uan_no,t.machine1_value,t.machine2_value,t.machine1_days,t.machine2_days,sum(t.food_value) from "+
 			"(select t.emp_code,t.basic,t.da,t.hra,t.add_hra,t.incentive,t.spl_incentive,t.ot_rate,t.lta,t.medical,t.stair_alw, "+
 			"t.basic_value,t.da_value,t.hra_value,t.add_hra_value,t.incentive_value,t.spl_incen_value,t.ot_value,t.lta_value, "+
 			"t.medical_value,t.pf_value,t.advance,t.loan,t.esis_value,t.atten_days,t.arrear_days,t.absent_days,t.stair_days,t.extra_hrs,t.misc_value,t.stair_value, "+ 
 			"t.coupon_amt,t.machine1_value,t.machine2_value,t.machine1_days,t.machine2_days,t.food_value from emptran t "+
 			"where t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and t.atten_days<>0 and ifnull(t.del_tag,'')<>'D' "+  
 			"union all "+
 			"select a.emp_code,0 basic,0 da,0 hra,0 add_hra,0 incentive,0 spl_incentive,0 ot_rate,0 lta,0 medical,0 stair_alw,"+ 
 			"sum(basic_value),sum(da_value),sum(hra_value),sum(add_hra_value),sum(incen_value),sum(spl_incen_value),0 ot_value,0 lta_value,"+ 
 			"0 medical_value,sum(pf_value),0 advance,0 loan,sum(esic_value),0 atten_days,0 arrear_days,0 absent_days,0 stair_days,0 extra_hrs,0 misc_value,0 stair_value,"+ 
 			"0 coupon_amt,0 machine1_value,0 machine2_value,0 machine1_days,0 machine2_days,0 food_value from arrear a "+
 			"where fin_year<=? and depo_code=? and cmp_code=? and arrear_mon=? and arrear_paid='Y' group by emp_code "+  
 			"union all "+
 			"select a.emp_code,0 basic,0 da,0 hra,0 add_hra,0 incentive,0 spl_incentive,0 ot_rate,0 lta,0 medical,0 stair_alw,"+ 
 			"sum(basic_value),sum(da_value),sum(hra_value),sum(add_hra_value),sum(incen_value),sum(spl_incen_value),0 ot_value,0 lta_value,"+ 
 			"0 medical_value,sum(pf_value),0 advance,0 loan,sum(esic_value),0 atten_days,0 arrear_days,0 absent_days,0 stair_days,0 extra_hrs,0 misc_value,0 stair_value,"+ 
 			"0 coupon_amt,0 machine1_value,0 machine2_value,0 machine1_days,0 machine2_days,0 food_value from arrear a "+
 			"where fin_year<=? and depo_code=? and cmp_code=? and arrear_mon=? and arrear_paid='O' group by emp_code) t,employeemast e "+  
 			"where  e.depo_code=? and e.cmp_code=? and e.emp_code=t.emp_code and ifnull(e.del_tag,'')<>'D'  group by t.emp_code "+orderby;

 			
 		 


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
			
 			while (rs.next())
 			{
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
 				

 				
 				emp.setBasic_value(rs.getDouble(16));
 				emp.setDa_value(rs.getDouble(17));
 				emp.setHra_value(rs.getDouble(18));
 				emp.setAdd_hra_value(rs.getDouble(19));
 				emp.setIncentive_value(rs.getDouble(20));
 				emp.setSpl_incen_value(rs.getDouble(21));
 				emp.setOt_value(rs.getDouble(22));
 				emp.setLta_value(rs.getDouble(23));
 				emp.setMedical_value(rs.getDouble(24));
 				emp.setPf_value(rs.getDouble(25));
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
 				
 				basic+=rs.getDouble(16);
 				da+=rs.getDouble(17);
 				hra+=rs.getDouble(18);
 				add_hra+=rs.getDouble(19);
 				incentive+=rs.getDouble(20);
 				spl_incentive+=rs.getDouble(21);
 				ot+=rs.getDouble(22);
 				lta+=rs.getDouble(23);
 				medical+=rs.getDouble(24);
 				pf+=rs.getDouble(25);
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
  		String field="absent_days";
  		if(repno==2)
  			 field="extra_hrs";
  		else if(repno==3)
 			 field="stair_days";
  		else if(repno==4)
			 field="atten_days";
  		 
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

  			String query="select e.emp_code,e1.emp_name,sum(e.apr),sum(e.may),sum(e.jun),sum(e.jul),sum(e.aug),sum(e.sep),sum(e.octt),sum(e.nov),sum(e.decc),sum(e.jan),sum(e.feb),sum(e.mar) from "+ 
			"(select emp_code,"+field+"  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
			"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all "+
			"select emp_code,0  apr,"+field+" may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
			"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all "+
			"select emp_code,0  apr,0 may,"+field+" jun,0 jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
			"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all "+
			"select emp_code,0  apr,0 may,0 jun,"+field+" jul,0 aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
			"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all "+
			"select emp_code,0  apr,0 may,0 jun,0 jul,"+field+" aug, 0 sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
			"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all "+
			"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, "+field+" sep, 0 octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
			"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all "+
			"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, "+field+" octt, 0 nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
			"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all "+
			"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, "+field+"  nov, 0 decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
			"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all "+
			"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, "+field+" decc, 0 jan, 0 feb, 0 mar  from emptran "+ 
			"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all "+
			"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, 0 decc, "+field+" jan, 0 feb, 0 mar  from emptran "+ 
			"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all "+
			"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, 0 decc, 0 jan, "+field+" feb, 0 mar  from emptran "+ 
			"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=? "+
			"union all "+
			"select emp_code,0  apr,0 may,0 jun,0 jul,0 aug, 0 sep, 0 octt, 0  nov, 0 decc, 0 jan, 0 feb, "+field+" mar  from emptran "+ 
			"where fin_year=? and depo_code=? and cmp_code=? and mnth_code=?) e, employeemast e1 "+
			"where e1.depo_code=? and e1.cmp_code=? and e1.emp_code=e.emp_code group by e.emp_code "; 


  			 
  			ps = con.prepareStatement(query);
  			k=1;
  			for(int j=0;j<12;j++)
  			{
  	  			ps.setInt(k++, fyear);
  	  			ps.setInt(k++, depo_code);
  	  			ps.setInt(k++, cmp_code);
  	  			ps.setInt(k++, mon[j]);
  				
  			}
	  			ps.setInt(k++, depo_code);
	  			ps.setInt(k++, cmp_code);
  			
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

  		} catch (Exception ex) {
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
		
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();

			String query1="update emptran e1, "+
			"(select emp_code,gross,basic,da,hra,add_hra,incentive,spl_incentive,lta,medical,ot_rate,stair_alw  from employeemast where depo_code=? and cmp_code=?) e2 "+
			"set e1.gross=e2.gross,e1.basic=e2.basic,e1.da=e2.da,e1.hra=e2.hra,e1.add_hra=e2.add_hra,e1.incentive=e2.incentive,e1.spl_incentive=e2.spl_incentive,"+
			"e1.lta=e2.lta,e1.medical=e2.medical,e1.ot_rate=e2.ot_rate,e1.stair_alw=e2.stair_alw "+
			"where e1.fin_year=? and e1.depo_code=? and e1.cmp_code=? and e1.mnth_code=? and  e1.emp_code=e2.emp_code"; 
			
			
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
			
			for(int j=1;j<7;j++)
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
	  	  
	    	Connection con=null;
	    	PreparedStatement ps3 = null;
	    	PreparedStatement ps2 = null;
	    	PreparedStatement ps1 = null;
	    	PreparedStatement ps4 = null;
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

				String query3="insert into arrear "+
				"select a.depo_code,a.cmp_code,a.emp_code,a.basic,a.da,a.hra,a.ahra,a.incen,a.splincen, "+
				"if(a.absent_days=0,(round(a.basic)+round((a.basic*a.arrear_days)/30)),round((a.basic+(a.basic*(a.arrear_days-a.absent_days)/30)))) bvalue, "+
				"if(a.absent_days=0,(round(a.da)+round((a.da*a.arrear_days)/30)),round((a.da+(a.da*(a.arrear_days-a.absent_days)/30)))) davalue, "+
				"if(a.absent_days=0,(round(a.hra)+round((a.hra*a.arrear_days)/30)),round((a.hra+(a.hra*(a.arrear_days-a.absent_days)/30)))) hravalue, "+
				"if(a.absent_days=0,(round(a.ahra)+round((a.ahra*a.arrear_days)/30)),round((a.ahra+(a.ahra*(a.arrear_days-a.absent_days)/30)))) ahravalue, "+
				"if(a.absent_days=0,(round(a.incen)+round((a.incen*a.arrear_days)/30)),round((a.incen+(a.incen*(a.arrear_days-a.absent_days)/30)))) incenvalue, "+
	 			"if(a.absent_days=0,(round(a.splincen)+round((a.splincen*a.arrear_days)/30)),round((a.splincen+(a.splincen*(a.arrear_days-a.absent_days)/30)))) sincenvalue, "+
				"a.atten_days,"+cmon+","+smon+","+emon+",a.mnth_code,0.00,0.00,0.00,"+fyear+",a.oldwages,a.newwages,a.arrear_days,'N' arrearpaid "+
				" from  "+
				"(select e.depo_code,e.cmp_code,e.emp_code,(e.basic-t.basic) basic,(e.da-t.da) da,(e.hra-t.hra) hra,(e.add_hra-t.add_hra) ahra ,(e.incentive-t.incentive) incen "+
				",(e.spl_incentive-t.spl_incentive) splincen,t.atten_days,t.mnth_code,day(last_day(concat(left(t.mnth_code,4),'-',right(t.mnth_code,2),'-01'))) mdays," +
				"t.arrear_days,t.oldwages,(e.basic+e.da+e.hra+e.add_hra+e.incentive+e.spl_incentive) newwages,t.absent_days from employeemast e "+
				"left join  "+
				"(select emp_code,basic,da,hra,add_hra,incentive,spl_incentive,atten_days,mnth_code,if(mnth_code="+smon+",0,arrear_days) arrear_days,(basic+da+hra+add_hra+incentive+spl_incentive) oldwages,absent_days from emptran "+ 
				"where depo_code=? and cmp_code=? and atten_days<>0 and mnth_code between ? and ? group by emp_code,mnth_Code) t "+
				"on e.emp_Code=t.emp_code where e.depo_code=? and e.cmp_code=? and e.doresign is null and(t.basic-e.basic) is not null ) a"; 

				
				
				//String query2="update arrear set pf_value=round(round((basic_value+da_value+incen_value)*12/100,2)),esic_value=round((basic_value+da_value+hra_value+add_hra_value+incen_value+spl_incen_value)*1.75/100) "+ 
				//"where fin_year=? and depo_code=? and cmp_code=? ";
				
				String query2="update arrear set pf_value=round(round((basic_value+da_value+incen_value)*12/100,2)),esic_value=round((basic_value+da_value+hra_value+add_hra_value+incen_value+spl_incen_value)*0.75/100) "+ 
				"where fin_year=? and depo_code=? and cmp_code=? ";
				if(cmon>2020044) // yashpal baghela 26/11/2020 
					 query2="update arrear set pf_value=round(round((basic_value+da_value+incen_value)*10/100,2)),esic_value=round((basic_value+da_value+hra_value+add_hra_value+incen_value+spl_incen_value)*0.75/100) "+ 
					 "where fin_year=? and depo_code=? and cmp_code=? ";
					
				
				String query4="update arrear a,emptran e set a.arrear_paid='Y' "+ 
				"where a.fin_year=? and a.depo_code=? and a.cmp_code=? and a.arrear_mon=? "+
				"and e.fin_year>=? and e.depo_code=? and e.cmp_code=? and a.emp_code=e.emp_Code and e.mnth_code=? and e.atten_days<>0 ";

				
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
				
				con.commit();
				con.setAutoCommit(true);
				ps3.close();
				ps2.close();
				ps1.close();
				ps4.close();
				
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
				
				//New Query implemented on 07/07/2020 (Yashpal) 
				String query = "select e.emp_name,monthname(concat(left(a.mnth_code,4),'-',right(a.mnth_code,2),'-01')) mon,e.esic_no,e.pf_no,a.emp_code, "+
				"(a.basic_value+a.da_value+a.hra_value+a.add_hra_value+a.incen_Value+a.spl_incen_value)-(a.pf_value+a.esic_value) net,a.mnth_code,a.arrear_paid  "+
				"from arrear a, employeemast e,emptran t  "+
				"where a.fin_year=? and a.depo_code=? and a.cmp_code=? and a.mnth_code between ? and ? "+
				"and e.depo_code=? and e.cmp_code=? and a.emp_code=e.emp_code and a.arrear_paid=? "+
				"and t.fin_year=? and t.depo_code=? and t.cmp_code=? and t.mnth_code=? and t.atten_days>0 and t.atten_lock=1  and t.emp_code=e.emp_code and t.emp_code=a.emp_code "+
				"and ifnull(t.del_tag,'')<>'D' ";


				ps = con.prepareStatement(query);
				ps.setInt(1, ayear); 
				ps.setInt(2, depo_code);
				ps.setInt(3, cmp_code);
				ps.setInt(4, smon);
				ps.setInt(5, emon);
				ps.setInt(6, depo_code);
				ps.setInt(7, cmp_code);
				ps.setString(8, paid);
				ps.setInt(9, fyear);
				ps.setInt(10, depo_code);
				ps.setInt(11, cmp_code);
				ps.setInt(12, mnth_code);
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
		  		 
		  		 
		  		try 
		  		{
		  			con=ConnectionFactory.getConnection();
		  			con.setAutoCommit(false);
		  		 

		  			String query=" select a.emp_code,e.emp_name,monthname(concat(left(a.mnth_code,4),'-',right(a.mnth_code,2),'-01')) mon,a.arrear_atten,a.basic_value,a.da_value, "+
					"a.hra_value,a.add_hra_value,a.incen_Value,a.spl_incen_value,(a.basic_value+a.da_value+a.hra_value+a.add_hra_value+a.incen_Value+a.spl_incen_value) totearn, "+
					"a.pf_value,a.esic_value,(a.pf_value+a.esic_value) totded,a.arrear_days,a.oldwages,a.newwages "+
					"from arrear a, employeemast e "+
					"where a.fin_year=? and a.depo_code=? and a.cmp_code=? and a.mnth_code between ? and ? "+
					"and e.depo_code=? and e.cmp_code=? and a.emp_code=e.emp_code and a.arrear_paid=? ";

		  			 
		  			ps = con.prepareStatement(query);
		  			ps.setInt(1, fyear);
	  	  			ps.setInt(2, depo_code);
	  	  			ps.setInt(3, cmp_code);
	  	  			ps.setInt(4, smon);
	  	  			ps.setInt(5, emon);
	  	  			ps.setInt(6, depo_code);
	  	  			ps.setInt(7, cmp_code);
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
					" where  fin_year=? and depo_code=? and cmp_code=? and  mnth_code=? and emp_code=? and arrear_paid in ('N','O') " ;

					//String query2="update emptran set bonus_per=?,bonus_value=? " +
					//" where  fin_year=? and depo_code=? and cmp_code=? and  mnth_code=? and emp_code=? " ;
				
					con.setAutoCommit(false);
					
					ps1 = con.prepareStatement(query1);
					//ps2 = con.prepareStatement(query2);
					
					int s=attnlist.size();
					for (int j=0;j<s;j++)
					{
						emp= (EmptranDto) attnlist.get(j);

						System.out.println(emp.getFin_year());
						System.out.println(emp.getDepo_code());
						System.out.println(emp.getCmp_code());
						System.out.println(emp.getMnth_code());
						System.out.println(emp.getEmp_code());
						
						
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
					String name[]={"zero","Sterile Days","Advance Entry","Other Allowance","Canteen Coupon","LTA/Medical","Machine Operator Entry"};
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
						while (rs.next())
						{
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


			    
			    public ArrayList getSalaryDetail(int depo_code,int cmp_code,int fyear,int repno,int mnth_code)
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


			    		String query="Select MONTHNAME(e.doc_date) AS 'Month Name',e.fin_year,e.emp_code,m.pf_no,m.uan_no,m.esic_no,m.emp_name,M.DESIGNATION,e.basic,e.da,e.hra,e.add_hra,e.incentive,(e.basic+e.da+e.hra+e.add_hra+e.incentive) btotal,"+
	    				"e.atten_days,e.basic_value,e.da_value,e.hra_value,e.add_hra_value,e.incentive_value, "+
	    				"(e.basic_value+e.da_value+e.hra_value+e.add_hra_value+e.incentive_value+e.food_value) grosstotal,e.arrear_days, "+
	    				"e.ot_rate,e.extra_hrs,e.ot_value,e.pf_value,e.esis_value,(e.pf_value+e.esis_value) totded, "+
	    				"(e.basic_value+e.da_value+e.hra_value+e.add_hra_value+e.incentive_value+e.spl_incen_value+e.lta_value+e.medical_value+e.stair_value+e.ot_value+e.food_value-e.pf_value-e.esis_value) net,e.food_value "+
	    				"from emptran e, employeemast m where e.fin_year=? and e.cmp_code=?  and e.mnth_code=? and e.atten_days<>0 and ifnull(e.del_tag,'')<>'D' and m.cmp_code=? and e.emp_code=m.emp_code ";

			    		// for arrear table query implement later after discussion
/*			    		select mnth_code,emp_code,arrear_days,arrear_atten,arrear_mon,
			    		sum(basic_value),sum(da_value),sum(hra_value),sum(add_hra_value),sum(incen_value),sum(spl_incen_value) from arrear 
			    		where fin_year=2018 and depo_code=10 and cmp_code=1 AND EMP_CODE=8  group by emp_code,mnth_code;
*/
			    		

			    		ps = con.prepareStatement(query);
			    		ps.setInt(1, fyear);
			    		ps.setInt(2, cmp_code);
			    		ps.setInt(3, mnth_code);
			    		ps.setInt(4, cmp_code);
			    		rs =ps.executeQuery();

			    		v = new ArrayList();

			    		while (rs.next())
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
			    			emp.setMisc_value(rs.getDouble(21)); // Total Wages Paid before deduction
			    			emp.setArrear_days(rs.getDouble(22));
			    			emp.setOt_rate(rs.getDouble(23));
			    			emp.setExtra_hrs(rs.getDouble(24));
			    			emp.setOt_value(rs.getDouble(25));
			    			emp.setPf_value(rs.getDouble(26));
			    			emp.setEsis_value(rs.getDouble(27));
			    			emp.setNet_value(rs.getDouble(29));
			    			emp.setFood_value(rs.getDouble(30));
			    			
			    			
			    		
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


	public double roundTwoDecimals(double d) 
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
