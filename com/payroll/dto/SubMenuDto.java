package com.payroll.dto;
 
import java.util.ArrayList;
 

public class SubMenuDto
{
	
	private String menu_code;
	private String menu_name;
	private String class_name;
	private String subclass_name;
	private ArrayList smenu_name;

	
	public String getSubclass_name() 
	{
		return subclass_name;
	}

	public void setSubclass_name(String subclass_name) {
		this.subclass_name = subclass_name;
	}

		public String getMenu_code() {
		return menu_code;
	}

	public void setMenu_code(String menu_code) {
		this.menu_code = menu_code;
	}

	public String getMenu_name() {
		return menu_name;
	}

	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public ArrayList getSmenu_name() {
		return smenu_name;
	}

	public void setSmenu_name(ArrayList smenu_name) {
		this.smenu_name = smenu_name;
	}


	
	
	
	 
	
}
