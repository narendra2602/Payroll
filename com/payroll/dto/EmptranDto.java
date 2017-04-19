package com.payroll.dto;

import java.util.Date;

public class EmptranDto 
{

	
	private int depo_code ;
	private int cmp_code ;
	private int doc_type;
	private int emp_code;
	private String emp_name;
	private String designation;
	private long esic_no;
	private int pf_no;
	private int doc_no;
	private Date doc_date;
	private double atten_days;
	private double arrear_days;
	private double absent_days;
	private double extra_hrs;
	private int stair_days;
	private double advance;
	private double loan;
	private double gross;
	private double basic;
	private double da;
	private double hra;
	private double add_hra;
	private double incentive;
	private double spl_incentive;
	private double lta;
	private double medical;
	private double ot_rate;
	private double stair_alw;
	private double basic_value;
	private double da_value;
	private double hra_value;
	private double add_hra_value;
	private double incentive_value;
	private double spl_incen_value;
	private double lta_value;
	private double medical_value;
	private double ot_value;
	private double stair_value;
	private double pf_value;
	private double esis_value;
	private double employer_esis_value;
	private double prof_tax;
	private int[] mon;  
	private String monname;
	private int mnthdays;
	private double misc_value;
	private double employee_pf;
	private double employer_pf;
	private double eps_pf;
	private double net_value;
	private int atten_lock;
	private double tds_value;
	private Date payment_date;
	private double bonus_per;
	private double bonus_value;
	private Date system_dt ;
	private int  created_by;
	private Date created_date;
	private int modified_by;
	private Date modified_date;
	private int  serialno;
	private int fin_year;
	private int mnth_code;
	private String remark ;
	
	private double[] atten;
	private double[] arrdays;
	private double[] bonusval;
	private double arrear_amt;
	private double arrear_sanc;
	
	private String bank;
	private String bank_add1;
	private String ifsc_code;
	private String bank_accno;
	private int bank_code;
	private double coupon_amt;
	private long diffdays;
	private long uan_no;
	
	private double arrear_basic_value;
	private double arrear_net_value;
	
	private double oldwages;
	private double newwages;
	
	
	
	
	public double[] getArrdays() {
		return arrdays;
	}
	public void setArrdays(double[] arrdays) {
		this.arrdays = arrdays;
	}
	public double getOldwages() {
		return oldwages;
	}
	public void setOldwages(double oldwages) {
		this.oldwages = oldwages;
	}
	public double getNewwages() {
		return newwages;
	}
	public void setNewwages(double newwages) {
		this.newwages = newwages;
	}
	public double getArrear_basic_value() {
		return arrear_basic_value;
	}
	public void setArrear_basic_value(double arrear_basic_value) {
		this.arrear_basic_value = arrear_basic_value;
	}
	public double getArrear_net_value() {
		return arrear_net_value;
	}
	public void setArrear_net_value(double arrear_net_value) {
		this.arrear_net_value = arrear_net_value;
	}
	public long getUan_no() {
		return uan_no;
	}
	public void setUan_no(long uan_no) {
		this.uan_no = uan_no;
	}
	public long getDiffdays() {
		return diffdays;
	}
	public void setDiffdays(long diffdays) {
		this.diffdays = diffdays;
	}
	public double getCoupon_amt() {
		return coupon_amt;
	}
	public void setCoupon_amt(double coupon_amt) {
		this.coupon_amt = coupon_amt;
	}
	public int getBank_code() {
		return bank_code;
	}
	public void setBank_code(int bank_code) {
		this.bank_code = bank_code;
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
	public String getBank_accno() {
		return bank_accno;
	}
	public void setBank_accno(String bank_accno) {
		this.bank_accno = bank_accno;
	}
	public double getArrear_amt() {
		return arrear_amt;
	}
	public void setArrear_amt(double arrear_amt) {
		this.arrear_amt = arrear_amt;
	}
	public double getArrear_sanc() {
		return arrear_sanc;
	}
	public void setArrear_sanc(double arrear_sanc) {
		this.arrear_sanc = arrear_sanc;
	}
	public double[] getAtten() {
		return atten;
	}
	public void setAtten(double[] atten) {
		this.atten = atten;
	}
	public double[] getBonusval() {
		return bonusval;
	}
	public void setBonusval(double[] bonusval) {
		this.bonusval = bonusval;
	}
	public int getAtten_lock() {
		return atten_lock;
	}
	public void setAtten_lock(int atten_lock) {
		this.atten_lock = atten_lock;
	}
	public double getNet_value() {
		return net_value;
	}
	public void setNet_value(double net_value) {
		this.net_value = net_value;
	}
	public double getEmployee_pf() {
		return employee_pf;
	}
	public void setEmployee_pf(double employee_pf) {
		this.employee_pf = employee_pf;
	}
	public double getEmployer_pf() {
		return employer_pf;
	}
	public void setEmployer_pf(double employer_pf) {
		this.employer_pf = employer_pf;
	}
	public double getEps_pf() {
		return eps_pf;
	}
	public void setEps_pf(double eps_pf) {
		this.eps_pf = eps_pf;
	}
	public double getMisc_value() {
		return misc_value;
	}
	public void setMisc_value(double misc_value) {
		this.misc_value = misc_value;
	}
	public int getMnthdays() {
		return mnthdays;
	}
	public void setMnthdays(int mnthdays) {
		this.mnthdays = mnthdays;
	}
	public String getMonname() {
		return monname;
	}
	public void setMonname(String monname) {
		this.monname = monname;
	}
	public int[] getMon() {
		return mon;
	}
	public void setMon(int[] mon) {
		this.mon = mon;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public double getLoan() {
		return loan;
	}
	public void setLoan(double loan) {
		this.loan = loan;
	}
	public double getAdd_hra_value() {
		return add_hra_value;
	}
	public void setAdd_hra_value(double add_hra_value) {
		this.add_hra_value = add_hra_value;
	}
	public double getEmployer_esis_value() {
		return employer_esis_value;
	}
	public void setEmployer_esis_value(double employer_esis_value) {
		this.employer_esis_value = employer_esis_value;
	}
	public String getEmp_name() {
		return emp_name;
	}
	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
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
	public double getGross()
	{
		return gross;
	}
	public void setGross(double gross) {
		this.gross = gross;
	}
	public int getStair_days() {
		return stair_days;
	}
	public void setStair_days(int stair_days) {
		this.stair_days = stair_days;
	}
	public double getStair_alw() {
		return stair_alw;
	}
	public void setStair_alw(double stair_alw) {
		this.stair_alw = stair_alw;
	}
	public double getStair_value() {
		return stair_value;
	}
	public void setStair_value(double stair_value) {
		this.stair_value = stair_value;
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
	public int getDoc_type() {
		return doc_type;
	}
	public void setDoc_type(int doc_type) {
		this.doc_type = doc_type;
	}
	public int getEmp_code() {
		return emp_code;
	}
	public void setEmp_code(int emp_code) {
		this.emp_code = emp_code;
	}
	public int getDoc_no() {
		return doc_no;
	}
	public void setDoc_no(int doc_no) {
		this.doc_no = doc_no;
	}
	public Date getDoc_date() {
		return doc_date;
	}
	public void setDoc_date(Date doc_date) {
		this.doc_date = doc_date;
	}
	
	public double getAtten_days() {
		return atten_days;
	}
	public void setAtten_days(double atten_days) {
		this.atten_days = atten_days;
	}
	 
	public double getArrear_days() {
		return arrear_days;
	}
	public void setArrear_days(double arrear_days) {
		this.arrear_days = arrear_days;
	}
	public double getAbsent_days() {
		return absent_days;
	}
	public void setAbsent_days(double absent_days) {
		this.absent_days = absent_days;
	}
	public double getExtra_hrs() {
		return extra_hrs;
	}
	public void setExtra_hrs(double extra_hrs) {
		this.extra_hrs = extra_hrs;
	}
	public double getAdvance() {
		return advance;
	}
	public void setAdvance(double advance) {
		this.advance = advance;
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
	public double getOt_rate() {
		return ot_rate;
	}
	public void setOt_rate(double ot_rate) {
		this.ot_rate = ot_rate;
	}
	public double getBasic_value() {
		return basic_value;
	}
	public void setBasic_value(double basic_value) {
		this.basic_value = basic_value;
	}
	public double getDa_value() {
		return da_value;
	}
	public void setDa_value(double da_value) {
		this.da_value = da_value;
	}
	public double getHra_value() {
		return hra_value;
	}
	public void setHra_value(double hra_value) {
		this.hra_value = hra_value;
	}
	public double getIncentive_value() {
		return incentive_value;
	}
	public void setIncentive_value(double incentive_value) {
		this.incentive_value = incentive_value;
	}
	public double getSpl_incen_value() {
		return spl_incen_value;
	}
	public void setSpl_incen_value(double spl_incen_value) {
		this.spl_incen_value = spl_incen_value;
	}
	public double getLta_value() {
		return lta_value;
	}
	public void setLta_value(double lta_value) {
		this.lta_value = lta_value;
	}
	public double getMedical_value() {
		return medical_value;
	}
	public void setMedical_value(double medical_value) {
		this.medical_value = medical_value;
	}
	public double getOt_value() {
		return ot_value;
	}
	public void setOt_value(double ot_value) {
		this.ot_value = ot_value;
	}
	public double getPf_value() {
		return pf_value;
	}
	public void setPf_value(double pf_value) {
		this.pf_value = pf_value;
	}
	public double getEsis_value() {
		return esis_value;
	}
	public void setEsis_value(double esis_value) {
		this.esis_value = esis_value;
	}
	public double getProf_tax() {
		return prof_tax;
	}
	public void setProf_tax(double prof_tax) {
		this.prof_tax = prof_tax;
	}
	public double getTds_value() {
		return tds_value;
	}
	public void setTds_value(double tds_value) {
		this.tds_value = tds_value;
	}
	public Date getPayment_date() {
		return payment_date;
	}
	public void setPayment_date(Date payment_date) {
		this.payment_date = payment_date;
	}
	public double getBonus_per() {
		return bonus_per;
	}
	public void setBonus_per(double bonus_per) {
		this.bonus_per = bonus_per;
	}
	public double getBonus_value() {
		return bonus_value;
	}
	public void setBonus_value(double bonus_value) {
		this.bonus_value = bonus_value;
	}
	public Date getSystem_dt() {
		return system_dt;
	}
	public void setSystem_dt(Date system_dt) {
		this.system_dt = system_dt;
	}
	public int getCreated_by() {
		return created_by;
	}
	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	public int getModified_by() {
		return modified_by;
	}
	public void setModified_by(int modified_by) {
		this.modified_by = modified_by;
	}
	public Date getModified_date() {
		return modified_date;
	}
	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}
	public int getSerialno() {
		return serialno;
	}
	public void setSerialno(int serialno) {
		this.serialno = serialno;
	}
	public int getFin_year() {
		return fin_year;
	}
	public void setFin_year(int fin_year) {
		this.fin_year = fin_year;
	}
	public int getMnth_code() {
		return mnth_code;
	}
	public void setMnth_code(int mnth_code) {
		this.mnth_code = mnth_code;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	

	  
	  
	  
	  
}
