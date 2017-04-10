package com.payroll.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class LoginDto {
	
	private int login_id;
	private String login_name;
	private String login_pass;
	private int login_mnth;
	private int mnth_code;
	private int login_year;
	private int depo_code;
	private int div_code;
	private Date fr_date;
	private Date to_date;
	private Date sdate;
	private Date edate;
	 
	 
	private int fin_year;
	 
	private Vector<?> fmonth;
	private HashMap<?,?> fmon;
	 
	private Vector<?> fyear;
	private String mnthName;
	private String yearDesc;
	
	 
	private Vector<?> prtList;
	private Vector<?> prtList1;
	 
	private Vector<?> bankList;

	
	private HashMap<?,?> prtmap;
	public Vector<?> getBankList() {
		return bankList;
	}


	public void setBankList(Vector<?> bankList) {
		this.bankList = bankList;
	}
	private HashMap<?,?> prtmap1;
	private String divnm;
	private String brnnm;
	
	private ContractMastDto bdto;

	
	 
	 
	private String message;
	private String footer;
	private String drvnm;
	private String printernm;
	private String btnnm;
	private String totinv;
	
	private int mno;
	private int cmp_code;
	
	private String prefix;
	private String fname;
	private String lname;
	private String designation;
	private String department;
	private String emaiId;
	private String isActive;
	
	private List<ContractMastDto> branchList;
	private List<Integer> packageList;
	private List<Integer> divisionList;
	private List<UserDto> reportList;
	private Vector<EmployeeMastDto> empList;

	
	
	/// most important /////
	private int pack_code;
	/////////////////////////
 
	private Vector<?> newyear;
	private Vector<?> cmpList;
	
	


	public Vector<EmployeeMastDto> getEmpList() {
		return empList;
	}


	public void setEmpList(Vector<EmployeeMastDto> empList) {
		this.empList = empList;
	}


	public Vector<?> getCmpList() {
		return cmpList;
	}


	public void setCmpList(Vector<?> cmpList) {
		this.cmpList = cmpList;
	}


  

	public Vector<?> getNewyear() {
		return newyear;
	}


	public void setNewyear(Vector<?> newyear) {
		this.newyear = newyear;
	}


	 
	 


	public String toString()
	{
		return fname+" "+lname;
	}
	
	
	public Vector<?> getPrtList1() {
		return prtList1;
	}


	public void setPrtList1(Vector<?> prtList1) {
		this.prtList1 = prtList1;
	}


	public HashMap<?, ?> getPrtmap1() {
		return prtmap1;
	}


	public void setPrtmap1(HashMap<?, ?> prtmap1) {
		this.prtmap1 = prtmap1;
	}


	 

	public List<ContractMastDto> getBranchList() {
		return branchList;
	}
	public void setBranchList(List<ContractMastDto> branchList) {
		this.branchList = branchList;
	}
	
	public List<Integer> getPackageList() {
		return packageList;
	}
	public void setPackageList(List<Integer> packageList) {
		this.packageList = packageList;
	}
	public List<Integer> getDivisionList() {
		return divisionList;
	}
	public void setDivisionList(List<Integer> divisionList) {
		this.divisionList = divisionList;
	}
	public List<UserDto> getReportList() {
		return reportList;
	}
	public void setReportList(List<UserDto> reportList) {
		this.reportList = reportList;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getEmaiId() {
		return emaiId;
	}
	public void setEmaiId(String emaiId) {
		this.emaiId = emaiId;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	 
	public int getCmp_code() {
		return cmp_code;
	}
	public void setCmp_code(int cmp_code) {
		this.cmp_code = cmp_code;
	}
	 
	public int getPack_code() {
		return pack_code;
	}
	public void setPack_code(int pack_code) {
		this.pack_code = pack_code;
	}
	 
	 
	 
	public String getBtnnm() {
		return btnnm;
	}
	public void setBtnnm(String btnnm) {
		this.btnnm = btnnm;
	}
	 
	 
	public int getMno() {
		return mno;
	}
	public void setMno(int mno) {
		this.mno = mno;
	}
	
	public String getTotinv() {
		return totinv;
	}
	public void setTotinv(String totinv) {
		this.totinv = totinv;
	}
	public HashMap<?, ?> getPrtmap() {
		return prtmap;
	}
	public void setPrtmap(HashMap<?, ?> prtmap) {
		this.prtmap = prtmap;
	}
	public String getBrnnm() {
		return brnnm;
	}
	public void setBrnnm(String brnnm) {
		this.brnnm = brnnm;
	}
	public String getDivnm() {
		return divnm;
	}
	public void setDivnm(String divnm) {
		this.divnm = divnm;
	}
	public ContractMastDto getBdto() {
		return bdto;
	}
	public void setBdto(ContractMastDto bdto) {
		this.bdto = bdto;
	}
	public String getFooter() {
		return footer;
	}
	public void setFooter(String footer) {
		this.footer = footer;
	}
	public String getPrinternm() {
		return printernm;
	}
	public void setPrinternm(String printernm) {
		this.printernm = printernm;
	}
	public String getDrvnm() {
		return drvnm;
	}
	public void setDrvnm(String drvnm) {
		this.drvnm = drvnm;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	 
	public HashMap<?, ?> getFmon() {
		return fmon;
	}
	public void setFmon(HashMap<?, ?> fmon) {
		this.fmon = fmon;
	}
	 
	 
	public Vector<?> getPrtList() {
		return prtList;
	}
	public void setPrtList(Vector<?> prtList) {
		this.prtList = prtList;
	}
	 
	
	 
	 
	public Vector<?> getFmonth() {
		return fmonth;
	}
	public void setFmonth(Vector<?> fmonth) {
		this.fmonth = fmonth;
	}
	 
	 
 
	public String getMnthName() {
		return mnthName;
	}
	public void setMnthName(String mnthName) {
		this.mnthName = mnthName;
	}
	public String getYearDesc() {
		return yearDesc;
	}
	public void setYearDesc(String yearDesc) {
		this.yearDesc = yearDesc;
	}
	public Vector<?> getFyear() {
		return fyear;
	}
	public void setFyear(Vector<?> fyear) {
		this.fyear = fyear;
	}
	 
	 
	public int getFin_year() {
		return fin_year;
	}
	public void setFin_year(int fin_year) {
		this.fin_year = fin_year;
	}
	 
 
	public int getLogin_id() {
		return login_id;
	}
	public void setLogin_id(int login_id) {
		this.login_id = login_id;
	}
	public String getLogin_name() {
		return login_name;
	}
	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}
	public String getLogin_pass() {
		return login_pass;
	}
	public void setLogin_pass(String login_pass) {
		this.login_pass = login_pass;
	}
	public int getLogin_mnth() {
		return login_mnth;
	}
	public void setLogin_mnth(int login_mnth) {
		this.login_mnth = login_mnth;
	}
	public int getMnth_code() {
		return mnth_code;
	}
	public void setMnth_code(int mnth_code) {
		this.mnth_code = mnth_code;
	}
	public int getLogin_year() {
		return login_year;
	}
	public void setLogin_year(int login_year) {
		this.login_year = login_year;
	}
	public int getDepo_code() {
		return depo_code;
	}
	public void setDepo_code(int depo_code) {
		this.depo_code = depo_code;
	}
	public int getDiv_code() {
		return div_code;
	}
	public void setDiv_code(int div_code) {
		this.div_code = div_code;
	}
	public Date getFr_date() {
		return fr_date;
	}
	public void setFr_date(Date fr_date) {
		this.fr_date = fr_date;
	}
	public Date getTo_date() {
		return to_date;
	}
	public void setTo_date(Date to_date) {
		this.to_date = to_date;
	}
	public Date getSdate() {
		return sdate;
	}
	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}
	public Date getEdate() {
		return edate;
	}
	public void setEdate(Date edate) {
		this.edate = edate;
	}
	
	
	
}
