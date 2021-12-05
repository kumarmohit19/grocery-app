package com.webapp.controller;

import org.springframework.ui.ModelMap;

public class GroceryListController {

	public String showGroceries(ModelMap model) {
		return "list-groceries";
	}
}