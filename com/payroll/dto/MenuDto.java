package com.payroll.dto;

import java.util.ArrayList;

public class MenuDto
{
	
	private int tab_code;
	private String tab_name;
	
	private ArrayList menu_name;
	
	
	public ArrayList getMenu_name() {
		return menu_name;
	}
	public void setMenu_name(ArrayList menu_name) {
		this.menu_name = menu_name;
	}
	public int getTab_code() {
		return tab_code;
	}
	public void setTab_code(int tab_code) {
		this.tab_code = tab_code;
	}
	public String getTab_name() {
		return tab_name;
	}
	public void setTab_name(String tab_name) {
		this.tab_name = tab_name;
	}
	
	
}
