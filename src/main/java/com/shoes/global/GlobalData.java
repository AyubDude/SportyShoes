package com.shoes.global;

import java.util.ArrayList;
import java.util.List;

import com.shoes.model.Product;
import com.shoes.model.User;

public class GlobalData {
	
	public static List<Product> cart;
	
	static {
		cart = new ArrayList<Product>();
	}
	
	public static User LOGINUSER;
	
	static {
		LOGINUSER = new User();
	}

}
