package com.payroll.dto;

import java.util.Date;

public class YearDto
{

	private int yearcode;
	private String yeardesc;
	private Date msdate;
	private Date medate;
	private int myear;
 
	
	public int getMyear() {
		return myear;
	}
	public void setMyear(int myear) {
		this.myear = myear;
	}
	public Date getMsdate() {
		return msdate;
	}
	public void setMsdate(Date msdate) {
		this.msdate = msdate;
	}
	public Date getMedate() {
		return medate;
	}
	public void setMedate(Date medate) {
		this.medate = medate;
	}
	public int getYearcode() {
		return yearcode;
	}
	public void setYearcode(int yearcode) {
		this.yearcode = yearcode;
	}
	public String getYeardesc() {
		return yeardesc;
	}
	public void setYeardesc(String yeardesc) {
		this.yeardesc = yeardesc;
	}
	
	 public String toString()
     {
         return yeardesc;
     }
	
}
