package com.doodle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {

	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register"; 
    }
}
