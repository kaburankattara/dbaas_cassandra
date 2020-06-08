package com.dbaas.cassandra.app.keySpaceList.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbaas.cassandra.app.login.form.LoginForm;

@Controller
@RequestMapping("/keySpaceList")
public class  KeySpaceListController {

	/**
	 * メソッド呼出前処理
	 *
	 * @return form
	 */
	@ModelAttribute("form")
	private LoginForm setUpForm() {
		return new LoginForm();
	}
	
	@RequestMapping()
	public String index(@ModelAttribute("form") LoginForm form) {
		return "keySpaceList/keySpaceList";
	}
}
