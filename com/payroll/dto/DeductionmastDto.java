package com.payroll.dto;

import java.util.Date;

public class DeductionmastDto 
{


	private int depo_code;
	private String tax_type;
	private String tax_desc;
	private double tax_per;
	private double add_tax1;
	private String add_desc1;
	private double add_tax2;
	private String add_desc2;
	private Date sdate;
	private Date edate;
	private int serialno;
	  
	public int getDepo_code() {
		return depo_code;
	}
	public void setDepo_code(int depo_code) {
		this.depo_code = depo_code;
	}
	public String getTax_type() {
		return tax_type;
	}
	public void setTax_type(String tax_type) {
		this.tax_type = tax_type;
	}
	public String getTax_desc() {
		return tax_desc;
	}
	public void setTax_desc(String tax_desc) {
		this.tax_desc = tax_desc;
	}
	public double getTax_per() {
		return tax_per;
	}
	public void setTax_per(double tax_per) {
		this.tax_per = tax_per;
	}
	public double getAdd_tax1() {
		return add_tax1;
	}
	public void setAdd_tax1(double add_tax1) {
		this.add_tax1 = add_tax1;
	}
	public String getAdd_desc1() {
		return add_desc1;
	}
	public void setAdd_desc1(String add_desc1) {
		this.add_desc1 = add_desc1;
	}
	public double getAdd_tax2() {
		return add_tax2;
	}
	public void setAdd_tax2(double add_tax2) {
		this.add_tax2 = add_tax2;
	}
	public String getAdd_desc2() {
		return add_desc2;
	}
	public void setAdd_desc2(String add_desc2) {
		this.add_desc2 = add_desc2;
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
	public int getSerialno() {
		return serialno;
	}
	public void setSerialno(int serialno) {
		this.serialno = serialno;
	}
	  
	  
	
	
}
