package com.payroll.dto;

import java.util.Date;

public class EmployeeMastDto 
{

	private int depo_code; 
	private int  cmp_code ;
	private int emp_code ;
	private String emp_name ;
	private String designation ;
	private String father_name ;
	private String surname ;
	private String department ;
	private String madd1 ;
	private String madd2 ;
	private String madd3 ;
	private String mcity ;
	private String mstate;
	private String mpin ;
	private String mphone; 
	private String mobile; 
	private String memail; 
	private long esic_no; 
	private int pf_no; 
	private String pan_no; 
	private Date dobirth ;
	private Date dojoin ;
	private Date doresign;
	private double gross;
	private double basic;
	private double da ;
	private double hra;
	private double add_hra;
	private double incentive;
	private double spl_incentive;
	private double lta;
	private double medical;
	private double bonus; 
	private double ot_rate;
	private double stair_alw;
	private double food_allowance; // new field inserted on 12/11/2019
	private String label_print;
	private String emp_status;
	private int paymentmode;
	private String bank;
	private String bank_add1;
	private String ifsc_code;
	private String bank_accno;
	private int bank_code;
	private long uan_no;
	private double bonus_per; 
	private double bonus_limit; 
	private double bonus_check; 
	private long adhar_no;

	public String toString()
	{
		return emp_name+" ["+emp_code+"]";
	}
	
	
	
	
	public double getFood_allowance() {
		return food_allowance;
	}




	public void setFood_allowance(double food_allowance) {
		this.food_allowance = food_allowance;
	}




	public long getAdhar_no() {
		return adhar_no;
	}





	public void setAdhar_no(long adhar_no) {
		this.adhar_no = adhar_no;
	}





	public double getBonus_per() {
		return bonus_per;
	}





	public void setBonus_per(double bonus_per) {
		this.bonus_per = bonus_per;
	}





	public double getBonus_limit() {
		return bonus_limit;
	}





	public void setBonus_limit(double bonus_limit) {
		this.bonus_limit = bonus_limit;
	}





	public double getBonus_check() {
		return bonus_check;
	}





	public void setBonus_check(double bonus_check) {
		this.bonus_check = bonus_check;
	}





	public long getUan_no() {
		return uan_no;
	}





	public void setUan_no(long uan_no) {
		this.uan_no = uan_no;
	}





	public int getBank_code() {
		return bank_code;
	}




	public void setBank_code(int bank_code) {
		this.bank_code = bank_code;
	}




	public String getBank_accno() {
		return bank_accno;
	}




	public void setBank_accno(String bank_accno) {
		this.bank_accno = bank_accno;
	}




	public double getGross() {
		return gross;
	}


	public void setGross(double gross) {
		this.gross = gross;
	}




	public String getDesignation() {
		return designation;
	}




	public void setDesignation(String designation) {
		this.designation = designation;
	}




	public double getStair_alw() {
		return stair_alw;
	}




	public void setStair_alw(double stair_alw) {
		this.stair_alw = stair_alw;
	}




	public int getDepo_code() {
		return depo_code;
	}
	public void setDepo_code(int depo_code) {
		this.depo_code = depo_code;
	}
	public int getCmp_code() {
		return cmp_code;
	}
	public void setCmp_code(int cmp_code) {
		this.cmp_code = cmp_code;
	}
	public int getEmp_code() {
		return emp_code;
	}
	public void setEmp_code(int emp_code) {
		this.emp_code = emp_code;
	}
	public String getEmp_name() {
		return emp_name;
	}
	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}
	public String getFather_name() {
		return father_name;
	}
	public void setFather_name(String father_name) {
		this.father_name = father_name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getMadd1() {
		return madd1;
	}
	public void setMadd1(String madd1) {
		this.madd1 = madd1;
	}
	public String getMadd2() {
		return madd2;
	}
	public void setMadd2(String madd2) {
		this.madd2 = madd2;
	}
	public String getMadd3() {
		return madd3;
	}
	public void setMadd3(String madd3) {
		this.madd3 = madd3;
	}
	public String getMcity() {
		return mcity;
	}
	public void setMcity(String mcity) {
		this.mcity = mcity;
	}
	public String getMstate() {
		return mstate;
	}
	public void setMstate(String mstate) {
		this.mstate = mstate;
	}
	public String getMpin() {
		return mpin;
	}
	public void setMpin(String mpin) {
		this.mpin = mpin;
	}
	public String getMphone() {
		return mphone;
	}
	public void setMphone(String mphone) {
		this.mphone = mphone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMemail() {
		return memail;
	}
	public void setMemail(String memail) {
		this.memail = memail;
	}
	
	public long getEsic_no() {
		return esic_no;
	}

	public void setEsic_no(long esic_no) {
		this.esic_no = esic_no;
	}




	public int getPf_no() {
		return pf_no;
	}
	public void setPf_no(int pf_no) {
		this.pf_no = pf_no;
	}
	public String getPan_no() {
		return pan_no;
	}
	public void setPan_no(String pan_no) {
		this.pan_no = pan_no;
	}
	public Date getDobirth() {
		return dobirth;
	}
	public void setDobirth(Date dobirth) {
		this.dobirth = dobirth;
	}
	public Date getDojoin() {
		return dojoin;
	}
	public void setDojoin(Date dojoin) {
		this.dojoin = dojoin;
	}
	public Date getDoresign() {
		return doresign;
	}
	public void setDoresign(Date doresign) {
		this.doresign = doresign;
	}
	public double getBasic() {
		return basic;
	}
	public void setBasic(double basic) {
		this.basic = basic;
	}
	public double getDa() {
		return da;
	}
	public void setDa(double da) {
		this.da = da;
	}
	public double getHra() {
		return hra;
	}
	public void setHra(double hra) {
		this.hra = hra;
	}
	public double getAdd_hra() {
		return add_hra;
	}
	public void setAdd_hra(double add_hra) {
		this.add_hra = add_hra;
	}
	public double getIncentive() {
		return incentive;
	}
	public void setIncentive(double incentive) {
		this.incentive = incentive;
	}
	public double getSpl_incentive() {
		return spl_incentive;
	}
	public void setSpl_incentive(double spl_incentive) {
		this.spl_incentive = spl_incentive;
	}
	public double getLta() {
		return lta;
	}
	public void setLta(double lta) {
		this.lta = lta;
	}
	public double getMedical() {
		return medical;
	}
	public void setMedical(double medical) {
		this.medical = medical;
	}
	public double getBonus() {
		return bonus;
	}
	public void setBonus(double bonus) {
		this.bonus = bonus;
	}
	public double getOt_rate() {
		return ot_rate;
	}
	public void setOt_rate(double ot_rate) {
		this.ot_rate = ot_rate;
	}
	public String getLabel_print() {
		return label_print;
	}
	public void setLabel_print(String label_print) {
		this.label_print = label_print;
	}
	public String getEmp_status() {
		return emp_status;
	}
	public void setEmp_status(String emp_status) {
		this.emp_status = emp_status;
	}
	public int getPaymentmode() {
		return paymentmode;
	}
	public void setPaymentmode(int paymentmode) {
		this.paymentmode = paymentmode;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getBank_add1() {
		return bank_add1;
	}
	public void setBank_add1(String bank_add1) {
		this.bank_add1 = bank_add1;
	}
	public String getIfsc_code() {
		return ifsc_code;
	}
	public void setIfsc_code(String ifsc_code) {
		this.ifsc_code = ifsc_code;
	}


	   
	   
	   
	
}
