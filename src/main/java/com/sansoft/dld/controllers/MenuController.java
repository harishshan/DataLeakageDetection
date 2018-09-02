package com.sansoft.dld.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MenuController {

	@RequestMapping(value="/menu", method = RequestMethod.GET)
	public @ResponseBody String getMenuList(HttpServletRequest request,HttpServletResponse response)
	{
		String message = "Hello World";
		try 
		{
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return message;
	}
}
