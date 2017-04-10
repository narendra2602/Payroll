package com.payroll.dto;

import java.util.Date;

public class MonthDto

{

	private int mnthcode;
	private String mnthname;
	private Date sdate;
	private Date edate;
    private String mnthabbr;
    private int mnthno;
    private int mnthyear;
    private int emnthcode;
	private int mkt_ord;
	private int fin_ord;
	private int mnthdays;
	
	
	
    
    
    
	public int getMnthdays() {
		return mnthdays;
	}
	public void setMnthdays(int mnthdays) {
		this.mnthdays = mnthdays;
	}
	public int getFin_ord() {
		return fin_ord;
	}
	public void setFin_ord(int fin_ord) {
		this.fin_ord = fin_ord;
	}
	public int getMkt_ord() {
		return mkt_ord;
	}
	public void setMkt_ord(int mkt_ord) {
		this.mkt_ord = mkt_ord;
	}
	public int getEmnthcode() {
		return emnthcode;
	}
	public void setEmnthcode(int emnthcode) {
		this.emnthcode = emnthcode;
	}
	public int getMnthyear() {
		return mnthyear;
	}
	public void setMnthyear(int mnthyear) {
		this.mnthyear = mnthyear;
	}
	public String getMnthabbr() {
		return mnthabbr;
	}
	public void setMnthabbr(String mnthabbr) {
		this.mnthabbr = mnthabbr;
	}
	
	
	public int getMnthno() {
		return mnthno;
	}
	public void setMnthno(int mnthno) {
		this.mnthno = mnthno;
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
	public int getMnthcode() {
		return mnthcode;
	}
	public void setMnthcode(int mnthcode) {
		this.mnthcode = mnthcode;
	}
	public String getMnthname() {
		return mnthname;
	}
	public void setMnthname(String mnthname) {
		this.mnthname = mnthname;
	}
	
	 public String toString()
     {
         return mnthname;
     }
}
