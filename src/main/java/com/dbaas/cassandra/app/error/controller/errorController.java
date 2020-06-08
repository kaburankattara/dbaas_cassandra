package com.dbaas.cassandra.app.error.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbaas.cassandra.app.login.form.LoginForm;

@Controller
@RequestMapping("/error")
public class  errorController {

	/**
	 * メソッド呼出前処理
	 *
	 * @return form
	 */
	@ModelAttribute("form")
	private LoginForm setUpForm() {
		return new LoginForm();
	}
	
	@GetMapping()
	public String index(@ModelAttribute("form") LoginForm form) {
		return "error/index";
	}
}
