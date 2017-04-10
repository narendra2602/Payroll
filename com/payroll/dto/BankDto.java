package com.payroll.dto;

public class BankDto 
{
	private  int  bank_code  ;
	private  String  bank_name  ;
	
	public String toString()
	{
		return bank_name;
		
	}

	public int getBank_code() {
		return bank_code;
	}

	public void setBank_code(int bank_code) {
		this.bank_code = bank_code;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	
	
}
