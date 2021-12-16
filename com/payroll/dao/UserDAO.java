package com.payroll.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.payroll.dto.ContractMastDto;
import com.payroll.dto.DivisionDto;
import com.payroll.dto.LoginDto;
import com.payroll.dto.UserDto;


public class UserDAO 
{
	 


	public Vector getPackage(int uid)
	{
		PreparedStatement ps = null;
		ResultSet rs=null;
		Connection con=null;
		Vector v =null;
		try 
		{
			con=ConnectionFactory.getConnection();
			con.setAutoCommit(false);

			if(uid==0)
				uid=1;

			String divQ = "select pack_code,pack_name from packagemast where pack_code in "+ 
					"(select pack_id from userpack where user_id=? and status='Y') ";
			ps = con.prepareStatement(divQ);
			ps.setInt(1, uid);
			rs =ps.executeQuery();
			DivisionDto dv=null;
			v = new Vector();
			while (rs.next())
			{
				dv = new DivisionDto();
				dv.setDiv_code(rs.getInt(1));
				dv.setDiv_name(rs.getString(2));
				v.add(dv);
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
			System.out.println("-------------Exception in UserDAO.getPackage " + ex);

		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : " );

				if(rs != null){rs.close();}
				if(ps != null){ps.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in UserDAO.Connection.close --- getPackage "+e);
			}
		}
		return v;
	}


	public Vector getBranch(int uid)
	{
		PreparedStatement ps = null;
		ResultSet rs=null;
		Connection con=null;
		Vector v =null;
		int newcode=0;
		try 
		{
			con=ConnectionFactory.getConnection();
			con.setAutoCommit(false);

			String brnQ = "select depo_code,cmp_add1,cmp_add2,cmp_add3,cmp_city,cmp_phone,cmp_fax,cmp_email," +
					"cmp_code,cmp_name,cmp_abvr,license_no from contractormast where cmp_code in " +
					"(select depo_code from userdepo where user_id=? and status='Y') "+
					"order by cmp_code" ;



			ps = con.prepareStatement(brnQ);
			ps.setInt(1, uid);
			rs =ps.executeQuery();
			ContractMastDto brn=null;
			v = new Vector();
			while (rs.next())
			{
				brn = new ContractMastDto();
				brn.setDepo_code(rs.getInt(1));
				brn.setCmp_name(rs.getString(10));
				brn.setCmp_add1(rs.getString(2));
				brn.setCmp_add2(rs.getString(3));
				brn.setCmp_add3(rs.getString(4));
				brn.setCmp_city(rs.getString(5));
				brn.setCmp_phone(rs.getString(6));
				brn.setCmp_fax(rs.getString(7));
				brn.setCmp_email(rs.getString(8));
				brn.setCmp_code(rs.getInt(9));
				brn.setCmp_abvr(rs.getString(11));
				brn.setLicense_no(rs.getString(12));
				v.add(brn);
				newcode=rs.getInt(9);
			}

				brn = new ContractMastDto();
				brn.setDepo_code(10);
				brn.setCmp_name("NEW CONTRACTOR");
				brn.setCmp_code(99);
				brn.setNewcode(newcode+1);
				v.add(brn);

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
			System.out.println("-------------Exception in UserDAO.getBranch " + ex);

		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : " );

				if(rs != null){rs.close();}
				if(ps != null){ps.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in UserDAO.Connection.close "+e);
			}
		}
		return v;
	}


	
	
	

	public int getUserId(String lname,String pass)
	{
		PreparedStatement ps2 = null;
		ResultSet rs=null;
		Connection con=null;
		int uid=0;
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
				uid=rs.getInt(1);
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
			System.out.println("-------------Exception in UserDAO.getUserId " + ex);

		}
		finally {
			try {

				if(rs != null){rs.close();}
				if(ps2 != null){ps2.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in UserDAO.getUserId.Connection.close "+e);
			}
		}
		return uid;
	}

	public Vector getUserRights(String tp)
	{
		PreparedStatement ps = null;
		ResultSet rs=null;
		Connection con=null;
		Vector v =null;
		try 
		{
			con=ConnectionFactory.getConnection();
			con.setAutoCommit(false);

/*			String menu="select aa.*,p.pack_name from (select a.*,s.smenu_code,ifnull(s.smenu_name,'') from " + 
					"(select m.pack_code,t.tab_code,t.tab_name,m.menu_code,m.menu_name from tabmaster t,menumaster m " +
					"where t.tab_code=m.tab_code and m.pack_code < 5 and m.tran_type=? and m.class_name is not null  order by m.pack_code,m.tab_code,m.menu_code) a " +
					"left join submenumaster s on  a.pack_code=s.pack_Code and a.tab_code=s.tab_code and a.menu_code=s.menu_code "+
					"and s.tran_type=? ) aa, packagemast p where aa.pack_code=p.pack_code and p.status='Y' ";
*/
			String menu="select a.*,p.pack_name from  " + 
					"(select m.pack_code,t.tab_code,t.tab_name,m.menu_code,m.menu_name from tabmaster t,menumaster m " +
					" where t.tab_code=m.tab_code and m.pack_code < 5 and m.tran_type=? and m.class_name is not null  order by m.pack_code,m.tab_code,m.menu_code) a, " +
					" packagemast p where a.pack_code=p.pack_code and p.status='Y' ";

			
			ps = con.prepareStatement(menu);
			ps.setString(1, tp);
			rs =ps.executeQuery();
			UserDto usr=null;
			v = new Vector();
			while (rs.next())
			{
				usr = new UserDto();
				usr.setPack_code(rs.getInt(1));
				usr.setTab_code(rs.getInt(2));
				usr.setTab_name(rs.getString(3));
				usr.setMenu_code(rs.getInt(4));
				usr.setMenu_name(rs.getString(5).trim());
				usr.setPack_name(rs.getString(6));

				v.add(usr);
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
			System.out.println("-------------Exception in UserDAO.getUserRights " + ex);

		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : " );

				if(rs != null){rs.close();}
				if(ps != null){ps.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in UserDAO.Connection.close "+e);
			}
		}
		return v;
	}


	public int createNewUser(LoginDto ldto)
	{
		PreparedStatement ps = null;
		ResultSet rs=null;
		Connection con=null;

		int userID=0;
		try 
		{
			con=ConnectionFactory.getConnection();
			con.setAutoCommit(false);

			String loginTable=" insert into login " +
					" (login_name,login_pass,status,prefix,fname,lname,designation,department,emailid,login_mnth,login_year) " +
					" values(?,md5(?),?,?,?,?,?,?,?,MONTH(CURDATE()),(select max(fin_year) from perdmast where fin_year=YEAR(CURDATE()))) ";


			ps= con.prepareStatement(loginTable, Statement.RETURN_GENERATED_KEYS);  
			ps.setString(1,ldto.getLogin_name());
			ps.setString(2,ldto.getLogin_pass());
			ps.setString(3,ldto.getIsActive());
			ps.setString(4,ldto.getPrefix());
			ps.setString(5,ldto.getFname());
			ps.setString(6,ldto.getLname());
			ps.setString(7,ldto.getDesignation());
			ps.setString(8,ldto.getDepartment());
			ps.setString(9,ldto.getEmaiId());

			ps.executeUpdate();  
			rs = ps.getGeneratedKeys();    
			rs.next();  
			userID = rs.getInt(1);

			rs.close();
			rs=null;

			//insert user branches
			ps.close();
			ps=null;

			String userBranch = "insert into userdepo values(?,?,?)";
			ps=con.prepareStatement(userBranch);
			for(ContractMastDto bdo : ldto.getBranchList())
			{
				ps.setInt(1,userID);
				ps.setInt(2,bdo.getDepo_code());
				ps.setString(3,"Y");
				ps.executeUpdate();
			}

			//insert all package for user
			ps.close();
			ps=null;

			String insertAllPackage="insert into userpack (select "+userID+", pack_code,'N' stattus from packagemast where pack_code < 4 )";
			ps=con.prepareStatement(insertAllPackage);
			ps.executeUpdate();
			
			//update user packaes
			ps.close();
			ps=null;

			String userPackage = "update userpack set status=? where user_id=? and pack_id=?";
			ps=con.prepareStatement(userPackage);
			for(int value : ldto.getPackageList())
			{
				ps.setString(1,"Y");
				ps.setInt(2,userID);
				ps.setInt(3,value);
				
				ps.executeUpdate();
				
			}

			//insert all reports for user (sales/sample/accounts).
			ps.close();
			ps=null;
			
			String insertAllReports="insert into userrights (select "+userID+" as uid,m.pack_code,m.tab_code,m.menu_code," +
			"0 smenu_code,'N' stattus from menumaster m "+
			" where m.pack_code<5 order by m.pack_code,m.tab_code,m.menu_code,smenu_code)";
			ps=con.prepareStatement(insertAllReports);
			ps.executeUpdate();
			
			//updated user reports
			ps.close();
			ps=null;

			
			String userReport = "update userrights set status=? where user_id=? and pack_code=? and tab_code=? and menu_code=? and smenu_code=?";
			ps=con.prepareStatement(userReport);
			for(UserDto udt : ldto.getReportList())
			{
				ps.setString(1,udt.getStatus());
				ps.setInt(2,userID);
				ps.setInt(3,udt.getPack_code());
				ps.setInt(4,udt.getTab_code());
				ps.setInt(5,udt.getMenu_code());
				ps.setInt(6,udt.getSmenu_code());
			
				ps.executeUpdate();
			}


			con.commit();
			con.setAutoCommit(true);

			ps.close();

		} 
		catch (Exception ex) 
		{   ex.printStackTrace();
			try 
			{
				con.rollback();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			System.out.println("-------------Exception in UserDAO.createNewUser " + ex);
			userID=0;
		}
		finally 
		{
			try 
			{
				System.out.println("No. of Records Update/Insert : " );

				if(rs != null){rs.close();}
				if(ps != null){ps.close();}
				if(con != null){con.close();}
			} 
			catch (SQLException e) 
			{
				System.out.println("-------------Exception in UserDAO.Connection.close "+e);
			}
		}
		return userID;
	}

	public int copyUserRightFromExistingUser(LoginDto ldto)
	{
		PreparedStatement ps = null;
		ResultSet rs=null;
		Connection con=null;

		int userID=0;
		try 
		{
			con=ConnectionFactory.getConnection();
			con.setAutoCommit(false);

			String loginTable=" insert into login " +
					" (login_name,login_pass,status,prefix,fname,lname,designation,department,emailid,login_mnth,login_year) " +
					" values(?,md5(?),?,?,?,?,?,?,?,MONTH(CURDATE()),(select mkt_year from perdmast where fin_year=YEAR(CURDATE()) and mm=MONTH(CURDATE())) ) ";


			ps= con.prepareStatement(loginTable, Statement.RETURN_GENERATED_KEYS);  
			ps.setString(1,ldto.getLogin_name());
			ps.setString(2,ldto.getLogin_pass());
			ps.setString(3,ldto.getIsActive());
			ps.setString(4,ldto.getPrefix());
			ps.setString(5,ldto.getFname());
			ps.setString(6,ldto.getLname());
			ps.setString(7,ldto.getDesignation());
			ps.setString(8,ldto.getDepartment());
			ps.setString(9,ldto.getEmaiId());

			ps.executeUpdate();  
			rs = ps.getGeneratedKeys();    
			rs.next();  
			userID = rs.getInt(1);

			rs.close();
			rs=null;


			
			CallableStatement callableStatement = con.prepareCall("{ call copyUserRights(?,?) }");
			callableStatement.setInt(1, ldto.getLogin_id()); //existin user
			callableStatement.setInt(2, userID);//new user
			
			callableStatement.execute();


			con.commit();
			con.setAutoCommit(true);

			ps.close();

		} 
		catch (Exception ex) 
		{
			try 
			{
				con.rollback();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			System.out.println("-------------Exception in UserDAO.createNewUser " + ex);
			ex.printStackTrace();
			userID=0;
		}
		finally 
		{
			try 
			{
				System.out.println("No. of Records Update/Insert : " );

				if(rs != null){rs.close();}
				if(ps != null){ps.close();}
				if(con != null){con.close();}
			} 
			catch (SQLException e) 
			{
				System.out.println("-------------Exception in UserDAO.Connection.close "+e);
			}
		}
		return userID;
	}
	
	
	
	public Vector<Object> getUserForUpdation()
	{
		PreparedStatement ps = null;
		ResultSet rs=null;
		ResultSet rs1=null;

		Connection con=null;
		Vector<Object> userList=null;
		List<ContractMastDto> branchVector=null;
		List<Integer> packList=null;
		List<Integer> divList=null;
		List<UserDto> reportList=null;
		int count=0;
		int pack[]=new int[10];
		try   
		{
			con=ConnectionFactory.getConnection();
			con.setAutoCommit(false);

			String loginTable="select login_id,login_name,login_pass,status,prefix,fname,lname,designation,department,emailid from login where login_name <> 'admin'";

			String brnQ = "select c.depo_code,c.cmp_add1,c.cmp_add2,c.cmp_add3,c.cmp_city," +
					"c.cmp_phone,c.cmp_fax,c.cmp_email,c.cmp_code,cmp_name,ifnull(d.status,'N') stus " +
					" from contractormast c left join userdepo d on c.cmp_code=d.depo_code and  d.user_id=? " +
					" order by c.cmp_code " ;

//			String packQ="select * from userpack where user_id=? and status='Y'";

			String packQ="select u.user_id,u.pack_id,ifnull(u.status,'N')  from packagemast p left join userpack u on p.pack_code=u.pack_id and u.user_id=? and u.status='Y'";

//			String divisionQ="select * from userdiv where user_id=? and status='Y'";
			

	

			
			String userRightsQ= "select t.*,ifnull(u.status,'N')  from  "+
			"(select a.*,p.pack_name from "+ 
			"(select m.pack_code,t.tab_code,t.tab_name,m.menu_code,m.menu_name,m.tran_type from tabmaster t,menumaster m "+  
			"where t.tab_code=m.tab_code and m.pack_code < 4  ) a   "+
			",packagemast p where a.pack_code=p.pack_code and p.status='Y') t left join userrights u "+
			" on  u.pack_code=t.pack_code and u.tab_code=t.tab_code and u.menu_code=t.menu_code  and u.user_id=? "+
			" order by t.pack_code,t.tab_code,t.menu_code ";  

			
/*			String userRightsQ= "select t.*,ifnull(u.status,'N')  from  "+
			"(select aa.*,p.pack_name from "+ 
			"(select a.*,ifnull(s.smenu_code,0) smenu_code,ifnull(s.smenu_name,'') smenu_name,s.serialno sno from "+   
			"(select m.pack_code,t.tab_code,t.tab_name,m.menu_code,m.menu_name,m.tran_type,m.serialno from tabmaster t,menumaster m "+  
			"where t.tab_code=m.tab_code and m.pack_code < 4  ) a   "+
			"left join submenumaster s on  a.pack_code=s.pack_Code and a.tab_code=s.tab_code and a.menu_code=s.menu_code) aa "+
			",packagemast p where aa.pack_code=p.pack_code and p.status='Y') t left join userrights u "+
			" on  u.pack_code=t.pack_code and u.tab_code=t.tab_code and u.menu_code=t.menu_code and u.smenu_code=t.smenu_code and u.user_id=? "+
			" order by t.pack_code,t.tab_code,t.serialno,t.menu_code,t.sno,t.smenu_code ";
*/			
			Statement st=con.createStatement();
			rs = st.executeQuery(loginTable);
			userList = new Vector<Object>();
			LoginDto ldto =null;
			while(rs.next())
			{

				ldto = new LoginDto();

				ldto.setLogin_id(rs.getInt(1));
				ldto.setLogin_name(rs.getString(2));
				ldto.setLogin_pass(rs.getString(3));
				ldto.setIsActive(rs.getString(4));
				ldto.setPrefix(rs.getString(5));
				ldto.setFname(rs.getString(6));
				ldto.setLname(rs.getString(7));
				ldto.setDesignation(rs.getString(8));
				ldto.setDepartment(rs.getString(9));
				ldto.setEmaiId(rs.getString(10));


				ps = con.prepareStatement(brnQ);
				ps.setInt(1, rs.getInt(1));
				rs1 =ps.executeQuery();
				ContractMastDto brn=null;
				branchVector = new ArrayList<ContractMastDto>();
				while (rs1.next())
				{
					brn = new ContractMastDto();
					brn.setDepo_code(rs1.getInt(1));
					brn.setCmp_name(rs1.getString(10));
					brn.setCmp_add1(rs1.getString(2));
					brn.setCmp_add2(rs1.getString(3));
					brn.setCmp_add3(rs1.getString(4));
					brn.setCmp_city(rs1.getString(5));
					brn.setCmp_phone(rs1.getString(6));
					brn.setCmp_fax(rs1.getString(7));
					brn.setCmp_email(rs1.getString(8));
					brn.setCmp_code(rs1.getInt(9));
					brn.setStatus(rs1.getString(11)); 
					branchVector.add(brn);
				}
				rs1.close();
				ps.close();

				ps=null;
				rs1=null;
				ldto.setBranchList(branchVector);



				//User Selected Package...
				ps = con.prepareStatement(packQ);
				ps.setInt(1, rs.getInt(1));
				rs1 =ps.executeQuery();
				packList = new ArrayList<Integer>();
				count=0;
				while (rs1.next())
				{
					packList.add(rs1.getInt(2));
					if (rs1.getString(3).equalsIgnoreCase("Y"))
					{
						pack[count]=rs1.getInt(2);
						count++;
						
					}
					
				}

				rs1.close();
				ps.close();

				ps=null;
				rs1=null;
				ldto.setPackageList(packList);


				

				//User Selected Reports...

				ps = con.prepareStatement(userRightsQ);
				ps.setInt(1, rs.getInt(1));
				rs1 =ps.executeQuery();
				UserDto udto=null;
				reportList = new ArrayList<UserDto>();
				boolean addRecord=false;
				while (rs1.next())
				{
					for (int i=0;i<count;i++)
					{
						if(rs1.getInt(1)==pack[i])
						{
							addRecord=true;
							break;
						}
					}
					if (addRecord)
					{
						udto = new UserDto();
						udto.setPack_code(rs1.getInt(1));
						udto.setTab_code(rs1.getInt(2));
						udto.setTab_name(rs1.getString(3));
						udto.setMenu_code(rs1.getInt(4));
						//					udto.setMenu_name(rs1.getString(5));
						udto.setMenu_name(rs1.getString(5).trim());
						udto.setTran_type(rs1.getString(6));
						udto.setStatus(rs1.getString(8));
						udto.setPack_name(rs1.getString(7));
						reportList.add(udto);
						addRecord=false;
					}
					
					
				}
				rs1.close();
				ps.close();

				ps=null;
				rs1=null;
				ldto.setReportList(reportList);
				
				
				//adding logindto object in userlist...
				userList.add(ldto);

			}




			con.commit();
			con.setAutoCommit(true);

			 

		} 
		catch (Exception ex) 
		{
			try 
			{

				con.rollback();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			System.out.println("-------------Exception in UserDAO.createNewUser " + ex);

		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : " );

				if(rs != null){rs.close();}
				if(ps != null){ps.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in UserDAO.Connection.close "+e);
			}
		}
		return userList;
	}


	public int updateUser(LoginDto ldto)
	{
		PreparedStatement ps = null;
		ResultSet rs=null;
		Connection con=null;

		int userID=0;
		try 
		{
			con=ConnectionFactory.getConnection();
			con.setAutoCommit(false);

			String loginTable=" update login set status=?,prefix=?,fname=?,lname=?,designation=?,department=?,emailid=? where login_id=?  ";

			ps= con.prepareStatement(loginTable);  
			
			ps.setString(1,ldto.getIsActive());
			ps.setString(2,ldto.getPrefix());
			ps.setString(3,ldto.getFname());
			ps.setString(4,ldto.getLname());
			ps.setString(5,ldto.getDesignation());
			ps.setString(6,ldto.getDepartment());
			ps.setString(7,ldto.getEmaiId());
			ps.setInt(8,ldto.getLogin_id());
			
			userID=ps.executeUpdate();  
			

			

			//insert user branches
			ps.close();
			ps=null;

			
			String userDepoStatusN = "delete from userdepo where user_id=? ";
			ps=con.prepareStatement(userDepoStatusN);
			ps.setInt(1,ldto.getLogin_id());
			ps.executeUpdate();
			

			ps.close();
			ps=null;
			
			String userBranch = "insert into userdepo (user_id,depo_code,status) values (?,?,?) ";
			ps=con.prepareStatement(userBranch);
			for(ContractMastDto bdo : ldto.getBranchList())
			{
				ps.setInt(1,ldto.getLogin_id());
				ps.setInt(2,bdo.getCmp_code());
				ps.setString(3,"Y");
				
				ps.executeUpdate();
			}

			

			//update user report
			ps.close();
			ps=null;

			
			String userRightsStatusN = "delete from  userrights where user_id=? ";
			ps=con.prepareStatement(userRightsStatusN);
			ps.setInt(1,ldto.getLogin_id());
			ps.executeUpdate();
			

			ps.close();
			ps=null;
			
			
			String userReport = "insert into userrights (USER_ID,PACK_CODE,TAB_CODE,MENU_CODE,SMENU_CODE,STATUS) VALUES (?,?,?,?,?,?) ";
			ps=con.prepareStatement(userReport);
			for(UserDto udt : ldto.getReportList())
			{
				if(udt.getStatus().equalsIgnoreCase("Y"))
				{
					ps.setInt(1,ldto.getLogin_id());
					ps.setInt(2,udt.getPack_code());
					ps.setInt(3,udt.getTab_code());
					ps.setInt(4,udt.getMenu_code());
					ps.setInt(5,udt.getSmenu_code());
					ps.setString(6,udt.getStatus());

					ps.executeUpdate();
				}
			}



			con.commit();
			con.setAutoCommit(true);

			ps.close();

		} 
		catch (Exception ex) 
		{
			try 
			{

				con.rollback();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			System.out.println("-------------Exception in UserDAO.updateUser " + ex);
			userID=0;
		}
		finally {
			try {
				System.out.println("No. of Records Update/Insert : " );

				if(rs != null){rs.close();}
				if(ps != null){ps.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in UserDAO.Connection.close "+e);
			}
		}
		return userID;
	}

	

	public int changePasword(int loginid,String pass)
	{
		PreparedStatement ps2 = null;
		Connection con=null;
		int i=0;
		try 
		{
			con=ConnectionFactory.getConnection();
			con.setAutoCommit(false);

			String loginQ ="update login set login_pass=md5(?) where login_id=? ";
			ps2 = con.prepareStatement(loginQ);
			ps2.setString(1, pass);
			ps2.setInt(2, loginid);
			i=ps2.executeUpdate();
			
			con.commit();
			con.setAutoCommit(true);
			ps2.close();

		} catch (Exception ex) {
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("-------------Exception in UserDAO.changePassowrd " + ex);

		}
		finally {
			try {

				if(ps2 != null){ps2.close();}
				if(con != null){con.close();}
			} catch (SQLException e) {
				System.out.println("-------------Exception in UserDAO.getUserId.Connection.close "+e);
			}
		}
		return i;
	}
  
    
}
