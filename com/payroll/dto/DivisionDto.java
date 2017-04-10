package com.payroll.dto;

public class DivisionDto 
{
	 
	private  int  div_code  ;
	private  String  div_name  ;
	
	public String toString()
	{
		return div_name;
		
	}

	public int getDiv_code() {
		return div_code;
	}

	public void setDiv_code(int div_code) {
		this.div_code = div_code;
	}

	public String getDiv_name() {
		return div_name;
	}

	public void setDiv_name(String div_name) {
		this.div_name = div_name;
	}

	}
